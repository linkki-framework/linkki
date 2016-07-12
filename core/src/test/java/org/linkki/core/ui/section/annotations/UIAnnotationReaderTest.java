/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.function.Supplier;

import org.junit.Test;
import org.linkki.core.ui.section.annotations.UIAnnotationReader.ModelObjectAnnotationException;

public class UIAnnotationReaderTest {

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
        @UITextField(position = 1, modelAttribute = "test")
        public void test() {
            //
        }

        @UIComboBox(position = 2, modelAttribute = "test2")
        public void abc() {
            //
        }

        @UITableColumn
        @UIDateField(position = 3, modelAttribute = "test3")
        public void isTest3() {
            //
        }
    }

}
