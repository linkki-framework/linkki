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

import static java.util.Objects.isNull;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.lang3.SystemUtils;
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

    CHROME("webdriver.chrome.driver") {

        @Override
        public WebDriver getWebdriver(Locale locale) {
            setSystemPropertyForChrome(this.getDriverName());
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

    CHROME_HEADLESS("webdriver.chrome.driver") {

        @Override
        public WebDriver getWebdriver(Locale locale) {
            setSystemPropertyForChrome(this.getDriverName());
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--lang=" + locale.getLanguage() + "-" + locale.getCountry());
            options.addArguments("--window-size=1280,800");

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

    private String driverName;

    /**
     * Instantiates a new browser type.
     *
     * @param driverName the driver name
     */
    private BrowserType(String driverName) {
        this.driverName = driverName;
    }

    protected void setSystemPropertyForChrome(final String webDriverName) {
        if (isNull(System.getProperty(webDriverName))) {
            String drivername;
            if (SystemUtils.IS_OS_WINDOWS) {
                drivername = "chromedriver.exe";
            } else if (SystemUtils.IS_OS_MAC_OSX) {
                drivername = "chromedriver_osx";
            } else {
                drivername = "chromedriver";
            }
            File file = new File("drivers/" + drivername);
            System.out.println("Setting " + webDriverName + " to " + file.getAbsolutePath());
            System.setProperty(webDriverName, file.getAbsolutePath());
        }
    }

    /**
     * Gets the driver name.
     *
     * @return the driverName
     */
    public String getDriverName() {
        return driverName;
    }

    public WebDriver getWebdriver() {
        return getWebdriver(Locale.GERMANY);
    }

    public abstract WebDriver getWebdriver(Locale locale);

}

