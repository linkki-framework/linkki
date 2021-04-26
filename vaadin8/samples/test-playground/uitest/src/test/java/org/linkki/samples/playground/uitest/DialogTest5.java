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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.uitest.extensions.DriverExtension;

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;
import com.vaadin.testbench.elements.WindowElement;

@DriverExtension.Configuration(restartAfterEveryTest = true)
public class DialogTest5 extends AbstractUiTest {

    @Test
    public void testDialogClosedOnViewChange() {
        navigateToView("dialog");

        assertThat($(WindowElement.class).all().size(), is(1));
        $(ButtonElement.class).caption("OK").first().click();

        clickButton("showDialog");
        assertThat($(WindowElement.class).all().size(), is(1));

        navigateToView("");
        assertThat($(WindowElement.class).all().size(), is(0));
    }

    @Test
    public void testOkCancelDialog() {
        navigateToView("dialog");
        $(ButtonElement.class).caption("OK").first().click();
        VerticalLayoutElement section = $(VerticalLayoutElement.class).id("OkCancelDialogPmo");

        section.$(TextFieldElement.class).id("caption").setValue("Awesome dialog");
        section.$(TextFieldElement.class).id("content").setValue("This is awesome!");
        section.$(TextFieldElement.class).id("okCaption").setValue("Hell yeah");
        section.$(TextFieldElement.class).id("cancelCaption").setValue("Not really");
        section.$(ButtonElement.class).id("showDialog").click();

        WindowElement dialog = $(WindowElement.class).caption("Awesome dialog").first();
        assertThat(dialog.$(LabelElement.class).first().getText(), is("This is awesome!"));
        assertThat(dialog.$(ButtonElement.class).id("okButton").getCaption(), is("Hell yeah"));
        assertThat(dialog.$(ButtonElement.class).id("cancelButton").getCaption(), is("Not really"));
    }
}
