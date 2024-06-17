package org.linkki.samples.playground.uitestnew.ts.nestedcomponent;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorUiSectionPmo;
import org.linkki.samples.playground.ts.nestedcomponent.NullableModelObjectInInvisibleNestedPmo;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;
import org.linkki.testbench.pageobjects.LinkkiTextElement;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;

class NullModelObjectTest extends PlaygroundUiTest {

    private TestCaseComponentElement testCaseComponent;

    @BeforeEach
    void goToTestCase() {
        testCaseComponent = goToTestCase(TestScenarioView.TS016, TestScenarioView.TC002);
    }

    @Order(1)
    @Test
    void testInitialState() {
        var section = testCaseComponent.$(LinkkiSectionElement.class)
                .id(BasicElementsLayoutBehaviorUiSectionPmo.class.getSimpleName());
        var inputElementsWithValue = section.getContentComponents().stream()
                .filter(this::hasValue)
                // checkboxes have the value false when cleared
                .filter(e -> !"vaadin-checkbox".equals(e.getTagName()))
                .filter(e -> !"vaadin-checkbox-group".equals(e.getTagName()))
                .toList();
        assertThat(inputElementsWithValue).size()
                .as("Verify that the selector for components with value property is correct").isEqualTo(13);
        assertThat(inputElementsWithValue)
                .map(this::getValueString)
                .as("Input elements do not have a value")
                .allMatch(String::isEmpty);
        assertThat(section.getContent().$(CheckboxElement.class).all()).hasSize(5);
        assertThat(section.getContent().$(CheckboxElement.class).all()).extracting(CheckboxElement::isChecked)
                .allMatch(Boolean.FALSE::equals);

        assertThat(inputElementsWithValue).map(e -> e.getPropertyBoolean("readonly"))
                .as("Input elements are readonly")
                .allMatch(Boolean.TRUE::equals);
    }

    @Order(2)
    @Test
    void testUpdateOnModelObjectCreation() {
        var layout = testCaseComponent.$(VerticalLayoutElement.class)
                .id(NullableModelObjectInInvisibleNestedPmo.class.getSimpleName());
        layout.$(ButtonElement.class).id("toggleModelObject").click();
        assertThat(layout.$(LinkkiTextElement.class).id("modelObjectLabel").getText())
                .isEqualTo("Model object present");

        assertThat(testCaseComponent.$(LinkkiSectionElement.class)
                .id(BasicElementsLayoutBehaviorUiSectionPmo.class.getSimpleName())
                .getContentComponents().stream().filter(this::hasValue))
                        .map(e -> e.getPropertyBoolean("readonly"))
                        .as("Input elements are writable")
                        .allMatch(Boolean.FALSE::equals);
    }

}
