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
package org.linkki.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.linkki.util.SuperFoo.Foo;

class BeanUtilsTest {

    @Test
    void testGetBeanInfo() {
        var beanInfo = BeanUtils.getBeanInfo(IFoo.class);

        assertThat(beanInfo.getMethodDescriptors()).hasSize(1);
    }

    @Test
    void testGetMethod_WithParams_Varargs() {
        var method = BeanUtils.getMethod(Foo.class, "baz", Integer.TYPE);

        assertThat(method.getParameterCount()).isEqualTo(1);
    }

    @Test
    void testGetMethod_WithParams_NoSuchMethod() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> BeanUtils.getMethod(Foo.class, "noSuchMethod"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "foo", "iFoo", "superFoo" })
    void testGetMethod_MethodIsFound(String methodName) {
        var method = BeanUtils.getMethod(Foo.class, m -> m.getName().equals(methodName));
        assertThat(method).isPresent();
    }

    @Test
    void testGetMethod_PrivateMethodIsNotFound() {
        var method = BeanUtils.getMethod(Foo.class, m -> m.getName().equals("bar"));
        assertThat(method).isEmpty();
    }

    @Test
    void testGetMethod_OneMatchingMethodIsFound() {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> BeanUtils.getMethod(Foo.class, m -> m.getName().equals("baz")));
    }

    @Test
    void testGetMethods_AllMatchingMethodsAreFound() {
        var declaredMethods = BeanUtils.getMethods(Foo.class, m -> m.getName().equals("baz"));
        assertThat(declaredMethods).hasSize(2);
    }

    @Test
    void testGetMethod_NoMethodIsFound() {
        var method = BeanUtils.getMethod(Foo.class, m -> m.getName().equals("hammaned"));
        assertThat(method).isEmpty();
    }

    @Test
    void testGetPropertyName_Method_Void() throws NoSuchMethodException, SecurityException {
        assertThat(BeanUtils.getPropertyName(IFoo.class.getMethod("iFoo"))).isEqualTo("iFoo");
    }

    @Test
    void testGetPropertyName_String_Void() {
        assertThat(BeanUtils.getPropertyName(Void.TYPE, "getFoo")).isEqualTo("getFoo");
        assertThat(BeanUtils.getPropertyName(Void.TYPE, "isFoo")).isEqualTo("isFoo");
        assertThat(BeanUtils.getPropertyName(Void.TYPE, "foo")).isEqualTo("foo");
    }

    @Test
    void testGetPropertyName_String_Get() {
        assertThat(BeanUtils.getPropertyName(String.class, "getFoo")).isEqualTo("foo");
        assertThat(BeanUtils.getPropertyName(Boolean.TYPE, "getFoo")).isEqualTo("foo");
    }

    @Test
    void testGetPropertyName_String_Is() {
        assertThat(BeanUtils.getPropertyName(String.class, "isFoo")).isEqualTo("foo");
        assertThat(BeanUtils.getPropertyName(Boolean.TYPE, "isFoo")).isEqualTo("foo");
    }

    @Test
    void testGetPropertyName_String_FullName() {
        assertThat(BeanUtils.getPropertyName(String.class, "fooBar")).isEqualTo("fooBar");
        assertThat(BeanUtils.getPropertyName(Boolean.TYPE, "fooBar")).isEqualTo("fooBar");
    }

    @Test
    void testGetValueFromFieldObjectField_Name_NotAccessible() {
        var object = new Foo();
        assertThat(BeanUtils.getField(Foo.class, "field").canAccess(object)).isFalse();

        assertThat(BeanUtils.getValueFromField(object, "field")).isEqualTo(1);
    }

    @Test
    void testGetValueFromFieldObjectField_Name_Accessible() {
        var object = new Foo();

        assertThat(BeanUtils.getValueFromField(object, "publicField")).isEqualTo(2);
    }

    @Test
    void testGetValueFromFieldObjectField_WithField_NotAccessible() {
        var field = BeanUtils.getField(Foo.class, "field");
        var object = new Foo();

        assertThat(field.canAccess(object)).isFalse();
        assertThat(BeanUtils.getValueFromField(object, field)).isEqualTo(1);
        assertThat(field.canAccess(object)).isFalse();
    }

    @Test
    void testGetValueFromFieldObjectField_WithField_Accessible() {
        var field = BeanUtils.getField(Foo.class, "publicField");
        var object = new Foo();

        assertThat(field.canAccess(object)).isTrue();
        assertThat(BeanUtils.getValueFromField(object, field)).isEqualTo(2);
        assertThat(field.canAccess(object)).isTrue();
    }

    @Test
    void testGetField() {
        assertThat(BeanUtils.getField(Foo.class, "publicField")).isNotNull();
    }

    @Test
    void testGetField_SuperClass() {
        assertThat(BeanUtils.getField(Foo.class, "superField")).isNotNull();
    }

    @Test
    void testGetField_Private() {
        assertThat(BeanUtils.getField(Foo.class, "field")).isNotNull();
    }

    @Test
    void testGetField_NoSuchField() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> BeanUtils.getField(Foo.class, "noSuchField"));
    }

    @Test
    void testGetDeclaredField_Private() {
        assertThat(BeanUtils.getDeclaredField(Foo.class, "field")).isNotNull();
    }

    @Test
    void testGetDeclaredField_SuperField() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> BeanUtils.getDeclaredField(Foo.class, "superField"));
    }

}
