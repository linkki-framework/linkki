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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import org.linkki.core.binding.descriptor.property.BoundProperty;

/**
 * Creates a {@link BoundProperty} from an {@link Annotation} annotated with
 * {@link LinkkiBoundProperty @LinkkiBoundProperty} and its {@link AnnotatedElement}.
 * 
 * @param <T> the type of the annotation that is annotated with {@link LinkkiBoundProperty}.
 * 
 * @see LinkkiBoundProperty
 */
public interface BoundPropertyCreator<T extends Annotation> {

    /**
     * Creates a {@link BoundProperty} from an {@link Annotation} annotated with
     * {@link LinkkiBoundProperty @LinkkiBoundProperty} and its {@link AnnotatedElement}.
     * <p>
     * For example {@code @Bind} is annotated with this annotation and could be used like this:
     * 
     * <pre>
     * <code>
     * &#64;Bind
     * private TextField name;
     * </code>
     * </pre>
     * 
     * To get the {@link BoundProperty} for this field the {@link BoundPropertyAnnotationReader} would
     * be used. It reads the annotations of the {@link Field} {@code name}, finds the {@code @Bind}
     * annotation which is annotated with {@link LinkkiBoundProperty @LinkkiBoundProperty} and finally
     * instantiates the referenced {@link BoundPropertyCreator} class. Using this instance it calls
     * {@link #createBoundProperty(Annotation, AnnotatedElement)} providing the {@code @Bind} annotation
     * instance and the {@link Field} {@code name}.
     * 
     * @param annotation the {@link Annotation} annotated with
     *            {@link LinkkiBoundProperty @LinkkiBoundProperty}
     * @param annotatedElement the element annotated with the annotation
     * @return the {@link BoundProperty} which describes the property names for the binding.
     */
    BoundProperty createBoundProperty(T annotation, AnnotatedElement annotatedElement);

}