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
package org.apache.shiro.jwt.realm;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.shiro.biz.realm.AbstractAuthorizingRealm;
import org.apache.shiro.jwt.token.JwtAuthorizationToken;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link JwtStatelessAuthorizingRealm}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class JwtStatelessAuthorizingRealmTest {

    @Test
    void shouldExtendAbstractAuthorizingRealm() {
        JwtStatelessAuthorizingRealm realm = new JwtStatelessAuthorizingRealm();
        assertThat(realm).isInstanceOf(AbstractAuthorizingRealm.class);
    }

    @Test
    void authenticationTokenClassShouldBeJwtAuthorizationToken() {
        JwtStatelessAuthorizingRealm realm = new JwtStatelessAuthorizingRealm();
        assertThat(realm.getAuthenticationTokenClass()).isEqualTo(JwtAuthorizationToken.class);
    }
}
