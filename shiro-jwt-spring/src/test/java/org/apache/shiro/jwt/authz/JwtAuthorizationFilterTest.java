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

import org.apache.shiro.biz.web.filter.authz.AbstracAuthorizationFilter;
import org.apache.shiro.jwt.JwtPayloadRepository;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link JwtAuthorizationFilter}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class JwtAuthorizationFilterTest {

    @Test
    void shouldExtendAbstracAuthorizationFilter() {
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
        assertThat(filter).isInstanceOf(AbstracAuthorizationFilter.class);
    }

    @Test
    void defaultAuthorizationHeaderNameShouldBeXAuthorization() {
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
        assertThat(filter.getAuthorizationHeaderName()).isEqualTo("X-Authorization");
    }

    @Test
    void defaultAuthorizationParamNameShouldBeToken() {
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
        assertThat(filter.getAuthorizationParamName()).isEqualTo("token");
    }

    @Test
    void defaultAuthorizationCookieNameShouldBeToken() {
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
        assertThat(filter.getAuthorizationCookieName()).isEqualTo("token");
    }

    @Test
    void checkExpiryShouldDefaultToFalse() {
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
        assertThat(filter.isCheckExpiry()).isFalse();
    }

    @Test
    void settersShouldUpdateFields() {
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
        filter.setAuthorizationHeaderName("Custom-Header");
        filter.setAuthorizationParamName("customParam");
        filter.setAuthorizationCookieName("customCookie");
        filter.setCheckExpiry(true);
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        filter.setJwtPayloadRepository(repo);

        assertThat(filter.getAuthorizationHeaderName()).isEqualTo("Custom-Header");
        assertThat(filter.getAuthorizationParamName()).isEqualTo("customParam");
        assertThat(filter.getAuthorizationCookieName()).isEqualTo("customCookie");
        assertThat(filter.isCheckExpiry()).isTrue();
        assertThat(filter.getJwtPayloadRepository()).isSameAs(repo);
    }
}
