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
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.TestBind;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyAnnotationReaderTest.AnnotationWithDefectBoundPropertyCreator.DefectBoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyAnnotationReaderTest.AnnotationWithDummyBoundPropertyCreator.DummyBoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyAnnotationReaderTest.AnnotationWithUninstantiableBoundPropertyCreator.UninstantiableBoundPropertyCreator;
import org.linkki.core.defaults.nls.TestUiComponent;

public class BoundPropertyAnnotationReaderTest {

    public String foo = "bar";

    @Test
    public void testIsBoundPropertyPresent() throws Exception {
        assertThat(BoundPropertyAnnotationReader.isBoundPropertyPresent(BoundPropertyAnnotationReaderTest.class),
                   is(false));
        assertThat(BoundPropertyAnnotationReader.isBoundPropertyPresent(BoundPropertyAnnotationReaderTest.class
                .getMethod("testIsBoundPropertyPresent")), is(false));
        assertThat(BoundPropertyAnnotationReader.isBoundPropertyPresent(BoundPropertyAnnotationReaderTest.class
                .getField("foo")), is(false));

        assertThat(BoundPropertyAnnotationReader.isBoundPropertyPresent(TestBind.class), is(false));

        assertThat(BoundPropertyAnnotationReader.isBoundPropertyPresent(ClassWithBoundPropertyAnnotations.class),
                   is(false));

        assertThat(BoundPropertyAnnotationReader.isBoundPropertyPresent(ClassWithBoundPropertyAnnotations.class
                .getMethod("getComponent")), is(true));
        assertThat(BoundPropertyAnnotationReader.isBoundPropertyPresent(ClassWithBoundPropertyAnnotations.class
                .getField("component")), is(true));
        assertThat(BoundPropertyAnnotationReader.isBoundPropertyPresent(ClassWithBoundPropertyAnnotations.class
                .getField("componentWithDefectBoundPropertyCreator")), is(true));
        assertThat(BoundPropertyAnnotationReader.isBoundPropertyPresent(ClassWithBoundPropertyAnnotations.class
                .getField("componentWithUninstatiableBoundPropertyCreator")), is(true));
    }

    @Test
    public void testIsBoundPropertyPresent_multipleBoundPropertyCreators() throws Exception {
        assertTrue(BoundPropertyAnnotationReader.isBoundPropertyPresent(ClassWithBoundPropertyAnnotations.class
                .getField("componentWithMultipleBoundProperties")));
    }

    @Test
    public void testGetBoundProperty_FailsIfNoBoundPropertyAnnotationPresent() throws Exception {
        try {
            BoundPropertyAnnotationReader.getBoundProperty(BoundPropertyAnnotationReaderTest.class.getField("foo"));
            fail("expected an " + IllegalArgumentException.class.getSimpleName());
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), containsString(BoundPropertyAnnotationReaderTest.class.getSimpleName()));
            assertThat(e.getMessage(), containsString("foo"));
            assertThat(e.getMessage(), containsString("isBoundPropertyPresent"));
        }
    }

    @Test
    public void testGetBoundProperty_FailsWithMultipleBoundProperties() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            BoundPropertyAnnotationReader.getBoundProperty(ClassWithBoundPropertyAnnotations.class
                    .getField("componentWithMultipleBoundProperties"));
        });

    }

    @Test
    public void testGetBoundProperty_multipleSameBoundProperties() throws Exception {
        BoundPropertyAnnotationReader.getBoundProperty(ClassWithBoundPropertyAnnotations.class
                .getField("componentWithMultipleSameBoundProperties"));
    }

    @Test
    public void testGetBoundProperty_FailsIfCreatorCantBeCreated() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            BoundPropertyAnnotationReader.getBoundProperty(ClassWithBoundPropertyAnnotations.class
                    .getField("componentWithUninstatiableBoundPropertyCreator"));
        });

    }

    @Test
    public void testGetBoundProperty_PropagatesExceptionFromCreator() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            BoundPropertyAnnotationReader.getBoundProperty(ClassWithBoundPropertyAnnotations.class
                    .getField("componentWithDefectBoundPropertyCreator"));
        });
    }

    @Test
    public void testGetBoundProperty() throws Exception {
        BoundProperty boundProperty = BoundPropertyAnnotationReader
                .getBoundProperty(ClassWithBoundPropertyAnnotations.class
                        .getField("component"));

        assertThat(boundProperty.getPmoProperty(), is("foo"));
    }

    public static class ClassWithBoundPropertyAnnotations {

        @AnnotationWithDummyBoundPropertyCreator("foo")
        public TestUiComponent component = new TestUiComponent();

        @AnnotationWithDefectBoundPropertyCreator
        public TestUiComponent componentWithDefectBoundPropertyCreator = new TestUiComponent();

        @AnnotationWithUninstantiableBoundPropertyCreator
        public TestUiComponent componentWithUninstatiableBoundPropertyCreator = new TestUiComponent();

        @TestBind
        @AnnotationWithDummyBoundPropertyCreator("notPmoPropertyName")
        public TestUiComponent componentWithMultipleBoundProperties = new TestUiComponent();

        @TestBind(modelAttribute = "someModelAttribute")
        @AnnotationWithDummyBoundPropertyCreator("componentWithMultipleSameBoundProperties")
        public TestUiComponent componentWithMultipleSameBoundProperties = new TestUiComponent();

        @TestBind
        public TestUiComponent getComponent() {
            return component;
        }

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = { ElementType.FIELD, ElementType.METHOD })
    @LinkkiBoundProperty(DummyBoundPropertyCreator.class)
    public @interface AnnotationWithDummyBoundPropertyCreator {

        String value();

        public static class DummyBoundPropertyCreator
                implements BoundPropertyCreator<AnnotationWithDummyBoundPropertyCreator> {

            @Override
            public BoundProperty createBoundProperty(AnnotationWithDummyBoundPropertyCreator annotation,
                    AnnotatedElement annotatedElement) {
                return BoundProperty.of(annotation.value());
            }

        }

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = { ElementType.FIELD, ElementType.METHOD })
    @LinkkiBoundProperty(DefectBoundPropertyCreator.class)
    public @interface AnnotationWithDefectBoundPropertyCreator {

        public static class DefectBoundPropertyCreator
                implements BoundPropertyCreator<AnnotationWithDefectBoundPropertyCreator> {

            @Override
            public BoundProperty createBoundProperty(AnnotationWithDefectBoundPropertyCreator annotation,
                    AnnotatedElement annotatedElement) {
                throw new IllegalStateException("Won't create");
            }

        }

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = { ElementType.FIELD, ElementType.METHOD })
    @LinkkiBoundProperty(UninstantiableBoundPropertyCreator.class)
    public @interface AnnotationWithUninstantiableBoundPropertyCreator {

        public interface UninstantiableBoundPropertyCreator
                extends BoundPropertyCreator<AnnotationWithUninstantiableBoundPropertyCreator> {
            // can't be instantiated
        }

    }

}
