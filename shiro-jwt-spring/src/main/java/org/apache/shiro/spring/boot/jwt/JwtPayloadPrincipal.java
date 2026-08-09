package org.apache.shiro.spring.boot.jwt;

import org.apache.shiro.biz.authz.principal.ShiroPrincipal;

import io.github.easy4j.jwt.JwtPayload;

/**
 * Principal that wraps a {@link JwtPayload}, extending {@link ShiroPrincipal}
 * with JWT-specific payload data.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
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
