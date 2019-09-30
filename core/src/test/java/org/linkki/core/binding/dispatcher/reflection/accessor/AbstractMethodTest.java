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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.linkki.util.LazyCachingSupplier.lazyCaching;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AbstractMethodTest {
    @Mock
    private PropertyAccessDescriptor<?, ?> descriptor;

    @Test
    public void testHasNoMethod() {
        when(descriptor.getReflectionWriteMethod()).thenReturn(lazyCaching(Optional::empty));

        AbstractMethod<?> writeMethod = new WriteMethod<>(descriptor);

        assertFalse(writeMethod.hasMethod());
    }

}
