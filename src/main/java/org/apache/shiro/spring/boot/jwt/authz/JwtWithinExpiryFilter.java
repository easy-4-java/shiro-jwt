package org.apache.shiro.spring.boot.jwt.authz;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;

public class JwtWithinExpiryFilter extends JwtAuthorizationFilter {

	@Override
	protected boolean onAccessSuccess(Object mappedValue, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		return true;
	}

}
