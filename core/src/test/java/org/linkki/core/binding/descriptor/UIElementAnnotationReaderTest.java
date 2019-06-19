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

package org.linkki.core.binding.descriptor;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.linkki.core.defaults.section.annotations.TestUIField;
import org.linkki.core.defaults.section.annotations.TestUIField2;

public class UIElementAnnotationReaderTest {

    @Test
    public void testGetUiElements_DynamicFields() {
        UIElementAnnotationReader uiElementAnnotationReader = new UIElementAnnotationReader(DynamicFieldPmo.class);

        List<PropertyElementDescriptors> uiElements = uiElementAnnotationReader.getUiElements()
                .collect(Collectors.toList());

        assertThat(uiElements.size(), is(1));
        PropertyElementDescriptors elementDescriptors = uiElements.get(0);
        ElementDescriptor fooDescriptor = elementDescriptors
                .getDescriptor(new DynamicFieldPmo(Type.FOO));
        assertThat(fooDescriptor, is(notNullValue()));
        assertThat(fooDescriptor.getBoundProperty().getModelAttribute(), is("foo"));
        ElementDescriptor barDescriptor = elementDescriptors
                .getDescriptor(new DynamicFieldPmo(Type.BAR));
        assertThat(barDescriptor, is(notNullValue()));
        assertThat(barDescriptor.getBoundProperty().getModelAttribute(), is("bar"));
    }

    enum Type {
        FOO,
        BAR
    }

    public static class DynamicFieldPmo {

        private final Type type;

        public DynamicFieldPmo(Type type) {
            this.type = type;
        }

        @TestUIField(position = 20, label = "Foo/Bar", modelAttribute = "foo")
        @TestUIField2(position = 20, modelAttribute = "bar")
        public void foobar() {
            // model binding
        }

        public Class<?> getFoobarComponentType() {
            return type == Type.FOO ? TestUIField.class : TestUIField2.class;
        }
    }

}
