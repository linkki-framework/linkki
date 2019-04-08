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
package org.linkki.core.ui.section.annotations.adapters;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.ui.section.annotations.UIButton;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickShortcut;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;

import edu.umd.cs.findbugs.annotations.NonNull;

@RunWith(MockitoJUnitRunner.class)
public class ButtonBindingDefinitionTest {

    
    @Captor
    private ArgumentCaptor<ClickShortcut> clickShortcutCaptor;

    private UIButton getAnnotation(String name) {
        try {
            @NonNull
            
            UIButton annotation = getClass().getMethod(name, new Class<?>[] {})
                    .getAnnotation(UIButton.class);
            return annotation;
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @UIButton(position = 0)
    public UIButton defaultAnnotation() {
        return getAnnotation("defaultAnnotation");
    }

    @UIButton(position = 0, showIcon = true, icon = FontAwesome.AMBULANCE, styleNames = ValoTheme.BUTTON_ICON_ONLY)
    public UIButton customAnnotation() {
        return getAnnotation("customAnnotation");
    }

    @UIButton(position = 0, shortcutKeyCode = KeyCode.E, shortcutModifierKeys = ModifierKey.ALT)
    public UIButton shortcutButton() {
        return getAnnotation("shortcutButton");
    }

    @UIButton(position = 1, showCaption = true, caption = "test")
    public UIButton anotherAnnotation() {
        return getAnnotation("anotherAnnotation");
    }

    @Test
    public void testNewComponent_DefaultAnnotations() {
        ButtonBindingDefinition adapter = new ButtonBindingDefinition(defaultAnnotation());
        Component component = adapter.newComponent();
        assertThat(component, is(instanceOf(Button.class)));
        Button button = (Button)component;
        assertThat(button.getIcon(), is(nullValue()));
        assertThat(button.getStyleName(), is(""));
    }

    @Test
    public void testNewComponent_CustomAnnotationsAreUsed() {
        ButtonBindingDefinition adapter = new ButtonBindingDefinition(customAnnotation());
        Component component = adapter.newComponent();
        assertThat(component, is(instanceOf(Button.class)));
        Button button = (Button)component;
        assertThat(button.getIcon(), is(FontAwesome.AMBULANCE));
        assertThat(button.getStyleName(), is(ValoTheme.BUTTON_ICON_ONLY));
    }

    @Test
    public void testNewComponent_UseShortcutKeyCode() {
        Button button = new ButtonBindingDefinition(shortcutButton()).newComponent();

        assertThat(button, not(nullValue()));
        // cannot really test the keycode. But when we set a new shortcut the old one should be
        // removed - so we caption the previous shortcut when it is removed - very ugly, may fail
        // when implementation of Vaadin changes :(
        Button buttonSpy = spy(button);
        buttonSpy.setClickShortcut(KeyCode.ENTER);
        verify(buttonSpy).removeShortcutListener(clickShortcutCaptor.capture());
        
        @NonNull
        ClickShortcut value = clickShortcutCaptor.getValue();
        assertThat(value.getKeyCode(), is(KeyCode.E));
        assertThat(value.getModifiers(), is(new int[] { ModifierKey.ALT }));
    }

}
