/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher.accessor;

import static org.junit.Assert.assertThat;
import static org.linkki.core.binding.dispatcher.accessor.PropertyDescriptorFactory.createPropertyDescriptor;
import static org.linkki.test.matcher.Matchers.absent;
import static org.linkki.test.matcher.Matchers.present;

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
