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

package org.linkki.samples.playground.uitestnew.ts.layouts;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.layouts.AbstractBasicElementsLayoutBehaviorPmo;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorModelObject;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorModelObject.SampleEnum;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiTextElement;
import org.openqa.selenium.Keys;

import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.radiobutton.testbench.RadioButtonGroupElement;
import com.vaadin.flow.component.textfield.testbench.TextAreaElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.TestBenchElement;

abstract class TS001AbstractBasicElementsLayoutTest extends PlaygroundUiTest {

    private static final String ID_VISIBLE = "allElementsVisible";
    private static final String ID_REQUIRED = "allElementsRequired";
    private static final String ID_READ_ONLY = "allElementsReadOnly";
    private static final String ID_ENABLED = "allElementsEnabled";
    private TestCaseComponentElement testCaseSection;

    @BeforeEach
    protected void goToTestCase() {
        testCaseSection = goToTestCase(TestScenarioView.TS001, getTestCaseId());
        testCaseSection.getContentWrapper().$(CheckboxElement.class).id(ID_REQUIRED).setChecked(false);
        testCaseSection.getContentWrapper().$(CheckboxElement.class).id(ID_VISIBLE).setChecked(true);
        testCaseSection.getContentWrapper().$(CheckboxElement.class).id(ID_READ_ONLY).setChecked(false);
        testCaseSection.getContentWrapper().$(CheckboxElement.class).id(ID_ENABLED).setChecked(true);
    }

    protected abstract String getTestCaseId();

    protected TestCaseComponentElement getTestCaseSection() {
        return testCaseSection;
    }

    @Test
    void testLabel_HasText() {
        var label = testCaseSection.getContentWrapper().$(LinkkiTextElement.class).id("textLabel");

        assertThat(label.getText()).isEqualTo("I am a text");
    }

    @Test
    void testSetValue_TextField() {
        var textField = testCaseSection.getContentWrapper().$(TextFieldElement.class).id("text");
        var oldValue = textField.getValue();

        textField.sendKeys(Keys.END + " new");

        assertThat(textField.getValue()).isEqualTo(oldValue + " new");
    }

    @Test
    void testSetValue_TextArea() {
        var textArea = testCaseSection.getContentWrapper()
                .$(TextAreaElement.class)
                .id(BasicElementsLayoutBehaviorModelObject.PROPERTY_LONGTEXT);
        assertThat(textArea.getValue()).startsWith("Lorem ipsum");

        textArea.setValue("");
        textArea.sendKeys("bla bla");

        assertThat(textArea.getValue()).isEqualTo("bla bla");
    }

    @Test
    void testSetValue_CheckBox() {
        var checkBox = testCaseSection.getContentWrapper()
                .$(CheckboxElement.class)
                .id(BasicElementsLayoutBehaviorModelObject.PROPERTY_BOOLEANVALUE);
        assertThat(checkBox.isChecked()).isTrue();

        checkBox.click();

        assertThat(checkBox.isChecked()).isFalse();
    }

    @Test
    void testSetValue_RadioButtons() {
        var radioButtons = testCaseSection.getContentWrapper()
                .$(RadioButtonGroupElement.class)
                .id("enumValueRadioButton");
        assertThat(radioButtons.getSelectedText()).isNull();

        radioButtons.selectByText(SampleEnum.VALUE3.getName());

        assertThat(radioButtons.getSelectedText()).isEqualTo(SampleEnum.VALUE3.getName());
    }

    @Test
    void testLink_HasTextAndHref() {
        var link = testCaseSection.getContentWrapper()//
                .$(LinkkiTextElement.class)
                .id("link");

        // conditions
        assertThat(link.getText()).isEqualTo("I am a Link to #");
        assertThat(link.getContent().getDomAttribute("href")).endsWith("#");
    }

