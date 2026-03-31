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
package org.linkki.testbench.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;

@ResourceLock(Resources.SYSTEM_PROPERTIES)
class DriverPropertiesTest {

    private Properties savedProperties;

    @BeforeEach
    void setUp() {
        savedProperties = new Properties();
        savedProperties.putAll(System.getProperties());
        Stream.of("test.protocol", "test.hostname", "test.port", "test.path", "test.headless",
                  "test.hostname.staging", "test.protocol.staging", "test.port.staging", "test.path.staging",
                  "test.hostname.systemA", "test.protocol.systemA", "test.port.systemA",
                  "test.hostname.systemB", "test.protocol.systemB", "test.port.systemB")
                .forEach(System::clearProperty);
    }

    @AfterEach
    void tearDown() {
        System.setProperties(savedProperties);
    }

    @Test
    void testGetTestHostname_Default() {
        assertThat(DriverProperties.getTestHostname()).isEqualTo("localhost");
    }

    @Test
    void testGetTestHostname_Configured() {
        System.setProperty("test.hostname", "example.com");
        assertThat(DriverProperties.getTestHostname()).isEqualTo("example.com");
    }

    @Test
    void testGetTestHostname_WithSystemName() {
        System.setProperty("test.hostname.staging", "staging.example.com");
        assertThat(DriverProperties.getTestHostname("staging")).isEqualTo("staging.example.com");
    }

    @Test
    void testGetTestHostname_WithBlankSystemName_UsesDefault() {
        System.setProperty("test.hostname", "example.com");
        assertThat(DriverProperties.getTestHostname("")).isEqualTo("example.com");
    }

    @Test
    void testGetTestPort_Default() {
        assertThat(DriverProperties.getTestPort()).isEqualTo("8080");
    }

    @Test
    void testGetTestPort_Configured() {
        System.setProperty("test.port", "9090");
        assertThat(DriverProperties.getTestPort()).isEqualTo("9090");
    }

    @Test
    void testGetTestPort_WithSystemName() {
        System.setProperty("test.port.staging", "9090");
        assertThat(DriverProperties.getTestPort("staging")).isEqualTo("9090");
    }

    @Test
    void testGetTestProtocol_DefaultHttp() {
        assertThat(DriverProperties.getTestProtocol()).isEqualTo("http");
    }

    @Test
    void testGetTestProtocol_DefaultHttpsForPort443() {
        System.setProperty("test.port", "443");
        assertThat(DriverProperties.getTestProtocol()).isEqualTo("https");
    }

    @Test
    void testGetTestProtocol_Configured() {
        System.setProperty("test.protocol", "https");
        assertThat(DriverProperties.getTestProtocol()).isEqualTo("https");
    }

    @Test
    void testGetTestProtocol_WithSystemName() {
        System.setProperty("test.protocol.staging", "https");
        assertThat(DriverProperties.getTestProtocol("staging")).isEqualTo("https");
    }

    @Test
    void testGetTestPath_Default() {
        assertThat(DriverProperties.getTestPath("myapp")).isEqualTo("/myapp");
    }

    @Test
    void testGetTestPath_DefaultWithLeadingSlash() {
        assertThat(DriverProperties.getTestPath("/myapp")).isEqualTo("/myapp");
    }

    @Test
    void testGetTestPath_Configured() {
        System.setProperty("test.path", "custompath");
        assertThat(DriverProperties.getTestPath("default")).isEqualTo("/custompath");
    }

    @Test
    void testGetTestPath_WithSystemName() {
        System.setProperty("test.path.staging", "stagingpath");
        assertThat(DriverProperties.getTestPath("staging", "default")).isEqualTo("/stagingpath");
    }

    @Test
    void testIsHeadless_DefaultFalse() {
        assertThat(DriverProperties.isHeadless()).isFalse();
    }

    @Test
    void testIsHeadless_True() {
        System.setProperty("test.headless", "true");
        assertThat(DriverProperties.isHeadless()).isTrue();
    }

    @Test
    void testIsHeadless_False() {
        System.setProperty("test.headless", "false");
        assertThat(DriverProperties.isHeadless()).isFalse();
    }

    @Test
    void testGetTestUrl_WithSystemName_UsesSystemHostAndPort() {
        System.setProperty("test.hostname.staging", "staging.example.com");
        System.setProperty("test.protocol.staging", "https");
        System.setProperty("test.port.staging", "9090");
        assertThat(DriverProperties.getTestUrl("staging", "myapp", "page"))
                .isEqualTo("https://staging.example.com:9090/myapp/page");
    }

