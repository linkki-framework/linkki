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
package org.linkki.core.ui.element.annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.element.annotation.UIButtonIntegrationTest.ButtonTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class UIButtonIntegrationTest extends ComponentAnnotationIntegrationTest<Button, ButtonTestPmo> {

    public UIButtonIntegrationTest() {
        super(TestModelObjectWithString::new, ButtonTestPmo::new);
    }

    @Test
    public void testStaticButtonProperties() {
        Button button = getDynamicComponent();
        assertThat(button.getIcon().getElement().getAttribute("icon"),
                   is(VaadinIcon.ADJUST.create().getElement().getAttribute("icon")));

        // TODO test for shortcut key - I see no possibility to get the configured shortcut

        Button staticBindingButton = getStaticComponent();
        assertThat(staticBindingButton.getText(), is("static"));
    }

    @Test
    public void testDynamicCaptionType() {
        Button button = getDynamicComponent();
        assertThat(button.getText(), is("dynamic"));

        getDefaultPmo().setCaption("different caption");
        modelChanged();
        assertThat(button.getText(), is("different caption"));
    }

    @Test
    public void testButtonClick() {
        getDefaultPmo().setEnabled(true);
        modelChanged();
        Button button = getDynamicComponent();

        button.click();

        assertTrue(getDefaultPmo().isClicked());
    }

    @Test
    public void testDerivedCaption() {
        Button button = getComponentById("doFoo");

        assertThat(button.getText(), is("DoFoo"));
    }

    @Test
    public void testThemeVariants() {
        Button button = getComponentById("smallSuccess");
        String smallVariant = ButtonVariant.LUMO_SMALL.getVariantName();
        String successVariant = ButtonVariant.LUMO_SUCCESS.getVariantName();

        assertThat(button.getThemeNames(), containsInAnyOrder(smallVariant, successVariant));
    }

    @Override
    public void testLabelBinding() {
        // do nothing
    }

    @UISection
    protected static class ButtonTestPmo extends AnnotationTestPmo {

        private boolean clicked = false;

        private String caption = "dynamic";

        public ButtonTestPmo(Object modelObject) {
            super(modelObject);
        }

        @UIButton(position = 1, visible = VisibleType.DYNAMIC, captionType = CaptionType.DYNAMIC, icon = VaadinIcon.ADJUST, showIcon = true, enabled = EnabledType.DYNAMIC)
        @Override
        public void value() {
            clicked = true;
        }

        public String getValueCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        @Override
        @UIButton(position = 2, label = TEST_LABEL, visible = VisibleType.INVISIBLE, caption = "static", enabled = EnabledType.DISABLED)
        public void staticValue() {
            // does nothing
        }

        @UIButton(position = 30)
        public void getButton() {
            // does nothing
        }

        @UIButton(position = 3)
        public void doFoo() {
            // does nothing
        }

        @UIButton(position = 4, variants = { ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL })
        public void smallSuccess() {
            // does nothing
        }

        public boolean isClicked() {
            return clicked;
        }

        public void setClicked(boolean clicked) {
            this.clicked = clicked;
        }
    }

    protected static class TestModelObjectWithString extends TestModelObject<String> {

        @CheckForNull
        private String value = null;

        @CheckForNull
        @Override
        public String getValue() {
            return value;
        }

        @Override
        public void setValue(@CheckForNull String value) {
            this.value = value;
        }
    }
}
