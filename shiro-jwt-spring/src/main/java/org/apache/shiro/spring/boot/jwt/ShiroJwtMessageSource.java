package org.apache.shiro.spring.boot.jwt;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

public class ShiroJwtMessageSource extends ResourceBundleMessageSource {

	public ShiroJwtMessageSource() {
		setBasename("org.apache.shiro.spring.boot.jwt.messages");
		setDefaultEncoding("UTF-8");
	}

	public static MessageSourceAccessor getAccessor() {
		return new MessageSourceAccessor(new ShiroJwtMessageSource());
	}

}
