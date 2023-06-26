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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractMethodTest {

    @Test
    void testHasMethod_False() {
        var descriptor = new PropertyAccessDescriptor<TestObject, Long>(TestObject.class, TestObject.READ_ONLY_LONG_PROPERTY);

        var writeMethod = new WriteMethod<>(descriptor);

        assertFalse(writeMethod.hasMethod());
    }

    @Test
    void testHasMethod() {
        var descriptor = new PropertyAccessDescriptor<TestObject, Integer>(TestObject.class, TestObject.INT_PROPERTY);

        var writeMethod = new WriteMethod<>(descriptor);

        assertTrue(writeMethod.hasMethod());
    }

}
