/*
 * Copyright Faktor Zehn AG.
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

package org.linkki.core.binding.aspect;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.junit.Test;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.util.handler.Handler;

public class AspectAnnotationReaderTest {

    @Test
    public void testCreateAspectDefinitionsFrom() throws NoSuchMethodException, SecurityException {
        TestAnnotation annotationToTest = TestClass.class.getMethod("something").getAnnotation(TestAnnotation.class);
        List<LinkkiAspectDefinition> definitions = AspectAnnotationReader.createAspectDefinitionsFrom(annotationToTest);
        assertThat(definitions.size(), is(2));

        LinkkiAspectDefinition definition = definitions.get(0);
        assertThat(definition, instanceOf(TestAspectDefinition.class));
        assertThat(((TestAspectDefinition)definition).initialized, is(true));

        LinkkiAspectDefinition anotherDefinition = definitions.get(1);
        assertThat(anotherDefinition, instanceOf(AnotherTestAspectDefinition.class));
        assertThat(((AnotherTestAspectDefinition)anotherDefinition).anotherInitialized, is(true));
    }

    private static class TestClass {
        @TestAnnotation
        public void something() {
            // does nothing
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @LinkkiAspect(TestAspectDefinition.class)
    @LinkkiAspect(AnotherTestAspectDefinition.class)
    private @interface TestAnnotation {
        // not used
    }

    private static class TestAspectDefinition implements LinkkiAspectDefinition {

        private boolean initialized;

        @SuppressWarnings("unused")
        public TestAspectDefinition() {
            super();
        }

        @Override
        public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
            return Handler.NOP_HANDLER;
        }

        @Override
        public void initialize(Annotation annotation) {
            initialized = true;
        }
    }

    private static class AnotherTestAspectDefinition implements LinkkiAspectDefinition {

        private boolean anotherInitialized;

        @SuppressWarnings("unused")
        public AnotherTestAspectDefinition() {
            super();
        }

        @Override
        public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
            return Handler.NOP_HANDLER;
        }

        @Override
        public void initialize(Annotation annotation) {
            anotherInitialized = true;
        }
    }
}
