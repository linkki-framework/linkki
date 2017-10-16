/*
 * Copyright Faktor Zehn AG.
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
