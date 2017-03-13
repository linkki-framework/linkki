/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.findbugs.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;

/**
 * Annotation to use in package-info.java to specify that all method parameters and return values
 * are to be considered {@link Nonnull @Nonnull} by FindBugs and Eclipse(when this annotation is
 * used as the 'NonNullByDefault' annotation in Eclipse's compiler settings).
 */
@Documented
@Retention(RUNTIME)
// Findbugs applies this to all locations listed in @TypeQualifierDefault
@Nonnull
@TypeQualifierDefault({ METHOD, PARAMETER })
public @interface ParametersAndReturnValuesAreNonnullByDefault {
    // just an annotation
}
