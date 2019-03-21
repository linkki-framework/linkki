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

import static org.linkki.util.LazyCachingSupplier.lazyCaching;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WriteMethodTest {
    @Mock
    PropertyAccessDescriptor<TestObject, Boolean> descriptor;

    @Test
    @SuppressWarnings("unused")
    // warning suppressed as object is created to test the constructor, not to use it
    public void testConstructor() {
        when(descriptor.getReflectionWriteMethod()).thenReturn(lazyCaching(Optional::empty));
        new WriteMethod<>(descriptor);
    }

    @Test
    public void testWriteValue() {
        TestObject testObject = new TestObject();
        descriptor = new PropertyAccessDescriptor<>(TestObject.class, TestObject.BOOLEAN_PROPERTY);

        WriteMethod<TestObject, Boolean> writeMethod = descriptor.createWriteMethod();
        writeMethod.writeValue(testObject, true);
    }

}
