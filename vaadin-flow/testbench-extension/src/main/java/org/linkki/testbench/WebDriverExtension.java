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
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchDriverProxy;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverExtension.class);

    private final boolean headless;
    private final String initialUrl;

    private final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    /**
     * Creates a {@link WebDriverExtension} with the given context path. The fully qualified URL is
     * built by using the context path together with the {@link DriverProperties}.
     *
     * @param contextPath the context path that is used to build the fully qualified URL
     */
    public WebDriverExtension(String contextPath) {
        this("", contextPath);
    }

    /**
     * Creates a {@link WebDriverExtension} for a given system. The name is used as a suffix to
     * retrieve the system properties, i.e. test.port.systemName.
     */
    public WebDriverExtension(String systemName, String contextPath) {
        this(DriverProperties.isHeadless(), DriverProperties.getTestUrl(systemName, contextPath, ""));
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
        var driverInThread = driver.get();
        if (driverInThread != null) {
            return driverInThread;
        } else {
            throw new IllegalStateException("Driver has not been initialized");
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        UITestConfiguration config = getConfiguration(context);
        if (!config.restartAfterEveryTest()) {
            createTestBenchDriver(config);
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        UITestConfiguration config = getConfiguration(context);
        if (config.restartAfterEveryTest()) {
            createTestBenchDriver(config);
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (driver.get() == null) {
            // driver setup might have failed
            return;
        }

        boolean testFailed = context.getExecutionException().isPresent();

        // On test failure, take screenshot before e.g. logging out is done by @AfterEach-methods
        if (testFailed) {
            ScreenshotUtil.takeScreenshot(driver.get(), context.getDisplayName());
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if (driver.get() == null) {
            // driver setup might have failed
            return;
        }

        UITestConfiguration config = getConfiguration(context);
        if (!config.restartAfterEveryTest()) {
            driver.get().quit();
            driver.remove();
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if (driver.get() == null) {
            // driver setup might have failed
            return;
        }

        UITestConfiguration config = getConfiguration(context);
        if (config.restartAfterEveryTest()) {
            driver.get().quit();
            driver.remove();
        }
    }

    private void createTestBenchDriver(UITestConfiguration config) {
        var browserType = headless ? BrowserType.CHROME_HEADLESS : BrowserType.CHROME;
        var locale = Locale.forLanguageTag(config.locale());

        if (driver.get() == null) {
            var webDriver = createWebDriver(browserType, locale);
            var testbenchDriver = createTestBenchDriver(webDriver);
            driver.set(testbenchDriver);
        }
    }

    private WebDriver createWebDriver(BrowserType browserType, Locale locale) {
        SessionNotCreatedException lastException = null;
        for (var attempt = 1; attempt <= 3; attempt++) {
            try {
                return browserType.getWebdriver(locale);
            } catch (SessionNotCreatedException e) {
                lastException = e;
                LOGGER.debug("Creating Webdriver failed (attempt {}) with {}", attempt, e.getMessage(), e);
            }
        }
        throw lastException;
    }

    private TestBenchDriverProxy createTestBenchDriver(WebDriver webDriver) {
        var newDriver = TestBench.createDriver(webDriver);
        newDriver.manage().window().setSize(new Dimension(1440, 900));

        newDriver.get(initialUrl);
        new WebDriverWait(newDriver, Duration.ofSeconds(10))
                .until(d -> ((JavascriptExecutor)d).executeScript("return document.readyState")
                        .toString().equals("complete"));
        return newDriver;
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
