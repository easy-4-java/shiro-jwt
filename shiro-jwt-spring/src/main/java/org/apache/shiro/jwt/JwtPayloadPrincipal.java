package org.apache.shiro.jwt;

import org.apache.shiro.biz.authz.principal.ShiroPrincipal;

import io.github.easy4j.jwt.JwtPayload;

/**
 * Principal that wraps a {@link JwtPayload}, extending {@link ShiroPrincipal}
 * with JWT-specific payload data.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
@SuppressWarnings("serial")
public class JwtPayloadPrincipal extends ShiroPrincipal {

	private final JwtPayload payload;

	public JwtPayloadPrincipal(JwtPayload payload) {
		this.payload = payload;
	}

	public JwtPayload getPayload() {
		return payload;
	}

}
