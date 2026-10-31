package org.apache.shiro.jwt.authc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.shiro.jwt.token.JwtAuthorizationToken;
import org.junit.jupiter.api.Test;

class JwtAuthenticatingFilterRequestTest {

    private final RequestFilter filter = new RequestFilter();

    @Test
    void readsJwtFromServletHeaderParameterAndCookie() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Authorization")).thenReturn("header-token", (String) null, (String) null);
        when(request.getParameter("token")).thenReturn("param-token", (String) null);
        when(request.getCookies()).thenReturn(new Cookie[] {new Cookie("token", "cookie-token")});

        assertThat(filter.getAccessToken(request)).isEqualTo("header-token");
        assertThat(filter.getAccessToken(request)).isEqualTo("param-token");
        assertThat(filter.getAccessToken(request)).isEqualTo("cookie-token");
        assertThat(filter.isJwtSubmission(request, mock(HttpServletResponse.class))).isTrue();
    }

    @Test
    void createsJwtAuthorizationTokenFromRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Authorization")).thenReturn("jwt-value");
        when(request.getRemoteAddr()).thenReturn("192.0.2.11");

        JwtAuthorizationToken token = (JwtAuthorizationToken) filter.createJwtToken(request,
                mock(HttpServletResponse.class));

        assertThat(token.getToken()).isEqualTo("jwt-value");
        assertThat(token.getHost()).isEqualTo("192.0.2.11");
    }

    @Test
    void missingJwtWritesUnauthorizedJsonResponse() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        when(response.getOutputStream()).thenReturn(new ServletOutputStream() {
            @Override public boolean isReady() { return true; }
            @Override public void setWriteListener(WriteListener listener) { }
            @Override public void write(int value) throws IOException { bytes.write(value); }
        });

        assertThat(filter.onAccessDenied(request, response)).isFalse();
        verify(response).setContentType("application/json");
        assertThat(bytes.toString("UTF-8")).contains("401");
    }

    private static class RequestFilter extends JwtAuthenticatingFilter {
        @Override
        protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
            return false;
        }
    }
}
