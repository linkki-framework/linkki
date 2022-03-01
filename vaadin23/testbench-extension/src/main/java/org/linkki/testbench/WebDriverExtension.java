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
package org.linkki.testbench;

import java.util.Locale;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.linkki.testbench.util.ScreenshotUtil;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.vaadin.testbench.TestBench;

public class WebDriverExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private final boolean headless;
    private final String initialUrl;

    private WebDriver driver;

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
    public void afterAll(ExtensionContext context) {
        UITestConfiguration config = getConfiguration(context);
        if (!config.restartAfterEveryTest()) {
            driver.quit();
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        UITestConfiguration config = getConfiguration(context);
        boolean testFailed = context.getExecutionException().isPresent();

        if (testFailed) {
            ScreenshotUtil.takeScreenshot(driver, context.getDisplayName());
        }

        if (config.restartAfterEveryTest()) {
            driver.quit();
        }
    }

    private void createDriver(UITestConfiguration config) {
        BrowserType browserType = headless ? BrowserType.CHROME_HEADLESS : BrowserType.CHROME;
        driver = TestBench.createDriver(browserType.getWebdriver(Locale.forLanguageTag(config.locale())));
        driver.manage().window().setSize(new Dimension(1440, 900));

        driver.get(initialUrl);
        new WebDriverWait(driver, 10).until(d -> ((JavascriptExecutor)d).executeScript("return document.readyState")
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
