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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReadMethodTest {

    @Test
    public void testReadValue() {
        TestObject testObject = new TestObject();
        PropertyAccessDescriptor<TestObject, Long> descriptor = new PropertyAccessDescriptor<>(TestObject.class,
                TestObject.READ_ONLY_LONG_PROPERTY);

        ReadMethod<TestObject, Long> readMethod = descriptor.createReadMethod();

        assertEquals(42, readMethod.readValue(testObject).longValue());
    }

    @Test
    public void testReadValue_NoGetterMethod() {
        PropertyAccessDescriptor<?, ?> descriptor = new PropertyAccessDescriptor<>(TestObject.class,
                TestObject.DO_SOMETHING_METHOD);

        ReadMethod<?, ?> readMethod = descriptor.createReadMethod();

        assertFalse(readMethod.canRead());
    }

    @Test
    public void testReadValue_VoidGetterMethod() {
        PropertyAccessDescriptor<?, ?> descriptor = new PropertyAccessDescriptor<>(TestObject.class,
                "void");

        ReadMethod<?, ?> readMethod = descriptor.createReadMethod();

        assertFalse(readMethod.canRead());
    }

}
