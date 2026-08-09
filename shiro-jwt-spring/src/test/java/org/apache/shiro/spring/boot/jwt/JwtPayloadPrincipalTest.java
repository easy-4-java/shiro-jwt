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
package org.apache.shiro.spring.boot.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.shiro.biz.authz.principal.ShiroPrincipal;
import org.junit.jupiter.api.Test;

import io.github.easy4j.jwt.JwtPayload;

/**
 * Unit tests for {@link JwtPayloadPrincipal}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class JwtPayloadPrincipalTest {

    @Test
    void shouldExtendShiroPrincipal() {
        JwtPayload payload = new JwtPayload();
        JwtPayloadPrincipal principal = new JwtPayloadPrincipal(payload);
        assertThat(principal).isInstanceOf(ShiroPrincipal.class);
    }

    @Test
    void getPayloadShouldReturnConstructorValue() {
        JwtPayload payload = new JwtPayload();
        JwtPayloadPrincipal principal = new JwtPayloadPrincipal(payload);
        assertThat(principal.getPayload()).isSameAs(payload);
    }
}
