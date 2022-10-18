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

package org.linkki.core.binding.descriptor.property.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator.ModelBindingBoundPropertyCreator;

/**
 * Defines how a {@link BoundProperty} is derived from an {@link AnnotatedElement}. This is a
 * meta-annotation that means it is applied on another annotation which should be used in client code.
 * <p>
 * For example a {@code @UIField} annotation might be be annotated with this annotation. The defined
 * {@link BoundPropertyCreator} could read the {@code @UIField} annotation to create a
 * {@link BoundProperty} with the value of the appropriate properties of the {@code @UIField}
 * annotation.
 * <p>
 * The default {@link BoundPropertyCreator} is {@link ModelBindingBoundPropertyCreator} which uses the
 * property name of the annotated element (normally the method in a PMO) and defines a model binding
 * using the annotation properties which are annotated with {@link ModelObjectProperty} and
 * {@link ModelAttribute}. For example a {@code @UIField} annotation has a property
 * {@code modelObject()} which is annotated with {@link ModelObjectProperty} and a property
 * {@code modelAttribute} which is annotated with {@code ModelAttribute}.
 *
 * @see BoundPropertyCreator
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface LinkkiBoundProperty {

    Class<? extends BoundPropertyCreator<?>> value() default ModelBindingBoundPropertyCreator.class;

    /**
     * Annotation that marks the {@code modelObject} property within an annotation that is marked with
     * {@link LinkkiBoundProperty}.
     *
     * @deprecated This annotation has a naming conflict with {@link org.linkki.core.pmo.ModelObject},
     *         use {@link ModelObjectProperty} instead.
     */
    @Deprecated(since = "2.3")
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface ModelObject {
        // no value
    }

    /**
     * Annotation that marks the {@code modelObject} property within an annotation that is marked with
     * {@link LinkkiBoundProperty}.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface ModelObjectProperty {
        // no value
    }

    /**
     * Annotation that marks the {@code modelAttribute} property within an annotation that is marked
     * with {@link LinkkiBoundProperty}.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface ModelAttribute {
        // no value
    }
}
