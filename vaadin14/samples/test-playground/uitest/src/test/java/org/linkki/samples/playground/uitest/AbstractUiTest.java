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

import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.binary.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.linkki.samples.playground.uitest.extensions.DriverExtension;
import org.linkki.samples.playground.uitest.extensions.ScreenshotOnFailureExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.testbench.MenuBarElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.testbench.TabElement;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.TestBenchTestCase;

/**
 * Helper class facilitates to use various Testbench functionality for UI testing. It helps to get
 * required web driver to launch the application on specific browser. UI test is done in this browser by
 * accessing various elements within the opened web page and by accessing their properties. SetUp method
 * must be called at the beginning of the test.
 * <p>
 * Various browser configuration options are available using the
 * {@link org.linkki.samples.playground.uitest.extensions.DriverExtension.Configuration @DriverExtension.Configuration}
 * annotation.
 */
@TestMethodOrder(OrderAnnotation.class)
public class AbstractUiTest extends TestBenchTestCase {

    @RegisterExtension
    protected static DriverExtension driverExtension = new DriverExtension();

    @RegisterExtension
    protected ScreenshotOnFailureExtension screenshotExtension = new ScreenshotOnFailureExtension(this);

    @BeforeEach
    public void setUp() {
        setDriver(driverExtension.getDriver());
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
     * Opens the {@link Tab} with the given id.
     */
    public void openTab(String id) {
        $(TabElement.class).id(id).click();
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
     * Find {@link ComboBox} based on the given ID then select the item with the given value.
     *
     * @param id ID of the {@link ComboBox}
     * @return The text as {@link String} of the selected item
     */
    public String getComboBoxSelection(String id) {
        return $(ComboBoxElement.class).id(id).getSelectedText();
    }

    /**
     * Find and click clear button of {@link ComboBox} to clear selection <br>
     * Implementation taken from <a href=
     * "https://code-fever.de/artikel/selenium-shadow-dom-und-shadow-root.html">https://code-fever.de/artikel/selenium-shadow-dom-und-shadow-root.html</a>
     * 
     * @param id ID of the {@link ComboBox}
     */
    public void clearComboBoxSelection(String id) {
        final WebElement shadowHost = $(ComboBoxElement.class).id(id).$(TextFieldElement.class).id("input");
        final JavascriptExecutor jsExecutor = (JavascriptExecutor)driver;
        final WebElement shadowRoot = (WebElement)jsExecutor.executeScript("return arguments[0].shadowRoot",
                                                                           shadowHost);
        final WebElement textFieldContainer = shadowRoot.findElement(By.className("vaadin-text-field-container"));
        // By.id("vaadin-text-field-input-5") geht nicht, da '-5' je nach ComboBox variiert
        WebElement textFieldInputField = textFieldContainer.findElement(By.cssSelector("div[part=\"input-field\"]"));
        WebElement clearButton = textFieldInputField.findElement(By.id("clearButton"));
        clearButton.click();
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

    public void selectCheckbox(String id) {
        CheckboxElement checkboxElement = $(CheckboxElement.class).id(id);
        if (!checkboxElement.isChecked()) {
            checkboxElement.click();
        }
    }

    public void unselectCheckbox(String id) {
        CheckboxElement checkboxElement = $(CheckboxElement.class).id(id);
        if (checkboxElement.isChecked()) {
            checkboxElement.click();
        }
    }

    /**
     * Find the menubar element based on the text and click on the first menu item.
     *
     * @param text the text of the {@link MenuBar}
     */
    public void clickMenuItem(String text) {
        $(MenuBarElement.class).first().getButtons().stream()//
                .filter(e -> StringUtils.equals(e.getText(), text))//
                .findFirst()//
                .ifPresent(TestBenchElement::click);
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

    /**
     * Finds and returns {@link VerticalLayoutElement} section by the given {@link Class}
     * 
     * @param cls Classname of the Section
     * @return Section as {@link VerticalLayoutElement}
     */
    public VerticalLayoutElement getSection(Class<?> cls) {
        return $(VerticalLayoutElement.class).id(cls.getSimpleName());
    }
}
