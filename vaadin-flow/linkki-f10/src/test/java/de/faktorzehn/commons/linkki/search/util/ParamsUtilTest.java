/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package de.faktorzehn.commons.linkki.search.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

@SuppressWarnings("deprecation")
class ParamsUtilTest {

    @Test
    void testExpand() {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("key1", "value1");
        params.put("key2", "value2");

        Map<String, List<String>> expanded = ParamsUtil.expand(params);

        assertThat(expanded.keySet()).contains("key1", "key2");
        assertThat(expanded.get("key1")).contains("value1");
        assertThat(expanded.get("key2")).contains("value2");
    }

    @Test
    void testExpand_EmptyValues() {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("key1", null);
        params.put("key2", "value");
        params.put("key3", "");

        Map<String, List<String>> expanded = ParamsUtil.expand(params);

        assertThat(expanded.keySet()).contains("key1", "key2", "key3");
        assertThat(expanded.get("key1")).containsNull();
        assertThat(expanded.get("key2")).contains("value");
        assertThat(expanded.get("key3")).contains("");
    }

    @Test
    void testFlatten() {
        Map<String, List<String>> params = new LinkedHashMap<>();
        params.put("key1", Arrays.asList("value1"));
        params.put("key2", Arrays.asList("value2"));

        Map<String, String> flattened = ParamsUtil.flatten(params);

        assertThat(flattened.keySet()).contains("key1", "key2");
        assertThat(flattened.get("key1")).isEqualTo("value1");
        assertThat(flattened.get("key2")).isEqualTo("value2");
    }

    @Test
    void testFlatten_NullValue() {
        Map<String, List<String>> params = new LinkedHashMap<>();
        params.put("key1", null);

        try {
            ParamsUtil.flatten(params);
            fail("Expected a NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    void testFlatten_EmptyValue() {
        Map<String, List<String>> params = new LinkedHashMap<>();
        params.put("key1", Collections.emptyList());

        try {
            ParamsUtil.flatten(params);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    void testRemoveEmptyValues() {
        Map<String, List<String>> params = new LinkedHashMap<>();
        params.put("singleValue", Arrays.asList("", "", "value"));
        params.put("noValues", Arrays.asList("", null, null));
        params.put("multipleValues", Arrays.asList("", "value1", null, "", "value2"));
        params.put("emptyList", Collections.emptyList());
        params.put("nullList", null);
        // removeEmptyValues may not modify the parameter
        params = Collections.unmodifiableMap(params);

        Map<String, List<String>> sanitized = ParamsUtil.removeEmptyValues(params);

        assertThat(sanitized.keySet()).contains("singleValue", "multipleValues");
        assertThat(sanitized.get("singleValue")).contains("value");
        assertThat(sanitized.get("multipleValues")).contains("value1", "value2");
    }

    @Test
    void testParseIsoDate() {
        assertThat(ParamsUtil.parseIsoDate("2022-01-01")).isEqualTo(Optional.of(LocalDate.of(2022, 1, 1)));
    }

    @Test
    void testParseIsoDate_Null() {
        assertThat(ParamsUtil.parseIsoDate(null)).isEmpty();
    }

    @Test
    void testParseIsoDate_Invalid() {
        assertThat(ParamsUtil.parseIsoDate("xyz")).isEmpty();
    }

    @Test
    void testFormatIsoDate() {
        assertThat(ParamsUtil.formatIsoDate(LocalDate.of(2022, 1, 1))).isEqualTo("2022-01-01");
    }

    @Test
    void testFormatIsoDate_Null() {
        assertThat(ParamsUtil.formatIsoDate(null)).isNullOrEmpty();
    }

    @Test
    void testParseBoolean_True() {
        assertThat(ParamsUtil.parseBoolean("true")).isEqualTo(Optional.of(true));
    }

    @Test
    void testParseBoolean_False() {
        assertThat(ParamsUtil.parseBoolean("false")).isEqualTo(Optional.of(false));
    }

    @Test
    void testParseBoolean_Null() {
        assertThat(ParamsUtil.parseBoolean(null)).isEmpty();
    }

    @Test
    void testFormatBoolean_True() {
        assertThat(ParamsUtil.formatBoolean(true)).isEqualTo("true");
    }

    @Test
    void testFormatBoolean_False() {
        assertThat(ParamsUtil.formatBoolean(false)).isEqualTo("false");
    }

    @Test
    void testFormatBoolean_Null() {
        assertThat(ParamsUtil.formatBoolean(null)).isNull();
    }

}
