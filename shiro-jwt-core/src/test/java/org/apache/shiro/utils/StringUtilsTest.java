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
package org.apache.shiro.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link StringUtils}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class StringUtilsTest {

    @Test
    void tokenizeToStringArrayShouldHandleNull() {
        String[] result = StringUtils.tokenizeToStringArray(null);
        assertThat(result).isEmpty();
    }

    @Test
    void tokenizeToStringArrayShouldHandleEmpty() {
        String[] result = StringUtils.tokenizeToStringArray("");
        assertThat(result).isEmpty();
    }

    @Test
    void tokenizeToStringArrayShouldSplitByComma() {
        String[] result = StringUtils.tokenizeToStringArray("a,b,c");
        assertThat(result).containsExactly("a", "b", "c");
    }

    @Test
    void tokenizeToStringArrayShouldSplitBySemicolon() {
        String[] result = StringUtils.tokenizeToStringArray("a;b;c");
        assertThat(result).containsExactly("a", "b", "c");
    }

    @Test
    void tokenizeToStringArrayShouldSplitBySpace() {
        String[] result = StringUtils.tokenizeToStringArray("a b c");
        assertThat(result).containsExactly("a", "b", "c");
    }

    @Test
    void tokenizeToStringArrayShouldSplitByTab() {
        String[] result = StringUtils.tokenizeToStringArray("a\tb\tc");
        assertThat(result).containsExactly("a", "b", "c");
    }

    @Test
    void tokenizeToStringArrayShouldSplitByMixedDelimiters() {
        String[] result = StringUtils.tokenizeToStringArray("a,b;c d\te");
        assertThat(result).containsExactly("a", "b", "c", "d", "e");
    }

    @Test
    void configLocationDelimitersShouldContainExpectedChars() {
        assertThat(StringUtils.CONFIG_LOCATION_DELIMITERS).contains(",");
        assertThat(StringUtils.CONFIG_LOCATION_DELIMITERS).contains(";");
        assertThat(StringUtils.CONFIG_LOCATION_DELIMITERS).contains(" ");
    }
}
