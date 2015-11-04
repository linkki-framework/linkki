/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations.adapters;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.adapters.UIButtonAdapter;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;

public class UIButtonAdapterTest {

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

    @Test
    public void testNewComponent_DefaultAnnotations() {
        UIButtonAdapter adapter = new UIButtonAdapter(defaultAnnotation());
        Component component = adapter.newComponent();
        assertThat(component, is(instanceOf(Button.class)));
        Button button = (Button)component;
        assertThat(button.getCaption(), is(""));
        assertThat(button.getIcon(), is(nullValue()));
        assertThat(button.getStyleName(), is(""));

    }

    @Test
    public void testNewComponent_CustomAnnotationsAreUsed() {
        UIButtonAdapter adapter = new UIButtonAdapter(customAnnotation());
        Component component = adapter.newComponent();
        assertThat(component, is(instanceOf(Button.class)));
        Button button = (Button)component;
        assertThat(button.getIcon(), is(FontAwesome.AMBULANCE));
        assertThat(button.getStyleName(), is(ValoTheme.BUTTON_ICON_ONLY));

    }

}
