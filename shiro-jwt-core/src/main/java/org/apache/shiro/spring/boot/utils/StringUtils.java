/*
 * Copyright (c) 2018, Loong Wan (https://github.com/loong10k).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.shiro.spring.boot.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * String utility methods for tokenizing configuration location paths.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
public class StringUtils {

	/**
	 * Any number of these characters are considered delimiters between multiple
	 * context config paths in a single String value.
	 */
	public static String CONFIG_LOCATION_DELIMITERS = ",; \t\n";
	
	public static String[] tokenizeToStringArray(String str) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(str)) {
			return new String[0];
		}
		StringTokenizer tokenizer = new StringTokenizer(str, CONFIG_LOCATION_DELIMITERS);
		List<String> tokens = new ArrayList<>();
		while (tokenizer.hasMoreTokens()) {
			tokens.add(tokenizer.nextToken());
		}
		return tokens.toArray(new String[0]);
	}
	
}
