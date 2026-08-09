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
package org.apache.shiro.spring.boot.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link JSONResult}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class JSONResultTest {

    @Test
    void fillResultStringShouldContainStatus() {
        String result = JSONResult.fillResultString(200, "ok", "data");
        assertThat(result).contains("\"status\":200");
    }

    @Test
    void fillResultStringShouldContainMessage() {
        String result = JSONResult.fillResultString(200, "success", null);
        assertThat(result).contains("\"message\":\"success\"");
    }

    @Test
    void fillResultStringShouldContainResult() {
        String result = JSONResult.fillResultString(200, "ok", "test-data");
        assertThat(result).contains("\"result\":\"test-data\"");
    }

    @Test
    void fillResultStringShouldHandleNullResult() {
        String result = JSONResult.fillResultString(500, "error", null);
        assertThat(result).contains("\"status\":500");
        assertThat(result).contains("\"message\":\"error\"");
    }

    @Test
    void fillResultStringShouldHandleIntegerResult() {
        String result = JSONResult.fillResultString(200, "ok", 42);
        assertThat(result).contains("\"result\":42");
    }
}
