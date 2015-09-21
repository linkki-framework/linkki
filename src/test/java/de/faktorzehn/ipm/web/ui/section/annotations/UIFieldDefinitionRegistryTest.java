/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.section.annotations;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.lang.annotation.Annotation;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.junit.Test;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;

import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UICheckBoxAdapter;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIComboBoxAdapter;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIDateFieldAdapter;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIDecimalFieldAdapter;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIDoubleFieldAdapter;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIIntegerFieldAdapter;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UITextAreaAdapter;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UITextFieldAdapter;

public class UIFieldDefinitionRegistryTest {

    private final UIFieldDefinitionRegistry registry = new UIFieldDefinitionRegistry();

    @UICheckBox(position = 0)
    @UIComboBox(position = 0)
    @UIDateField(position = 0)
    @UIDecimalField(position = 0)
    @UIDoubleField(position = 0)
    @UIIntegerField(position = 0)
    @UITextArea(position = 0)
    @UITextField(position = 0)
    @OverridingMethodsMustInvokeSuper
    public void annotatedMethod() {
        // Nothing to do
    }

    private <T extends Annotation> T annotation(Class<T> annotationClass) {
        try {
            T annotation = getClass().getMethod("annotatedMethod", new Class<?>[] {}).getAnnotation(annotationClass);
            return Preconditions.checkNotNull(annotation, "Missing annotation @%s", annotationClass.getSimpleName());
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testContainsAnnotation() {
        assertThat(registry.containsAnnotation(null), is(false));
        assertThat(registry.containsAnnotation(annotation(OverridingMethodsMustInvokeSuper.class)), is(false));

        assertThat(registry.containsAnnotation(annotation(UICheckBox.class)), is(true));
        assertThat(registry.containsAnnotation(annotation(UIComboBox.class)), is(true));
        assertThat(registry.containsAnnotation(annotation(UIDateField.class)), is(true));
        assertThat(registry.containsAnnotation(annotation(UIDecimalField.class)), is(true));
        assertThat(registry.containsAnnotation(annotation(UIDoubleField.class)), is(true));
        assertThat(registry.containsAnnotation(annotation(UIIntegerField.class)), is(true));
        assertThat(registry.containsAnnotation(annotation(UITextArea.class)), is(true));
        assertThat(registry.containsAnnotation(annotation(UITextField.class)), is(true));
    }

    @Test
    public void testFieldDefinition() {
        //@formatter:off
        assertThat(registry.fieldDefinition(annotation(UICheckBox.class)), is(instanceOf(UICheckBoxAdapter.class)));
        assertThat(registry.fieldDefinition(annotation(UIComboBox.class)), is(instanceOf(UIComboBoxAdapter.class)));
        assertThat(registry.fieldDefinition(annotation(UIDateField.class)), is(instanceOf(UIDateFieldAdapter.class)));
        assertThat(registry.fieldDefinition(annotation(UIDecimalField.class)), is(instanceOf(UIDecimalFieldAdapter.class)));
        assertThat(registry.fieldDefinition(annotation(UIDoubleField.class)), is(instanceOf(UIDoubleFieldAdapter.class)));
        assertThat(registry.fieldDefinition(annotation(UIIntegerField.class)), is(instanceOf(UIIntegerFieldAdapter.class)));
        assertThat(registry.fieldDefinition(annotation(UITextArea.class)), is(instanceOf(UITextAreaAdapter.class)));
        assertThat(registry.fieldDefinition(annotation(UITextField.class)), is(instanceOf(UITextFieldAdapter.class)));
        //@formatter:on
    }

    @Test(expected = NullPointerException.class)
    public void testFieldDefinition_ThrowsExceptionForNullAnnotation() {
        registry.fieldDefinition(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFieldDefinition_ThrowsExceptionForUnknownAnnotation() {
        registry.fieldDefinition(annotation(OverridingMethodsMustInvokeSuper.class));
    }

}
