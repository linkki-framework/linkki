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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.util.BeanUtils;

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
     * 
     * @see BoundProperty#of(java.lang.reflect.Method)
     * @see BoundProperty#of(String)
     */
    BoundProperty createBoundProperty(T annotation, AnnotatedElement annotatedElement);

    /**
     * Creates an {@link BoundProperty#empty() empty BoundProperty}, so that any aspects bound to it use
     * no prefix for their methods.
     */
    static class EmptyPropertyCreator implements BoundPropertyCreator<Annotation> {

        @Override
        public BoundProperty createBoundProperty(Annotation annotation, AnnotatedElement annotatedElement) {
            return BoundProperty.empty();
        }

    }

    /**
     * Creates a {@link BoundProperty}, using only a property name derived from the
     * {@link AnnotatedElement}'s name with no {@linkplain BoundProperty#getModelObject() model object}
     * or {@linkplain BoundProperty#getModelAttribute() attribute}.
     */
    static class SimpleMemberNameBoundPropertyCreator implements BoundPropertyCreator<Annotation> {

        @Override
        public BoundProperty createBoundProperty(Annotation annotation, AnnotatedElement annotatedElement) {
            return createBoundProperty(annotatedElement);
        }

        public static BoundProperty createBoundProperty(AnnotatedElement annotatedElement) {
            if (annotatedElement instanceof Field) {
                return BoundProperty.of((Field)annotatedElement);
            }
            if (annotatedElement instanceof Method) {
                return BoundProperty.of((Method)annotatedElement);
            }
            throw new IllegalArgumentException("The " + AnnotatedElement.class.getSimpleName() + " must be either a "
                    + Field.class.getSimpleName() + " or a " + Method.class.getSimpleName() + " but is a "
                    + annotatedElement.getClass().getName());
        }

    }

    static class ModelBindingBoundPropertyCreator implements BoundPropertyCreator<Annotation> {
        @Override
        public BoundProperty createBoundProperty(Annotation annotation, AnnotatedElement annotatedElement) {

            Optional<Method> modelObjectAttribute = BeanUtils
                    .getMethods(annotation.annotationType(),
                                m -> m.isAnnotationPresent(LinkkiBoundProperty.ModelObject.class))
                    .reduce((m1, m2) -> {
                        throw new IllegalStateException(
                                "Duplicate definition of @" + LinkkiBoundProperty.ModelObject.class.getSimpleName()
                                        + " in " + annotation.annotationType().getName());
                    });
            Optional<Method> modelAttributeAttribute = BeanUtils
                    .getMethods(annotation.annotationType(),
                                m -> m.isAnnotationPresent(LinkkiBoundProperty.ModelAttribute.class))
                    .reduce((m1, m2) -> {
                        throw new IllegalStateException(
                                "Duplicate definition of @"
                                        + LinkkiBoundProperty.ModelAttribute.class.getSimpleName()
                                        + " in " + annotation.annotationType().getName());
                    });

            if (modelObjectAttribute.isPresent() && modelAttributeAttribute.isPresent()) {
                return SimpleMemberNameBoundPropertyCreator.createBoundProperty(annotatedElement)
                        .withModelObject(getValue(modelObjectAttribute.get(), annotation))
                        .withModelAttribute(getValue(modelAttributeAttribute.get(), annotation));
            } else {
                throw new IllegalStateException(
                        String.format("Either %s or %s annotation is missing on the repective attribute in %s",
                                      LinkkiBoundProperty.ModelObject.class.getName(),
                                      LinkkiBoundProperty.ModelAttribute.class.getName(),
                                      annotation.annotationType().getName()));
            }
        }

        private static String getValue(Method method, Annotation annotationWithBoundProperty) {
            try {
                return (String)method.invoke(annotationWithBoundProperty);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(
                        "Cannot get value from " + method + " on " + annotationWithBoundProperty, e);
            }
        }
    }
}