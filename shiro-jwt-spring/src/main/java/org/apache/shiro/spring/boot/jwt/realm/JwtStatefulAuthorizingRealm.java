package org.apache.shiro.spring.boot.jwt.realm;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.biz.realm.AbstractAuthorizingRealm;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthorizationToken;

public class JwtStatefulAuthorizingRealm extends AbstractAuthorizingRealm {

	@Override
	public Class<? extends AuthenticationToken> getAuthenticationTokenClass() {
		return JwtAuthorizationToken.class;
	}

}
