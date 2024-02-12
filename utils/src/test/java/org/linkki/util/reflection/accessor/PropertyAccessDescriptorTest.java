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
package org.linkki.util.reflection.accessor;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.util.reflection.TestInterface;
import org.linkki.util.reflection.TestObject;

class PropertyAccessDescriptorTest {

    @Test
    void testGetReflectionInvokeMethod() {
        var descriptor = new PropertyAccessDescriptor<>(TestObject.class, TestObject.PROPERTY_DO_SOMETHING);

        var method = descriptor.getReflectionInvokeMethod().get();

        assertThat(method).isPresent();
    }

    /**
     * We do not check return type because of compatibility to older versions.
     */
    @Test
    void testGetReflectionInvokeMethod_ReturnTypeNotVoid() {
        var descriptor = new PropertyAccessDescriptor<>(TestObject.class, "getStringProperty");

        var method = descriptor.getReflectionInvokeMethod().get();

        assertThat(method).isPresent();
    }

    @Test
    void testGetReflectionMethod_HasArgument() {
        var descriptor = new PropertyAccessDescriptor<>(TestObject.class, "setStringProperty");

        var method = descriptor.getReflectionInvokeMethod().get();

        assertThat(method).isEmpty();
    }

    @Test
    void testGetReflectionReadMethod() {
        var descriptor = new PropertyAccessDescriptor<>(TestObject.class, TestObject.PROPERTY_STRING);

        var method = descriptor.getReflectionReadMethod().get();

        assertThat(method).isPresent();
    }

    @Test
    void testGetReflectionReadMethod_NoMethodStartingWithIsOrGet() {
        var descriptor = new PropertyAccessDescriptor<>(TestObject.class, TestObject.NON_PROPERTY);

        var method = descriptor.getReflectionReadMethod().get();

        assertThat(method).isEmpty();
    }

    @Test
    void testGetReflectionReadMethod_ReturnTypeVoid() {
        var descriptor = new PropertyAccessDescriptor<>(TestObject.class, "void");

        var method = descriptor.getReflectionReadMethod().get();

        assertThat(method).isEmpty();
    }

    @Test
    void testGetReflectionReadMethod_HasArgument() {
        var descriptor = new PropertyAccessDescriptor<>(TestObject.class, TestObject.NON_PROPERTY_GETTER_HAS_ARGUMENT);

        var method = descriptor.getReflectionReadMethod().get();

        assertThat(method).isEmpty();
    }

    /**
     * This is accepted due to compatibility to
     */
    @Test
    void testGetReflectionReadMethod_NonBooleanPropertyWithIs() {
        var descriptor = new PropertyAccessDescriptor<>(TestObject.class, TestObject.PROPERTY_STRING_PROPERTY_WITH_IS);

        var method = descriptor.getReflectionReadMethod().get();

        assertThat(method).isPresent();
    }

    @Test
    void testGetReflectionWriteMethod() {
        var descriptor = new PropertyAccessDescriptor<>(TestObject.class, TestObject.PROPERTY_STRING);

        var method = descriptor.getReflectionWriteMethod().get();

        assertThat(method).isPresent();
    }

    @Test
    void testGetReflectionWriteMethod_NoSetter() {
        var descriptor = new PropertyAccessDescriptor<>(TestObject.class, TestObject.PROPERTY_READ_ONLY_LONG);

        var method = descriptor.getReflectionWriteMethod().get();

        assertThat(method).isEmpty();
    }

    /**
     * This is accepted due to compatibility to older versions.
     */
    @Test
    void testGetReflectionWriteMethod_SetterWithReturnType() {
        var descriptor = new PropertyAccessDescriptor<>(TestObject.class, TestObject.PROPERTY_WITH_NON_VOID_SETTER);

        var method = descriptor.getReflectionWriteMethod().get();

        assertThat(method).isPresent();
    }

    @Test
    void testCreateXXXMethod_NonExistentProperty() {
        var descriptor = new PropertyAccessDescriptor<>(TestObject.class, "doesNotExist");

        Assertions.assertThatNoException().isThrownBy(descriptor::createReadMethod);
        Assertions.assertThatNoException().isThrownBy(descriptor::createWriteMethod);
        Assertions.assertThatNoException().isThrownBy(descriptor::createInvokeMethod);
    }

    @Test
    void testCreateReadWriteMethod_ReadOnlyProperty() {
        var accessDescriptor = new PropertyAccessDescriptor<TestObject, Long>(TestObject.class,
                TestObject.PROPERTY_READ_ONLY_LONG);

        var readMethod = accessDescriptor.createReadMethod();
        assertThat(readMethod).isNotNull();
        assertThat(readMethod.isPresent()).isTrue();
        assertThat(readMethod.toString()).as("Class and property should be correctly passed to the read method")
                .contains(TestObject.class.getSimpleName()).contains(TestObject.PROPERTY_READ_ONLY_LONG);

        var writeMethod = accessDescriptor.createWriteMethod();
        assertThat(writeMethod).isNotNull();
        assertThat(writeMethod.isPresent()).isFalse();
        assertThat(readMethod.toString()).as("Class and property should be correctly passed to the write method")
                .contains(TestObject.class.getSimpleName()).contains(TestObject.PROPERTY_READ_ONLY_LONG);
    }

    @Test
    void testCreateReadWriteMethod_DefaultMethodFromInterface() {
        TestInterface testLambda = System.out::println;
        PropertyAccessDescriptor<TestInterface, Boolean> accessDescriptor = new PropertyAccessDescriptor<>(
                testLambda.getClass(), TestInterface.PROPERTY_RO_DEFAULT_BOOLEAN);

        var writeMethod = accessDescriptor.createWriteMethod();
        assertThat(writeMethod).isNotNull();
        assertThat(writeMethod.isPresent()).isFalse();

        var readMethod = accessDescriptor.createReadMethod();
        assertThat(readMethod).isNotNull();
        assertThat(readMethod.isPresent()).isTrue();
        assertThat(readMethod.readValue(testLambda)).isEqualTo(TestInterface.DEFAULT_PROPERTY_VALUE);
    }

    @Test
    void testCreateInvokeMethod() {
        var accessDescriptor = new PropertyAccessDescriptor<>(TestObject.class, TestObject.PROPERTY_DO_SOMETHING);
        var instance = new TestObject();
        assertThat(instance.isBooleanProperty()).isFalse();

        var invokeMethod = accessDescriptor.createInvokeMethod();

        assertThat(invokeMethod).isNotNull();
        assertThat(invokeMethod.isPresent()).isTrue();
        assertThat(invokeMethod.toString()).as("Class and property should be correctly passed to the write method")
                .contains(TestObject.class.getSimpleName()).contains(TestObject.PROPERTY_DO_SOMETHING);
    }

    @Test
    void testToString() {
        var accessDescriptor = new PropertyAccessDescriptor<>(TestObject.class, TestObject.PROPERTY_DO_SOMETHING);

        assertThat(accessDescriptor.toString()).contains(TestObject.class.getSimpleName())
                .contains(TestObject.PROPERTY_DO_SOMETHING);
    }
}
