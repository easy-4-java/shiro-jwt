package org.apache.shiro.spring.boot.jwt.authc;

import java.util.Objects;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.biz.web.mgt.SessionCreationEnabledSubjectFactory;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthorizationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;

/**
 * Subject factory that disables session creation for stateless JWT authorization tokens
 * when the remember-me flag is set, ensuring proper stateless authentication semantics.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
public class JwtSubjectFactory extends SessionCreationEnabledSubjectFactory {

	public JwtSubjectFactory(boolean sessionCreationEnabled) {
		super(sessionCreationEnabled);
	}

	@Override
	public Subject createSubject(SubjectContext context) {
		boolean authenticated = context.isAuthenticated();
		if (authenticated) {
			AuthenticationToken token = context.getAuthenticationToken();
			if (Objects.nonNull(token) && token instanceof JwtAuthorizationToken) {
				JwtAuthorizationToken clientToken = (JwtAuthorizationToken) token;
				if (clientToken.isRememberMe()) {
					context.setAuthenticated(false);
				}
			}
		}
		return super.createSubject(context);
	}

}
