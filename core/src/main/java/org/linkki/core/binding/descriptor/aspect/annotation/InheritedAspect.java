/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.core.binding.descriptor.aspect.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta-annotation indicating that an annotation type (that is also annotated with {@link LinkkiAspect})
 * is automatically inherited from superclasses and interfaces.
 * <p>
 * When scanning a class' superclasses and interfaces for aspects, only annotations on which
 * {@link InheritedAspect @InheritedAspect} is present are taken into account.
 * <p>
 * This differs from the {@link Inherited @Inherited} meta-annotation that only leads to annotations
 * being inherited from superclasses.
 * <p>
 * Note that this meta-annotation has no effect outside of linkki's {@link AspectAnnotationReader}.
 *
 * @since 1.1
 */
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InheritedAspect {

    // marker interface

}
