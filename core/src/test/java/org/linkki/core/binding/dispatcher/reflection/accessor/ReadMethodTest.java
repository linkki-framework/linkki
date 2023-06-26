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
package org.linkki.core.binding.dispatcher.reflection.accessor;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.util.LookupProvider;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ReadMethodTest {

    @Test
    void testCanRead_NoGetterMethod() {
        PropertyAccessDescriptor<?, ?> descriptor = new PropertyAccessDescriptor<>(TestObject.class,
                TestObject.DO_SOMETHING_METHOD);

        ReadMethod<?, ?> readMethod = descriptor.createReadMethod();

        assertFalse(readMethod.canRead());
    }

    @Test
    void testCanRead_VoidGetterMethod() {
        PropertyAccessDescriptor<?, ?> descriptor = new PropertyAccessDescriptor<>(TestObject.class,
                "void");

        ReadMethod<?, ?> readMethod = descriptor.createReadMethod();

        assertFalse(readMethod.canRead());
    }

    @Test
    void testReadValue() {
        TestObject testObject = new TestObject();
        PropertyAccessDescriptor<TestObject, Long> descriptor = new PropertyAccessDescriptor<>(TestObject.class,
                TestObject.READ_ONLY_LONG_PROPERTY);

        ReadMethod<TestObject, Long> readMethod = descriptor.createReadMethod();

        assertEquals(42, readMethod.readValue(testObject).longValue());
    }

    @Test
    void testReadValue_CanReadFalse() {
        PropertyAccessDescriptor<TestObject, Void> descriptor = new PropertyAccessDescriptor<>(TestObject.class,
                TestObject.DO_SOMETHING_METHOD);
        var readMethod = descriptor.createReadMethod();
        assertFalse(readMethod.canRead());
        var instance = new TestObject();

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> readMethod.readValue(instance))
                .withMessageContaining("Cannot find getter method")
                .withMessageContaining(TestObject.class.getName())
                .withMessageContaining(TestObject.DO_SOMETHING_METHOD);
    }

    @Test
    void testReadValue_MethodWithException() {
        var descriptor = new PropertyAccessDescriptor<TestObject, String>(TestObject.class,
                TestObject.EXCEPTION_PROPERTY);
        var instance = new TestObject();

        var readMethod = descriptor.createReadMethod();

        assertThatExceptionOfType(LinkkiBindingException.class)
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
                    TestObject.READ_ONLY_LONG_PROPERTY, () -> Optional.of(method));

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
                    TestObject.EXCEPTION_PROPERTY, () -> Optional.of(method));

            assertThatExceptionOfType(LinkkiBindingException.class)
                    .isThrownBy(() -> readMethod.readValue(instance))
                    .withStackTraceContaining("test exception");
        }
    }
}
