package org.apache.shiro.spring.boot.jwt;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.biz.authz.principal.ShiroPrincipalRepositoryImpl;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthorizationToken;

import io.github.easy4j.jwt.JwtPayload;

/**
 * Repository that retrieves authentication info by parsing the JWT token
 * and constructing a {@link JwtPayloadPrincipal} from the payload.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
public class JwtPrincipalRepository extends ShiroPrincipalRepositoryImpl {

	private final JwtPayloadRepository jwtPayloadRepository;
	private boolean checkExpiry = false;

	public JwtPrincipalRepository(JwtPayloadRepository jwtPayloadRepository) {
		this.jwtPayloadRepository = jwtPayloadRepository;
	}

	@Override
	public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		JwtAuthorizationToken jwtToken = (JwtAuthorizationToken) token;
		JwtPayload payload = getJwtPayloadRepository().getPayload(jwtToken, isCheckExpiry());
		JwtPayloadPrincipal principal = new JwtPayloadPrincipal(payload);
		principal.setPerms(payload.getPerms());
		principal.setRole(payload.getRkey());
		principal.setRoles(payload.getRoles());
		principal.setRoleid(payload.getRid());
		principal.setUserid(payload.getSubject());
		principal.setUserkey(payload.getUkey());
		principal.setUsercode(payload.getUcode());
		principal.setInitial(payload.isInitial());
		principal.setProfile(payload.getProfile());
		return new SimpleAuthenticationInfo(principal, jwtToken.getCredentials(), "JWT");
	}

	public JwtPayloadRepository getJwtPayloadRepository() {
		return jwtPayloadRepository;
	}

	public boolean isCheckExpiry() {
		return checkExpiry;
	}

	public void setCheckExpiry(boolean checkExpiry) {
		this.checkExpiry = checkExpiry;
	}

}
