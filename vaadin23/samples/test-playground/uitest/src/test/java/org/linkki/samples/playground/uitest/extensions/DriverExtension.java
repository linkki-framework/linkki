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

package org.linkki.samples.playground.uitest.extensions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.linkki.samples.playground.uitest.BrowserType;
import org.linkki.samples.playground.uitest.DriverProperties;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DriverExtension
        implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private final static String DEFAULT_LOCALE = "de";
    private final static String DEFAULT_BASE_PATH = "";
    private final static String DEFAULT_PATH = "";
    private final static boolean DEFAULT_RESTART = false;

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Configuration {

        /**
         * The browser locale.
         * 
         * @see Locale#forLanguageTag(String)
         */
        String locale() default DEFAULT_LOCALE;

        /**
         * The base path of the deployed application. The default of
         * {@link DriverProperties#getTestPath()} will be used if no value has been set.
         */
        String basePath() default DEFAULT_BASE_PATH;

        /**
         * The path of the page that is getting tested.
         * 
         * @see DriverProperties#getTestUrl(String)
         */
        String path() default DEFAULT_PATH;

        /**
         * Whether to restart the browser after every {@code @Test} method. The default is to use one
         * browser instance for each test class.
         */
        boolean restartAfterEveryTest() default DEFAULT_RESTART;
    }

    private WebDriver driver;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (!shouldRestartAfterEveryTest(context)) {
            createDriver(getLocale(context), getUrl(context));
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        if (shouldRestartAfterEveryTest(context)) {
            createDriver(getLocale(context), getUrl(context));
        }
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if (!shouldRestartAfterEveryTest(context)) {
            driver.quit();
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (shouldRestartAfterEveryTest(context)) {
            driver.quit();
        }
    }

    private void createDriver(String locale, String url) {
        BrowserType browserType = DriverProperties.isHeadless() ? BrowserType.CHROME_HEADLESS : BrowserType.CHROME;
        driver = browserType.getWebdriver(Locale.forLanguageTag(locale));
        driver.get(url);
        driver.manage().window().maximize();

        new WebDriverWait(driver, 10).until(d -> ((JavascriptExecutor)d).executeScript("return document.readyState")
                .toString().equals("complete"));
    }

    public WebDriver getDriver() {
        return driver;
    }

    private boolean shouldRestartAfterEveryTest(ExtensionContext context) {
        Configuration config = getConfiguration(context);
        return config != null ? config.restartAfterEveryTest() : DEFAULT_RESTART;
    }

    private String getUrl(ExtensionContext context) {
        Configuration config = getConfiguration(context);

        if (config != null && !config.basePath().equals(DEFAULT_BASE_PATH)) {
            return DriverProperties.getTestUrl(config.basePath(), config.path());
        } else {
            return DriverProperties.getTestUrl(config != null ? config.path() : DEFAULT_PATH);
        }
    }

    private String getLocale(ExtensionContext context) {
        Configuration config = getConfiguration(context);
        return config != null ? config.locale() : DEFAULT_LOCALE;
    }

    private Configuration getConfiguration(ExtensionContext context) {
        return context.getRequiredTestClass().getAnnotation(Configuration.class);
    }
}
