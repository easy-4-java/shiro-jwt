package org.apache.shiro.jwt;

import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.biz.authz.principal.ShiroPrincipal;
import org.apache.shiro.jwt.token.JwtAuthorizationToken;
import org.apache.shiro.subject.Subject;

import io.github.easy4j.jwt.JwtPayload;

/**
 * Repository interface for issuing, verifying, and parsing JSON Web Tokens (JWT).
 * Provides default (no-op) implementations for all methods.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
public interface JwtPayloadRepository {

	default String issueJwt(AuthenticationToken token, Subject subject) {
		if (subject.getPrincipal() instanceof ShiroPrincipal) {
			ShiroPrincipal principal = (ShiroPrincipal) subject.getPrincipal();
			return issueJwt(principal);
		}
		return "";
	}

	default String issueJwt(ShiroPrincipal principal) {
		return issueJwt(principal.getUserid(), principal.getProfile());
	}

	default String issueJwt(String userId, Map<String, Object> profile) {
		return "";
	}

	default boolean verify(AuthenticationToken token, Subject subject, boolean checkExpiry) throws AuthenticationException {
		return false;
	}

	default boolean verify(String token, boolean checkExpiry) throws AuthenticationException {
		return false;
	}

	default JwtPayload getPayload(JwtAuthorizationToken token, boolean checkExpiry) {
		return null;
	}

	default JwtPayload getPayload(String token, boolean checkExpiry) {
		return null;
	}

}
