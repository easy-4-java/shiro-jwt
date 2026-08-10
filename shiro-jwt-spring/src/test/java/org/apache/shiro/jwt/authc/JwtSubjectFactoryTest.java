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

import org.apache.shiro.biz.web.mgt.SessionCreationEnabledSubjectFactory;
import org.apache.shiro.jwt.token.JwtAuthorizationToken;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link JwtSubjectFactory}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class JwtSubjectFactoryTest {

    @Test
    void shouldExtendSessionCreationEnabledSubjectFactory() {
        JwtSubjectFactory factory = new JwtSubjectFactory(true);
        assertThat(factory).isInstanceOf(SessionCreationEnabledSubjectFactory.class);
    }

    @Test
    void constructorShouldAcceptSessionCreationEnabled() {
        JwtSubjectFactory factoryEnabled = new JwtSubjectFactory(true);
        JwtSubjectFactory factoryDisabled = new JwtSubjectFactory(false);
        assertThat(factoryEnabled).isNotNull();
        assertThat(factoryDisabled).isNotNull();
    }
}
