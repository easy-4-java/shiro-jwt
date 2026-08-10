package org.apache.shiro.jwt.realm;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.biz.realm.AbstractAuthorizingRealm;
import org.apache.shiro.jwt.token.JwtAuthorizationToken;

/**
 * Stateful authorizing realm that accepts {@link JwtAuthorizationToken} for authentication.
 * Session state is preserved across requests.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
public class JwtStatefulAuthorizingRealm extends AbstractAuthorizingRealm {

	@Override
	public Class<? extends AuthenticationToken> getAuthenticationTokenClass() {
		return JwtAuthorizationToken.class;
	}

}
