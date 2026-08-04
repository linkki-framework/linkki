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
package org.linkki.testbench;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

/**
 * Tests that {@link WebDriverExtension#setupDriver(WebDriver)} properly closes WebDriver instances
 * on setup failure to avoid orphaned chromedriver/chrome processes.
 */
class WebDriverExtensionTest {

    static final String WINDOW_SIZE_FAILURE_MESSAGE = "Simulated window size failure";
    static final String NAVIGATION_FAILURE_MESSAGE = "Simulated navigation failure";

    private static final WebDriverExtension EXTENSION = new WebDriverExtension(false, "http://localhost/test") {
        @Override
        protected WebDriver wrapWithTestBench(WebDriver webDriver) {
            return webDriver;
        }
    };

    private WebDriver driver;
    private WebDriver.Options options;
    private WebDriver.Window window;

    @BeforeEach
    void setUp() {
        driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
        options = mock(WebDriver.Options.class);
        window = mock(WebDriver.Window.class);
        when(driver.manage()).thenReturn(options);
        when(options.window()).thenReturn(window);
        when(((JavascriptExecutor)driver).executeScript(any(String.class))).thenReturn("complete");
    }

    @Test
    void testSetupDriver_quitsDriverWhenWindowSizeThrows() {
        doThrow(new RuntimeException(WINDOW_SIZE_FAILURE_MESSAGE)).when(window).setSize(any());

        assertThatThrownBy(() -> EXTENSION.setupDriver(driver))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(WINDOW_SIZE_FAILURE_MESSAGE);

        verify(driver).quit();
    }

    @Test
    void testSetupDriver_quitsDriverWhenNavigationThrows() {
        doThrow(new RuntimeException(NAVIGATION_FAILURE_MESSAGE)).when(driver).get(any());

        assertThatThrownBy(() -> EXTENSION.setupDriver(driver))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(NAVIGATION_FAILURE_MESSAGE);

        verify(driver).quit();
    }

    @Test
    void testSetupDriver_quitsDriverWhenPageNotReady() {
        when(((JavascriptExecutor)driver).executeScript(any(String.class))).thenReturn("loading");

        assertThatThrownBy(() -> EXTENSION.setupDriver(driver))
                .isInstanceOf(TimeoutException.class);

        verify(driver).quit();
    }

    @Test
    void testSetupDriver_doesNotQuitDriverOnSuccess() {
        EXTENSION.setupDriver(driver);

        verify(driver, never()).quit();
    }
}
