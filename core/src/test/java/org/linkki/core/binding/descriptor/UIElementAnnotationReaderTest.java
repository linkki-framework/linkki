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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.section.annotations.TestUIField;
import org.linkki.core.defaults.section.annotations.TestUIField2;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.pmo.ModelObject;

public class UIElementAnnotationReaderTest {

    private UIElementAnnotationReader reader = new UIElementAnnotationReader(TestPmo.class);

    @Test
    public void testReadAnnotations() {
        assertNotNull(reader.findDescriptors("abc"));
        assertNotNull(reader.findDescriptors("test"));
        assertNotNull(reader.findDescriptors("test3"));

        assertThat(reader.getUiElements().count(), is(3L));
    }

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

    public static class TestPmo {

        private TestObject testObject = new TestObject();

        @ModelObject
        public TestObject getTestObject() {
            return testObject;
        }

        @BindTooltip("TestTooltip")
        @TestUIField(position = 1, label = "", modelAttribute = "test")
        public void test() {
            //
        }

        @TestUIField(position = 2, label = "", modelAttribute = "test2")
        public void abc() {
            //
        }

        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        @TestUIField(position = 3, label = "", modelAttribute = "test3")
        public void test3() {
            //
        }
    }

    public static class PmoWithVoidModelObjectMethod {

        @ModelObject
        public void getModelObject() {
            // do nothing
        }
    }

    public static class PmoWithModelObjectField {
        @ModelObject
        private TestSub testSub = new TestSub();
    }

    public static class PmoWithNamedModelObject {

        public static final String MODEL_OBJECT = "testObject";

        @ModelObject
        private TestSub testSub = new TestSub();
        private TestObject testObject = new TestObject();

        @ModelObject(name = MODEL_OBJECT)
        public TestObject getTestObject() {
            return testObject;
        }
    }

    public static class PmoWithOverridenModelObjectMethod extends TestPmo {

        private TestSub testSub = new TestSub();

        @Override
        @ModelObject
        public TestSub getTestObject() {
            return testSub;
        }
    }

    public static class PmoWithTwoDefaultModelObjects {
        @ModelObject
        private TestSub testSub = new TestSub();
        private TestObject testObject = new TestObject();

        @ModelObject
        public TestObject getTestObject() {
            return testObject;
        }
    }

    public static class PmoWithTwoDefaultModelObjectMethods {

        private TestObject testObject = new TestObject();
        private TestSub testSub = new TestSub();

        @ModelObject
        public TestObject getTestObject() {
            return testObject;
        }

        @ModelObject
        public TestSub getTestSub() {
            return testSub;
        }
    }

    public static class PmoWithModelObjectFieldInSuperclass extends PmoWithModelObjectField {
        // nothing to do
    }

    public static class TestObject {
        // nothing to do
    }

    public static class TestSub extends TestObject {
        // nothing to do
    }

}
