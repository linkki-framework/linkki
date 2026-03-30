package org.linkki.samples.playground.uitestnew.ts.linkkitheme;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.linkkitheme.FormItemLabelAlignmentComponent;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;

import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.tabs.testbench.TabElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class LinkkiThemeTest extends PlaygroundUiTest {

    @Test
    void testBackgroundColorOfInputField() {
        var testCaseSection = goToTestCase(TestScenarioView.TS007, TestScenarioView.TC002);
        testCaseSection.getContentWrapper().$(TabElement.class)
                .id(FormItemLabelAlignmentComponent.ALIGNMENT_START).click();

        var pmo = testCaseSection.$(LinkkiSectionElement.class).id("BasicElementsLayoutLabelStartPmo");
        var enabled = pmo.$(CheckboxElement.class).id("allElementsEnabled");

        var inputField = pmo.$(TextFieldElement.class).id("text").$("vaadin-input-container").first();
        assertThat(inputField.getDomAttribute("part")).isEqualTo("input-field");

        enabled.setChecked(true);
        assertThat(inputField.getCssValue("background-color"))
                .isEqualTo("rgba(26, 57, 96, 0.1)");

        enabled.setChecked(false);
        assertThat(inputField.getCssValue("background-color"))
                .isEqualTo("rgba(25, 59, 103, 0.05)");
    }

}
