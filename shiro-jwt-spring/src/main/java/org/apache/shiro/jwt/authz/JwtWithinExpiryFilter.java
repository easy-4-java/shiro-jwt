package org.apache.shiro.jwt.authz;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;

/**
 * JWT authorization filter that always grants access on successful authentication,
 * used when JWT expiry verification is handled separately.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
public class JwtWithinExpiryFilter extends JwtAuthorizationFilter {

	@Override
	protected boolean onAccessSuccess(Object mappedValue, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		return true;
	}

}
