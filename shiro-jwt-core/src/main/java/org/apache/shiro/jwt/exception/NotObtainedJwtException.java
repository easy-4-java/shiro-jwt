package org.apache.shiro.jwt.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Exception thrown when a required JSON Web Token (JWT) is not present in the request.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
@SuppressWarnings("serial")
public class NotObtainedJwtException extends AuthenticationException {
	
	public NotObtainedJwtException() {
		super();
	}

	public NotObtainedJwtException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotObtainedJwtException(String message) {
		super(message);
	}

	public NotObtainedJwtException(Throwable cause) {
		super(cause);
	}
	
}
