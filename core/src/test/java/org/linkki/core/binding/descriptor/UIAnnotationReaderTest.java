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
package org.linkki.core.binding.descriptor;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.function.Supplier;

import org.junit.Test;
import org.linkki.core.binding.descriptor.UIAnnotationReader.ModelObjectAnnotationException;
import org.linkki.core.ui.section.annotations.BindTooltip;
import org.linkki.core.ui.section.annotations.BindTooltip.TooltipType;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UIDateField;
import org.linkki.core.ui.section.annotations.UITableColumn;
import org.linkki.core.ui.section.annotations.UITextField;

public class UIAnnotationReaderTest {

    private UIAnnotationReader reader = new UIAnnotationReader(TestPmo.class);

    @Test
    public void testReadAnnotations() {
        assertNotNull(reader.findDescriptors("abc"));
        assertNotNull(reader.findDescriptors("test"));
        assertNotNull(reader.findDescriptors("test3"));

        assertThat(reader.getUiElements().count(), is(3L));
    }

    @Test(expected = ModelObjectAnnotationException.class)
    public void testGetModelObjectSupplier_noAnnotation() {
        Supplier<?> modelObjectSupplier = UIAnnotationReader.getModelObjectSupplier(new TestObject(),
                                                                                    ModelObject.DEFAULT_NAME);

        assertThat(modelObjectSupplier, is(notNullValue()));
        modelObjectSupplier.get();
    }

    @Test(expected = ModelObjectAnnotationException.class)
    public void testGetModelObjectSupplier_ThrowsExceptionIfNoMatchingAnnotationExists() {
        UIAnnotationReader.getModelObjectSupplier(new PmoWithNamedModelObject(), "someOtherName");
    }

    @Test(expected = ModelObjectAnnotationException.class)
    public void testGetModelObjectSupplier_ThrowsExceptionIfAnnotatedMethodReturnsVoid() {
        UIAnnotationReader.getModelObjectSupplier(new PmoWithVoidModelObjectMethod(), ModelObject.DEFAULT_NAME);
    }

    @Test
    public void testGetModelObjectSupplier() {
        Supplier<?> modelObjectSupplier = UIAnnotationReader
                .getModelObjectSupplier(new PmoWithNamedModelObject(), PmoWithNamedModelObject.MODEL_OBJECT);

        assertThat(modelObjectSupplier, is(notNullValue()));
        assertThat(modelObjectSupplier.get(), is(instanceOf(TestObject.class)));
    }

    @Test
    public void testHasModelObjectAnnotatedMethod() {
        assertThat(UIAnnotationReader.hasModelObjectAnnotation(new TestPmo(), ModelObject.DEFAULT_NAME), is(true));
        assertThat(UIAnnotationReader.hasModelObjectAnnotation(new PmoWithNamedModelObject(),
                                                               PmoWithNamedModelObject.MODEL_OBJECT),
                   is(true));
        assertThat(UIAnnotationReader.hasModelObjectAnnotation(new PmoWithNamedModelObject(), ModelObject.DEFAULT_NAME),
                   is(true));
    }

    @Test
    public void testHasModelObjectAnnotatedMethod_noAnnotation() {
        assertThat(UIAnnotationReader.hasModelObjectAnnotation(new Object(), ModelObject.DEFAULT_NAME),
                   is(false));
    }

    @Test
    public void testHasModelObjectAnnotatedMethod_noMatchingAnnotation() {
        assertThat(UIAnnotationReader.hasModelObjectAnnotation(new PmoWithNamedModelObject(), "someOtherName"),
                   is(false));
        assertThat(UIAnnotationReader.hasModelObjectAnnotation(new Object(), "FooBar"), is(false));
    }

    @Test
    public void testModelObjectAnnotatedMethod_OverrideMethodInSubclass() {
        PmoWithOverridenModelObjectMethod testSubclassPmo = new PmoWithOverridenModelObjectMethod();

        assertThat(UIAnnotationReader.hasModelObjectAnnotation(testSubclassPmo,
                                                               ModelObject.DEFAULT_NAME),
                   is(true));
        assertThat(UIAnnotationReader.getModelObjectSupplier(testSubclassPmo, ModelObject.DEFAULT_NAME).get(),
                   is(testSubclassPmo.testSub));
    }

    @Test
    public void testModelObjectAnnotatedField() {
        PmoWithModelObjectField pmoWithModelObjectField = new PmoWithModelObjectField();
        assertThat(UIAnnotationReader.getModelObjectSupplier(pmoWithModelObjectField, ModelObject.DEFAULT_NAME).get(),
                   is(pmoWithModelObjectField.testSub));
    }

    @Test
    public void testPrivateModelObjectAnnotatedFieldInSuperclass() {
        PmoWithModelObjectFieldInSuperclass pmoWithModelObjectFieldInSuperclass = new PmoWithModelObjectFieldInSuperclass();
        Object modelObject = UIAnnotationReader
                .getModelObjectSupplier(pmoWithModelObjectFieldInSuperclass, ModelObject.DEFAULT_NAME).get();
        assertThat(modelObject, is(not(nullValue())));
        assertThat(modelObject, instanceOf(TestSub.class));
    }

    @Test
    public void testMixedModelObjectFieldAndMethod() {
        PmoWithNamedModelObject pmoWithNamedModelObject = new PmoWithNamedModelObject();
        Object defaultModelObject = UIAnnotationReader
                .getModelObjectSupplier(pmoWithNamedModelObject, ModelObject.DEFAULT_NAME).get();
        Object namedModelObject = UIAnnotationReader
                .getModelObjectSupplier(pmoWithNamedModelObject, PmoWithNamedModelObject.MODEL_OBJECT).get();

        assertThat(defaultModelObject, instanceOf(TestSub.class));
        assertThat(namedModelObject, instanceOf(TestObject.class));
    }

    @Test(expected = ModelObjectAnnotationException.class)
    public void testTwoDefaultModelObjectAnnotations() {
        PmoWithTwoDefaultModelObjects pmoWithTwoDefaultModelObjects = new PmoWithTwoDefaultModelObjects();
        UIAnnotationReader.getModelObjectSupplier(pmoWithTwoDefaultModelObjects, ModelObject.DEFAULT_NAME).get();
    }

    public static class TestPmo {

        private TestObject testObject = new TestObject();

        @ModelObject
        public TestObject getTestObject() {
            return testObject;
        }

        @BindTooltip("TestTooltip")
        @UITextField(position = 1, modelAttribute = "test")
        public void test() {
            //
        }

        @UIComboBox(position = 2, modelAttribute = "test2")
        public void abc() {
            //
        }

        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        @UITableColumn
        @UIDateField(position = 3, modelAttribute = "test3")
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
