/*
 * Copyright Faktor Zehn AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
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
