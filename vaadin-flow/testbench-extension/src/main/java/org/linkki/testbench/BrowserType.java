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

import static java.util.Objects.isNull;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linkki.testbench.util.DriverProperties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;

/**
 * BrowserType supported by webdriver. Currently only Chrome is supported.
 */
public enum BrowserType {

    CHROME {

        @Override
        public WebDriver getWebdriver(Locale locale) {
            setChromeDriverSystemProperty();

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-search-engine-choice-screen");

            options.setExperimentalOption("prefs", Map.of("intl.accept_languages", locale.getLanguage()));

            LoggingPreferences logs = new LoggingPreferences();
            logs.enable(LogType.PERFORMANCE, Level.ALL);
            options.setCapability("goog:loggingPrefs", logs);

            return new ChromeDriver(options);
        }
    },

    CHROME_HEADLESS {

        @Override
        public WebDriver getWebdriver(Locale locale) {
            var options = new ChromeOptions();
            options.addArguments("--disable-search-engine-choice-screen");
            options.addArguments("--headless=new");
            // supposed to solve "Time out receiving message from renderer: 600.000"
            options.addArguments("--disable-gpu");

            var url = DriverProperties.getTestUrl("", "");
            options.addArguments("--unsafely-treat-insecure-origin-as-secure=" + url);

            options.setExperimentalOption("prefs", Map.of("intl.accept_languages", locale.getLanguage()));

            LoggingPreferences logs = new LoggingPreferences();
            logs.enable(LogType.PERFORMANCE, Level.ALL);
            options.setCapability("goog:loggingPrefs", logs);

            var driver = new ChromeDriver(options);
            LOGGER.info("Chrome driver version: " + driver.getCapabilities().getCapability("browserVersion"));
            return driver;
        }
    };

    private static final Logger LOGGER = Logger.getLogger(BrowserType.class.getName());

    protected void setChromeDriverSystemProperty() {
        String property = "webdriver.chrome.driver";
        if (isNull(System.getProperty(property))) {
            File driver = getDriverForCurrentOS();
            if (!driver.exists()) {
                LOGGER.severe("Driver not present: " + driver.getAbsolutePath() +
                        " - Download the chromedriver binary from https://googlechromelabs.github.io/chrome-for-testing/#stable and make sure you have chrome installed.");
                throw new IllegalStateException("Driver not present: " + driver.getAbsolutePath());
            }

            LOGGER.info("Setting " + property + " to " + driver.getAbsolutePath());
            System.setProperty(property, driver.getAbsolutePath());
        }
    }

    private File getDriverForCurrentOS() {
        File driverDirectory = new File(new File(System.getProperty("user.home"), ".vaadin"), "drivers");
        if (System.getProperty("os.name").startsWith("Windows")) {
            return new File(driverDirectory, "chromedriver.exe");
        } else {
            return new File(driverDirectory, "chromedriver");
        }
    }

    public abstract WebDriver getWebdriver(Locale locale);

}
