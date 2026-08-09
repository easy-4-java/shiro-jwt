package org.apache.shiro.spring.boot.jwt.authz;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.biz.authc.AuthcResponse;
import org.apache.shiro.biz.authc.AuthcResponseCode;
import org.apache.shiro.biz.authz.AuthorizationFailureHandler;
import org.apache.shiro.biz.utils.SubjectUtils;
import org.apache.shiro.biz.web.servlet.http.HttpStatus;
import org.apache.shiro.spring.boot.jwt.ShiroJwtMessageSource;
import org.apache.shiro.spring.boot.jwt.authc.JwtAuthenticationFailureHandler;
import org.apache.shiro.spring.boot.jwt.exception.ExpiredJwtException;
import org.apache.shiro.spring.boot.jwt.exception.IncorrectJwtException;
import org.apache.shiro.spring.boot.jwt.exception.InvalidJwtToken;
import org.apache.shiro.spring.boot.jwt.exception.NotObtainedJwtException;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.JSONObject;

/**
 * Handler invoked when JWT authorization fails. Maps specific JWT exception types
 * (expired, incorrect, invalid, not-obtained) to appropriate HTTP error responses.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
public class JwtAuthorizationFailureHandler implements AuthorizationFailureHandler {

	private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFailureHandler.class);

	protected MessageSourceAccessor messages = ShiroJwtMessageSource.getAccessor();

	@Override
	public boolean supports(AuthenticationException ex) {
		return SubjectUtils.isAssignableFrom(ex.getClass(), ExpiredJwtException.class, IncorrectJwtException.class,
				InvalidJwtToken.class, NotObtainedJwtException.class);
	}

	@Override
	public boolean onAuthorizationFailure(Object mappedValue, AuthenticationException ex, ServletRequest request,
			ServletResponse response) throws IOException {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ExceptionUtils.getRootCauseMessage(ex));
		}
		try {
			WebUtils.toHttp(response).setStatus(HttpStatus.SC_OK);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			if (ex instanceof ExpiredJwtException) {
				write(response, AuthcResponseCode.SC_AUTHZ_TOKEN_EXPIRED, ex.getMessage());
			} else if (ex instanceof IncorrectJwtException) {
				write(response, AuthcResponseCode.SC_AUTHZ_TOKEN_INCORRECT, ex.getMessage());
			} else if (ex instanceof InvalidJwtToken) {
				write(response, AuthcResponseCode.SC_AUTHZ_TOKEN_INVALID, ex.getMessage());
			} else if (ex instanceof NotObtainedJwtException) {
				write(response, AuthcResponseCode.SC_AUTHZ_TOKEN_REQUIRED, ex.getMessage());
			} else {
				JSONObject.writeJSONString(response.getOutputStream(), AuthcResponse.error(
						AuthcResponseCode.SC_AUTHC_FAIL.getCode(),
						messages.getMessage(AuthcResponseCode.SC_AUTHC_FAIL.getMsgKey())));
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			JSONObject.writeJSONString(response.getOutputStream(), AuthcResponse.error("Unauthentication."));
		}
		return false;
	}

	private void write(ServletResponse response, AuthcResponseCode code, String defaultMessage) throws IOException {
		JSONObject.writeJSONString(response.getOutputStream(),
				AuthcResponse.error(code.getCode(), messages.getMessage(code.getMsgKey(), defaultMessage)));
	}

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE - 1;
	}

}
