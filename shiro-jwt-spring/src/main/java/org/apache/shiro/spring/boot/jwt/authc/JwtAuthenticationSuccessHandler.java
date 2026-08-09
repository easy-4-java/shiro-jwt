package org.apache.shiro.spring.boot.jwt.authc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.biz.authc.AuthenticationSuccessHandler;
import org.apache.shiro.biz.authz.principal.ShiroPrincipal;
import org.apache.shiro.biz.utils.SubjectUtils;
import org.apache.shiro.biz.web.servlet.http.HttpStatus;
import org.apache.shiro.spring.boot.jwt.JwtPayloadRepository;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthenticationToken;
import org.apache.shiro.spring.boot.utils.SubjectJwtUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.JSONObject;

/**
 * Handler invoked upon successful JWT authentication. Issues a new JWT via the
 * configured {@link JwtPayloadRepository} and returns the token map as a JSON response.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler, Ordered {

	private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationSuccessHandler.class);

	private JwtPayloadRepository jwtPayloadRepository;
	private boolean checkExpiry = false;

	public JwtAuthenticationSuccessHandler(JwtPayloadRepository jwtPayloadRepository, boolean checkExpiry) {
		this.jwtPayloadRepository = jwtPayloadRepository;
		this.checkExpiry = checkExpiry;
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		return SubjectUtils.isAssignableFrom(token.getClass(), JwtAuthenticationToken.class);
	}

	@Override
	public void onAuthenticationSuccess(AuthenticationToken token, ServletRequest request, ServletResponse response,
			Subject subject) {
		try {
			String tokenString = "";
			if (Objects.nonNull(subject.getPrincipal())
					&& ShiroPrincipal.class.isAssignableFrom(subject.getPrincipal().getClass())) {
				tokenString = getJwtPayloadRepository().issueJwt(token, subject);
			}
			Map<String, Object> tokenMap = SubjectJwtUtils.tokenMap(subject, tokenString);
			WebUtils.toHttp(response).setStatus(HttpStatus.SC_OK);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
			JSONObject.writeJSONString(response.getOutputStream(), tokenMap);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE - 1;
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
