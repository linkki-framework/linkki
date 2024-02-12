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

package org.linkki.core.binding.descriptor.modelobject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.linkki.core.binding.descriptor.modelobject.ModelObjects.ModelObjectAnnotationException;
import org.linkki.core.defaults.section.annotations.TestUIField;
import org.linkki.core.defaults.section.annotations.TestUIField2;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.pmo.ModelObject;

public class ModelObjectsTest {

    private static Stream<Arguments> pmoValues() {
        return Stream.of(Arguments.of(new TestObject()),
                         Arguments.of(new TestSub()),
                         Arguments.of((TestObject)null));
    }

    @ParameterizedTest
    @MethodSource("pmoValues")
    void testClassSupplierFor_MethodAccessMember(TestObject modelObject) {
        var pmo = new TestPmo();
        pmo.testObject = modelObject;

        var supplier = ModelObjects.classSupplierFor(pmo, ModelObject.DEFAULT_NAME);

        // class should always equal declared type, irrespective of model object value
        assertThat(supplier.get(), is(TestObject.class));
    }

    @ParameterizedTest
    @MethodSource("pmoValues")
    void testClassSupplierFor_FieldAccessMember(TestObject modelObject) {
        var pmo = new PmoWithModelObjectField();
        pmo.testObject = modelObject;

        var supplier = ModelObjects.classSupplierFor(pmo, ModelObject.DEFAULT_NAME);

        // class should always equal declared type, irrespective of model object value
        assertThat(supplier.get(), is(TestObject.class));
    }

    @Test
    void testClassSupplierFor_MissingModelObject() {
        var pmo = new PmoWithoutModelObject();

        assertThrows(ModelObjectAnnotationException.class,
                     () -> ModelObjects.classSupplierFor(pmo, ModelObject.DEFAULT_NAME));
    }

    @Test
    public void testGetModelObjectSupplier_noAnnotation() {
        assertThrows(ModelObjectAnnotationException.class, () -> {
            ModelObjects.supplierFor(new TestObject(), ModelObject.DEFAULT_NAME);
        });

    }

    @Test
    public void testGetModelObjectSupplier_ThrowsExceptionIfNoMatchingAnnotationExists() {
        assertThrows(ModelObjectAnnotationException.class, () -> {
            ModelObjects.supplierFor(new PmoWithNamedModelObject(), "someOtherName");
        });
    }

    @Test
    public void testGetModelObjectSupplier_ThrowsExceptionIfAnnotatedMethodReturnsVoid() {
        assertThrows(ModelObjectAnnotationException.class, () -> {
            ModelObjects.supplierFor(new PmoWithVoidModelObjectMethod(),
                                     ModelObject.DEFAULT_NAME);
        });
    }

    @Test
    public void testGetModelObjectSupplier() {
        Supplier<?> modelObjectSupplier = ModelObjects
                .supplierFor(new PmoWithNamedModelObject(), PmoWithNamedModelObject.MODEL_OBJECT);

        assertThat(modelObjectSupplier, is(notNullValue()));
        assertThat(modelObjectSupplier.get(), is(instanceOf(TestObject.class)));
    }

    @Test
    public void testHasModelObjectAnnotatedMethod() {
        assertThat(ModelObjects.isAccessible(new TestPmo(), ModelObject.DEFAULT_NAME),
                   is(true));
        assertThat(ModelObjects.isAccessible(new PmoWithNamedModelObject(),
                                             PmoWithNamedModelObject.MODEL_OBJECT),
                   is(true));
        assertThat(ModelObjects.isAccessible(new PmoWithNamedModelObject(),
                                             ModelObject.DEFAULT_NAME),
                   is(true));
    }

    @Test
    public void testHasModelObjectAnnotatedMethod_noAnnotation() {
        assertThat(ModelObjects.isAccessible(new Object(), ModelObject.DEFAULT_NAME),
                   is(false));
    }

    @Test
    public void testHasModelObjectAnnotatedMethod_noMatchingAnnotation() {
        assertThat(ModelObjects.isAccessible(new PmoWithNamedModelObject(), "someOtherName"),
                   is(false));
        assertThat(ModelObjects.isAccessible(new Object(), "FooBar"), is(false));
    }

    @Test
    public void testModelObjectAnnotatedMethod_OverrideMethodInSubclass() {
        PmoWithOverridenModelObjectMethod testSubclassPmo = new PmoWithOverridenModelObjectMethod();

        assertThat(ModelObjects.isAccessible(testSubclassPmo,
                                             ModelObject.DEFAULT_NAME),
                   is(true));
        assertThat(ModelObjects.supplierFor(testSubclassPmo, ModelObject.DEFAULT_NAME).get(),
                   is(testSubclassPmo.testSub));
    }

    @Test
    public void testModelObjectAnnotatedField() {
        PmoWithModelObjectSubField pmoWithModelObjectField = new PmoWithModelObjectSubField();
        assertThat(ModelObjects.supplierFor(pmoWithModelObjectField, ModelObject.DEFAULT_NAME)
                .get(),
                   is(pmoWithModelObjectField.testSub));
    }

    @Test
    public void testPrivateModelObjectAnnotatedFieldInSuperclass() {
        PmoWithModelObjectFieldInSuperclass pmoWithModelObjectFieldInSuperclass =
                new PmoWithModelObjectFieldInSuperclass();
        Object modelObject = ModelObjects
                .supplierFor(pmoWithModelObjectFieldInSuperclass, ModelObject.DEFAULT_NAME).get();
        assertThat(modelObject, is(not(nullValue())));
        assertThat(modelObject, instanceOf(TestSub.class));
    }

    @Test
    public void testMixedModelObjectFieldAndMethod() {
        PmoWithNamedModelObject pmoWithNamedModelObject = new PmoWithNamedModelObject();
        Object defaultModelObject = ModelObjects
                .supplierFor(pmoWithNamedModelObject, ModelObject.DEFAULT_NAME).get();
        Object namedModelObject = ModelObjects
                .supplierFor(pmoWithNamedModelObject, PmoWithNamedModelObject.MODEL_OBJECT).get();

        assertThat(defaultModelObject, instanceOf(TestSub.class));
        assertThat(namedModelObject, instanceOf(TestObject.class));
    }

    @Test
    public void testTwoDefaultModelObjectAnnotations() {
        PmoWithTwoDefaultModelObjects pmoWithTwoDefaultModelObjects = new PmoWithTwoDefaultModelObjects();

        assertThrows(ModelObjectAnnotationException.class, () -> {
            ModelObjects.supplierFor(pmoWithTwoDefaultModelObjects, ModelObject.DEFAULT_NAME)
                    .get();
        });
    }

    @Test
    public void testTwoDefaultModelObjectMethodAnnotations() {
        PmoWithTwoDefaultModelObjectMethods pmoWithTwoDefaultModelObjects = new PmoWithTwoDefaultModelObjectMethods();

        assertThrows(ModelObjectAnnotationException.class, () -> {
            ModelObjects.supplierFor(pmoWithTwoDefaultModelObjects, ModelObject.DEFAULT_NAME)
                    .get();
        });
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
        private TestObject testObject = new TestObject();
    }

    public static class PmoWithModelObjectSubField {
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

    public static class PmoWithoutModelObject {
        // nothing to do
    }

    public static class PmoWithModelObjectFieldInSuperclass extends PmoWithModelObjectSubField {
        // nothing to do
    }

    public static class TestObject {
        // nothing to do
    }

    public static class TestSub extends TestObject {
        // nothing to do
    }
}
