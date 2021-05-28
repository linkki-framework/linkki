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

package org.linkki.samples.playground.uitest.vaadin14;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.uitest.AbstractUiTest;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;

public class ButtonTest extends AbstractUiTest {

    @Test
    public void testButton_Click() {
        ButtonElement button = getButton("primary");

        button.click();

        NotificationElement notification = $(NotificationElement.class).first();
        assertThat(notification.getText(), is("Primary Button clicked"));
    }

    @Test
    public void testThemeVariant_Primary() {
        ButtonElement button = getButton("primary");

        assertThat(button.getAttribute("theme"), is("primary"));
    }

    @Test
    public void testThemeVariant_Inline() {
        ButtonElement button = getButton("inline");

        assertThat(button.getAttribute("theme"), is("tertiary-inline"));
    }

    @Test
    public void testThemeVariant_SmallSuccess() {
        ButtonElement button = getButton("smallSuccess");

        assertThat(button.getAttribute("theme"), is("small success"));
    }

    private ButtonElement getButton(String id) {
        return $(VerticalLayoutElement.class).id("AllUiElementsUiSectionPmo").$(ButtonElement.class).id(id);
    }

}
