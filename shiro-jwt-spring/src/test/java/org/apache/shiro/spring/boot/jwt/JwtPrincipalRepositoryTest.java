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

import org.apache.shiro.biz.authz.principal.ShiroPrincipalRepositoryImpl;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link JwtPrincipalRepository}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class JwtPrincipalRepositoryTest {

    @Test
    void shouldExtendShiroPrincipalRepositoryImpl() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        JwtPrincipalRepository principalRepo = new JwtPrincipalRepository(repo);
        assertThat(principalRepo).isInstanceOf(ShiroPrincipalRepositoryImpl.class);
    }

    @Test
    void getJwtPayloadRepositoryShouldReturnConstructorValue() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        JwtPrincipalRepository principalRepo = new JwtPrincipalRepository(repo);
        assertThat(principalRepo.getJwtPayloadRepository()).isSameAs(repo);
    }

    @Test
    void checkExpiryShouldDefaultToFalse() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        JwtPrincipalRepository principalRepo = new JwtPrincipalRepository(repo);
        assertThat(principalRepo.isCheckExpiry()).isFalse();
    }

    @Test
    void setCheckExpiryShouldUpdateValue() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        JwtPrincipalRepository principalRepo = new JwtPrincipalRepository(repo);
        principalRepo.setCheckExpiry(true);
        assertThat(principalRepo.isCheckExpiry()).isTrue();
    }
}
