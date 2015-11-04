/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding.dispatcher.accessor;

import static de.faktorzehn.ipm.test.matcher.Matchers.absent;
import static de.faktorzehn.ipm.test.matcher.Matchers.present;
import static de.faktorzehn.ipm.web.binding.dispatcher.accessor.PropertyDescriptorFactory.createPropertyDescriptor;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class PropertyDescriptorFactoryTest {

    @Test
    public void testCreatePropertyDescriptor() {
        assertThat(createPropertyDescriptor(TestObject.class, TestObject.STRING_PROPERTY), present());
        assertThat(createPropertyDescriptor(TestObject.class, TestObject.INT_PROPERTY), present());
        assertThat(createPropertyDescriptor(TestObject.class, TestObject.BOOLEAN_PROPERTY), present());
        assertThat(createPropertyDescriptor(TestObject.class, TestObject.READ_ONLY_LONG_PROPERTY), present());
    }

    @Test
    public void testCreatePropertyDescriptor_interfaceMethods() {
        assertThat(createPropertyDescriptor(TestObject.class, TestInterface.RO_DEFAULT_BOOLEAN_PROPERTY)
                .map(pd -> pd.getReadMethod()), present());
        assertThat(createPropertyDescriptor(TestObject.class, TestInterface.RO_DEFAULT_METHOD), present());
        assertThat(createPropertyDescriptor(TestObject.class, TestInterface.NON_PROPERTY), absent());
        assertThat(createPropertyDescriptor(TestObject.class, TestInterface.HELLO_STRING_METHOD), absent());
    }
}
