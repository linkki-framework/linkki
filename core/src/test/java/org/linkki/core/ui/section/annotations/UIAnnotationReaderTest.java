/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.function.Supplier;

import org.junit.Test;
import org.linkki.core.ui.section.annotations.UIAnnotationReader.ModelObjectAnnotationException;

public class UIAnnotationReaderTest {

    private UIAnnotationReader annotationReader = new UIAnnotationReader(new TestObject().getClass());

    @Test
    public void testGetFieldDefinitions() {
        assertThat(annotationReader.getUiElements(), hasSize(3));
    }

    @Test
    public void testGetFieldDefinition() {
        assertThat(annotationReader.get("test"), is(notNullValue()));
        assertThat(annotationReader.get("test2"), is(notNullValue()));
        assertThat(annotationReader.get("test3"), is(notNullValue()));
        assertThat(annotationReader.get("illegal"), is(nullValue()));
    }

    @Test
    public void testHasFieldDefinition() {
        assertThat(annotationReader.hasAnnotation("test"), is(true));
        assertThat(annotationReader.hasAnnotation("test2"), is(true));
        assertThat(annotationReader.hasAnnotation("test3"), is(true));
        assertThat(annotationReader.hasAnnotation("illegalProperty"), is(false));
    }

    @Test(expected = NullPointerException.class)
    public void testHasFieldDefinition_NullProperty() {
        assertThat(annotationReader.hasAnnotation(null), is(false));
    }

    @Test
    public void testGetTableColumnDescriptorString() {
        assertThat(annotationReader.getTableColumnDescriptor("test"), is(nullValue()));
        assertThat(annotationReader.getTableColumnDescriptor("test2"), is(nullValue()));
        assertThat(annotationReader.getTableColumnDescriptor("test3"), is(notNullValue()));
    }

    @Test
    public void testGetTableColumnDescriptorFieldDescriptor() {
        ElementDescriptor test = annotationReader.get("test");
        ElementDescriptor test3 = annotationReader.get("test3");

        assertThat(test, is(notNullValue()));
        assertThat(test3, is(notNullValue()));

        assertThat(annotationReader.getTableColumnDescriptor(test), is(nullValue()));
        assertThat(annotationReader.getTableColumnDescriptor(test3), is(notNullValue()));
    }

    @Test
    public void testHasTableColumnAnnotation() {
        ElementDescriptor test = annotationReader.get("test");
        ElementDescriptor test3 = annotationReader.get("test3");

        assertThat(test, is(notNullValue()));
        assertThat(test3, is(notNullValue()));

        assertThat(annotationReader.hasTableColumnAnnotation(test), is(false));
        assertThat(annotationReader.hasTableColumnAnnotation(test3), is(true));
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
