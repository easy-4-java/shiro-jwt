package org.apache.shiro.spring.boot.jwt.authc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.biz.authc.AuthcResponse;
import org.apache.shiro.biz.authc.AuthcResponseCode;
import org.apache.shiro.biz.authc.AuthenticationFailureHandler;
import org.apache.shiro.biz.utils.SubjectUtils;
import org.apache.shiro.biz.web.servlet.http.HttpStatus;
import org.apache.shiro.spring.boot.jwt.ShiroJwtMessageSource;
import org.apache.shiro.spring.boot.jwt.exception.ExpiredJwtException;
import org.apache.shiro.spring.boot.jwt.exception.IncorrectJwtException;
import org.apache.shiro.spring.boot.jwt.exception.InvalidJwtToken;
import org.apache.shiro.spring.boot.jwt.exception.NotObtainedJwtException;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.JSONObject;

public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler, Ordered {

	private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFailureHandler.class);

	protected MessageSourceAccessor messages = ShiroJwtMessageSource.getAccessor();

	@Override
	public boolean supports(AuthenticationException ex) {
		return SubjectUtils.isAssignableFrom(ex.getClass(), ExpiredJwtException.class, IncorrectJwtException.class,
				InvalidJwtToken.class, NotObtainedJwtException.class);
	}

	@Override
	public void onAuthenticationFailure(AuthenticationToken token, ServletRequest request, ServletResponse response,
			AuthenticationException ex) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ExceptionUtils.getRootCauseMessage(ex));
		}
		try {
			WebUtils.toHttp(response).setStatus(HttpStatus.SC_OK);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
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
		} catch (NoSuchMessageException | IOException e) {
			LOG.error(e.getMessage(), e);
		}
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
