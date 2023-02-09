package org.linkki.samples.playground.uitestnew.ts.components;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.radiobutton.testbench.RadioButtonElement;
import com.vaadin.flow.component.radiobutton.testbench.RadioButtonGroupElement;

public class TC008UIRadioButtonsTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS005, TestScenarioView.TC008);
    }

    @Test
    void testRadioButtonsGroup_WithoutNull() {
        RadioButtonGroupElement radioButtonsGroupElement = $(RadioButtonGroupElement.class).id("directionWithoutNull");

        List<RadioButtonElement> allRadioButons = radioButtonsGroupElement.$(RadioButtonElement.class).all();

        Assertions.assertThat(allRadioButons).hasSize(4);
    }

    @Test
    void testRadioButtonsGroup_WithNull() {
        RadioButtonGroupElement radioButtonsGroupElement = $(RadioButtonGroupElement.class).id("directionWithNull");

        List<RadioButtonElement> allRadioButons = radioButtonsGroupElement.$(RadioButtonElement.class).all();

        Assertions.assertThat(allRadioButons).hasSize(5);
    }


}
