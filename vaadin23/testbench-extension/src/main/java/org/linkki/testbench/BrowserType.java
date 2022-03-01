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

import static java.util.Objects.isNull;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
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

            Map<String, Object> prefs = new HashMap<>();
            prefs.put("intl.accept_languages", locale.getLanguage());
            options.setExperimentalOption("prefs", prefs);

            LoggingPreferences logs = new LoggingPreferences();
            logs.enable(LogType.PERFORMANCE, Level.ALL);
            options.setCapability("goog:loggingPrefs", logs);

            return new ChromeDriver(options);
        }
    },

    CHROME_HEADLESS {

        @Override
        public WebDriver getWebdriver(Locale locale) {
            setChromeDriverSystemProperty();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            // supposed to solve "Time out receiving message from renderer: 600.000"
            options.addArguments("--disable-gpu");
            options.addArguments("--lang=" + locale.getLanguage() + "-" + locale.getCountry());

            LoggingPreferences logs = new LoggingPreferences();
            logs.enable(LogType.PERFORMANCE, Level.ALL);
            options.setCapability("goog:loggingPrefs", logs);

            Map<String, String> environment = new HashMap<>();
            environment.put("LANGUAGE", locale.getLanguage() + "_" + locale.getCountry());
            ChromeDriverService service = new ChromeDriverService.Builder().usingAnyFreePort()
                    .withEnvironment(environment).build();

            return new ChromeDriver(service, options);
        }
    };

    protected void setChromeDriverSystemProperty() {
        String property = "webdriver.chrome.driver";
        if (isNull(System.getProperty(property))) {
            String driver = new File("drivers/" + getDriverForCurrentOS()).getAbsolutePath();
            System.out.println("Setting " + property + " to " + driver);
            System.setProperty(property, driver);
        }
    }

    private String getDriverForCurrentOS() {
        String os = System.getProperty("os.name");

        if (os.startsWith("Windows")) {
            return "chromedriver.exe";
        } else if (os.startsWith("Mac OS X")) {
            return "chromedriver_osx";
        } else {
            return "chromedriver";
        }
    }

    public abstract WebDriver getWebdriver(Locale locale);

}
