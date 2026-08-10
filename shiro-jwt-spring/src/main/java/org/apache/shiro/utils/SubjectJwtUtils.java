package org.apache.shiro.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.biz.authc.AuthcResponseCode;
import org.apache.shiro.biz.authz.principal.ShiroPrincipal;
import org.apache.shiro.biz.utils.SubjectUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.util.CollectionUtils;

/**
 * Utility class for building JWT token response maps from the authenticated subject.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
public class SubjectJwtUtils extends SubjectUtils {

	private static final String EMPTY = "null";

	public static Map<String, Object> tokenMap(Subject subject, String token) {
		Map<String, Object> tokenMap = new HashMap<String, Object>();
		tokenMap.put("code", AuthcResponseCode.SC_AUTHC_SUCCESS.getCode());
		tokenMap.put("message", "Authentication Success.");
		tokenMap.put("status", "success");

		Object principal = subject.getPrincipal();
		if (Objects.nonNull(principal) && ShiroPrincipal.class.isAssignableFrom(principal.getClass())) {
			ShiroPrincipal shiroPrincipal = (ShiroPrincipal) principal;
			tokenMap.put("initial", shiroPrincipal.isInitial());
			tokenMap.put("nickname", StringUtils.defaultString(shiroPrincipal.getNickname(), EMPTY));
			tokenMap.put("userid", StringUtils.defaultString(shiroPrincipal.getUserid(), EMPTY));
			tokenMap.put("userkey", StringUtils.defaultString(shiroPrincipal.getUserkey(), EMPTY));
			tokenMap.put("usercode", StringUtils.defaultString(shiroPrincipal.getUsercode(), EMPTY));
			tokenMap.put("username", shiroPrincipal.getUsername());
			tokenMap.put("roleid", StringUtils.defaultString(shiroPrincipal.getRoleid(), EMPTY));
			tokenMap.put("role", StringUtils.defaultString(shiroPrincipal.getRole(), EMPTY));
			tokenMap.put("roles",
					CollectionUtils.isEmpty(shiroPrincipal.getRoles()) ? new ArrayList<>() : shiroPrincipal.getRoles());
			tokenMap.put("perms",
					CollectionUtils.isEmpty(shiroPrincipal.getPerms()) ? new ArrayList<>() : shiroPrincipal.getPerms());
			tokenMap.put("profile", CollectionUtils.isEmpty(shiroPrincipal.getProfile()) ? new HashMap<>()
					: shiroPrincipal.getProfile());
			tokenMap.put("faced", shiroPrincipal.isFace());
			tokenMap.put("faceId", StringUtils.defaultString(shiroPrincipal.getFaceId(), EMPTY));
			tokenMap.put("token", token);
		} else {
			tokenMap.put("initial", false);
			tokenMap.put("nickname", "匿名账户");
			tokenMap.put("userid", EMPTY);
			tokenMap.put("userkey", EMPTY);
			tokenMap.put("usercode", EMPTY);
			tokenMap.put("username", EMPTY);
			tokenMap.put("perms", new ArrayList<>());
			tokenMap.put("roleid", EMPTY);
			tokenMap.put("role", EMPTY);
			tokenMap.put("roles", new ArrayList<>());
			tokenMap.put("restricted", false);
			tokenMap.put("profile", new HashMap<>());
			tokenMap.put("faced", false);
			tokenMap.put("faceId", EMPTY);
			tokenMap.put("token", EMPTY);
		}
		return tokenMap;
	}

}
