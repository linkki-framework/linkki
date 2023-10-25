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

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Used to read the test configuration for the UI test driver.
 */
public final class DriverProperties {

    private static final String PROTOCOL_HTTP = "http";
    private static final String PROTOCOL_HTTPS = "https";

    private static final String HOST_DEFAULT = "localhost";

    private static final String PORT_HTTP = "80";
    private static final String PORT_HTTPS = "433";
    private static final String PORT_DEFAULT = "8080";

    private DriverProperties() {
        // prevent instantiation
    }

    /**
     * Creates the URL by appending the defined {@link #getTestHostname() host name},
     * {@link #getTestPort() port} and the two given paths.
     */
    public static String getTestUrl(String defaultBasePath, String path) {
        try {
            var testPath = getTestPath(defaultBasePath) + "/" + path;
            return new URI(getTestProtocol(), null, getTestHostname(), getTestPortValue(), testPath, null, null)
                    .toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the {@code test.protocol} property, defaults to "https" in case of port 443 otherwise to
     * "http".
     */
    public static String getTestProtocol() {
        return SystemProperties.get("test.protocol").orElse(getDefaultProtocol());
    }

    private static String getDefaultProtocol() {
        return getTestPort().equals(PORT_HTTPS) ? PROTOCOL_HTTPS : PROTOCOL_HTTP;
    }

    /**
     * Gets the {@code test.hostname} property, defaults to {@code localhost}.
     */
    public static String getTestHostname() {
        return SystemProperties.get("test.hostname").orElse(HOST_DEFAULT);
    }

    /**
     * Gets the {@code test.path} property or the given defaultPath if no property is set. The path
     * always starts with /.
     */
    public static String getTestPath(String defaultPath) {
        var testPath = SystemProperties.get("test.path").orElse(defaultPath);
        if (!testPath.startsWith("/")) {
            testPath = "/" + testPath;
        }
        return testPath;
    }

    /**
     * Gets the defined {@link #getTestPort() test port} or -1 if the http respectively https port is
     * used together with the corresponding protocol.
     * 
     * @return the defined test port or -1 in case the http or https port is used
     */
    private static int getTestPortValue() {
        var testPort = getTestPort();
        if ((getTestProtocol().equals(PROTOCOL_HTTP) && testPort.equals(PORT_HTTP)) ||
                (getTestProtocol().equals(PROTOCOL_HTTPS) && testPort.equals(PORT_HTTPS))) {
            return -1;
        } else {
            return Integer.parseInt(testPort);
        }
    }

    /**
     * Gets the {@code test.port} property, defaults to {@code 8080}.
     */
    public static String getTestPort() {
        return SystemProperties.get("test.port").orElse(PORT_DEFAULT);
    }

    /**
     * Checks if headless browser has to be used by reading the {@code test.headless} property. Headless
     * browser can be used to run a test without browser UI. Defaults to {@code false}.
     */
    public static boolean isHeadless() {
        return SystemProperties.get("test.headless").map(Boolean::valueOf).orElse(false);
    }

}
