package org.apache.shiro.spring.boot.jwt.realm;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.biz.realm.AbstractAuthorizingRealm;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthorizationToken;

/**
 * Stateful authorizing realm that accepts {@link JwtAuthorizationToken} for authentication.
 * Session state is preserved across requests.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
public class JwtStatefulAuthorizingRealm extends AbstractAuthorizingRealm {

	@Override
	public Class<? extends AuthenticationToken> getAuthenticationTokenClass() {
		return JwtAuthorizationToken.class;
	}

}
