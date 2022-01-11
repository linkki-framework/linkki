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

package org.linkki.samples.playground.uitestnew.ts.layouts;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ui.PlaygroundApplicationView;
import org.openqa.selenium.By;

import com.vaadin.flow.component.formlayout.testbench.FormLayoutElement;

class TC002UIFormSectionTest extends TC001AbstractSectionTest {

    @Override
    protected String getTestCaseId() {
        return PlaygroundApplicationView.TC002;
    }

    @Test
    void testFormSection_FormItemLabelWidth() {
        FormLayoutElement formLayoutElement = getTestCaseSection().$(FormLayoutElement.class).first();

        // all form item labels should inherit the width of 200px that is set in form layout section
        // but we just check first form item
        String formItemLabelWidthAttribute = formLayoutElement.findElement(By.tagName("vaadin-form-item"))
                .findElement(By.className("linkki-label-caption")).getCssValue("width");
        assertThat(formItemLabelWidthAttribute).isEqualTo("200px");
    }
}
