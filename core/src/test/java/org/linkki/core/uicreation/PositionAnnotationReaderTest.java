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
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition;
import org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition;
import org.linkki.core.defaults.section.annotations.TestUIField;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;

public class PositionAnnotationReaderTest {

    @Test
    public void testGetPositionAnnotatedElement() throws Exception {
        int position = PositionAnnotationReader.getPosition(PosTestPmo.class.getMethod("test"));

        assertThat(position, is(42));
    }

    @Test
    public void testGetPositionAnnotatedElement_SamePosTwice() throws Exception {
        int position = PositionAnnotationReader.getPosition(PosTestPmo.class.getMethod("testSamePos"));

        assertThat(position, is(42));
    }

    @Test
    public void testGetPositionAnnotatedElement_DiffPos() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            PositionAnnotationReader.getPosition(PosTestPmo.class.getMethod("testDiffPos"));
        });

    }

    @Test
    public void testGetPositionAnnotatedElement_BindingDefinition() throws Exception {
        int position = PositionAnnotationReader.getPosition(PosTestPmo.class.getMethod("testDeprecated"));

        assertThat(position, is(4711));
    }

    static class PosTestPmo {

        @TestUIField(position = 42, label = "")
        public void test() {
            //
        }

        @TestUIField(position = 42, label = "")
        @TestUIField2(position = 42)
        public void testSamePos() {
            //
        }

        @TestUIField(position = 42, label = "")
        @TestUIField2(position = 27)
        public void testDiffPos() {
            //
        }

        @TestUIFieldDeprecated(position = 4711)
        public void testDeprecated() {
            //
        }

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @LinkkiPositioned
    @interface TestUIField2 {

        @LinkkiPositioned.Position
        int position();

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @LinkkiBindingDefinition(DeprecatedBindingDefinition.class)
    @interface TestUIFieldDeprecated {

        int position();

    }

    public static class DeprecatedBindingDefinition implements BindingDefinition {

        private TestUIFieldDeprecated annotation;

        public DeprecatedBindingDefinition(TestUIFieldDeprecated annotation) {
            this.annotation = annotation;
        }

        @SuppressWarnings("deprecation")
        @Override
        public int position() {
            return annotation.position();
        }


        @Override
        public Object newComponent() {
            return new Object();
        }

        @Override
        public String label() {
            return "";
        }

        @Override
        public EnabledType enabled() {
            return EnabledType.DISABLED;
        }

        @Override
        public VisibleType visible() {
            return VisibleType.VISIBLE;
        }

        @Override
        public RequiredType required() {
            return RequiredType.REQUIRED;
        }

        @Override
        public String modelObject() {
            return "";
        }

        @Override
        public String modelAttribute() {
            return "";
        }

    }

}
