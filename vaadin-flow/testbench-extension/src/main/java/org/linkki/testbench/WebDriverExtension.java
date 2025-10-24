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

import java.time.Duration;
import java.util.Locale;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.linkki.testbench.util.DriverProperties;
import org.linkki.testbench.util.ScreenshotUtil;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.vaadin.testbench.TestBench;

/**
 * JUnit test extension for Vaadin Testbench UI tests.
 * <p>
 * Create a static field annotated with {@link RegisterExtension} in your test in order to use this
 * extension, e.g.:
 * 
 * <pre>
 * {@code @RegisterExtension}
 * {@code
 * private static WebDriverExtension driverExtension = new WebDriverExtension(CONTEXT_PATH);
 * }
 * </pre>
 */
public class WebDriverExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback,
        AfterTestExecutionCallback {

    private final boolean headless;
    private final String initialUrl;

    private WebDriver driver;

    /**
     * Creates a {@link WebDriverExtension} with the given context path. The fully qualified URL is
     * built by using the context path together with the {@link DriverProperties}.
     * 
     * @param contextPath the context path that is used to build the fully qualified URL
     */
    public WebDriverExtension(String contextPath) {
        this(DriverProperties.isHeadless(), DriverProperties.getTestUrl(contextPath, ""));
    }

    /**
     * Constructor to explicitly specify the headless mode and the fully qualified URL.
     * 
     * @param headless <code>true</code> for headless mode activated
     * @param initialUrl the fully qualified URL
     */
    public WebDriverExtension(boolean headless, String initialUrl) {
        this.headless = headless;
        this.initialUrl = initialUrl;
    }

    public WebDriver getDriver() {
        if (driver != null) {
            return driver;
        } else {
            throw new IllegalStateException("Driver has not been initialized");
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        UITestConfiguration config = getConfiguration(context);
        if (!config.restartAfterEveryTest()) {
            createDriver(config);
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        UITestConfiguration config = getConfiguration(context);
        if (config.restartAfterEveryTest()) {
            createDriver(config);
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        if (driver == null) {
            // driver setup might have failed
            return;
        }

        boolean testFailed = context.getExecutionException().isPresent();

        // On test failure, take screenshot before e.g. logging out is done by @AfterEach-methods
        if (testFailed) {
            ScreenshotUtil.takeScreenshot(driver, context.getDisplayName());
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if (driver == null) {
            // driver setup might have failed
            return;
        }

        UITestConfiguration config = getConfiguration(context);
        if (!config.restartAfterEveryTest()) {
            driver.quit();
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (driver == null) {
            // driver setup might have failed
            return;
        }

        UITestConfiguration config = getConfiguration(context);
        if (config.restartAfterEveryTest()) {
            driver.quit();
        }
    }

    private void createDriver(UITestConfiguration config) {
        BrowserType browserType = headless ? BrowserType.CHROME_HEADLESS : BrowserType.CHROME;
        driver = TestBench.createDriver(browserType.getWebdriver(Locale.forLanguageTag(config.locale())));
        driver.manage().window().setSize(new Dimension(1440, 900));

        driver.get(initialUrl);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> ((JavascriptExecutor)d).executeScript("return document.readyState")
                        .toString().equals("complete"));
    }

    private UITestConfiguration getConfiguration(ExtensionContext context) {
        UITestConfiguration config = context.getRequiredTestClass().getAnnotation(UITestConfiguration.class);
        if (config != null) {
            return config;
        } else {
            return DefaultUITestConfiguration.class.getAnnotation(UITestConfiguration.class);
        }
    }

    @UITestConfiguration
    private static class DefaultUITestConfiguration {
        // required to access default values of @UITestConfiguration
    }

}
