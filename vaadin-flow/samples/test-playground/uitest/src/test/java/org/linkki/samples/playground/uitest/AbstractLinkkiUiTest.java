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
package org.linkki.samples.playground.uitest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.testbench.UITestConfiguration;
import org.linkki.testbench.WebDriverExtension;
import org.linkki.testbench.conditions.VaadinElementConditions;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;
import org.linkki.testbench.util.DriverProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.testbench.MenuBarElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.flow.component.shared.HasSuffix;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.testbench.TabElement;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.AbstractBrowserTestBase;
import com.vaadin.testbench.TestBenchElement;

/**
 * Helper class facilitates to use various Testbench functionality for UI testing. It helps to get
 * required web driver to launch the application on specific browser. UI test is done in this
 * browser by accessing various elements within the opened web page and by accessing their
 * properties. SetUp method must be called at the beginning of the test.
 * <p>
 * Various browser configuration options are available using the {@link UITestConfiguration}
 * annotation.
 */
@TestMethodOrder(OrderAnnotation.class)
public abstract class AbstractLinkkiUiTest extends AbstractBrowserTestBase {

    public static final String DEFAULT_CONTEXT_PATH = "linkki-sample-test-playground-vaadin-flow";

    @RegisterExtension
    protected static WebDriverExtension driverExtension = new WebDriverExtension(DEFAULT_CONTEXT_PATH);

    @Override
    public WebDriver getDriver() {
        return driverExtension.getDriver();
    }

    public void goToView(String viewName) {
        getDriver().navigate().to(DriverProperties.getTestUrl(DEFAULT_CONTEXT_PATH, viewName));
        $(TestBenchElement.class).withAttributeContaining("class", "linkki-main-area").waitForFirst();
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
     * Find the menubar element based on the id and click on the menu item if it exists.
     *
     * @param id the id of the {@link MenuBar}
     */
    public void clickMenuItemById(String id) {
        $(MenuBarElement.class).id("linkki-application-menu")//
                .getButtons().stream()//
                .map(b -> b.findElements(By.id(id)))//
                .flatMap(Collection<WebElement>::stream)
                .findFirst()//
                .ifPresent(WebElement::click);
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
     * @param cls class name of the section
     * @return Section as {@link VerticalLayoutElement}
     */
    public LinkkiSectionElement getSection(Class<?> cls) {
        return $(LinkkiSectionElement.class).id(cls.getSimpleName());
    }

    /**
     * Clicks the button in the section header with the given index
     *
     * @param cls class name of the section
     * @param buttonIndex index of the button
     */
    public void clickSectionHeaderButton(Class<?> cls, int buttonIndex) {
        getSection(cls).getHeaderComponents(ButtonElement.class).get(buttonIndex).click();
    }

    /**
     * Returns the value of the web element as String.
     * <p>
     * As {@link com.vaadin.flow.component.checkbox.Checkbox}es always have the value "on", the
     * property "checked" is used instead.
     * <p>
     * As {@link com.vaadin.flow.component.combobox.MultiSelectComboBox} does not define the
     * property value, "_inputElementValue" is used instead.
     */
    @SuppressWarnings("unchecked")
    public String getValueString(TestBenchElement webElement) {
        // checkbox value is mapped to the property checked
        if ("vaadin-checkbox".equals(webElement.getTagName())) {
            return webElement.getPropertyString("checked");
        } else if ("vaadin-multi-select-combo-box".equals(webElement.getTagName())) {
            return String.join(", ", webElement.getPropertyString("_inputElementValue"));
        } else if ("vaadin-checkbox-group".equals(webElement.getTagName())) {
            return String.join(",", (ArrayList<String>)webElement.getProperty("value"));
        } else {
            return webElement.getPropertyString("value");
        }
    }

    public boolean hasValue(TestBenchElement webElement) {
        return getValueString(webElement) != null;
    }

    /**
     * Clicks on the element in the context menu of a header button with the given text
     *
     * @param text displayed text of the context menu element
     */
    public void clickContextMenuItem(String text) {
        findContextMenuItem(text).click();
    }

    public void openSubMenu(String text) {
        findContextMenuItem(text).sendKeys(Keys.ARROW_RIGHT);
    }

    private WebElement findContextMenuItem(String text) {
        String contextMenuItemTag = "vaadin-menu-bar-item";

        try {
            return waitUntil($ -> {
                for (WebElement e : findElements(By.tagName(contextMenuItemTag))) {
                    if (text.equals(e.getText())) {
                        return e;
                    }
                }
                return null;
            });
        } catch (TimeoutException e) {
            throw new NoSuchElementException(
                    String.format("No %s element found with text \"%s\"", contextMenuItemTag, text));
        }
    }

    /**
     * Only works when the tested Component implements {@link HasSuffix} in Vaadin-flow
     *
     * @param element the {@link WebElement} to select the suffix from
     * @return WebElement which contains Suffix
     */

    public WebElement getSuffix(WebElement element) {
        return element.findElement(By.cssSelector("div[slot='suffix']"));
    }

    public GridElement getGrid(Class<? extends ContainerPmo<?>> tablePmoClass) {
        GridElement gridElement = $(GridElement.class).id(tablePmoClass.getSimpleName() + "_table");
        gridElement.scrollIntoView();
        return gridElement;
    }

    /**
     * Searches for a dialog with the given title
     *
     * @param title The title of the dialog
     * @return The {@link DialogElement}
     */
    public DialogElement findDialog(String title) {
        try {
            return waitUntil(VaadinElementConditions.dialogDisplayed(title));
        } catch (TimeoutException e) {
            throw new IllegalStateException("No dialog with title \"" + title + "\" present", e);
        }
    }

    /**
     * Confirms dialog with Ok
     *
     * @param dialog The {@link DialogElement dialog} to confirm and to close
     */
    public void confirmDialog(DialogElement dialog) {
        ButtonElement okButton = dialog.$(ButtonElement.class).id("okButton");
        okButton.click();
        waitUntil(VaadinElementConditions.isClosed(dialog));
    }
}
