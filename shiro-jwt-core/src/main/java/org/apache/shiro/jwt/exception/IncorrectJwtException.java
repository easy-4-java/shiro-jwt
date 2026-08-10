package org.apache.shiro.jwt.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Exception thrown when a JSON Web Token (JWT) is incorrect or malformed.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
@SuppressWarnings("serial")
public class IncorrectJwtException extends AuthenticationException {
	
	public IncorrectJwtException() {
		super();
	}

	public IncorrectJwtException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectJwtException(String message) {
		super(message);
	}

	public IncorrectJwtException(Throwable cause) {
		super(cause);
	}
	
}