    @Test
    void testReadOnly() {
        var hasReadOnly = getFormInputs().all();
        assertThat(hasReadOnly).withRepresentation(this::toElementTag).hasSize(15);
        assertThat(hasReadOnly)
                .withRepresentation(this::toElementTag)
                .noneMatch(e -> e.hasAttribute("readonly"), "having the attribute readonly");

        testCaseSection.getContentWrapper().$(CheckboxElement.class).id(ID_READ_ONLY).setChecked(true);

        assertThat(hasReadOnly)
                .withRepresentation(this::toElementTag)
                .allMatch(e -> e.hasAttribute("readonly"), "having the attribute readonly");
    }

    @Test
    void testDisabled() {
        var hasEnabled = getFormInputs().all();
        assertThat(hasEnabled).withRepresentation(this::toElementTag).hasSize(15);
        assertThat(hasEnabled)
                .withRepresentation(this::toElementTag)
                .as("No input elements should have the attribute disabled")
                .noneMatch(e -> e.hasAttribute("disabled"), "having the attribute disabled");

        testCaseSection.getContentWrapper().$(CheckboxElement.class).id(ID_ENABLED).setChecked(false);

        assertThat(hasEnabled)
                .withRepresentation(this::toElementTag)
                .allMatch(e -> e.hasAttribute("disabled"), "having the attribute disabled");
    }

    @Test
    void testRequiredIndicator() {
        var hasRequiredIndicator = getFormInputs().all();
        assertThat(hasRequiredIndicator).withRepresentation(this::toElementTag).hasSize(15);
        assertThat(hasRequiredIndicator)
                .withRepresentation(this::toElementTag)
                .noneMatch(e -> e.hasAttribute("required"), "having attribute required");

        testCaseSection.getContentWrapper().$(CheckboxElement.class).id(ID_REQUIRED).setChecked(true);

        assertThat(hasRequiredIndicator)
                .withRepresentation(this::toElementTag)
                .allMatch(e -> e.hasAttribute("required"), "having attribute required");
    }

    @Test
    void testVisible() {
        var components = getContentComponents().all();
        assertThat(components).withRepresentation(this::toElementTag).hasSize(19);

        testCaseSection.getContentWrapper().$(CheckboxElement.class).id(ID_VISIBLE).setChecked(false);

        assertThat(components)
                .withRepresentation(this::toElementTag)
                .noneMatch(TestBenchElement::isDisplayed, "being displayed");
    }

    @Test
    void testDisplayValidation() {
        var textFieldElement = testCaseSection.getContentWrapper().$(TextFieldElement.class).id("text");

        testCaseSection.getContentWrapper().$(CheckboxElement.class).id(ID_REQUIRED).setChecked(true);
        textFieldElement.setValue("");
        textFieldElement.sendKeys("\t");

        assertThat(textFieldElement.hasAttribute("invalid")).as("The presence of the attribute invalid").isTrue();
    }

    private ElementQuery<TestBenchElement> getContentComponents() {
        return testCaseSection.getContentWrapper()
                .$(TestBenchElement.class)
                .withCondition(TestBenchElement::isDisplayed)
                .withClassName(AbstractBasicElementsLayoutBehaviorPmo.CSS_CLASS).single()
                .$(TestBenchElement.class)
                .withoutClassName(AbstractBasicElementsLayoutBehaviorPmo.CSS_CLASS_HEADER)
                .withCondition(TestBenchElement::isDisplayed)
                // somehow the div inside the shadow dom of a formlayout is found...
                .withCondition(e -> !e.getTagName().equals("div"))
                // all elements directly created with linkki have an ID
                .withCondition(e -> e.hasAttribute("id"))
                .withCondition(e -> !e.hasAttribute("slot"));
    }

    private ElementQuery<TestBenchElement> getFormInputs() {
        return getContentComponents()
                .withCondition(e -> e.getProperty("value") != null
                        // multi select returns null if value is empty
                        || e.getTagName().equals("vaadin-multi-select-combo-box"));
    }

    private String toElementTag(Object o) {
        if (o instanceof Collection<?> collection) {
            return collection.stream()
                    .map(TestBenchElement.class::cast)
                    .map(TestBenchElement::getTagName).toList().toString();
        } else if (o instanceof TestBenchElement element) {
            return element.getTagName();
        } else {
            return o.toString();
        }
    }
}
