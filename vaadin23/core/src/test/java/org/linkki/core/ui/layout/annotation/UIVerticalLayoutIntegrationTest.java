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

package org.linkki.core.ui.layout.annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.HorizontalAlignment;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class UIVerticalLayoutIntegrationTest {

    @Test
    public void testCreation() {
        BindingContext bindingContext = new BindingContext();
        VerticalLayoutPmo pmo = new VerticalLayoutPmo();

        Component component = VaadinUiCreator.createComponent(pmo, bindingContext);
        assertThat("UiCreator should be able to create a VerticalLayout", component,
                   is(instanceOf(VerticalLayout.class)));

        VerticalLayout layout = (VerticalLayout)component;

        assertThat("Default vertical alignment should be center", layout.getDefaultHorizontalComponentAlignment(),
                   is(Alignment.START));

        assertThat("Child component should be correctly created", layout.getComponentCount(), is(1));
        assertThat("Child component should be correctly created", layout.getComponentAt(0),
                   is(instanceOf(TextField.class)));

        TextField textField = (TextField)layout.getComponentAt(0);
        pmo.setText("new text");
        bindingContext.modelChanged();
        assertThat("Child components should be correctly bound", textField.getValue(), is("new text"));
    }

    @Test
    public void testAlignment_Middle() {
        VerticalLayout layout = (VerticalLayout)UiCreator
                .createComponent(new MiddleAlignedVerticalLayoutPmo(), new BindingContext())
                .getComponent();

        assertThat("Default horizontal alignment should be as set in the annotation",
                   layout.getDefaultHorizontalComponentAlignment(),
                   is(Alignment.CENTER));
    }

    @Test
    public void testAlignment_Left() {
        VerticalLayout layout = (VerticalLayout)UiCreator
                .createComponent(new LeftAlignedVerticalLayoutPmo(), new BindingContext())
                .getComponent();

        assertThat("Default horizontal alignment should be as set in the annotation",
                   layout.getDefaultHorizontalComponentAlignment(),
                   is(Alignment.START));
    }

    @Test
    public void testAlignment_Right() {
        VerticalLayout layout = (VerticalLayout)UiCreator
                .createComponent(new RightAlignedVerticalLayoutPmo(), new BindingContext())
                .getComponent();

        assertThat("Default horizontal alignment should be as set in the annotation",
                   layout.getDefaultHorizontalComponentAlignment(),
                   is(Alignment.END));
    }

    @Test
    void testPadding_false() {
        VerticalLayout layout = (VerticalLayout)UiCreator
                .createComponent(new PaddingVerticalLayoutPmo(), new BindingContext())
                .getComponent();

        assertThat(layout.isPadding(), is(false));
    }

    @Test
    void testSpacing_false() {
        VerticalLayout layout = (VerticalLayout)UiCreator
                .createComponent(new SpacingVerticalLayoutPmo(), new BindingContext())
                .getComponent();

        assertThat(layout.isSpacing(), is(false));
    }

    @Test
    void testSpacingPadding_false() {
        VerticalLayout layout = (VerticalLayout)UiCreator
                .createComponent(new SpacingPaddingVerticalLayoutPmo(), new BindingContext())
                .getComponent();

        assertThat(layout.isPadding(), is(false));
        assertThat(layout.isSpacing(), is(false));
    }

    @Test
    void testSpacingPaddingAlignment_falseRight() {
        VerticalLayout layout = (VerticalLayout)UiCreator
                .createComponent(new SpacingPaddingAlignmentVerticalLayoutPmo(), new BindingContext())
                .getComponent();

        assertThat(layout.isPadding(), is(false));
        assertThat(layout.isSpacing(), is(false));
        assertThat("Default horizontal alignment should be as set in the annotation",
                   layout.getDefaultHorizontalComponentAlignment(),
                   is(Alignment.END));
    }

    @UIVerticalLayout
    public static class VerticalLayoutPmo {

        private String text = "";

        @UITextField(label = "", position = 0)
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    @UIVerticalLayout(alignment = HorizontalAlignment.MIDDLE)
    public static class MiddleAlignedVerticalLayoutPmo {
        // only class annotation is needed
    }

    @UIVerticalLayout(alignment = HorizontalAlignment.LEFT)
    public static class LeftAlignedVerticalLayoutPmo {
        // only class annotation is needed
    }

    @UIVerticalLayout(alignment = HorizontalAlignment.RIGHT)
    public static class RightAlignedVerticalLayoutPmo {
        // only class annotation is needed
    }

    @UIVerticalLayout(padding = false)
    public static class PaddingVerticalLayoutPmo {
        // only class annotation is needed
    }

    @UIVerticalLayout(spacing = false)
    public static class SpacingVerticalLayoutPmo {
        // only class annotation is needed
    }

    @UIVerticalLayout(spacing = false, padding = false)
    public static class SpacingPaddingVerticalLayoutPmo {
        // only class annotation is needed
    }

    @UIVerticalLayout(spacing = false, padding = false, alignment = HorizontalAlignment.RIGHT)
    public static class SpacingPaddingAlignmentVerticalLayoutPmo {
        // only class annotation is needed
    }
}
