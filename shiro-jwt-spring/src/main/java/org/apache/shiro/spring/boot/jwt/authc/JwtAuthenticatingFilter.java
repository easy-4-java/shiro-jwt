package org.apache.shiro.spring.boot.jwt.authc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.biz.authc.AuthcResponse;
import org.apache.shiro.biz.utils.WebUtils2;
import org.apache.shiro.biz.web.filter.authc.PostLoginRequest;
import org.apache.shiro.biz.web.filter.authc.TrustableRestAuthenticatingFilter;
import org.apache.shiro.biz.web.servlet.http.HttpStatus;
import org.apache.shiro.spring.boot.jwt.JwtPayloadRepository;
import org.apache.shiro.spring.boot.jwt.exception.InvalidJwtToken;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthenticationToken;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthorizationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet filter that intercepts HTTP requests and extracts JWT tokens from the
 * authorization header, query parameter, or cookie. Supports stateless session
 * management and optional JWT expiry verification.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
public class JwtAuthenticatingFilter extends TrustableRestAuthenticatingFilter {

	private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticatingFilter.class);

	protected static final String AUTHORIZATION_HEADER = "X-Authorization";
	protected static final String AUTHORIZATION_PARAM = "token";

	private String authorizationHeaderName = AUTHORIZATION_HEADER;
	private String authorizationParamName = AUTHORIZATION_PARAM;
	private String authorizationCookieName = AUTHORIZATION_PARAM;
	private JwtPayloadRepository jwtPayloadRepository;
	private boolean checkExpiry = false;
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		if (isSessionStateless()) {
			if (!isLoginRequest(request, response) && isJwtSubmission(request, response)) {
				AuthenticationToken token = createJwtToken(request, response);
				try {
					Subject subject = getSubject(request, response);
					subject.login(token);
					if (checkExpiry) {
						boolean accessAllowed = getJwtPayloadRepository().verify(token, subject, isCheckExpiry());
						if (!accessAllowed) {
							throw new InvalidJwtToken("Invalid JWT value.");
						}
					}
					return onAccessSuccess(token, subject, request, response);
				} catch (AuthenticationException e) {
					return onAccessFailure(token, e, request, response);
				}
			}
			return false;
		}
		return super.isAccessAllowed(request, response, mappedValue);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		if (isLoginRequest(request, response)) {
			if (isLoginSubmission(request, response)) {
				if (LOG.isTraceEnabled()) {
					LOG.trace("Login submission detected. Attempting to execute login.");
				}
				return executeLogin(request, response);
			}
			String message = "Authentication url [" + getLoginUrl() + "] Not Http Post request.";
			if (LOG.isTraceEnabled()) {
				LOG.trace(message);
			}
			writeFailure(response, HttpStatus.SC_BAD_REQUEST, message);
			return false;
		}
		if (!isJwtSubmission(request, response)) {
			String message = String.format(
					"Attempting to access a path which requires authentication. %s = Authorization Header or %s = Authorization Param or %s = Authorization Cookie is not present in the request",
					getAuthorizationHeaderName(), getAuthorizationParamName(), getAuthorizationCookieName());
			if (LOG.isTraceEnabled()) {
				LOG.trace(message);
			}
			writeFailure(response, HttpStatus.SC_UNAUTHORIZED, message);
			return false;
		}
		return false;
	}

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		if (WebUtils2.isObjectRequest(request)) {
			try {
				PostLoginRequest loginRequest = objectMapper.readValue(request.getReader(), PostLoginRequest.class);
				String host = getHost(request);
				if (isCaptchaEnabled()) {
					return new JwtAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword(),
							loginRequest.getCaptcha(), loginRequest.isRememberMe(), host);
				}
				return new JwtAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword(),
						loginRequest.isRememberMe(), host);
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return super.createToken(request, response);
	}

	@Override
	protected AuthenticationToken createToken(String username, String password, ServletRequest request,
			ServletResponse response) {
		boolean rememberMe = isRememberMe(request);
		String host = getHost(request);
		if (isCaptchaEnabled()) {
			return new JwtAuthenticationToken(username, password, getCaptcha(request), rememberMe, host);
		}
		return new JwtAuthenticationToken(username, password, rememberMe, host);
	}

	protected AuthenticationToken createJwtToken(ServletRequest request, ServletResponse response) {
		String host = WebUtils2.getRemoteAddr(request);
		String jwtToken = getAccessToken(request);
		return new JwtAuthorizationToken(host, jwtToken, isRememberMe(request));
	}

	protected boolean isJwtSubmission(ServletRequest request, ServletResponse response) {
		String authzHeader = getAccessToken(request);
		return (request instanceof HttpServletRequest) && Objects.nonNull(authzHeader);
	}

	protected String getAccessToken(ServletRequest request) {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		String token = httpRequest.getHeader(getAuthorizationHeaderName());
		if (StringUtils.isBlank(token)) {
			token = httpRequest.getParameter(getAuthorizationParamName());
		}
		if (StringUtils.isBlank(token)) {
			Cookie[] cookies = httpRequest.getCookies();
			if (Objects.isNull(cookies) || cookies.length == 0) {
				return null;
			}
			for (Cookie cookie : cookies) {
				if (getAuthorizationCookieName().equals(cookie.getName())) {
					token = cookie.getValue();
					break;
				}
			}
		}
		return token;
	}

	private void writeFailure(ServletResponse response, int status, String message) throws IOException {
		WebUtils.toHttp(response).setStatus(HttpStatus.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		JSONObject.writeJSONString(response.getOutputStream(), AuthcResponse.fail(status, message));
	}

	public String getAuthorizationHeaderName() {
		return authorizationHeaderName;
	}

	public void setAuthorizationHeaderName(String authorizationHeaderName) {
		this.authorizationHeaderName = authorizationHeaderName;
	}

	public String getAuthorizationParamName() {
		return authorizationParamName;
	}

	public void setAuthorizationParamName(String authorizationParamName) {
		this.authorizationParamName = authorizationParamName;
	}

	public String getAuthorizationCookieName() {
		return authorizationCookieName;
	}

	public void setAuthorizationCookieName(String authorizationCookieName) {
		this.authorizationCookieName = authorizationCookieName;
	}

	public JwtPayloadRepository getJwtPayloadRepository() {
		return jwtPayloadRepository;
	}

	public void setJwtPayloadRepository(JwtPayloadRepository jwtPayloadRepository) {
		this.jwtPayloadRepository = jwtPayloadRepository;
	}

	public boolean isCheckExpiry() {
		return checkExpiry;
	}

	public void setCheckExpiry(boolean checkExpiry) {
		this.checkExpiry = checkExpiry;
	}

}
