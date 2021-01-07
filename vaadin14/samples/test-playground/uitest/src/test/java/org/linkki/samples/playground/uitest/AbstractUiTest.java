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

import java.awt.MenuBar;
import java.awt.TextField;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.menubar.testbench.MenuBarElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;

/**
 * Helper class facilitates to use various Testbench functionality for UI testing. It helps to get
 * required web driver to launch the application on specific browser. UI test is done in this browser by
 * accessing various elements within the opened web page and by accessing their properties. SetUp method
 * must be called at the beginning of the test.
 */
public class AbstractUiTest extends TestBenchTestCase {

    /** The default application's URL path. */
    public static final String MAIN_URL_PATH = "";

    private final String urlPath;

    public AbstractUiTest() {
        this(MAIN_URL_PATH);
    }

    public AbstractUiTest(String urlPath) {
        this.urlPath = urlPath;
    }

    @BeforeEach
    public void setUp() {
        setUp(Locale.GERMANY);
    }

    protected void setUp(Locale locale) {
        BrowserType browserType = DriverProperties.isHeadless() ? BrowserType.CHROME_HEADLESS : BrowserType.CHROME;
        WebDriver webDriver = browserType.getWebdriver(locale);
        setDriver(TestBench.createDriver(webDriver));
        String url = DriverProperties.getTestUrl(urlPath);
        startApplication(url);
        waitUntil(d -> ((JavascriptExecutor)d).executeScript("return document.readyState")
                .toString().equals("complete"));
    }

    /**
     * Starts the application on the browser with the given URL and maximizes the browser.
     *
     * @param url URL of the application
     * @see DriverProperties#getTestUrl(String)
     */
    public void startApplication(String url) {
        if (getDriver() != null) {
            System.out.println("Starting application on " + url);
            getDriver().get(url);
            maximize();
        }
    }

    /**
     * Closes the browser
     */
    @AfterEach
    public void closeBrowser() {
        getDriver().quit();
    }

    /**
     * Clicks the {@link Button} with the given ID.
     *
     * @param id ID of the {@link Button} that should be clicked
     */
    public void clickButton(String id) {
        $(ButtonElement.class).id(id).click();
    }

    /**
     * Find {@link ComboBox} based on the given ID then select the item with the given value.
     *
     * @param id ID of the {@link ComboBox}
     * @param value Value to be selected
     */
    public void selectCombobox(String id, String value) {
        $(ComboBoxElement.class).id(id).selectByText(value);
    }

    /**
     * Type the given text into the {@link TextField} with the given ID.
     *
     * @param id ID of the {@link TextField}
     * @param value input value
     */
    public void typeInTextBox(String id, String value) {
        $(TextFieldElement.class).id(id).sendKeys(value);
    }

    /**
     * Find the menubar element based on id then click on the first menu item.
     *
     * @param id ID of the {@link MenuBar}
     */
    public void clickMenuItem(String id) {
        $(MenuBarElement.class).id(id).click();
    }

    /**
     * Maximize the browser.
     */
    private void maximize() {
        getDriver().manage().window().maximize();
    }

    /**
     * Finds the element within the current page that matches the {@link By locator} and has the
     * caption.
     * <p>
     * Note that the test would fail if no element matches the given locator.
     * 
     * @param locator {@link By Locator} of the {@link WebElement}
     * @param caption Caption of the {@link WebElement} that has to be found
     * @return the optional {@link WebElement} matching the given locator and caption if any
     */
    public Optional<WebElement> findElement(By locator, String caption) {
        List<WebElement> elements = getDriver().findElements(locator);
        return elements.stream().filter(e -> e.getText().equalsIgnoreCase(caption)).findFirst();
    }

    /**
     * Finds element {@link WebElement} by {@link By locator}.
     */
    @Override
    public WebElement findElement(By locator) {
        return getDriver().findElement(locator);
    }

    public void navigateToView(String viewName) {
        getDriver().get(DriverProperties.getTestUrl(urlPath) + viewName);
    }
}
