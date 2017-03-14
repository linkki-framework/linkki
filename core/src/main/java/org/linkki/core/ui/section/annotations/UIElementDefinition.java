/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import static java.util.Objects.requireNonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

import javax.annotation.Nullable;

import com.vaadin.ui.Component;

/**
 * A common interface for annotations that are used to create and bind UI elements in a view
 * generated from an annotated PMO.
 * <p>
 * As annotations can't implement an interface, the {@link UIAnnotationReader} is used to get
 * definition instances for the annotated methods of a (PMO) class.
 * <p>
 * The static methods {@link #isLinkkiBindingDefinition(Annotation)} and {@link #from(Annotation)}
 * can be used to check annotations and create {@link UIElementDefinition} instances from them.
 * 
 * @see UIAnnotationReader
 * @see LinkkiBindingDefinition
 */
public interface UIElementDefinition extends BindingDefinition {

    Component newComponent();

    int position();

    String label();

    boolean showLabel();

    String modelObject();

    /**
     * Returns {@code true} if the given annotation is a non-null annotation marked as
     * {@link LinkkiBindingDefinition}.
     */
    public static boolean isLinkkiBindingDefinition(@Nullable Annotation annotation) {
        if (annotation == null) {
            return false;
        } else {
            return annotation.annotationType().isAnnotationPresent(LinkkiBindingDefinition.class);
        }
    }

    /**
     * Returns the {@link UIFieldDefinition} for the given annotation. Throws an exception if the
     * annotation is {@code null} or not annotated as a {@link LinkkiBindingDefinition}. In other
     * words, this method should only be invoked if {@link #isLinkkiBindingDefinition(Annotation)}
     * returns {@code true} for the given annotation.
     */
    public static UIElementDefinition from(Annotation annotation) {
        Class<? extends Annotation> annotationClass = requireNonNull(annotation, "annotation must not be null")
                .annotationType();

        LinkkiBindingDefinition[] linkkiElements = annotationClass.getAnnotationsByType(LinkkiBindingDefinition.class);
        if (linkkiElements.length == 0) {
            throw new IllegalArgumentException(
                    annotation + " is not annotated as " + LinkkiBindingDefinition.class.getName());
        }
        Class<? extends UIElementDefinition> elementDefinitionClass = linkkiElements[0].value();

        try {
            return elementDefinitionClass.getConstructor(annotationClass).newInstance(annotation);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
