/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.section.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import com.google.gwt.thirdparty.guava.common.collect.Maps;

import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIButtonAdapter;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UICheckBoxAdapter;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIComboBoxAdapter;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIDateFieldAdapter;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIDecimalFieldAdapter;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIDoubleFieldAdapter;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIIntegerFieldAdapter;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UITextAreaAdapter;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UITextFieldAdapter;

/**
 * A registry that links the known <code>@UI...</code> annotations to their respective adapters.
 */
public class UIElementDefinitionRegistry {

    private final Map<Class<? extends Annotation>, Class<? extends UIElementDefinition>> annotations = Maps
            .newHashMap();

    public UIElementDefinitionRegistry() {
        annotations.put(UICheckBox.class, UICheckBoxAdapter.class);
        annotations.put(UIComboBox.class, UIComboBoxAdapter.class);
        annotations.put(UIDateField.class, UIDateFieldAdapter.class);
        annotations.put(UIDecimalField.class, UIDecimalFieldAdapter.class);
        annotations.put(UIDoubleField.class, UIDoubleFieldAdapter.class);
        annotations.put(UIIntegerField.class, UIIntegerFieldAdapter.class);
        annotations.put(UITextArea.class, UITextAreaAdapter.class);
        annotations.put(UITextField.class, UITextFieldAdapter.class);
        annotations.put(UIButton.class, UIButtonAdapter.class);
    }

    /**
     * Returns {@code true} if the given annotation is a non-null annotation known in this registry.
     */
    public boolean containsAnnotation(Annotation annotation) {
        if (annotation == null) {
            return false;
        }
        return annotations.containsKey(annotation.annotationType());
    }

    /**
     * Returns the {@link UIFieldDefinition} for the given annotation. Throws an exception if the
     * annotation is {@code null} or not a known annotation in this registry. In other words, this
     * method should only be invoked if {@link #containsAnnotation(Annotation)} returns {@code true}
     * for the given annotation.
     */
    public UIElementDefinition elementDefinition(Annotation annotation) {
        Preconditions.checkNotNull(annotation, "annotation must not be null");

        Class<? extends Annotation> annotationClass = annotation.annotationType();
        Class<? extends UIElementDefinition> elementDefinitionClass = annotations.get(annotationClass);
        if (elementDefinitionClass == null) {
            throw new IllegalArgumentException(annotation + " is not a known annotation");
        }

        try {
            return elementDefinitionClass.getConstructor(annotationClass).newInstance(annotation);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
