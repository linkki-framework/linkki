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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.linkki.util.reflection.LookupProvider;
import org.linkki.util.reflection.TestObject;

class ReadMethodTest {

    @Test
    void testCanRead() throws NoSuchMethodException {
        var method = TestObject.class.getMethod("getStringProperty");
        var readMethod = new ReadMethod<>(TestObject.class, "irrelevant", () -> Optional.of(method));

        assertThat(readMethod.isPresent()).isTrue();
    }

    @Test
    void testCanRead_NoMethod() {
        var readMethod = new ReadMethod<>(TestObject.class, "irrelevant", Optional::empty);

        assertThat(readMethod.isPresent()).isFalse();
    }

    @Test
    void testReadValue() {
        PropertyAccessDescriptor<TestObject, Long> descriptor = new PropertyAccessDescriptor<>(TestObject.class,
                TestObject.PROPERTY_READ_ONLY_LONG);
        TestObject testObject = new TestObject();
        var readMethod = descriptor.createReadMethod();

        var value = readMethod.readValue(testObject);

        assertThat(value).isEqualTo(42);
    }

    @Test
    void testReadValue_NoMethod() {
        var readMethod = new ReadMethod<>(TestObject.class, "property", Optional::empty);
        var instance = new TestObject();

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> readMethod.readValue(instance))
                .withMessageContaining("read method")
                .withMessageContaining(TestObject.class.getSimpleName())
                .withMessageContaining("property");
    }

    /**
     * This is okay as we cannot enforce the return type with generic types. This test is just to
     * indicate that this use case has been considered.
     */
    @Test
    void testReadValue_notMatchingType() {
        var descriptor = new PropertyAccessDescriptor<TestObject, String>(TestObject.class,
                TestObject.PROPERTY_READ_ONLY_LONG);
        var readMethod = descriptor.createReadMethod();
        assertThat(readMethod.isPresent()).isTrue();
        var instance = new TestObject();

        assertThatNoException().isThrownBy(() -> readMethod.readValue(instance));
    }

    @Test
    void testReadValue_MethodWithException() {
        var descriptor = new PropertyAccessDescriptor<TestObject, String>(TestObject.class,
                TestObject.PROPERTY_EXCEPTION);
        var instance = new TestObject();

        var readMethod = descriptor.createReadMethod();

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> readMethod.readValue(instance))
                .withStackTraceContaining("test exception");

    }

    @Test
    void testReadValue_DifferentClassloader() throws NoSuchMethodException,
            ClassNotFoundException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException,
            IOException {
        try (var simulatedRestartClassLoader = new SimulatedRestartClassLoader(TestObject.class)) {
            var classInCustomClassLoader = simulatedRestartClassLoader.loadClass(TestObject.class.getName());
            var method = classInCustomClassLoader.getDeclaredMethod("getReadOnlyLongProperty");
            assertThat(method.getDeclaringClass().getClassLoader()).isNotEqualTo(LookupProvider.class.getClassLoader());
            var instance = classInCustomClassLoader.getDeclaredConstructor().newInstance();
            var readMethod = new ReadMethod<Object, Object>(classInCustomClassLoader,
                    TestObject.PROPERTY_READ_ONLY_LONG, () -> Optional.of(method));

            var result = readMethod.readValue(instance);

            assertThat(result).isEqualTo(42L);
        }
    }

    @Test
    void testReadValue_DifferentClassloader_MethodWithException() throws NoSuchMethodException,
            ClassNotFoundException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException,
            IOException {
        try (var simulatedRestartClassLoader = new SimulatedRestartClassLoader(TestObject.class)) {
            var classInCustomClassLoader = simulatedRestartClassLoader.loadClass(TestObject.class.getName());
            var method = classInCustomClassLoader.getDeclaredMethod("getThrowException");
            assertThat(method.getDeclaringClass().getClassLoader()).isNotEqualTo(LookupProvider.class.getClassLoader());
            var instance = classInCustomClassLoader.getDeclaredConstructor().newInstance();
            var readMethod = new ReadMethod<Object, Object>(classInCustomClassLoader,
                    TestObject.PROPERTY_EXCEPTION, () -> Optional.of(method));

            assertThatExceptionOfType(RuntimeException.class)
                    .isThrownBy(() -> readMethod.readValue(instance))
                    .withStackTraceContaining("test exception");
        }
    }
}
