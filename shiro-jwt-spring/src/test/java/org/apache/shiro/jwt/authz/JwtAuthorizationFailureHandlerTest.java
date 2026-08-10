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
package org.apache.shiro.jwt.authz;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.shiro.biz.authz.AuthorizationFailureHandler;
import org.apache.shiro.jwt.exception.ExpiredJwtException;
import org.apache.shiro.jwt.exception.IncorrectJwtException;
import org.apache.shiro.jwt.exception.InvalidJwtToken;
import org.apache.shiro.jwt.exception.NotObtainedJwtException;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link JwtAuthorizationFailureHandler}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class JwtAuthorizationFailureHandlerTest {

    @Test
    void shouldImplementAuthorizationFailureHandler() {
        JwtAuthorizationFailureHandler handler = new JwtAuthorizationFailureHandler();
        assertThat(handler).isInstanceOf(AuthorizationFailureHandler.class);
    }

    @Test
    void supportsShouldReturnTrueForExpiredJwtException() {
        JwtAuthorizationFailureHandler handler = new JwtAuthorizationFailureHandler();
        assertThat(handler.supports(new ExpiredJwtException())).isTrue();
    }

    @Test
    void supportsShouldReturnTrueForIncorrectJwtException() {
        JwtAuthorizationFailureHandler handler = new JwtAuthorizationFailureHandler();
        assertThat(handler.supports(new IncorrectJwtException())).isTrue();
    }

    @Test
    void supportsShouldReturnTrueForInvalidJwtToken() {
        JwtAuthorizationFailureHandler handler = new JwtAuthorizationFailureHandler();
        assertThat(handler.supports(new InvalidJwtToken())).isTrue();
    }

    @Test
    void supportsShouldReturnTrueForNotObtainedJwtException() {
        JwtAuthorizationFailureHandler handler = new JwtAuthorizationFailureHandler();
        assertThat(handler.supports(new NotObtainedJwtException())).isTrue();
    }

    @Test
    void orderShouldBeMaxValueMinusOne() {
        JwtAuthorizationFailureHandler handler = new JwtAuthorizationFailureHandler();
        assertThat(handler.getOrder()).isEqualTo(Integer.MAX_VALUE - 1);
    }
}
