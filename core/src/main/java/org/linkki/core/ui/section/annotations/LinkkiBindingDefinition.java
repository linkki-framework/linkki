/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an annotation used to mark a UI element created by Linkki, such as {@link UILabel} or
 * {@link UITextField}.
 * <p>
 * Every such annotation is accompanied by a {@link UIElementDefinition} which in turn is used by
 * the {@link UIAnnotationReader} to create the actual UI element based on the annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface LinkkiBindingDefinition {

    /**
     * The {@link UIElementDefinition} used to implement the annotated Annotation.
     * 
     * @see UIElementDefinition
     */
    Class<? extends UIElementDefinition> value();

}
