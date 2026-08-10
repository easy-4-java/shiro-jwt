package org.apache.shiro.jwt;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

public class ShiroJwtMessageSource extends ResourceBundleMessageSource {

	public ShiroJwtMessageSource() {
		setBasename("org.apache.shiro.jwt.messages");
		setDefaultEncoding("UTF-8");
	}

	public static MessageSourceAccessor getAccessor() {
		return new MessageSourceAccessor(new ShiroJwtMessageSource());
	}

}
