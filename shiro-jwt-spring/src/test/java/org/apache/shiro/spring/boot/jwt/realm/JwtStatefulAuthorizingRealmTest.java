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
package org.apache.shiro.spring.boot.jwt.realm;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.shiro.biz.realm.AbstractAuthorizingRealm;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthorizationToken;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link JwtStatefulAuthorizingRealm}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class JwtStatefulAuthorizingRealmTest {

    @Test
    void shouldExtendAbstractAuthorizingRealm() {
        JwtStatefulAuthorizingRealm realm = new JwtStatefulAuthorizingRealm();
        assertThat(realm).isInstanceOf(AbstractAuthorizingRealm.class);
    }

    @Test
    void authenticationTokenClassShouldBeJwtAuthorizationToken() {
        JwtStatefulAuthorizingRealm realm = new JwtStatefulAuthorizingRealm();
        assertThat(realm.getAuthenticationTokenClass()).isEqualTo(JwtAuthorizationToken.class);
    }
}
