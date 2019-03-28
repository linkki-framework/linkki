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

package org.linkki.core.binding.aspect;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.util.handler.Handler;

import edu.umd.cs.findbugs.annotations.CheckForNull;

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

    private Matcher<LinkkiAspectDefinition> isInitializedWith(Class<? extends Annotation> annotation) {
        return new TypeSafeMatcher<LinkkiAspectDefinition>() {

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
}
