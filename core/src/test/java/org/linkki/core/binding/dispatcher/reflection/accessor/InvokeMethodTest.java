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
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class InvokeMethodTest {

    @Test
    void testCanInvoke() {
        PropertyAccessDescriptor<?, ?> descriptor = new PropertyAccessDescriptor<>(TestObject.class,
                TestObject.STRING_PROPERTY);
        assertThat(descriptor.getReflectionInvokeMethod().get()).isEmpty();

        var invokeMethod = new InvokeMethod<>(descriptor);

        assertThat(invokeMethod.canInvoke()).isFalse();
    }

    @Test
    void testInvoke() {
        var testObject = new TestObject();
        assertThat(testObject.isBooleanProperty()).isFalse();
        var descriptor = new PropertyAccessDescriptor<>(TestObject.class, TestObject.DO_SOMETHING_METHOD);

        var invokeMethod = descriptor.createInvokeMethod();
        invokeMethod.invoke(testObject);

        assertThat(testObject.isBooleanProperty()).isTrue();
    }

    @Test
    void testInvoke_MethodWithException() {
        var descriptor = new PropertyAccessDescriptor<TestObject, String>(TestObject.class,
                TestObject.EXCEPTION_PROPERTY);
        var instance = new TestObject();

        var invokeMethod = descriptor.createInvokeMethod();

        assertThatExceptionOfType(LinkkiBindingException.class)
                .isThrownBy(() -> invokeMethod.invoke(instance))
                .withStackTraceContaining("test exception");
    }

    @Test
    void testInvoke_DifferentClassloader() throws NoSuchMethodException,
                                                  ClassNotFoundException,
                                                  InvocationTargetException,
                                                  InstantiationException,
                                                  IllegalAccessException,
                                                  IOException {
        try (var simulatedRestartClassLoader = new SimulatedRestartClassLoader(TestObject.class)) {
            var classInCustomClassLoader = simulatedRestartClassLoader.loadClass(TestObject.class.getName());
            var method = Arrays.stream(classInCustomClassLoader.getMethods())
                    .filter(m -> m.getName().equals("doSomething")).findFirst().get();
            assertThat(method.getDeclaringClass().getClassLoader()).isNotEqualTo(LookupProvider.class.getClassLoader());
            var instance = classInCustomClassLoader.getDeclaredConstructor().newInstance();
            assertThat(classInCustomClassLoader.getDeclaredMethod("isBooleanProperty").invoke(instance))
                    .isEqualTo(false);
            var invokeMethod = new InvokeMethod<Object>(classInCustomClassLoader, TestObject.DO_SOMETHING_METHOD,
                    () -> Optional.of(method));

            invokeMethod.invoke(instance);

            assertThat(classInCustomClassLoader.getDeclaredMethod("isBooleanProperty").invoke(instance))
                    .isEqualTo(true);
        }
    }

    @Test
    void testInvokeMethod_DifferentClassloader_MethodWithException() throws NoSuchMethodException,
                                                                            ClassNotFoundException,
                                                                            InvocationTargetException,
                                                                            InstantiationException,
                                                                            IllegalAccessException,
                                                                            IOException {
        try (var simulatedRestartClassLoader = new SimulatedRestartClassLoader(TestObject.class)) {
            var classInCustomClassLoader = simulatedRestartClassLoader.loadClass(TestObject.class.getName());
            var method = Arrays.stream(classInCustomClassLoader.getMethods())
                    .filter(m -> m.getName().equals(TestObject.EXCEPTION_PROPERTY)).findFirst().get();
            assertThat(method.getDeclaringClass().getClassLoader()).isNotEqualTo(LookupProvider.class.getClassLoader());
            var instance = classInCustomClassLoader.getDeclaredConstructor().newInstance();
            var invokeMethod = new InvokeMethod<Object>(classInCustomClassLoader, TestObject.EXCEPTION_PROPERTY,
                    () -> Optional.of(method));

            assertThatExceptionOfType(LinkkiBindingException.class)
                    .isThrownBy(() -> invokeMethod.invoke(instance))
                    .withStackTraceContaining("test exception");
        }
    }
}
