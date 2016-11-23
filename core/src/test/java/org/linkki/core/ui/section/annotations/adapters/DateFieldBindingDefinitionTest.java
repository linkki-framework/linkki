/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations.adapters;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;
import org.linkki.core.ui.section.annotations.UIDateField;
import org.linkki.core.ui.util.UiUtil;
import org.linkki.util.DateFormatRegistry;

import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;

public class DateFieldBindingDefinitionTest {

    private static final String CUSTOM_DATE_FORMAT = "yy.dd.MM";

    @UIDateField(position = 0)
    public UIDateField defaultAnnotation() {
        try {
            return getClass().getMethod("defaultAnnotation", new Class<?>[] {}).getAnnotation(UIDateField.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @UIDateField(position = 0, dateFormat = CUSTOM_DATE_FORMAT)
    public UIDateField customAnnotation() {
        try {
            return getClass().getMethod("customAnnotation", new Class<?>[] {}).getAnnotation(UIDateField.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testNewComponent_DefaultDateFormatIsUsed() {
        // Precondition
        assertThat(UiUtil.getUiLocale(), is(Locale.GERMAN));

        DateFieldBindingDefinition adapter = new DateFieldBindingDefinition(defaultAnnotation());
        Component component = adapter.newComponent();
        assertThat(component, is(instanceOf(DateField.class)));
        DateField dateField = (DateField)component;
        assertThat(dateField.getDateFormat(), is(DateFormatRegistry.PATTERN_DE));
    }

    @Test
    public void testNewComponent_CustomDateFormatIsUsed() {

        DateFieldBindingDefinition adapter = new DateFieldBindingDefinition(customAnnotation());
        Component component = adapter.newComponent();
        assertThat(component, is(instanceOf(DateField.class)));
        DateField dateField = (DateField)component;
        assertThat(dateField.getDateFormat(), is(CUSTOM_DATE_FORMAT));
    }

}
