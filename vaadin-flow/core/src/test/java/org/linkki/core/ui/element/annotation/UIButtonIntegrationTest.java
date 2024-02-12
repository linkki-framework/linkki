/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.linkki.core.ui.element.annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.element.annotation.UIButton.ButtonComponentDefinitionCreator;
import org.linkki.core.ui.element.annotation.UIButtonIntegrationTest.ButtonTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.KeyCode;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;

import edu.umd.cs.findbugs.annotations.CheckForNull;

class UIButtonIntegrationTest extends ComponentAnnotationIntegrationTest<Button, ButtonTestPmo> {

    UIButtonIntegrationTest() {
        super(TestModelObjectWithString::new, ButtonTestPmo::new);
    }

    @Test
    void testStaticButtonProperties() {
        Button button = getDynamicComponent();
        assertThat(button.getIcon().getElement().getAttribute("icon"),
                   is(VaadinIcon.ADJUST.create().getElement().getAttribute("icon")));

        // TODO test for shortcut key - I see no possibility to get the configured shortcut

        Button staticBindingButton = getStaticComponent();
        assertThat(staticBindingButton.getText(), is("static"));
    }

    @Test
    void testCreateButtonShortcut() {
        ButtonComponentDefinitionCreator buttonComponentDefinitionCreator =
                new UIButton.ButtonComponentDefinitionCreator();

        Optional<Key> shortcutKey = buttonComponentDefinitionCreator.createShortcutKey(getAnnotation("value"));

        List<String> keys = shortcutKey.get().getKeys();
        assertThat(keys.size(), is(1));
        assertThat(keys.get(0), is("Enter"));
    }

    @Test
    void testCreateButtonShortcut_NoKeys() {
        ButtonComponentDefinitionCreator buttonComponentDefinitionCreator =
                new UIButton.ButtonComponentDefinitionCreator();

        Optional<Key> shortcutKey = buttonComponentDefinitionCreator.createShortcutKey(getAnnotation("doFoo"));

        assertThat(shortcutKey.isEmpty(), is(true));
    }

    @Test
    void testCreateButtonShortcut_MultipleKeys() {
        ButtonComponentDefinitionCreator buttonComponentDefinitionCreator =
                new UIButton.ButtonComponentDefinitionCreator();

        Optional<Key> shortcutKey = buttonComponentDefinitionCreator.createShortcutKey(getAnnotation("staticValue"));

        List<String> keys = shortcutKey.get().getKeys();
        assertThat(keys.size(), is(3));
        assertThat(keys.get(0), is("KeyA"));
        assertThat(keys.get(1), is("KeyB"));
        assertThat(keys.get(2), is("KeyC"));
    }

    @Test
    void testDynamicCaptionType() {
        Button button = getDynamicComponent();
        assertThat(button.getText(), is("dynamic"));

        getDefaultPmo().setCaption("different caption");
        modelChanged();
        assertThat(button.getText(), is("different caption"));
    }

    @Test
    void testButtonClick() {
        getDefaultPmo().setEnabled(true);
        modelChanged();
        Button button = getDynamicComponent();

        button.click();

        assertTrue(getDefaultPmo().isClicked());
    }

    @Test
    void testDerivedCaption() {
        Button button = getComponentById("doFoo");

        assertThat(button.getText(), is("DoFoo"));
    }

    @Test
    void testThemeVariants() {
        Button button = getComponentById("smallSuccess");
        String smallVariant = ButtonVariant.LUMO_SMALL.getVariantName();
        String successVariant = ButtonVariant.LUMO_SUCCESS.getVariantName();

        assertThat(button.getThemeNames(), containsInAnyOrder(smallVariant, successVariant));
    }

    private UIButton getAnnotation(String method) {
        try {
            return ButtonTestPmo.class.getMethod(method).getAnnotation(UIButton.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
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

        @UIButton(position = 10,
                visible = VisibleType.DYNAMIC,
                captionType = CaptionType.DYNAMIC,
                icon = VaadinIcon.ADJUST,
                showIcon = true,
                enabled = EnabledType.DYNAMIC,
                shortcutKeyCode = KeyCode.ENTER)
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
        @UIButton(position = 20,
                label = TEST_LABEL,
                visible = VisibleType.INVISIBLE,
                caption = "static",
                enabled = EnabledType.DISABLED,
                shortcutKeyCode = {
                        "KeyA", "KeyB", "KeyC" })
        public void staticValue() {
            // does nothing
        }

        @UIButton(position = 30)
        public void doFoo() {
            // does nothing
        }

        @UIButton(position = 40, variants = { ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL })
        public void smallSuccess() {
            // does nothing
        }

        @UIButton(position = 50)
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
