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
package org.linkki.core.binding.dispatcher.accessor;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class PropertyAccessDescriptorTest {

    @Test
    public void testConstructor_nonExistentProperty() {
        PropertyAccessDescriptor<TestObject, ?> accessDescriptor = new PropertyAccessDescriptor<>(TestObject.class,
                "doesNotExist");
        assertThat(accessDescriptor.createReadMethod().canRead(), is(false));
        assertThat(accessDescriptor.createWriteMethod().canWrite(), is(false));
    }

    @Test
    public void testCreateReadMethod_readOnlyProperty() {
        PropertyAccessDescriptor<TestObject, Long> accessDescriptor = new PropertyAccessDescriptor<>(TestObject.class,
                TestObject.READ_ONLY_LONG_PROPERTY);

        WriteMethod<TestObject, Long> writeMethod = accessDescriptor.createWriteMethod();
        assertThat(writeMethod, is(notNullValue()));
        assertThat(writeMethod.canWrite(), is(false));
        ReadMethod<TestObject, Long> readMethod = accessDescriptor.createReadMethod();
        assertThat(readMethod, is(notNullValue()));
        assertThat(readMethod.canRead(), is(true));
    }

    @Test
    public void testCreateReadMethod_defaultMethodProperty() {

        TestInterface testLambda = () -> System.out.println();
        PropertyAccessDescriptor<TestInterface, Boolean> accessDescriptor = new PropertyAccessDescriptor<>(
                testLambda.getClass(),
                TestInterface.RO_DEFAULT_BOOLEAN_PROPERTY);

        WriteMethod<TestInterface, Boolean> writeMethod = accessDescriptor.createWriteMethod();
        assertThat(writeMethod, is(notNullValue()));
        assertThat(writeMethod.canWrite(), is(false));
        ReadMethod<TestInterface, Boolean> readMethod = accessDescriptor.createReadMethod();
        assertThat(readMethod, is(notNullValue()));
        assertThat(readMethod.canRead(), is(true));
        assertThat(readMethod.readValue(testLambda), is(TestInterface.DEFAULT_PROPERTY_VALUE));
    }

}