    @Test
    void testGetTestUrl_WithSystemName_OmitsStandardHttpsPort() {
        System.setProperty("test.hostname.staging", "staging.example.com");
        System.setProperty("test.protocol.staging", "https");
        System.setProperty("test.port.staging", "443");
        assertThat(DriverProperties.getTestUrl("staging", "myapp", "page"))
                .isEqualTo("https://staging.example.com/myapp/page");
    }

    @Test
    void testGetTestUrl_DefaultConfiguration() {
        assertThat(DriverProperties.getTestUrl("myapp", "page"))
                .isEqualTo("http://localhost:8080/myapp/page");
    }

    @Test
    void testGetTestUrl_CustomConfiguration() {
        System.setProperty("test.hostname", "example.com");
        System.setProperty("test.port", "9090");
        System.setProperty("test.protocol", "https");
        assertThat(DriverProperties.getTestUrl("myapp", "page"))
                .isEqualTo("https://example.com:9090/myapp/page");
    }

    @Test
    void testGetTestUrl_CustomPath() {
        System.setProperty("test.path", "custombase");
        assertThat(DriverProperties.getTestUrl("default", "page"))
                .isEqualTo("http://localhost:8080/custombase/page");
    }

    @Test
    void testGetTestUrl_HttpPort80_OmitsPort() {
        System.setProperty("test.protocol", "http");
        System.setProperty("test.port", "80");
        assertThat(DriverProperties.getTestUrl("app", "page"))
                .isEqualTo("http://localhost/app/page");
    }

    @Test
    void testGetTestUrl_HttpsPort443_OmitsPort() {
        System.setProperty("test.protocol", "https");
        System.setProperty("test.port", "443");
        assertThat(DriverProperties.getTestUrl("app", "page"))
                .isEqualTo("https://localhost/app/page");
    }

    @Test
    void testGetAllTestBaseUrls_Empty() {
        assertThat(DriverProperties.getAllTestBaseUrls()).isEmpty();
    }

    @Test
    void testGetAllTestBaseUrls_NoSystemName() {
        System.setProperty("test.hostname", "app-host");
        System.setProperty("test.protocol", "https");
        System.setProperty("test.port", "8080");
        assertThat(DriverProperties.getAllTestBaseUrls())
                .containsExactly("https://app-host:8080");
    }

    @Test
    void testGetAllTestBaseUrls_WithEntries() {
        System.setProperty("test.hostname.systemA", "hostA");
        System.setProperty("test.protocol.systemA", "https");
        System.setProperty("test.port.systemA", "8080");
        System.setProperty("test.hostname.systemB", "hostB");
        System.setProperty("test.protocol.systemB", "http");
        System.setProperty("test.port.systemB", "8081");
        assertThat(DriverProperties.getAllTestBaseUrls())
                .containsExactlyInAnyOrder("https://hostA:8080",
                                           "http://hostB:8081");
    }

    @Test
    void testGetAllTestUrls_Empty() {
        assertThat(DriverProperties.getAllTestUrls("myapp", "page")).isEmpty();
    }

    @Test
    void testGetAllTestUrls_NoSystemName() {
        System.setProperty("test.hostname", "app-host");
        System.setProperty("test.protocol", "https");
        System.setProperty("test.port", "8080");
        assertThat(DriverProperties.getAllTestUrls("myapp", "page"))
                .containsExactly("https://app-host:8080/myapp/page");
    }

    @Test
    void testGetAllTestUrls_WithEntries() {
        System.setProperty("test.hostname.systemA", "hostA");
        System.setProperty("test.protocol.systemA", "https");
        System.setProperty("test.port.systemA", "8080");
        System.setProperty("test.hostname.systemB", "hostB");
        System.setProperty("test.protocol.systemB", "http");
        System.setProperty("test.port.systemB", "8081");
        assertThat(DriverProperties.getAllTestUrls("myapp", "page"))
                .containsExactlyInAnyOrder("https://hostA:8080/myapp/page",
                                           "http://hostB:8081/myapp/page");
    }

    @Test
    void testGetAllTestUrls_WithPathOverride() {
        System.setProperty("test.hostname.staging", "staging.example.com");
        System.setProperty("test.port.staging", "9090");
        System.setProperty("test.protocol.staging", "https");
        System.setProperty("test.path.staging", "custom-path");
        assertThat(DriverProperties.getAllTestUrls("default-app", "playground"))
                .containsExactly("https://staging.example.com:9090/custom-path/playground");
    }

    @Test
    void testUnresolvedMavenProperty_IsIgnored() {
        System.setProperty("test.hostname", "${test.hostname}");
        assertThat(DriverProperties.getTestHostname()).isEqualTo("localhost");
    }
}
