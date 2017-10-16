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
package org.linkki.core.ui.section.annotations;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.function.Supplier;

import org.junit.Test;
import org.linkki.core.ui.section.annotations.UIAnnotationReader.ModelObjectAnnotationException;

public class UIAnnotationReaderTest {

    private UIAnnotationReader reader = new UIAnnotationReader(TestObject.class);

    @Test(expected = ModelObjectAnnotationException.class)
    public void testGetModelObjectSupplier_noAnnotation() {
        Supplier<?> modelObjectSupplier = UIAnnotationReader.getModelObjectSupplier(new TestObject(),
                                                                                    ModelObject.DEFAULT_NAME);

        assertThat(modelObjectSupplier, is(notNullValue()));
        modelObjectSupplier.get();
    }

    @Test(expected = ModelObjectAnnotationException.class)
    public void testGetModelObjectSupplier_ThrowsExceptionIfNoMatchingAnnotationExists() {
        UIAnnotationReader.getModelObjectSupplier(new TestPmoWithNamedModelObject(), ModelObject.DEFAULT_NAME);
    }

    @Test(expected = ModelObjectAnnotationException.class)
    public void testGetModelObjectSupplier_ThrowsExceptionIfAnnotatedMethodReturnsVoid() {
        UIAnnotationReader.getModelObjectSupplier(new TestPmoWithVoidModelObjectMethod(), ModelObject.DEFAULT_NAME);
    }

    @Test
    public void testGetModelObjectSupplier() {
        Supplier<?> modelObjectSupplier = UIAnnotationReader
                .getModelObjectSupplier(new TestPmoWithNamedModelObject(), TestPmoWithNamedModelObject.MODEL_OBJECT);

        assertThat(modelObjectSupplier, is(notNullValue()));
        assertThat(modelObjectSupplier.get(), is(instanceOf(TestObject.class)));
    }

    @Test
    public void testHasModelObjectAnnotatedMethod() {
        assertThat(UIAnnotationReader.hasModelObjectAnnotatedMethod(new TestPmo(), ModelObject.DEFAULT_NAME), is(true));
        assertThat(UIAnnotationReader.hasModelObjectAnnotatedMethod(new TestPmoWithNamedModelObject(),
                                                                    TestPmoWithNamedModelObject.MODEL_OBJECT),
                   is(true));
    }

    @Test
    public void testHasModelObjectAnnotatedMethod_noAnnotation() {
        assertThat(UIAnnotationReader.hasModelObjectAnnotatedMethod(new TestObject(), ModelObject.DEFAULT_NAME),
                   is(false));
    }

    @Test
    public void testHasModelObjectAnnotatedMethod_noMatchingAnnotation() {
        assertThat(UIAnnotationReader.hasModelObjectAnnotatedMethod(new TestPmoWithNamedModelObject(),
                                                                    ModelObject.DEFAULT_NAME),
                   is(false));
        assertThat(UIAnnotationReader.hasModelObjectAnnotatedMethod(new TestPmo(), "FooBar"), is(false));
    }

    @Test
    public void testToolTipStatic() {
        ElementDescriptor desc = reader.findDescriptors("test").getDescriptor(new TestPmo());
        assertEquals("TestToolTip", desc.getToolTip());
        assertEquals(ToolTipType.STATIC, desc.getToolTipType());
    }

    @Test
    public void testToolTipDynamic() {
        ElementDescriptor desc = reader.findDescriptors("test3").getDescriptor(new TestPmo());
        assertEquals(ToolTipType.DYNAMIC, desc.getToolTipType());
    }

    public static class TestPmoWithVoidModelObjectMethod {

        @ModelObject
        public void getModelObject() {
            // do nothing
        }
    }

    public static class TestPmo {

        private TestObject testObject = new TestObject();

        @ModelObject()
        public TestObject getTestObject() {
            return testObject;
        }
    }

    public static class TestPmoWithNamedModelObject {

        public static final String MODEL_OBJECT = "testObject";

        private TestObject testObject = new TestObject();

        @ModelObject(name = MODEL_OBJECT)
        public TestObject getTestObject() {
            return testObject;
        }
    }

    public static class TestObject {
        @UIToolTip(text = "TestToolTip")
        @UITextField(position = 1, modelAttribute = "test")
        public void test() {
            //
        }

        @UIComboBox(position = 2, modelAttribute = "test2")
        public void abc() {
            //
        }

        @UIToolTip(text = "", toolTipType = ToolTipType.DYNAMIC)
        @UITableColumn
        @UIDateField(position = 3, modelAttribute = "test3")
        public void isTest3() {
            //
        }
    }

}
