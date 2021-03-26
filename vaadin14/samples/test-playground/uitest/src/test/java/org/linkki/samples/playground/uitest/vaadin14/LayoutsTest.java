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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ui.PlaygroundApplicationUI;
import org.linkki.samples.playground.uitest.AbstractUiTest;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.formlayout.testbench.FormLayoutElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.orderedlayout.testbench.HorizontalLayoutElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class LayoutsTest extends AbstractUiTest {

    @BeforeEach
    public void setTab() {
        openTab(PlaygroundApplicationUI.LAYOUTS_TAB_ID);
    }

    @Test
    public void testHorizontalLayout_Id() {
        HorizontalLayoutElement horizontalLayout = $(HorizontalLayoutElement.class).id("HorizontalLayoutPmo");
        // assertThat("Caption of UIHorizontalLayout is bindable", horizontalLayout.getCaption(),
        // is("UIHorizontalLayout"));
        assertThat(horizontalLayout.$(TextFieldElement.class).all().size(), is(1));
        assertThat(horizontalLayout.$(DivElement.class).all().size(), is(1));
        assertThat(horizontalLayout.$(ButtonElement.class).all().size(), is(1));
        assertThat(horizontalLayout.$(CheckboxElement.class).all().size(), is(1));
    }

    @Test
    public void testVerticalLayout_Id() {
        VerticalLayoutElement verticalLayout = $(VerticalLayoutElement.class).id("VerticalLayoutPmo");
        // assertThat("Caption of UIVerticalLayout is bindable", verticalLayout.getCaption(),
        // is("UIVerticalLayout"));
        assertThat(verticalLayout.$(TextFieldElement.class).all().size(), is(1));
        assertThat(verticalLayout.$(DivElement.class).all().size(), is(1));
        assertThat(verticalLayout.$(ButtonElement.class).all().size(), is(1));
        assertThat(verticalLayout.$(CheckboxElement.class).all().size(), is(1));
    }

    @Test
    public void testFormLayout() {
        FormLayoutElement formLayout = $(FormLayoutElement.class).id("FormLayoutPmo");
        // assertThat("Caption of UIFormLayout is bindable", formLayout.getCaption(),
        // is("UIFormLayout"));
        assertThat(formLayout.$(TextFieldElement.class).all().size(), is(2));
        assertThat(formLayout.$(DivElement.class).all().size(), is(2));
    }

}
