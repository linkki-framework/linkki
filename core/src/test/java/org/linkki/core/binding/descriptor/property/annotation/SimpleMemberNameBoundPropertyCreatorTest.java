/*
 * Copyright Faktor Zehn GmbH.
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
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator.SimpleMemberNameBoundPropertyCreator;
import org.linkki.core.pmo.ModelObject;

public class SimpleMemberNameBoundPropertyCreatorTest {

    @Test
    public void testCreateBoundProperty_Field() throws Exception {
        AnnotatedElement annotatedElement = ClassWithBoundPropertyAnnotations.class.getDeclaredField("field");
        AnnotationWithSimpleMemberNameBoundPropertyCreator annotation = annotatedElement
                .getAnnotation(AnnotationWithSimpleMemberNameBoundPropertyCreator.class);

        BoundProperty boundProperty = new SimpleMemberNameBoundPropertyCreator().createBoundProperty(annotation,
                                                                                                     annotatedElement);

        assertThat(boundProperty.getPmoProperty(), is("field"));
        assertThat(boundProperty.getModelObject(), is(ModelObject.DEFAULT_NAME));
        assertThat(boundProperty.getModelAttribute(), is("field"));
    }

    @Test
    public void testCreateBoundProperty_GetterMethod() throws Exception {
        AnnotatedElement annotatedElement = ClassWithBoundPropertyAnnotations.class.getDeclaredMethod("getGetter");
        AnnotationWithSimpleMemberNameBoundPropertyCreator annotation = annotatedElement
                .getAnnotation(AnnotationWithSimpleMemberNameBoundPropertyCreator.class);

        BoundProperty boundProperty = new SimpleMemberNameBoundPropertyCreator().createBoundProperty(annotation,
                                                                                                     annotatedElement);

        assertThat(boundProperty.getPmoProperty(), is("getter"));
        assertThat(boundProperty.getModelObject(), is(ModelObject.DEFAULT_NAME));
        assertThat(boundProperty.getModelAttribute(), is("getter"));
    }

    @Test
    public void testCreateBoundProperty_NonGetterMethod() throws Exception {
        AnnotatedElement annotatedElement = ClassWithBoundPropertyAnnotations.class.getDeclaredMethod("otherMethod");
        AnnotationWithSimpleMemberNameBoundPropertyCreator annotation = annotatedElement
                .getAnnotation(AnnotationWithSimpleMemberNameBoundPropertyCreator.class);

        BoundProperty boundProperty = new SimpleMemberNameBoundPropertyCreator().createBoundProperty(annotation,
                                                                                                     annotatedElement);

        assertThat(boundProperty.getPmoProperty(), is("otherMethod"));
        assertThat(boundProperty.getModelObject(), is(ModelObject.DEFAULT_NAME));
        assertThat(boundProperty.getModelAttribute(), is("otherMethod"));
    }

    @Test
    public void testCreateBoundProperty_FailsOnConstructor() throws Exception {
        AnnotatedElement annotatedElement = ClassWithBoundPropertyAnnotations.class.getConstructor();
        AnnotationWithSimpleMemberNameBoundPropertyCreator annotation = annotatedElement
                .getAnnotation(AnnotationWithSimpleMemberNameBoundPropertyCreator.class);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new SimpleMemberNameBoundPropertyCreator().createBoundProperty(annotation,
                                                                           annotatedElement);
        });

    }

    public static class ClassWithBoundPropertyAnnotations {

        @AnnotationWithSimpleMemberNameBoundPropertyCreator
        private String field = "";

        @AnnotationWithSimpleMemberNameBoundPropertyCreator
        public ClassWithBoundPropertyAnnotations() {
            // annotated constructor
        }

        @AnnotationWithSimpleMemberNameBoundPropertyCreator
        public Integer getGetter() {
            return 42;
        }

        @AnnotationWithSimpleMemberNameBoundPropertyCreator
        public boolean otherMethod() {
            return true;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = { ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR })
    @LinkkiBoundProperty(SimpleMemberNameBoundPropertyCreator.class)
    public @interface AnnotationWithSimpleMemberNameBoundPropertyCreator {
        // marker annotation
    }

}
