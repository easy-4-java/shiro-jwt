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
package org.apache.shiro.spring.boot.jwt.token;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.shiro.biz.authc.token.DefaultAuthenticationToken;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link JwtAuthenticationToken}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class JwtAuthenticationTokenTest {

    @Test
    void shouldExtendDefaultAuthenticationToken() {
        JwtAuthenticationToken token = new JwtAuthenticationToken();
        assertThat(token).isInstanceOf(DefaultAuthenticationToken.class);
    }

    @Test
    void defaultConstructorShouldCreateEmptyToken() {
        JwtAuthenticationToken token = new JwtAuthenticationToken();
        assertThat(token.getPrincipal()).isNull();
    }

    @Test
    void constructorWithUsernameAndPasswordShouldSetPrincipal() {
        JwtAuthenticationToken token = new JwtAuthenticationToken("user", "pass", false);
        assertThat(token.getPrincipal()).isEqualTo("user");
    }

    @Test
    void constructorWithCharPasswordShouldSetPrincipal() {
        JwtAuthenticationToken token = new JwtAuthenticationToken("user", "pass".toCharArray(), false);
        assertThat(token.getPrincipal()).isEqualTo("user");
    }

    @Test
    void constructorWithHostShouldPreserveHost() {
        JwtAuthenticationToken token = new JwtAuthenticationToken("user", "pass", false, "192.168.1.1");
        assertThat(token.getHost()).isEqualTo("192.168.1.1");
    }

    @Test
    void constructorWithCaptchaAndHostShouldPreserveValues() {
        JwtAuthenticationToken token = new JwtAuthenticationToken("user", "pass", "captcha", "host");
        assertThat(token.getPrincipal()).isEqualTo("user");
    }

    @Test
    void constructorWithCaptchaRememberMeAndHostShouldPreserveValues() {
        JwtAuthenticationToken token = new JwtAuthenticationToken("user", "pass", "captcha", true, "host");
        assertThat(token.getPrincipal()).isEqualTo("user");
    }

    @Test
    void constructorWithCharPasswordAndHostShouldPreserveValues() {
        JwtAuthenticationToken token = new JwtAuthenticationToken("user", "pass".toCharArray(), true, "host");
        assertThat(token.getHost()).isEqualTo("host");
    }

    @Test
    void constructorWithCharPasswordCaptchaAndHostShouldPreserveValues() {
        JwtAuthenticationToken token = new JwtAuthenticationToken("user", "pass".toCharArray(), "captcha", "host");
        assertThat(token.getPrincipal()).isEqualTo("user");
    }

    @Test
    void constructorWithCharPasswordCaptchaRememberMeAndHostShouldPreserveValues() {
        JwtAuthenticationToken token = new JwtAuthenticationToken("user", "pass".toCharArray(), "captcha", true, "host");
        assertThat(token.getPrincipal()).isEqualTo("user");
    }
}
