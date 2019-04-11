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

package org.linkki.core.uicreation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.junit.Test;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.defaults.nls.TestUiComponent;

public class ComponentAnnotationReaderTest {

    public static String TESTCOMPONENT = "testcomponent";

    @Test
    public void testReadComponentAnnotation() throws Exception {
        LinkkiComponentDefinition componentDefinition = ComponentAnnotationReader
                .getComponentDefinition(ClassWithComponentAnnotatedAnnotations.class.getMethod("annotated")
                        .getAnnotation(AnnotationWithComponentAnnotation.class),
                                        ClassWithComponentAnnotatedAnnotations.class.getMethod("annotated"));


        assertThat(componentDefinition.createComponent("input"), is(TESTCOMPONENT));
        assertThat(componentDefinition.createComponent(this), is(TESTCOMPONENT));
    }

    @Test
    public void testReadComponentAnnotation_AnnotatedField() throws Exception {
        LinkkiComponentDefinition componentDefinition = ComponentAnnotationReader
                .getComponentDefinition(ClassWithComponentAnnotatedAnnotations.class.getField("component")
                        .getAnnotation(AnnotationWithComponentAnnotation.class),
                                        ClassWithComponentAnnotatedAnnotations.class.getField("component"));


        assertThat(componentDefinition.createComponent("input"), is(TESTCOMPONENT));
        assertThat(componentDefinition.createComponent(this), is(TESTCOMPONENT));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadComponentAnnotation_MethodAnnotatedWithoutComponentAnnotation() throws Exception {
        @SuppressWarnings("unused")
        LinkkiComponentDefinition componentDefinition = ComponentAnnotationReader
                .getComponentDefinition(ClassWithComponentAnnotatedAnnotations.class
                        .getMethod("annotatedWithoutComponent")
                        .getAnnotation(AnnotationWithoutComponentAnnotation.class),
                                        ClassWithComponentAnnotatedAnnotations.class
                                                .getMethod("annotatedWithoutComponent"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadComponentAnnotation_MethodAnnotatedWithInvalidComponentAnnotation() throws Exception {
        @SuppressWarnings("unused")
        LinkkiComponentDefinition componentDefinition = ComponentAnnotationReader
                .getComponentDefinition(ClassWithComponentAnnotatedAnnotations.class
                        .getMethod("annotatedWithInvalidComponentAnnotation")
                        .getAnnotation(AnnotationWithInvalidCreator.class),
                                        ClassWithComponentAnnotatedAnnotations.class
                                                .getMethod("annotatedWithInvalidComponentAnnotation"));
    }

    @Test
    public void testIsLinkkiComponentDefinition() throws Exception {
        assertTrue(ComponentAnnotationReader
                .isComponentDefinition(ClassWithComponentAnnotatedAnnotations.class.getMethod("annotated")
                        .getAnnotation(AnnotationWithComponentAnnotation.class)));
        assertFalse(ComponentAnnotationReader
                .isComponentDefinition(ClassWithComponentAnnotatedAnnotations.class
                        .getMethod("annotatedWithoutComponent")
                        .getAnnotation(AnnotationWithoutComponentAnnotation.class)));
    }


    public class ClassWithComponentAnnotatedAnnotations {

        @AnnotationWithComponentAnnotation
        public TestUiComponent component = new TestUiComponent();

        @AnnotationWithComponentAnnotation
        public void annotated() {
            // nothing to do
        }

        public void notAnnotated() {
            // nothing to do
        }

        @AnnotationWithoutComponentAnnotation
        public void annotatedWithoutComponent() {
            // nothing to do
        }

        @AnnotationWithInvalidCreator
        public void annotatedWithInvalidComponentAnnotation() {
            // nothing to do
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = { ElementType.FIELD, ElementType.METHOD })
    @LinkkiComponent(AnnotationWithComponentAnnotation.Creator.class)
    public @interface AnnotationWithComponentAnnotation {

        public static class Creator implements ComponentDefinitionCreator<Annotation> {

            @Override
            public LinkkiComponentDefinition create(Annotation annotation,
                    AnnotatedElement annotatedElement) {
                return new LinkkiComponentDefinition() {

                    @Override
                    public Object createComponent(Object pmo) {
                        return TESTCOMPONENT;
                    }
                };
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = { ElementType.FIELD, ElementType.METHOD })
    public @interface AnnotationWithoutComponentAnnotation {
        // nothing to do
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = { ElementType.FIELD, ElementType.METHOD })
    @LinkkiComponent(AnnotationWithInvalidCreator.Creator.class)
    public @interface AnnotationWithInvalidCreator {
        public static class Creator implements ComponentDefinitionCreator<Annotation> {

            private Creator() {
                // prohibit instantiation
            }

            @Override
            public LinkkiComponentDefinition create(Annotation annotation,
                    AnnotatedElement annotatedElement) {
                return new LinkkiComponentDefinition() {

                    @Override
                    public Object createComponent(Object pmo) {
                        return TESTCOMPONENT;
                    }
                };
            }
        }
    }
}
