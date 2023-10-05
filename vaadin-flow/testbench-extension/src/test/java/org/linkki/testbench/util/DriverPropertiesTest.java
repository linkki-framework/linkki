/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.linkki.testbench.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DriverPropertiesTest {

    @BeforeAll
    static void setProperties() {
        System.setProperty("test.protocol", "https");
        System.setProperty("test.hostname", "example.com");
        System.setProperty("test.port", "8080");
    }

    @Test
    void testGetTestUrl() {
        String url = DriverProperties.getTestUrl("base", "target");

        assertThat(url).isEqualTo("https://example.com:8080/base/target");
    }

    @Test
    void testGetTestUrl_LeadingSlash() {
        String url = DriverProperties.getTestUrl("/base", "target");

        assertThat(url).isEqualTo("https://example.com:8080/base/target");
    }

}
