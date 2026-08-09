package org.apache.shiro.spring.boot.jwt.authz;

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
import org.apache.shiro.biz.web.filter.authz.AbstracAuthorizationFilter;
import org.apache.shiro.biz.web.servlet.http.HttpStatus;
import org.apache.shiro.spring.boot.jwt.JwtPayloadRepository;
import org.apache.shiro.spring.boot.jwt.ShiroJwtMessageSource;
import org.apache.shiro.spring.boot.jwt.exception.InvalidJwtToken;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthorizationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.JSONObject;

/**
 * Authorization filter that extracts JWT tokens from the request (header, parameter, or cookie),
 * performs login via the Shiro subject, and optionally verifies JWT expiry.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
public class JwtAuthorizationFilter extends AbstracAuthorizationFilter {

	private static final Logger LOG = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

	protected static final String AUTHORIZATION_HEADER = "X-Authorization";
	protected static final String AUTHORIZATION_PARAM = "token";
	protected MessageSourceAccessor messages = ShiroJwtMessageSource.getAccessor();

	private String authorizationHeaderName = AUTHORIZATION_HEADER;
	private String authorizationParamName = AUTHORIZATION_PARAM;
	private String authorizationCookieName = AUTHORIZATION_PARAM;
	private JwtPayloadRepository jwtPayloadRepository;
	private boolean checkExpiry = false;

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		if (isJwtSubmission(request, response)) {
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
				return onAccessSuccess(mappedValue, subject, request, response);
			} catch (AuthenticationException e) {
				return onAccessFailure(mappedValue, e, request, response);
			}
		}

		String message = String.format(
				"Attempting to access a path which requires authentication. %s = Authorization Header or %s = Authorization Param or %s = Authorization Cookie is not present in the request",
				getAuthorizationHeaderName(), getAuthorizationParamName(), getAuthorizationCookieName());
		if (LOG.isTraceEnabled()) {
			LOG.trace(message);
		}
		WebUtils.toHttp(response).setStatus(HttpStatus.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		JSONObject.writeJSONString(response.getOutputStream(), AuthcResponse.fail(HttpStatus.SC_UNAUTHORIZED, message));
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
		return false;
	}

	protected AuthenticationToken createJwtToken(ServletRequest request, ServletResponse response) {
		String host = WebUtils2.getRemoteAddr(request);
		String jwtToken = getAccessToken(request);
		return new JwtAuthorizationToken(host, jwtToken, false);
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
