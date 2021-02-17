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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator.ModelBindingBoundPropertyCreator;

public class ModelBindingBoundPropertyCreatorTest {

    @Test
    public void testCreateBoundProperty_Field() throws Exception {
        AnnotatedElement annotatedElement = ClassWithBoundPropertyAnnotations.class.getDeclaredField("field");
        AnnotatedAnnotation annotation = annotatedElement.getAnnotation(AnnotatedAnnotation.class);

        BoundProperty boundProperty = new ModelBindingBoundPropertyCreator().createBoundProperty(annotation,
                                                                                                 annotatedElement);

        assertThat(boundProperty.getPmoProperty(), is("field"));
        assertThat(boundProperty.getModelObject(), is(ClassWithBoundPropertyAnnotations.MODEL_OBJECT));
        assertThat(boundProperty.getModelAttribute(), is(ClassWithBoundPropertyAnnotations.MODEL_ATTRIBUTE));
    }

    @Test
    public void testCreateBoundProperty_GetterMethod() throws Exception {
        AnnotatedElement annotatedElement = ClassWithBoundPropertyAnnotations.class.getDeclaredMethod("getGetter");
        AnnotatedAnnotation annotation = annotatedElement.getAnnotation(AnnotatedAnnotation.class);

        BoundProperty boundProperty = new ModelBindingBoundPropertyCreator().createBoundProperty(annotation,
                                                                                                 annotatedElement);

        assertThat(boundProperty.getPmoProperty(), is("getter"));
        assertThat(boundProperty.getModelObject(), is(ClassWithBoundPropertyAnnotations.MODEL_OBJECT));
        assertThat(boundProperty.getModelAttribute(), is(ClassWithBoundPropertyAnnotations.MODEL_ATTRIBUTE));
    }

    @Test
    public void testCreateBoundProperty_NonGetterMethod() throws Exception {
        AnnotatedElement annotatedElement = ClassWithBoundPropertyAnnotations.class.getDeclaredMethod("otherMethod");
        AnnotatedAnnotation annotation = annotatedElement
                .getAnnotation(AnnotatedAnnotation.class);

        BoundProperty boundProperty = new ModelBindingBoundPropertyCreator().createBoundProperty(annotation,
                                                                                                 annotatedElement);

        assertThat(boundProperty.getPmoProperty(), is("otherMethod"));
        assertThat(boundProperty.getModelObject(), is(ClassWithBoundPropertyAnnotations.MODEL_OBJECT));
        assertThat(boundProperty.getModelAttribute(), is(ClassWithBoundPropertyAnnotations.MODEL_ATTRIBUTE));
    }

