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
package org.linkki.util.reflection.accessor;

import org.junit.jupiter.api.Test;
import org.linkki.util.reflection.LookupProvider;
import org.linkki.util.reflection.TestObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class WriteMethodTest {

    @Test
    void testCanWrite() throws NoSuchMethodException {
        var method = TestObject.class.getMethod("setStringProperty", String.class);
        var writeMethod = new WriteMethod<>(TestObject.class, "irrelevant", () -> Optional.of(method));

        assertThat(writeMethod.isPresent()).isTrue();
    }

    @Test
    void testCanWrite_NoMethod() {
        var writeMethod = new WriteMethod<>(TestObject.class, "irrelevant", Optional::empty);

        assertThat(writeMethod.isPresent()).isFalse();
    }

    @Test
    void testWriteValue() {
        TestObject testObject = new TestObject();
        assertThat(testObject.isBooleanProperty()).isFalse();
        PropertyAccessDescriptor<TestObject, Boolean> descriptor = new PropertyAccessDescriptor<>(TestObject.class, TestObject.PROPERTY_BOOLEAN);

        WriteMethod<TestObject, Boolean> writeMethod = descriptor.createWriteMethod();
        writeMethod.writeValue(testObject, true);

        assertThat(testObject.isBooleanProperty()).isTrue();
    }

    @Test
    void testWriteValue_NoMethod() {
        var writeMethod = new WriteMethod<>(TestObject.class, "property", Optional::empty);
        var instance = new TestObject();

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> writeMethod.writeValue(instance, "something"))
                .withMessageContaining(TestObject.class.getSimpleName())
                .withMessageContaining("property");
    }

    @Test
    void testWriteValue_MethodWithException() {
        var descriptor = new PropertyAccessDescriptor<TestObject, String>(TestObject.class,
                                                                          TestObject.PROPERTY_EXCEPTION);
        var instance = new TestObject();

        var writeMethod = descriptor.createWriteMethod();

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> writeMethod.writeValue(instance, ""))
                .withStackTraceContaining("test exception");
    }

    @Test
    void testWriteValue_NotMatchingType() {
        var testObject = new TestObject();
        var descriptor = new PropertyAccessDescriptor<TestObject, String>(TestObject.class, TestObject.PROPERTY_BOOLEAN);
        var writeMethod = descriptor.createWriteMethod();
        assertThat(writeMethod.isPresent()).isTrue();

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> writeMethod.writeValue(testObject, "new value"))
                .withMessageContaining(TestObject.class.getSimpleName())
                .withMessageContaining(TestObject.PROPERTY_BOOLEAN)
                .withStackTraceContaining(String.class.getSimpleName())
                .withStackTraceContaining(Boolean.class.getSimpleName());
    }

    @Test
    void testWriteValue_DifferentClassloader() throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        try (var simulatedRestartClassLoader = new SimulatedRestartClassLoader(TestObject.class)) {
            var classInCustomClassLoader = simulatedRestartClassLoader.loadClass(TestObject.class.getName());
            var method = Arrays.stream(classInCustomClassLoader.getMethods())
                               .filter(m -> m.getName().equals("setBooleanProperty")).findFirst().get();
            assertThat(method.getDeclaringClass().getClassLoader()).isNotEqualTo(LookupProvider.class.getClassLoader());
            var instance = classInCustomClassLoader.getDeclaredConstructor().newInstance();
            assertThat(classInCustomClassLoader.getDeclaredMethod("isBooleanProperty").invoke(instance))
                    .isEqualTo(false);
            var writeMethod = new WriteMethod<Object, Object>(classInCustomClassLoader, TestObject.PROPERTY_BOOLEAN, () -> Optional.of(method));

            writeMethod.writeValue(instance, true);

            assertThat(classInCustomClassLoader.getDeclaredMethod("isBooleanProperty").invoke(instance))
                    .isEqualTo(true);
        }
    }

    @Test
    void testWriteValue_DifferentClassloader_MethodWithException() throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        try (var simulatedRestartClassLoader = new SimulatedRestartClassLoader(TestObject.class)) {
            var classInCustomClassLoader = simulatedRestartClassLoader.loadClass(TestObject.class.getName());
            var method = Arrays.stream(classInCustomClassLoader.getMethods())
                               .filter(m -> m.getName().equals("setThrowException")).findFirst().get();
            assertThat(method.getDeclaringClass().getClassLoader()).isNotEqualTo(LookupProvider.class.getClassLoader());
            var instance = classInCustomClassLoader.getDeclaredConstructor().newInstance();
            var writeMethod = new WriteMethod<Object, Object>(classInCustomClassLoader, TestObject.PROPERTY_EXCEPTION, () -> Optional.of(method));

            assertThatExceptionOfType(RuntimeException.class)
                    .isThrownBy(() -> writeMethod.writeValue(instance, ""))
                    .withStackTraceContaining("test exception");
        }
    }
}
