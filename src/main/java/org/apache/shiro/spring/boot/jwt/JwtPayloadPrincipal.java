package org.apache.shiro.spring.boot.jwt;

import org.apache.shiro.biz.authz.principal.ShiroPrincipal;

import io.github.hiwepy.jwt.JwtPayload;

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
