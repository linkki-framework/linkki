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
import org.linkki.core.ui.layout.VerticalAlignment;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class UIHorizontalLayoutIntegrationTest {

    @Test
    public void testCreation() {
        BindingContext bindingContext = new BindingContext();
        HorizontalLayoutPmo pmo = new HorizontalLayoutPmo();

        Component component = VaadinUiCreator.createComponent(pmo, bindingContext);
        assertThat("UiCreator should be able to create a HorizontalLayout", component,
                   is(instanceOf(HorizontalLayout.class)));

        HorizontalLayout layout = (HorizontalLayout)component;

        assertThat("Default vertical alignment should be baseline", layout.getDefaultVerticalComponentAlignment(),
                   is(Alignment.BASELINE));

        assertThat("Child component should be correctly created", layout.getComponentCount(), is(1));
        assertThat("Child component should be correctly created", layout.getComponentAt(0),
                   is(instanceOf(TextField.class)));

        TextField textField = (TextField)layout.getComponentAt(0);
        pmo.setText("new text");
        bindingContext.modelChanged();
        assertThat("Child components should be correctly bound", textField.getValue(), is("new text"));
    }

    @Test
    public void testAlignment_Bottom() {
        HorizontalLayout layout = (HorizontalLayout)UiCreator
                .createComponent(new BottomAlignedHorizontalLayoutPmo(), new BindingContext())
                .getComponent();

        assertThat("Default vertical alignment should be as set in the annotation",
                   layout.getDefaultVerticalComponentAlignment(),
                   is(Alignment.END));
    }

    @Test
    public void testAlignment_Top() {
        HorizontalLayout layout = (HorizontalLayout)UiCreator
                .createComponent(new TopAlignedHorizontalLayoutPmo(), new BindingContext())
                .getComponent();

        assertThat("Default vertical alignment should be as set in the annotation",
                   layout.getDefaultVerticalComponentAlignment(),
                   is(Alignment.START));
    }

    @Test
    public void testAlignment_Middle() {
        HorizontalLayout layout = (HorizontalLayout)UiCreator
                .createComponent(new MiddleAlignedHorizontalLayoutPmo(), new BindingContext())
                .getComponent();

        assertThat("Default vertical alignment should be as set in the annotation",
                   layout.getDefaultVerticalComponentAlignment(),
                   is(Alignment.CENTER));
    }

    @Test
    void testPadding_true() {
        HorizontalLayout layout = (HorizontalLayout)UiCreator
                .createComponent(new PaddingHorizontalLayoutPmo(), new BindingContext())
                .getComponent();

        assertThat(layout.isPadding(), is(true));
    }

    @Test
    void testSpacing_false() {
        HorizontalLayout layout = (HorizontalLayout)UiCreator
                .createComponent(new SpacingHorizontalLayoutPmo(), new BindingContext())
                .getComponent();

        assertThat(layout.isSpacing(), is(false));
    }

    @Test
    void testSpacingPadding_falseTrue() {
        HorizontalLayout layout = (HorizontalLayout)UiCreator
                .createComponent(new SpacingPaddingHorizontalLayoutPmo(), new BindingContext())
                .getComponent();

        assertThat(layout.isPadding(), is(true));
        assertThat(layout.isSpacing(), is(false));
    }

    @Test
    void testSpacingPaddingAlignment_falseTrueBottom() {
        HorizontalLayout layout = (HorizontalLayout)UiCreator
                .createComponent(new SpacingPaddingAlignmentHorizontalLayoutPmo(), new BindingContext())
                .getComponent();

        assertThat(layout.isPadding(), is(true));
        assertThat(layout.isSpacing(), is(false));
        assertThat("Default horizontal alignment should be as set in the annotation",
                   layout.getDefaultVerticalComponentAlignment(),
                   is(Alignment.END));
    }


    @UIHorizontalLayout
    public static class HorizontalLayoutPmo {

        private String text = "";

        @UITextField(label = "", position = 0)
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    @UIHorizontalLayout(alignment = VerticalAlignment.BOTTOM)
    public static class BottomAlignedHorizontalLayoutPmo {
        // only class annotation is needed
    }

    @UIHorizontalLayout(alignment = VerticalAlignment.TOP)
    public static class TopAlignedHorizontalLayoutPmo {
        // only class annotation is needed
    }

    @UIHorizontalLayout(alignment = VerticalAlignment.MIDDLE)
    public static class MiddleAlignedHorizontalLayoutPmo {
        // only class annotation is needed
    }

    @UIHorizontalLayout(padding = true)
    public static class PaddingHorizontalLayoutPmo {
        // only class annotation is needed
    }

    @UIHorizontalLayout(spacing = false)
    public static class SpacingHorizontalLayoutPmo {
        // only class annotation is needed
    }

    @UIHorizontalLayout(spacing = false, padding = true)
    public static class SpacingPaddingHorizontalLayoutPmo {
        // only class annotation is needed
    }

    @UIHorizontalLayout(spacing = false, padding = true, alignment = VerticalAlignment.BOTTOM)
    public static class SpacingPaddingAlignmentHorizontalLayoutPmo {
        // only class annotation is needed
    }

}
