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
package org.linkki.core.ui.section.annotations;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.linkki.test.matcher.Matchers.assertThat;

import org.eclipse.jdt.annotation.Nullable;
import org.junit.Test;
import org.linkki.core.ui.section.annotations.UIButtonIntegrationTest.ButtonTestPmo;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;

public class UIButtonIntegrationTest extends ComponentAnnotationIntegrationTest<Button, ButtonTestPmo> {

    private static final String STYLES = "blabla";

    public UIButtonIntegrationTest() {
        super(TestModelObjectWithString::new, ButtonTestPmo::new);
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

        assertThat(getDefaultPmo().isClicked());
    }

    @UISection
    protected static class ButtonTestPmo extends AnnotationTestPmo {

        private boolean clicked = false;

        private String caption = "dynamic";

        public ButtonTestPmo(Object modelObject) {
            super(modelObject);
        }

        @UIButton(position = 1, visible = VisibleType.DYNAMIC, styleNames = STYLES, captionType = CaptionType.DYNAMIC, icon = VaadinIcons.ADJUST, showIcon = true, shortcutKeyCode = KeyCode.ARROW_RIGHT, shortcutModifierKeys = {
                ModifierKey.ALT }, enabled = EnabledType.DYNAMIC)
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

        public boolean isClicked() {
            return clicked;
        }

        public void setClicked(boolean clicked) {
            this.clicked = clicked;
        }
    }

    protected static class TestModelObjectWithString extends TestModelObject<String> {

        @Nullable
        private String value = null;

        @SuppressWarnings("null")
        @Nullable
        @Override
        public String getValue() {
            return value;
        }

        @Override
        public void setValue(@Nullable String value) {
            this.value = value;
        }
    }
}
