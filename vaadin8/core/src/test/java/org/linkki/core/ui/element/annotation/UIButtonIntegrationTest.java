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
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.element.annotation.UIButtonIntegrationTest.ButtonTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;

public class UIButtonIntegrationTest extends ComponentAnnotationIntegrationTest<Button, ButtonTestPmo> {

    private static final String STYLES = "blabla";

    public UIButtonIntegrationTest() {
        super(mo -> new ButtonTestPmo());
    }

    @Test
    public void testStaticButtonProperties() {
        Button button = getDynamicComponent();
        assertThat(button.getStyleName(), is(STYLES));
        assertThat(button.getIcon(), is(VaadinIcons.ADJUST));

        // TODO test for shortcut key - I see no possibility to get the configured shortcut

        Button staticBindingButton = getStaticComponent();
        assertThat(staticBindingButton.getCaption(), is("static"));
    }

    @Test
    public void testDynamicCaptionType() {
        Button button = getDynamicComponent();
        assertThat(button.getCaption(), is("dynamic"));

        getDefaultPmo().setCaption("different caption");
        modelChanged();
        assertThat(button.getCaption(), is("different caption"));
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
        assertThat(button.getCaption(), is("DoFoo"));
    }

    @UISection
    protected static class ButtonTestPmo extends AnnotationTestPmo {

        private boolean clicked = false;

        private String caption = "dynamic";

        public ButtonTestPmo() {
            super(null);
        }

        @UIButton(position = 1, visible = VisibleType.DYNAMIC, styleNames = STYLES, captionType = CaptionType.DYNAMIC, icon = VaadinIcons.ADJUST, showIcon = true, shortcutKeyCode = KeyCode.ARROW_RIGHT, shortcutModifierKeys = {
                ModifierKey.ALT }, enabled = EnabledType.DYNAMIC)
        @Override
        public void dynamic() {
            clicked = true;
        }

        public String getDynamicCaption() {
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

        public boolean isClicked() {
            return clicked;
        }

        public void setClicked(boolean clicked) {
            this.clicked = clicked;
        }
    }
}
