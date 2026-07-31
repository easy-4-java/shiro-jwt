package org.apache.shiro.spring.boot.jwt.realm;

import java.util.List;
import java.util.Set;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.biz.realm.AbstractAuthorizingRealm;
import org.apache.shiro.spring.boot.jwt.JwtPayloadPrincipal;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthorizationToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.util.CollectionUtils;

import io.github.easy4j.jwt.JwtPayload.RolePair;
import com.google.common.collect.Sets;

public class JwtStatelessAuthorizingRealm extends AbstractAuthorizingRealm {

	@Override
	public Class<?> getAuthenticationTokenClass() {
		return JwtAuthorizationToken.class;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		JwtPayloadPrincipal principal = (JwtPayloadPrincipal) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Set<String> sets = Sets.newHashSet();
		List<RolePair> roles = principal.getRoles();
		if (!CollectionUtils.isEmpty(roles)) {
			for (RolePair role : roles) {
				sets.add(role.getKey());
			}
		}
		info.setRoles(sets);
		info.setStringPermissions(principal.getPerms());
		return info;
	}

}
