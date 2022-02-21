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

package org.linkki.samples.playground.uitestnew.ts.dialogs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.framework.ui.dialogs.OkCancelDialog;
import org.linkki.samples.playground.pageobjects.LinkkiSectionElement;
import org.linkki.samples.playground.ts.dialogs.SetFormItemLabelWidthDialogPmo;
import org.linkki.samples.playground.ui.PlaygroundApplicationView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.html.testbench.LabelElement;

class SetFormItemLabelWidthTest extends PlaygroundUiTest {

    @BeforeEach
    void gotToTestCase() {
        goToTestCase(PlaygroundApplicationView.TS011, PlaygroundApplicationView.TC005);
    }

    @Test
    void testDialog_WithCustomLabelWidth() {
        LinkkiSectionElement section = getSection(SetFormItemLabelWidthDialogPmo.class);
        section.$(ButtonElement.class).id("showDialog").click();
        waitUntil(d -> $(DialogElement.class).exists());

        WebElement label = $(DialogElement.class).first().$(LabelElement.class).all().get(1);

        int labelWidth = label.getSize().getWidth();
        $(ButtonElement.class).id(OkCancelDialog.OK_BUTTON_ID).click();

        // 15rem=240px (for most browsers: 1rem=16px)
        assertThat(labelWidth, is(240));
    }
}
