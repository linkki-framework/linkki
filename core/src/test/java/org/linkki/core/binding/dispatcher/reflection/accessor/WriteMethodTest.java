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
import org.linkki.util.MemberAccessors;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class WriteMethodTest {

    @Test
    void testConstructor_NoMethod() {
        PropertyAccessDescriptor<?, ?> descriptor = new PropertyAccessDescriptor<>(TestObject.class,
                TestObject.DO_SOMETHING_METHOD);
        assertThat(descriptor.getReflectionWriteMethod().get()).isEmpty();

        assertThatNoException().isThrownBy(() -> new WriteMethod<>(descriptor));
    }

    @Test
    void testCanWrite() {
        PropertyAccessDescriptor<?, ?> descriptor = new PropertyAccessDescriptor<>(TestObject.class,
                TestObject.DO_SOMETHING_METHOD);
        assertThat(descriptor.getReflectionWriteMethod().get()).isEmpty();

        var writeMethod = new WriteMethod<>(descriptor);

        assertThat(writeMethod.canWrite()).isFalse();
    }

    @Test
    void testWriteValue() {
        TestObject testObject = new TestObject();
        assertThat(testObject.isBooleanProperty()).isFalse();
        PropertyAccessDescriptor<TestObject, Boolean> descriptor = new PropertyAccessDescriptor<>(TestObject.class, TestObject.BOOLEAN_PROPERTY);

        WriteMethod<TestObject, Boolean> writeMethod = descriptor.createWriteMethod();
        writeMethod.writeValue(testObject, true);

        assertThat(testObject.isBooleanProperty()).isTrue();
    }

    @Test
    void testWriteValue_MethodWithException() {
        var descriptor = new PropertyAccessDescriptor<TestObject, String>(TestObject.class,
                TestObject.EXCEPTION_PROPERTY);
        var instance = new TestObject();

        var writeMethod = descriptor.createWriteMethod();

        assertThatExceptionOfType(LinkkiBindingException.class)
                .isThrownBy(() -> writeMethod.writeValue(instance, ""))
                .withStackTraceContaining("test exception");
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
            var writeMethod = new WriteMethod<Object, Object>(classInCustomClassLoader, TestObject.BOOLEAN_PROPERTY, () -> Optional.of(method));

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
            var writeMethod = new WriteMethod<Object, Object>(classInCustomClassLoader, TestObject.EXCEPTION_PROPERTY, () -> Optional.of(method));

            assertThatExceptionOfType(LinkkiBindingException.class)
                    .isThrownBy(() -> writeMethod.writeValue(instance, ""))
                    .withStackTraceContaining("test exception");
        }
    }
}
