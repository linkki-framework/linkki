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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * Used to read the test configuration for the UI test driver.
 */
public final class DriverProperties {

    private static final String PROTOCOL_HTTP = "http";
    private static final String PROTOCOL_HTTPS = "https";

    private static final String HOST_DEFAULT = "localhost";

    private static final String PORT_HTTP = "80";
    private static final String PORT_HTTPS = "443";
    private static final String PORT_DEFAULT = "8080";

    private static final String PROPERTY_HOSTNAME = "test.hostname";
    private static final String PROPERTY_PROTOCOL = "test.protocol";
    private static final String PROPERTY_PORT = "test.port";
    private static final String PROPERTY_PATH = "test.path";

    private DriverProperties() {
        // prevent instantiation
    }

    /**
     * Creates an URL to navigate to the given paths in tests.
     */
    public static String getTestUrl(String defaultBasePath, String path) {
        return getTestUrl("", defaultBasePath, path);
    }

    /**
     * Creates an URL to navigate to the given paths in the given system in tests.
     */
    public static String getTestUrl(String systemName, String defaultBasePath, String path) {
        var testPath = String.join("/", getTestPath(systemName, defaultBasePath), path);
        return getConfiguredUrl(systemName, testPath);
    }

    public static List<String> getAllTestBaseUrls() {
        return allSystemNames()
                .map(systemName -> getConfiguredUrl(systemName, ""))
                .toList();
    }

    /**
     * Returns the test URL for each configured deployment, using per-system host, port and protocol.
     * {@code test.path.systemName} overrides {@code contextPath} for that system.
     * Returns an empty list if no {@code test.hostname*} properties are configured.
     */
    public static List<String> getAllTestUrls(String contextPath, String path) {
        return allSystemNames()
                .map(systemName -> getTestUrl(systemName, contextPath, path))
                .toList();
    }

    private static Stream<String> allSystemNames() {
        return SystemProperties.getPropertyNamesWithPrefix(PROPERTY_HOSTNAME).stream()
                .map(property -> property.substring(PROPERTY_HOSTNAME.length()))
                .map(systemNameWithDot -> systemNameWithDot.startsWith(".") ? systemNameWithDot.substring(1)
                        : systemNameWithDot);
    }

    private static String getConfiguredUrl(String systemName, String path) {
        try {
            return new URI(getTestProtocol(systemName),
                    null, getTestHostname(systemName), getTestPortValue(systemName), path, null, null)
                            .toString();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Gets the {@code test.protocol} property, defaults to "https" in case of port 443 otherwise to
     * "http".
     */
    public static String getTestProtocol() {
        return getTestProtocol("");
    }

    public static String getTestProtocol(String systemName) {
        return SystemProperties.get(getPropertyForSystem(PROPERTY_PROTOCOL, systemName))
                .orElse(getDefaultProtocol());
    }

    private static String getDefaultProtocol() {
        return getTestPort().equals(PORT_HTTPS) ? PROTOCOL_HTTPS : PROTOCOL_HTTP;
    }

    /**
     * Gets the {@code test.hostname} property, defaults to {@code localhost}.
     */
    public static String getTestHostname() {
        return getTestHostname("");
    }

    public static String getTestHostname(String systemName) {
        return SystemProperties.get(getPropertyForSystem(PROPERTY_HOSTNAME, systemName)).orElse(HOST_DEFAULT);
    }

    /**
     * Gets the {@code test.path} property or the given defaultPath if no property is set. The path
     * always starts with /.
     */
    public static String getTestPath(String defaultPath) {
        return getTestPath("", defaultPath);
    }

    public static String getTestPath(String systemName, String defaultPath) {
        var testPath = SystemProperties.get(getPropertyForSystem(PROPERTY_PATH, systemName)).orElse(defaultPath);
        if (!testPath.startsWith("/")) {
            testPath = "/" + testPath;
        }
        return testPath;
    }

    private static int getTestPortValue(String systemName) {
        var testPort = getTestPort(systemName);
        if ((getTestProtocol(systemName).equals(PROTOCOL_HTTP) && testPort.equals(PORT_HTTP)) ||
                (getTestProtocol(systemName).equals(PROTOCOL_HTTPS) && testPort.equals(PORT_HTTPS))) {
            return -1;
        } else {
            return Integer.parseInt(testPort);
        }
    }

    /**
     * Gets the {@code test.port} property, defaults to {@code 8080}.
     */
    public static String getTestPort() {
        return getTestPort("");
    }

    public static String getTestPort(String systemName) {
        return SystemProperties.get(getPropertyForSystem(PROPERTY_PORT, systemName)).orElse(PORT_DEFAULT);
    }

    /**
     * Gets the property for the given system name, may be blank.
     */
    private static String getPropertyForSystem(String property, String systemName) {
        return property + (StringUtils.isNotBlank(systemName) ? ("." + systemName) : "");
    }

    /**
     * Checks if headless browser has to be used by reading the {@code test.headless} property.
     * Headless browser can be used to run a test without browser UI. Defaults to {@code false}.
     */
    public static boolean isHeadless() {
        return SystemProperties.get("test.headless").map(Boolean::valueOf).orElse(false);
    }

}
