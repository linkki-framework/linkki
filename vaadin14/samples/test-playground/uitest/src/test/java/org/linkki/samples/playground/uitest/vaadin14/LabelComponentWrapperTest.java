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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ui.PlaygroundApplicationView;
import org.linkki.samples.playground.uitest.AbstractUiTest;

import com.vaadin.flow.component.orderedlayout.testbench.HorizontalLayoutElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class LabelComponentWrapperTest extends AbstractUiTest {

    @BeforeEach
    public void setTab() {
        openTab(PlaygroundApplicationView.LAYOUTS_TAB_ID);
    }

    @Test
    public void testHorizontalLayout_TextField() {
        HorizontalLayoutElement layout = $(HorizontalLayoutElement.class).id("HorizontalLayoutPmo");
        TextFieldElement field = layout.$(TextFieldElement.class).id("text");

        assertThat(field.getLabel(), is("Text"));
    }

    @Test
    public void testHorizontalLayout_IntegerField() {
        HorizontalLayoutElement layout = $(HorizontalLayoutElement.class).id("HorizontalLayoutPmo");
        TextFieldElement field = layout.$(TextFieldElement.class).id("amount");

        assertThat(field.getLabel(), is("Amount"));
    }

    @Test
    public void testVerticalLayout_TextField() {
        VerticalLayoutElement layout = $(VerticalLayoutElement.class).id("VerticalLayoutPmo");
        TextFieldElement field = layout.$(TextFieldElement.class).id("text");

        assertThat(field.getLabel(), is("Text"));
    }

    @Test
    public void testVerticalLayout_IntegerField() {
        VerticalLayoutElement layout = $(VerticalLayoutElement.class).id("VerticalLayoutPmo");
        TextFieldElement field = layout.$(TextFieldElement.class).id("amount");

        assertThat(field.getLabel(), is("Amount"));
    }

}
