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
import org.linkki.util.reflection.accessor.MemberAccessors;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class MemberAccessorsTest {

    private static final String VALUE = "testValue";

    @Test
    void testGetValue() throws NoSuchMethodException, SecurityException {
        TestObject testObject = new TestObject();
        Member fieldOrMethod = TestObject.class.getDeclaredMethod("getValue");

        String result = MemberAccessors.getValue(testObject, fieldOrMethod);

        assertThat(result).isEqualTo(testObject.getValue());
    }

    @Test
    void testGetValue_Field() throws NoSuchFieldException, SecurityException {
        TestObject testObject = new TestObject();
        Member fieldOrMethod = TestObject.class.getDeclaredField("field");

        String result = MemberAccessors.getValue(testObject, fieldOrMethod);

        assertThat(result).isEqualTo(testObject.field);
    }

    @Test
    void testGetValue_Field_NativeType() throws NoSuchFieldException, SecurityException {
        TestObject testObject = new TestObject();
        Member fieldOrMethod = TestObject.class.getDeclaredField("intField");

        int result = MemberAccessors.getValue(testObject, fieldOrMethod);

        assertThat(result).isEqualTo(testObject.intField);
    }

    @Test
    void testGetValue_Void() throws NoSuchMethodException, SecurityException {
        TestObject testObject = new TestObject();
        Member fieldOrMethod = TestObject.class.getDeclaredMethod("getVoid");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> MemberAccessors.getValue(testObject, fieldOrMethod));
    }

    @Test
    void testGetValue_NativeReturnType() throws NoSuchMethodException, SecurityException {
        TestObject testObject = new TestObject();
        Member fieldOrMethod = TestObject.class.getDeclaredMethod("getInt");

        int result = MemberAccessors.getValue(testObject, fieldOrMethod);

        assertThat(result).isEqualTo(testObject.getInt());
    }

    @Test
    void testGetValue_MethodThrowsException() throws NoSuchMethodException, SecurityException {
        TestObject testObject = new TestObject();
        Member fieldOrMethod = TestObject.class.getDeclaredMethod("getThrowException");

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> MemberAccessors.getValue(testObject, fieldOrMethod))
                .withStackTraceContaining("any exception thrown");
    }

    @Test
    void testGetValue_PrivateMethod() throws NoSuchMethodException, SecurityException {
        var testObject = new TestObject();
        var fieldOrMethod = TestObject.class.getDeclaredMethod("getPrivateValue");

        String result = MemberAccessors.getValue(testObject, fieldOrMethod);

        assertThat(result).isEqualTo("privateValue");
    }

    @Test
    void testGetValue_UnsupportedMemberType() throws NoSuchMethodException, SecurityException {
        TestObject testObject = new TestObject();
        Constructor<? extends TestObject> constructor = testObject.getClass().getDeclaredConstructor();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> MemberAccessors.getValue(testObject, constructor));
    }

    @Test
    void testGetType_Field() throws NoSuchFieldException {
        Member field = TestObject.class.getDeclaredField("field");
        assertThat(MemberAccessors.getType(field)).isEqualTo(String.class);
    }

    @Test
    void testGetType_Method() throws NoSuchMethodException {
        Member method = TestObject.class.getDeclaredMethod("getValue");
        assertThat(MemberAccessors.getType(method)).isEqualTo(String.class);
    }

    @Test
    void testGetType_NotMethodOrField() throws NoSuchMethodException {
        TestObject testObject = new TestObject();
        Constructor<? extends TestObject> constructor = testObject.getClass().getDeclaredConstructor();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> MemberAccessors.getType(constructor))
                .withMessageContaining("Only field or method is supported")
                .withMessageContaining("java.lang.reflect.Constructor")
                .withMessageContaining(TestObject.class.getName());
    }

    @Test
    void testGetValue_DifferentClassloader() throws NoSuchMethodException,
                                                    ClassNotFoundException,
                                                    InvocationTargetException,
                                                    InstantiationException,
                                                    IllegalAccessException,
                                                    IOException {
        try (var simulatedRestartClassLoader = new SimulatedRestartClassLoader(TestObject.class)) {
            var classInCustomClassLoader = simulatedRestartClassLoader.loadClass(TestObject.class.getName());
            var method = classInCustomClassLoader.getDeclaredMethod("getValue");
            assertThat(method.getDeclaringClass().getClassLoader()).isNotEqualTo(LookupProvider.class.getClassLoader());
            var instance = classInCustomClassLoader.getDeclaredConstructor().newInstance();

            assertThatNoException().isThrownBy(() -> MemberAccessors.getValue(instance, method));

            String result = MemberAccessors.getValue(instance, method);
            assertThat(result).isEqualTo(VALUE);
        }
    }

    @Test
    void testGetValue_DifferentClassloader_Exception() throws NoSuchMethodException,
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

            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> MemberAccessors.getValue(instance, method))
                    .withStackTraceContaining("any exception thrown");
        }
    }

    private static class SimulatedRestartClassLoader extends URLClassLoader {

        private final ClassLoader delegate;
        private final Class<?>[] classes;

        public SimulatedRestartClassLoader(Class<?>... classes) {
            super(Stream.of(classes).map(c -> c.getProtectionDomain().getCodeSource().getLocation()).toArray(URL[]::new), null);
            this.classes = classes;
            this.delegate = Thread.currentThread().getContextClassLoader();
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            if (Arrays.stream(classes).map(Class::getName).anyMatch(name::equals)) {
                return findClass(name);
            }
            return delegate.loadClass(name);
        }
    }

    public static class TestObject {

        private final String field = VALUE;

        private final int intField = ThreadLocalRandom.current().nextInt();

        public TestObject() {
            // nothing to do
        }

        public String getValue() {
            return field;
        }

        @SuppressWarnings("unused")
        public void getVoid() {
            return;
        }

        public int getInt() {
            return intField;
        }

        @SuppressWarnings("unused")
        public int getThrowException() {
            throw new RuntimeException("any exception thrown");
        }

        private String getPrivateValue() {
            return "privateValue";
        }
    }

}
