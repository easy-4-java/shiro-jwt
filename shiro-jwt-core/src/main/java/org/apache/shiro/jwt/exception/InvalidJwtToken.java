package org.apache.shiro.jwt.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Exception thrown when a JSON Web Token (JWT) is invalid or cannot be parsed.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
@SuppressWarnings("serial")
public class InvalidJwtToken extends AuthenticationException {
	
	public InvalidJwtToken() {
		super();
	}

	public InvalidJwtToken(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidJwtToken(String message) {
		super(message);
	}

	public InvalidJwtToken(Throwable cause) {
		super(cause);
	}
	
}