    @Test
    public void testCreateBoundProperty_FailsOnConstructor() throws Exception {
        AnnotatedElement annotatedElement = ClassWithBoundPropertyAnnotations.class.getConstructor();
        AnnotatedAnnotation annotation = annotatedElement.getAnnotation(AnnotatedAnnotation.class);

        ModelBindingBoundPropertyCreator modelBindingBoundPropertyCreator = new ModelBindingBoundPropertyCreator();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            modelBindingBoundPropertyCreator.createBoundProperty(annotation, annotatedElement);
        });
    }

    @Test
    public void testCreateBoundProperty_FailsIfModelObjectAnnotationMissing()
            throws NoSuchMethodException, SecurityException {
        AnnotatedElement annotatedElement = ClassWithBoundPropertyAnnotations.class
                .getDeclaredMethod("methodWithMissingModelObjectAnnotation");
        MissingModelObjectAnnotation annotation = annotatedElement
                .getAnnotation(MissingModelObjectAnnotation.class);

        ModelBindingBoundPropertyCreator modelBindingBoundPropertyCreator = new ModelBindingBoundPropertyCreator();

        Assertions.assertThrows(IllegalStateException.class, () -> {
            modelBindingBoundPropertyCreator.createBoundProperty(annotation, annotatedElement);
        });
    }

    @Test
    public void testCreateBoundProperty_FailsIfModelAttributeAnnotationMissing()
            throws NoSuchMethodException, SecurityException {
        AnnotatedElement annotatedElement = ClassWithBoundPropertyAnnotations.class
                .getDeclaredMethod("methodWithMissingModelAttributeAnnotation");
        MissingModelAttributeAnnotation annotation = annotatedElement
                .getAnnotation(MissingModelAttributeAnnotation.class);

        ModelBindingBoundPropertyCreator modelBindingBoundPropertyCreator = new ModelBindingBoundPropertyCreator();

        Assertions.assertThrows(IllegalStateException.class, () -> {
            modelBindingBoundPropertyCreator.createBoundProperty(annotation, annotatedElement);
        });
    }

    @Test
    public void testCreateBoundProperty_FailsIfDuplicateModelObject()
            throws NoSuchMethodException, SecurityException {
        AnnotatedElement annotatedElement = ClassWithBoundPropertyAnnotations.class
                .getDeclaredMethod("methodWithDuplicateModelObjectAnnotation");
        DuplicateModelObjectAnnotation annotation = annotatedElement
                .getAnnotation(DuplicateModelObjectAnnotation.class);

        ModelBindingBoundPropertyCreator modelBindingBoundPropertyCreator = new ModelBindingBoundPropertyCreator();

        Assertions.assertThrows(IllegalStateException.class, () -> {
            modelBindingBoundPropertyCreator.createBoundProperty(annotation, annotatedElement);
        });
    }

    @Test
    public void testCreateBoundProperty_FailsIfDuplicateModelAttribute()
            throws NoSuchMethodException, SecurityException {
        AnnotatedElement annotatedElement = ClassWithBoundPropertyAnnotations.class
                .getDeclaredMethod("methodWithDuplicateModelAttributeAnnotation");
        DuplicateModelAttributeAnnotation annotation = annotatedElement
                .getAnnotation(DuplicateModelAttributeAnnotation.class);

        ModelBindingBoundPropertyCreator modelBindingBoundPropertyCreator = new ModelBindingBoundPropertyCreator();

        Assertions.assertThrows(IllegalStateException.class, () -> {
            modelBindingBoundPropertyCreator.createBoundProperty(annotation, annotatedElement);
        });
    }

    public static class ClassWithBoundPropertyAnnotations {

        public static final String MODEL_OBJECT = "MO";
        public static final String MODEL_ATTRIBUTE = "MA";

        @AnnotatedAnnotation(modelAttribute = MODEL_ATTRIBUTE, modelObject = MODEL_OBJECT)
        private String field = "";

        @AnnotatedAnnotation(modelAttribute = MODEL_ATTRIBUTE, modelObject = MODEL_OBJECT)
        public ClassWithBoundPropertyAnnotations() {
            // annotated constructor
        }

        @AnnotatedAnnotation(modelAttribute = MODEL_ATTRIBUTE, modelObject = MODEL_OBJECT)
        public Integer getGetter() {
            return 42;
        }

        @AnnotatedAnnotation(modelAttribute = MODEL_ATTRIBUTE, modelObject = MODEL_OBJECT)
        public boolean otherMethod() {
            return true;
        }

        @MissingModelAttributeAnnotation
        public void methodWithMissingModelAttributeAnnotation() {
            // nothing to do
        }

        @MissingModelObjectAnnotation
        public void methodWithMissingModelObjectAnnotation() {
            // nothing to do
        }

        @DuplicateModelObjectAnnotation
        public void methodWithDuplicateModelObjectAnnotation() {
            // nothing to do
        }

        @DuplicateModelAttributeAnnotation
        public void methodWithDuplicateModelAttributeAnnotation() {
            // nothing to do
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = { ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR })
    @LinkkiBoundProperty
    public @interface AnnotatedAnnotation {

        @LinkkiBoundProperty.ModelObject
        String modelObject();

        @LinkkiBoundProperty.ModelAttribute
        String modelAttribute();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = { ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR })
    @LinkkiBoundProperty
    public @interface MissingModelAttributeAnnotation {

        @LinkkiBoundProperty.ModelObject
        String modelObject() default "";

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = { ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR })
    @LinkkiBoundProperty
    public @interface MissingModelObjectAnnotation {

        @LinkkiBoundProperty.ModelAttribute
        String modelAttribute() default "";

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = { ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR })
    @LinkkiBoundProperty
    public @interface DuplicateModelObjectAnnotation {

        @LinkkiBoundProperty.ModelObject
        String modelObject() default "";

        @LinkkiBoundProperty.ModelObject
        String modelObject2() default "";

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = { ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR })
    @LinkkiBoundProperty
    public @interface DuplicateModelAttributeAnnotation {

        @LinkkiBoundProperty.ModelAttribute
        String modelAttribute() default "";

        @LinkkiBoundProperty.ModelAttribute
        String modelAttribute2() default "";

    }
}
