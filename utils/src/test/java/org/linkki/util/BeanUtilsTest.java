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
package org.linkki.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.linkki.test.matcher.Matchers.absent;
import static org.linkki.test.matcher.Matchers.present;

import java.beans.BeanInfo;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.util.SuperFoo.Foo;

public class BeanUtilsTest {

    @Test
    public void testGetBeanInfo() {
        BeanInfo beanInfo = BeanUtils.getBeanInfo(IFoo.class);

        assertThat(beanInfo.getMethodDescriptors().length, is(1));
    }

    @Test
    public void testGetMethod_WithParams_Varargs() {
        Method method = BeanUtils.getMethod(Foo.class, "baz", Integer.TYPE);

        assertThat(method.getParameterCount(), is(1));
    }

    @Test
    public void testGetMethod_WithParams_NoSuchMethod() {
        assertThrows(IllegalArgumentException.class, () -> BeanUtils.getMethod(Foo.class, "noSuchMethod"));
    }

    @Test
    public void testGetMethod_PublicMethodIsFound() {
        Optional<Method> method = BeanUtils.getMethod(Foo.class, (m) -> m.getName().equals("foo"));
        assertThat(method, is(present()));
    }

    @Test
    public void testGetMethod_InterfaceMethodIsFound() {
        Optional<Method> method = BeanUtils.getMethod(Foo.class, (m) -> m.getName().equals("iFoo"));
        assertThat(method, is(present()));
    }

    @Test
    public void testGetMethod_SuperTypeMethodIsFound() {
        Optional<Method> method = BeanUtils.getMethod(Foo.class, (m) -> m.getName().equals("superFoo"));
        assertThat(method, is(present()));
    }

    @Test
    public void testGetMethod_PrivateMethodIsNotFound() {
        Optional<Method> method = BeanUtils.getMethod(Foo.class, (m) -> m.getName().equals("bar"));
        assertThat(method, is(absent()));
    }

    @Test
    public void testGetMethod_OneMatchingMethodIsFound() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            BeanUtils.getMethod(Foo.class, (m) -> m.getName().equals("baz"));
        });
    }

    @Test
    public void testGetMethods_AllMatchingMethodsAreFound() {
        Stream<Method> declaredMethods = BeanUtils.getMethods(Foo.class, (m) -> m.getName().equals("baz"));
        assertThat(declaredMethods.count(), is(2L));
    }

    @Test
    public void testGetMethod_NoMethodIsFound() {
        Optional<Method> method = BeanUtils.getMethod(Foo.class, (m) -> m.getName().equals("hammaned"));
        assertThat(method, is(not(present())));
    }

    @Test
    public void testGetPropertyName_Method_Void() throws NoSuchMethodException, SecurityException {
        assertThat(BeanUtils.getPropertyName(IFoo.class.getMethod("iFoo")), is("iFoo"));
    }

    @Test
    public void testGetPropertyName_String_Void() {
        assertThat(BeanUtils.getPropertyName(Void.TYPE, "getFoo"), is("getFoo"));
        assertThat(BeanUtils.getPropertyName(Void.TYPE, "isFoo"), is("isFoo"));
        assertThat(BeanUtils.getPropertyName(Void.TYPE, "foo"), is("foo"));
    }

    @Test
    public void testGetPropertyName_String_Get() {
        assertThat(BeanUtils.getPropertyName(String.class, "getFoo"), is("foo"));
        assertThat(BeanUtils.getPropertyName(Boolean.TYPE, "getFoo"), is("foo"));
    }

    @Test
    public void testGetPropertyName_String_Is() {
        assertThat(BeanUtils.getPropertyName(String.class, "isFoo"), is("foo"));
        assertThat(BeanUtils.getPropertyName(Boolean.TYPE, "isFoo"), is("foo"));
    }

    @Test
    public void testGetPropertyName_String_FullName() {
        assertThat(BeanUtils.getPropertyName(String.class, "fooBar"), is("fooBar"));
        assertThat(BeanUtils.getPropertyName(Boolean.TYPE, "fooBar"), is("fooBar"));
    }

    @Test
    public void testGetValueFromFieldObjectField_Name_NotAccessible() {
        Foo object = new Foo();
        assertThat(BeanUtils.getField(Foo.class, "field").canAccess(object), is(false));

        assertThat(BeanUtils.getValueFromField(object, "field"), is(1));
    }

    @Test
    public void testGetValueFromFieldObjectField_Name_Accessible() {
        Foo object = new Foo();

        assertThat(BeanUtils.getValueFromField(object, "publicField"), is(2));
    }

    @Test
    public void testGetValueFromFieldObjectField_WithField_NotAccessible() {
        Field field = BeanUtils.getField(Foo.class, "field");
        Foo object = new Foo();

        assertThat(field.canAccess(object), is(false));
        assertThat(BeanUtils.getValueFromField(object, field), is(1));
        assertThat(field.canAccess(object), is(false));
    }

    @Test
    public void testGetValueFromFieldObjectField_WithField_Accessible() {
        Field field = BeanUtils.getField(Foo.class, "publicField");
        Foo object = new Foo();

        assertThat(field.canAccess(object), is(true));
        assertThat(BeanUtils.getValueFromField(object, field), is(2));
        assertThat(field.canAccess(object), is(true));
    }

    @Test
    public void testGetField() {
        assertThat(BeanUtils.getField(Foo.class, "publicField"), is(notNullValue()));
    }

    @Test
    public void testGetField_SuperClass() {
        assertThat(BeanUtils.getField(Foo.class, "superField"), is(notNullValue()));
    }

    @Test
    public void testGetField_Private() {
        assertThat(BeanUtils.getField(Foo.class, "field"), is(notNullValue()));
    }

    @Test
    public void testGetField_NoSuchField() {
        assertThrows(RuntimeException.class, () -> BeanUtils.getField(Foo.class, "noSuchField"));
    }

    @Test
    public void testGetDeclaredField_Private() {
        assertThat(BeanUtils.getDeclaredField(Foo.class, "field"), is(notNullValue()));
    }

    @Test
    public void testGetDeclaredField_SuperField() {
        assertThrows(RuntimeException.class, () -> BeanUtils.getDeclaredField(Foo.class, "superField"));
    }


}
