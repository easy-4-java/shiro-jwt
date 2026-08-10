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
package org.apache.shiro.jwt.token;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.shiro.authc.HostAuthenticationToken;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link JwtAuthorizationToken}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class JwtAuthorizationTokenTest {

    @Test
    void shouldImplementHostAuthenticationToken() {
        JwtAuthorizationToken token = new JwtAuthorizationToken("127.0.0.1", "jwt-token", false);
        assertThat(token).isInstanceOf(HostAuthenticationToken.class);
    }

    @Test
    void principalShouldReturnToken() {
        JwtAuthorizationToken token = new JwtAuthorizationToken("127.0.0.1", "jwt-token-value", false);
        assertThat(token.getPrincipal()).isEqualTo("jwt-token-value");
    }

    @Test
    void credentialsShouldReturnToken() {
        JwtAuthorizationToken token = new JwtAuthorizationToken("127.0.0.1", "jwt-token-value", false);
        assertThat(token.getCredentials()).isEqualTo("jwt-token-value");
    }

    @Test
    void hostShouldReturnConstructorValue() {
        JwtAuthorizationToken token = new JwtAuthorizationToken("192.168.1.1", "jwt-token", true);
        assertThat(token.getHost()).isEqualTo("192.168.1.1");
    }

    @Test
    void tokenShouldReturnConstructorValue() {
        JwtAuthorizationToken token = new JwtAuthorizationToken("127.0.0.1", "my-jwt", false);
        assertThat(token.getToken()).isEqualTo("my-jwt");
    }

    @Test
    void rememberMeShouldReturnConstructorValue() {
        JwtAuthorizationToken tokenTrue = new JwtAuthorizationToken("127.0.0.1", "jwt", true);
        JwtAuthorizationToken tokenFalse = new JwtAuthorizationToken("127.0.0.1", "jwt", false);
        assertThat(tokenTrue.isRememberMe()).isTrue();
        assertThat(tokenFalse.isRememberMe()).isFalse();
    }
}
