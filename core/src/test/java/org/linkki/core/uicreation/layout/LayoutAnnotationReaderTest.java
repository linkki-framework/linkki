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

package org.linkki.core.uicreation.layout;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.linkki.test.matcher.Matchers.absent;
import static org.linkki.test.matcher.Matchers.assertThat;
import static org.linkki.test.matcher.Matchers.present;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;

public class LayoutAnnotationReaderTest {

    @Test
    public void testIsLayoutDefinition() {
        assertThat(LayoutAnnotationReader.isLayoutDefinition(DummyPmo.class.getAnnotation(DummyLayout.class)));
        assertThat(LayoutAnnotationReader.isLayoutDefinition(DummyLayout.class.getAnnotation(LinkkiLayout.class)),
                   is(false));
    }

    @Test
    public void testFindLayoutDefinition() {
        Optional<LinkkiLayoutDefinition> layoutDefinition = LayoutAnnotationReader.findLayoutDefinition(DummyPmo.class);
        assertThat(layoutDefinition, is(present()));
        assertThat(layoutDefinition.get(), is(instanceOf(DummyLayoutDefinition.class)));

        assertThat(LayoutAnnotationReader.findLayoutDefinition(String.class), is(absent()));
    }

    static class DummyLayoutDefinition implements LinkkiLayoutDefinition {

        @Override
        public void createChildren(Object parentComponent, Object pmo, BindingContext bindingContext) {
            // don't
        }

    }

    public static class DummyLayoutDefinitionCreator implements LayoutDefinitionCreator<DummyLayout> {

        @Override
        public LinkkiLayoutDefinition create(DummyLayout annotation, AnnotatedElement annotatedElement) {
            return new DummyLayoutDefinition();
        }

    }

    @Retention(RUNTIME)
    @Target(TYPE)
    @LinkkiLayout(DummyLayoutDefinitionCreator.class)
    @interface DummyLayout {
        // test
    }

    @DummyLayout
    static class DummyPmo {
        // test
    }

}
