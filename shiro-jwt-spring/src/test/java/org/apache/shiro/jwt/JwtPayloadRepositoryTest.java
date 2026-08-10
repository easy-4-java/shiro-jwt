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
package org.apache.shiro.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;

import org.apache.shiro.biz.authz.principal.ShiroPrincipal;
import org.apache.shiro.jwt.token.JwtAuthorizationToken;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link JwtPayloadRepository}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class JwtPayloadRepositoryTest {

    @Test
    void defaultIssueJwtWithShiroPrincipalShouldReturnEmpty() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        ShiroPrincipal principal = new ShiroPrincipal();
        principal.setUserid("user1");
        assertThat(repo.issueJwt(principal)).isEmpty();
    }

    @Test
    void defaultIssueJwtWithUserIdAndProfileShouldReturnEmpty() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        assertThat(repo.issueJwt("user1", new HashMap<>())).isEmpty();
    }

    @Test
    void defaultVerifyTokenStringShouldReturnFalse() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        assertThat(repo.verify("token", false)).isFalse();
    }

    @Test
    void defaultGetPayloadWithTokenShouldReturnNull() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        JwtAuthorizationToken token = new JwtAuthorizationToken("host", "jwt", false);
        assertThat(repo.getPayload(token, false)).isNull();
    }

    @Test
    void defaultGetPayloadWithStringShouldReturnNull() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        assertThat(repo.getPayload("token", false)).isNull();
    }
}
