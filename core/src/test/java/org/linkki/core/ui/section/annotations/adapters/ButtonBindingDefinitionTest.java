/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

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
import org.linkki.core.ui.section.annotations.CaptionType;
import org.linkki.core.ui.section.annotations.UIButton;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickShortcut;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;

@RunWith(MockitoJUnitRunner.class)
public class ButtonBindingDefinitionTest {

    @SuppressWarnings("null")
    @Captor
    private ArgumentCaptor<ClickShortcut> clickShortcutCaptor;

    @UIButton(position = 0)
    public UIButton defaultAnnotation() {
        try {
            return getClass().getMethod("defaultAnnotation", new Class<?>[] {}).getAnnotation(UIButton.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @UIButton(position = 0, showIcon = true, icon = FontAwesome.AMBULANCE, styleNames = ValoTheme.BUTTON_ICON_ONLY)
    public UIButton customAnnotation() {
        try {
            return getClass().getMethod("customAnnotation", new Class<?>[] {}).getAnnotation(UIButton.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @UIButton(position = 0, shortcutKeyCode = KeyCode.E, shortcutModifierKeys = ModifierKey.ALT)
    public UIButton shortcutButton() {
        try {
            return getClass().getMethod("shortcutButton", new Class<?>[] {}).getAnnotation(UIButton.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @UIButton(position = 1, showCaption = true, caption = "test")
    public UIButton anotherAnnotation() {
        try {
            return getClass().getMethod("anotherAnnotation", new Class<?>[] {}).getAnnotation(UIButton.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
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
    public void testNewComponent_showCaptionResultsInStaticCaptionType() {
        ButtonBindingDefinition adapter = new ButtonBindingDefinition(anotherAnnotation());

        assertThat(adapter.captionType(), is(CaptionType.STATIC));
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
        assertThat(clickShortcutCaptor.getValue().getKeyCode(), is(KeyCode.E));
        assertThat(clickShortcutCaptor.getValue().getModifiers(), is(new int[] { ModifierKey.ALT }));
    }

}
