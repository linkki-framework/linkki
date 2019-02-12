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

package org.linkki.core.binding.property;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

/**
 * Defines how a {@link BoundProperty} is derived from an {@link AnnotatedElement}. This is a
 * meta-annotation that means it is applied on another annotation which should be used in client code.
 * <p>
 * For example a {@code @Bind} annotation might be be annotated with this annotation. The defined
 * {@link BoundPropertyCreator} could read the {@code @Bind} annotation to create a
 * {@link BoundProperty} with the value of the appropriate properties of the {@code @Bind} annotation.
 * 
 * @see BoundPropertyCreator
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface LinkkiBoundProperty {

    Class<? extends BoundPropertyCreator<?>> value();

}
