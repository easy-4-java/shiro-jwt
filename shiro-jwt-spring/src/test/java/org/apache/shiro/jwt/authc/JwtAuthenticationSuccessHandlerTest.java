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
package org.apache.shiro.jwt.authc;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.shiro.biz.authc.AuthenticationSuccessHandler;
import org.apache.shiro.jwt.JwtPayloadRepository;
import org.apache.shiro.jwt.token.JwtAuthenticationToken;
import org.junit.jupiter.api.Test;
import org.springframework.core.Ordered;

/**
 * Unit tests for {@link JwtAuthenticationSuccessHandler}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class JwtAuthenticationSuccessHandlerTest {

    @Test
    void shouldImplementAuthenticationSuccessHandler() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        JwtAuthenticationSuccessHandler handler = new JwtAuthenticationSuccessHandler(repo, false);
        assertThat(handler).isInstanceOf(AuthenticationSuccessHandler.class);
    }

    @Test
    void shouldImplementOrdered() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        JwtAuthenticationSuccessHandler handler = new JwtAuthenticationSuccessHandler(repo, false);
        assertThat(handler).isInstanceOf(Ordered.class);
    }

    @Test
    void orderShouldBeMaxValueMinusOne() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        JwtAuthenticationSuccessHandler handler = new JwtAuthenticationSuccessHandler(repo, false);
        assertThat(handler.getOrder()).isEqualTo(Integer.MAX_VALUE - 1);
    }

    @Test
    void supportsShouldReturnTrueForJwtAuthenticationToken() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        JwtAuthenticationSuccessHandler handler = new JwtAuthenticationSuccessHandler(repo, false);
        JwtAuthenticationToken token = new JwtAuthenticationToken("user", "pass", false);
        assertThat(handler.supports(token)).isTrue();
    }

    @Test
    void getJwtPayloadRepositoryShouldReturnConstructorValue() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        JwtAuthenticationSuccessHandler handler = new JwtAuthenticationSuccessHandler(repo, true);
        assertThat(handler.getJwtPayloadRepository()).isSameAs(repo);
    }

    @Test
    void isCheckExpiryShouldReturnConstructorValue() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        JwtAuthenticationSuccessHandler handler = new JwtAuthenticationSuccessHandler(repo, true);
        assertThat(handler.isCheckExpiry()).isTrue();
    }

    @Test
    void settersShouldUpdateFields() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        JwtAuthenticationSuccessHandler handler = new JwtAuthenticationSuccessHandler(repo, false);
        JwtPayloadRepository newRepo = new JwtPayloadRepository() {};
        handler.setJwtPayloadRepository(newRepo);
        handler.setCheckExpiry(true);
        assertThat(handler.getJwtPayloadRepository()).isSameAs(newRepo);
        assertThat(handler.isCheckExpiry()).isTrue();
    }
}
