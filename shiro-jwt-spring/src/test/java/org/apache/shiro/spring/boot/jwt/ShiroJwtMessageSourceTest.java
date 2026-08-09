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

import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Unit tests for {@link ShiroJwtMessageSource}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class ShiroJwtMessageSourceTest {

    @Test
    void shouldExtendResourceBundleMessageSource() {
        ShiroJwtMessageSource source = new ShiroJwtMessageSource();
        assertThat(source).isInstanceOf(ResourceBundleMessageSource.class);
    }

    @Test
    void getAccessorShouldReturnNonNullAccessor() {
        MessageSourceAccessor accessor = ShiroJwtMessageSource.getAccessor();
        assertThat(accessor).isNotNull();
    }
}
