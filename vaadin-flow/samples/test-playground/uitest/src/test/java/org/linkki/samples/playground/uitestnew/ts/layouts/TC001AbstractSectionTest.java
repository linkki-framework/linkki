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

import java.util.List;

import org.junit.jupiter.api.Test;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.layout.annotation.UIFormSection;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.theme.LinkkiTheme;
import org.linkki.core.vaadin.component.base.LinkkiAnchor;
import org.linkki.core.vaadin.component.base.LinkkiText;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;

abstract class TC001AbstractSectionTest extends TS001AbstractBasicElementsLayoutTest {

    /**
     * Tests, that all elements in an {@link UISection} or an {@link UIFormSection} <b>do have</b>
     * {@link Label labels}, including {@link LinkkiText labels}, {@link LinkkiAnchor links},
     * {@link UICheckBox checkboxes} and {@link Button buttons}
     */
    @Test
    void testFormItem_HasLabel() {
        WebElement formLayoutElement = getTestCaseSection().$(LinkkiSectionElement.class).get(0);
        List<WebElement> formItems = formLayoutElement.findElements(By.cssSelector("vaadin-form-item"));

        assertThat(formItems.stream().allMatch(fi -> hasLabel(fi))).isTrue();
    }

    private boolean hasLabel(WebElement webElement) {
        return !webElement.findElements(By.className(LinkkiTheme.COMPONENTWRAPPER_LABEL)).isEmpty();
    }

}
