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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.junit.Test;
import org.linkki.core.binding.TestBind;
import org.linkki.core.binding.property.BoundPropertyAnnotationReaderTest.AnnotationWithDefectBoundPropertyCreator.DefectBoundPropertyCreator;
import org.linkki.core.binding.property.BoundPropertyAnnotationReaderTest.AnnotationWithDummyBoundPropertyCreator.DummyBoundPropertyCreator;
import org.linkki.core.binding.property.BoundPropertyAnnotationReaderTest.AnnotationWithUninstantiableBoundPropertyCreator.UninstantiableBoundPropertyCreator;
import org.linkki.core.ui.TestUiComponent;

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

    @Test(expected = IllegalArgumentException.class)
    public void testIsBoundPropertyPresent_FailsWithMultipleBoundPropertyCreators() throws Exception {
        BoundPropertyAnnotationReader.isBoundPropertyPresent(ClassWithBoundPropertyAnnotations.class
                .getField("componentWithMultipleBoundPropertyCreators"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBoundProperty_FailsIfNoBoundPropertyAnnotationPresent() throws Exception {
        BoundPropertyAnnotationReader.getBoundProperty(BoundPropertyAnnotationReaderTest.class.getField("foo"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBoundProperty_FailsWithMultipleBoundPropertyCreators() throws Exception {
        BoundPropertyAnnotationReader.getBoundProperty(ClassWithBoundPropertyAnnotations.class
                .getField("componentWithMultipleBoundPropertyCreators"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBoundProperty_FailsIfCreatorCantBeCreated() throws Exception {
        BoundPropertyAnnotationReader.getBoundProperty(ClassWithBoundPropertyAnnotations.class
                .getField("componentWithUninstatiableBoundPropertyCreator"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetBoundProperty_PropagatesExceptionFromCreator() throws Exception {
        BoundPropertyAnnotationReader.getBoundProperty(ClassWithBoundPropertyAnnotations.class
                .getField("componentWithDefectBoundPropertyCreator"));
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
        @AnnotationWithUninstantiableBoundPropertyCreator
        public TestUiComponent componentWithMultipleBoundPropertyCreators = new TestUiComponent();

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
                implements LinkkiBoundProperty.Creator<AnnotationWithDummyBoundPropertyCreator> {

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
                implements LinkkiBoundProperty.Creator<AnnotationWithDefectBoundPropertyCreator> {

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
                extends LinkkiBoundProperty.Creator<AnnotationWithUninstantiableBoundPropertyCreator> {
            // can't be instantiated
        }

    }

}
