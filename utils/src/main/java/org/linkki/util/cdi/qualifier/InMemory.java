/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.util.cdi.qualifier;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Annotation to qualify in-memory implementations of services etc.
 */
@Target({ TYPE, METHOD, FIELD, PARAMETER })
@java.lang.annotation.Documented
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@javax.inject.Qualifier
public @interface InMemory {
    // No further attributes needed

    public static final AnnotationLiteral<InMemory> LITERAL = new Literal();

    /**
     * Literal for {@link InMemory}.
     */
    public static class Literal extends AnnotationLiteral<InMemory> implements InMemory {
        private static final long serialVersionUID = 1L;

    }
}
