package org.apache.shiro.jwt.authz;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.shiro.jwt.token.JwtAuthorizationToken;
import org.junit.jupiter.api.Test;

class JwtAuthorizationFilterRequestTest {

    private final JwtAuthorizationFilter filter = new JwtAuthorizationFilter();

    @Test
    void readsTokenFromHeaderThenParameterThenCookie() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Authorization")).thenReturn("header-token", null, null);
        when(request.getParameter("token")).thenReturn("param-token", null);
        when(request.getCookies()).thenReturn(new Cookie[] {new Cookie("token", "cookie-token")});

        assertThat(filter.getAccessToken(request)).isEqualTo("header-token");
        assertThat(filter.getAccessToken(request)).isEqualTo("param-token");
        assertThat(filter.getAccessToken(request)).isEqualTo("cookie-token");
        assertThat(filter.isJwtSubmission(request, mock(HttpServletResponse.class))).isTrue();
    }

    @Test
    void createsJwtTokenFromServletHeaderAndRemoteAddress() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Authorization")).thenReturn("jwt-value");
        when(request.getRemoteAddr()).thenReturn("192.0.2.10");

        JwtAuthorizationToken token = (JwtAuthorizationToken) filter.createJwtToken(request,
                mock(HttpServletResponse.class));

        assertThat(token.getToken()).isEqualTo("jwt-value");
        assertThat(token.getHost()).isEqualTo("192.0.2.10");
    }

    @Test
    void missingTokenWritesUnauthorizedJsonResponse() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        when(response.getOutputStream()).thenReturn(new ServletOutputStream() {
            @Override public boolean isReady() { return true; }
            @Override public void setWriteListener(WriteListener listener) { }
            @Override public void write(int value) throws IOException { bytes.write(value); }
        });

        assertThat(filter.isAccessAllowed(request, response, null)).isFalse();
        verify(response).setContentType("application/json");
        assertThat(bytes.toString("UTF-8")).contains("401");
    }
}
