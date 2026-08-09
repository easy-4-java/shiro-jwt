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
package org.apache.shiro.spring.boot.jwt.authc;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.shiro.biz.authc.AuthenticationFailureHandler;
import org.apache.shiro.spring.boot.jwt.exception.ExpiredJwtException;
import org.apache.shiro.spring.boot.jwt.exception.IncorrectJwtException;
import org.apache.shiro.spring.boot.jwt.exception.InvalidJwtToken;
import org.apache.shiro.spring.boot.jwt.exception.NotObtainedJwtException;
import org.junit.jupiter.api.Test;
import org.springframework.core.Ordered;

/**
 * Unit tests for {@link JwtAuthenticationFailureHandler}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class JwtAuthenticationFailureHandlerTest {

    @Test
    void shouldImplementAuthenticationFailureHandler() {
        JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
        assertThat(handler).isInstanceOf(AuthenticationFailureHandler.class);
    }

    @Test
    void shouldImplementOrdered() {
        JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
        assertThat(handler).isInstanceOf(Ordered.class);
    }

    @Test
    void orderShouldBeMaxValueMinusOne() {
        JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
        assertThat(handler.getOrder()).isEqualTo(Integer.MAX_VALUE - 1);
    }

    @Test
    void supportsShouldReturnTrueForExpiredJwtException() {
        JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
        assertThat(handler.supports(new ExpiredJwtException())).isTrue();
    }

    @Test
    void supportsShouldReturnTrueForIncorrectJwtException() {
        JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
        assertThat(handler.supports(new IncorrectJwtException())).isTrue();
    }

    @Test
    void supportsShouldReturnTrueForInvalidJwtToken() {
        JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
        assertThat(handler.supports(new InvalidJwtToken())).isTrue();
    }

    @Test
    void supportsShouldReturnTrueForNotObtainedJwtException() {
        JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
        assertThat(handler.supports(new NotObtainedJwtException())).isTrue();
    }
}
