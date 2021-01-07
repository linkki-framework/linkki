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
package org.linkki.samples.playground.uitest;

/**
 * Used to read the test configuration from {@link SystemProperties}.
 */
public final class DriverProperties {

    private DriverProperties() {
        // prevent instantiation
    }

    /**
     * Create the URL by appending {@link #getTestHostname() host name}, {@link #getTestPort() port},
     * {@link #getTestPath() path} and the given path.
     */
    public static String getTestUrl(String path) {
        return String.format("http://%s:%s/%s/%s", getTestHostname(), getTestPort(), getTestPath(), path);
    }

    /**
     * Gets the {@code test.port} property, defaults to {@code 8080}.
     */
    public static String getTestPort() {
        return SystemProperties.get("test.port").orElse("8080");
    }

    /**
     * Gets the {@code test.path} property, defaults to {@code linkki-sample-test-playground-vaadin8}
     */
    public static String getTestPath() {
        return SystemProperties.get("test.path").orElse("linkki-sample-test-playground-vaadin14");
    }

    /**
     * Gets the {@code test.hostname} property, defaults to {@code localhost}
     */
    public static String getTestHostname() {
        return SystemProperties.get("test.hostname").orElse("localhost");
    }

    /**
     * Checks if headless browser has to be used by reading the {@code test.headless} property. Headless
     * browser can be used to run a test without browser UI. Defaults to {@code false}.
     */
    public static boolean isHeadless() {
        return SystemProperties.get("test.headless").map(Boolean::valueOf).orElse(false);
    }

}
