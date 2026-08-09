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
package org.apache.shiro.spring.boot.jwt.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.shiro.authc.AuthenticationException;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link IncorrectJwtException}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class IncorrectJwtExceptionTest {

    @Test
    void shouldExtendAuthenticationException() {
        assertThat(new IncorrectJwtException()).isInstanceOf(AuthenticationException.class);
    }

    @Test
    void defaultConstructorShouldCreateExceptionWithNullMessage() {
        IncorrectJwtException ex = new IncorrectJwtException();
        assertThat(ex.getMessage()).isNull();
        assertThat(ex.getCause()).isNull();
    }

    @Test
    void messageConstructorShouldPreserveMessage() {
        IncorrectJwtException ex = new IncorrectJwtException("incorrect jwt");
        assertThat(ex.getMessage()).isEqualTo("incorrect jwt");
    }

    @Test
    void causeConstructorShouldPreserveCause() {
        RuntimeException cause = new RuntimeException("root cause");
        IncorrectJwtException ex = new IncorrectJwtException(cause);
        assertThat(ex.getCause()).isSameAs(cause);
    }

    @Test
    void messageAndCauseConstructorShouldPreserveBoth() {
        RuntimeException cause = new RuntimeException("root cause");
        IncorrectJwtException ex = new IncorrectJwtException("incorrect jwt", cause);
        assertThat(ex.getMessage()).isEqualTo("incorrect jwt");
        assertThat(ex.getCause()).isSameAs(cause);
    }
}
