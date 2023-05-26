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
 *
 */

package org.linkki.samples.playground.uitestnew.ts.aspects;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.aspects.BindLabelPmo;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.By;

import com.vaadin.flow.component.html.testbench.NativeLabelElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

/**
 * UI tests for {@link BindLabelPmo} which verify the label binding behavior.
 */
class BindLabelTest extends PlaygroundUiTest {

    private static final String DYNAMIC_LABEL_TEXT = "DynamicLabelTest";

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        goToTestCase(TestScenarioView.TS008, TestScenarioView.TC015);
        setDynamicLabelText();
    }

    @Test
    void testDynamicLabel() {
        assertThat(getWrapperLabelTextOfElementWithId("dynamicButton")).isEqualTo(DYNAMIC_LABEL_TEXT);
        assertThat(getWrapperLabelTextOfElementWithId("dynamicCheckbox")).isEqualTo(DYNAMIC_LABEL_TEXT);
        assertThat(getWrapperLabelTextOfElementWithId("dynamicTextField")).isEqualTo(DYNAMIC_LABEL_TEXT);
    }

    @Test
    void testAutoLabel() {
        assertThat(getWrapperLabelTextOfElementWithId("autoDynamicButton")).isEqualTo(DYNAMIC_LABEL_TEXT);
        assertThat(getWrapperLabelTextOfElementWithId("autoStaticButton"))
                .isEqualTo("Auto type label with static content");
    }

    @Test
    void testStaticLabel() {
        assertThat(getWrapperLabelTextOfElementWithId("staticButton")).isEqualTo("Static label");
    }

    @Test
    void testNoneLabel() {
        assertThat(getWrapperLabelTextOfElementWithId("noneButton")).isEqualTo(StringUtils.EMPTY);
    }

    /**
     * Sets the {@link #DYNAMIC_LABEL_TEXT text} which should be used for dynamic labels.
     */
    private void setDynamicLabelText() {
        TextFieldElement labelTextField = getSection(BindLabelPmo.class).$(TextFieldElement.class).id("dynamicLabel");
        labelTextField.setValue(DYNAMIC_LABEL_TEXT);
    }

    /**
     * Gets the wrapper label of the UI element with the passed id.
     * 
     * @param id The ID of the investigated element
     * @return the label text
     */
    private String getWrapperLabelTextOfElementWithId(String id) {
        NativeLabelElement label = getSection(BindLabelPmo.class)
                .findElement(By.id(id))
                .findElement(By.xpath(".."))
                .$(NativeLabelElement.class)
                .attributeContains("class", "linkki-label-caption")
                .first();
        assertThat(label).isNotNull();
        return label.getText();
    }
}
