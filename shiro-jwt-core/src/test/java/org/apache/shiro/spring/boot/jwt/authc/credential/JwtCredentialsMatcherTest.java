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
package org.apache.shiro.spring.boot.jwt.authc.credential;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link JwtCredentialsMatcher}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class JwtCredentialsMatcherTest {

    @Test
    void shouldImplementCredentialsMatcher() {
        JwtCredentialsMatcher matcher = new JwtCredentialsMatcher();
        assertThat(matcher).isInstanceOf(CredentialsMatcher.class);
    }

    @Test
    void doCredentialsMatchShouldAlwaysReturnTrue() {
        JwtCredentialsMatcher matcher = new JwtCredentialsMatcher();
        AuthenticationToken token = new UsernamePasswordToken("user", "pass");
        AuthenticationInfo info = new SimpleAuthenticationInfo("user", "jwt-token", "realm");
        assertThat(matcher.doCredentialsMatch(token, info)).isTrue();
    }
}
