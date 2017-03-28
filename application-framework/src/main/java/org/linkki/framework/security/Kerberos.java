/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.security;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;

/**
 * CDI Qualifier for Kerberos specific implementations
 */
@Target({ TYPE, METHOD, FIELD, PARAMETER })
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Kerberos {

    /** The {@code @Kerberos} annotation. */
    public static final AnnotationLiteral<Kerberos> LITERAL = new AnnotationLiteral<Kerberos>() {
        private static final long serialVersionUID = 1L;
    };

    // currently no fields needed

}
