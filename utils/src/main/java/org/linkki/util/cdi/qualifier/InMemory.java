/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.util.cdi.qualifier;

import java.lang.annotation.RetentionPolicy;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.Target;

/**
 * Annotation to qualify in-memory implementations of services etc.
 */
@Target({ TYPE, METHOD, FIELD, PARAMETER })
@java.lang.annotation.Documented
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@javax.inject.Qualifier
public @interface InMemory {
    // No further attributes needed
}
