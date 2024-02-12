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

package org.linkki.core.binding.descriptor.aspect.annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.util.handler.Handler;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class AspectAnnotationReaderTest {

    @Test
    public void testCreateAspectDefinitionsFrom() throws NoSuchMethodException, SecurityException {
        TestAnnotation annotationToTest = TestClass.class.getMethod("something").getAnnotation(TestAnnotation.class);
        List<LinkkiAspectDefinition> definitions = AspectAnnotationReader
                .createAspectDefinitionsFrom(annotationToTest);
        assertThat(definitions.size(), is(2));

        LinkkiAspectDefinition definition = definitions.get(0);
        assertThat(definition, is(instanceOf(TestAspectDefinition.class)));
        assertThat(definition, isInitializedWith(TestAnnotation.class));

        LinkkiAspectDefinition anotherDefinition = definitions.get(1);
        assertThat(anotherDefinition, is(instanceOf(AnotherTestAspectDefinition.class)));
        assertThat(anotherDefinition, isInitializedWith(TestAnnotation.class));
    }

    @Test
    public void testCreateAspectDefinitionsFor() throws NoSuchMethodException, SecurityException {
        AnnotatedElement annotatedElement = TestClass.class.getMethod("somethingElse");
        List<LinkkiAspectDefinition> definitions = AspectAnnotationReader
                .createAspectDefinitionsFor(annotatedElement);
        assertThat(definitions.size(), is(3));

        LinkkiAspectDefinition definition = definitions.get(0);
        assertThat(definition, is(instanceOf(TestAspectDefinition.class)));
        assertThat(definition, isInitializedWith(TestAnnotation.class));

        LinkkiAspectDefinition anotherDefinition = definitions.get(1);
        assertThat(anotherDefinition, is(instanceOf(AnotherTestAspectDefinition.class)));
        assertThat(anotherDefinition, isInitializedWith(TestAnnotation.class));

        LinkkiAspectDefinition definitionFromSecondAnnotation = definitions.get(2);
        assertThat(definitionFromSecondAnnotation, is(instanceOf(TestAspectDefinition.class)));
        assertThat(definitionFromSecondAnnotation, isInitializedWith(AnotherTestAnnotation.class));
    }

    @Test
    public void testCreateAspectDefinitionsFor_Dynamic() throws NoSuchMethodException, SecurityException {
        class DynamicFieldTestPmo {
            @TestComponentAnnotation1(position = 10)
            @TestComponentAnnotation2(position = 10)
            @AnotherTestAnnotation
            @SuppressFBWarnings("UMAC_UNCALLABLE_METHOD_OF_ANONYMOUS_CLASS")
            public String getDynamic() {
                return "dyn";
            }
        }
        AnnotatedElement annotatedElement = DynamicFieldTestPmo.class.getMethod("getDynamic");

        List<LinkkiAspectDefinition> definitions = AspectAnnotationReader
                .createAspectDefinitionsFor(annotatedElement.getAnnotation(TestComponentAnnotation1.class),
                                            annotatedElement);

        assertThat(definitions.size(), is(2));
        assertThat(definitions.get(0), is(instanceOf(TestAspectDefinition.class)));
        assertThat(definitions.get(0), isInitializedWith(TestComponentAnnotation1.class));
        assertThat(definitions.get(1), is(instanceOf(TestAspectDefinition.class)));
        assertThat(definitions.get(1), isInitializedWith(AnotherTestAnnotation.class));

        // choosing the other component annotation has the same common aspect but a different aspect
        // from the component annotation
        definitions = AspectAnnotationReader
                .createAspectDefinitionsFor(annotatedElement.getAnnotation(TestComponentAnnotation2.class),
                                            annotatedElement);

        assertThat(definitions.size(), is(2));
        assertThat(definitions.get(0), is(instanceOf(YetAnotherTestAspectDefinition.class)));
        assertThat(definitions.get(0), isInitializedWith(TestComponentAnnotation2.class));
        assertThat(definitions.get(1), is(instanceOf(TestAspectDefinition.class)));
        assertThat(definitions.get(1), isInitializedWith(AnotherTestAnnotation.class));
    }

    private Matcher<LinkkiAspectDefinition> isInitializedWith(Class<? extends Annotation> annotation) {
        return new TypeSafeMatcher<>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("is initialized");
            }

            @Override
            protected boolean matchesSafely(LinkkiAspectDefinition definition) {
                Annotation initializedAnnotation = ((TestAspectDefinition)definition).initializedAnnotation;
                return initializedAnnotation != null && annotation.equals(initializedAnnotation.annotationType());
            }
        };
    }

    private static class TestClass {
        @TestAnnotation
        public void something() {
            // does nothing
        }

        @TestAnnotation
        @AnotherTestAnnotation
        public void somethingElse() {
            // does nothing
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @LinkkiAspect(TestAspectDefinitionCreator.class)
    @LinkkiAspect(AnotherTestAspectDefinitionCreator.class)
    private @interface TestAnnotation {
        // not used
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @LinkkiAspect(TestAspectDefinitionCreator.class)
    private @interface AnotherTestAnnotation {
        // not used
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @LinkkiBoundProperty(TestComponentAnnotation1BoundPropertyCreator.class)
    @LinkkiComponent(TestComponentAnnotation1ComponentDefinitionCreator.class)
    @LinkkiPositioned
    @LinkkiAspect(TestAspectDefinitionCreator.class)
    private @interface TestComponentAnnotation1 {

        @LinkkiPositioned.Position
        int position();

        String modelObject() default ModelObject.DEFAULT_NAME;

        String modelAttribute() default "";

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @LinkkiBoundProperty(TestComponentAnnotation2BoundPropertyCreator.class)
    @LinkkiComponent(TestComponentAnnotation2ComponentDefinitionCreator.class)
    @LinkkiPositioned
    @LinkkiAspect(YetAnotherTestAspectDefinitionCreator.class)
    public @interface TestComponentAnnotation2 {

        @LinkkiPositioned.Position
        int position();

        String modelObject() default ModelObject.DEFAULT_NAME;

        String modelAttribute() default "";

    }

    class TestComponentAnnotation1ComponentDefinition implements LinkkiComponentDefinition {
        @Override
        public Object createComponent(Object pmo) {
            return new TestUiComponent();
        }
    }

    class TestComponentAnnotation1ComponentDefinitionCreator
            implements ComponentDefinitionCreator<TestComponentAnnotation1> {
        @Override
        public LinkkiComponentDefinition create(TestComponentAnnotation1 annotation,
                AnnotatedElement annotatedElement) {
            return new TestComponentAnnotation1ComponentDefinition();
        }
    }

    class TestComponentAnnotation1BoundPropertyCreator implements BoundPropertyCreator<TestComponentAnnotation1> {
        @Override
        public BoundProperty createBoundProperty(TestComponentAnnotation1 annotation,
                AnnotatedElement annotatedElement) {
            return BoundProperty.of((Method)annotatedElement).withModelAttribute(annotation.modelAttribute())
                    .withModelObject(annotation.modelObject());
        }
    }

    class TestComponentAnnotation2ComponentDefinition implements LinkkiComponentDefinition {
        @Override
        public Object createComponent(Object pmo) {
            return new TestUiComponent();
        }
    }

    class TestComponentAnnotation2ComponentDefinitionCreator
            implements ComponentDefinitionCreator<TestComponentAnnotation2> {
        @Override
        public LinkkiComponentDefinition create(TestComponentAnnotation2 annotation,
                AnnotatedElement annotatedElement) {
            return new TestComponentAnnotation2ComponentDefinition();
        }
    }

    class TestComponentAnnotation2BoundPropertyCreator implements BoundPropertyCreator<TestComponentAnnotation2> {
        @Override
        public BoundProperty createBoundProperty(TestComponentAnnotation2 annotation,
                AnnotatedElement annotatedElement) {
            return BoundProperty.of((Method)annotatedElement).withModelAttribute(annotation.modelAttribute())
                    .withModelObject(annotation.modelObject());
        }
    }

    public static class TestAspectDefinitionCreator implements AspectDefinitionCreator<Annotation> {

        @Override
        public LinkkiAspectDefinition create(Annotation annotation) {
            return new TestAspectDefinition(annotation);
        }

    }

    public static class AnotherTestAspectDefinitionCreator implements AspectDefinitionCreator<Annotation> {

        @Override
        public LinkkiAspectDefinition create(Annotation annotation) {
            return new AnotherTestAspectDefinition(annotation);
        }

    }

    public static class YetAnotherTestAspectDefinitionCreator implements AspectDefinitionCreator<Annotation> {

        @Override
        public LinkkiAspectDefinition create(Annotation annotation) {
            return new YetAnotherTestAspectDefinition(annotation);
        }

    }

    private static class TestAspectDefinition implements LinkkiAspectDefinition {

        @CheckForNull
        private Annotation initializedAnnotation;

        public TestAspectDefinition(Annotation annotation) {
            this.initializedAnnotation = annotation;
        }

        @Override
        public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
            return Handler.NOP_HANDLER;
        }
    }

    private static class AnotherTestAspectDefinition extends TestAspectDefinition {
        public AnotherTestAspectDefinition(Annotation annotation) {
            super(annotation);
        }
    }

    private static class YetAnotherTestAspectDefinition extends TestAspectDefinition {
        public YetAnotherTestAspectDefinition(Annotation annotation) {
            super(annotation);
        }
    }
}
