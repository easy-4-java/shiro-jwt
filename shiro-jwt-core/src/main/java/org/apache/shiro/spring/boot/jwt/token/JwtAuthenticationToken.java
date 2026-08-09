package org.apache.shiro.spring.boot.jwt.token;

import org.apache.shiro.biz.authc.token.DefaultAuthenticationToken;

/**
 * Authentication token for JWT-based login, carrying username/password credentials
 * along with optional captcha, remember-me flag, and client host information.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
@SuppressWarnings("serial")
public class JwtAuthenticationToken extends DefaultAuthenticationToken {

	public JwtAuthenticationToken() {
		super();
	}

	public JwtAuthenticationToken(String username, String password, boolean rememberMe) {
		super(username, password, rememberMe);
	}

	public JwtAuthenticationToken(String username, String password, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
	}

	public JwtAuthenticationToken(String username, String password, String captcha, String host) {
		super(username, password, captcha, host);
	}

	public JwtAuthenticationToken(String username, String password, String captcha, boolean rememberMe, String host) {
		super(username, password, captcha, rememberMe, host);
	}

	public JwtAuthenticationToken(String username, char[] password, boolean rememberMe) {
		super(username, password, rememberMe);
	}

	public JwtAuthenticationToken(String username, char[] password, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
	}

	public JwtAuthenticationToken(String username, char[] password, String captcha, String host) {
		super(username, password, captcha, host);
	}

	public JwtAuthenticationToken(String username, char[] password, String captcha, boolean rememberMe, String host) {
		super(username, password, captcha, rememberMe, host);
	}

}
