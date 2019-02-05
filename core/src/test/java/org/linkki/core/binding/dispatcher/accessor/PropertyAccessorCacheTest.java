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

package org.linkki.core.binding.dispatcher.accessor;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class PropertyAccessorCacheTest {

    @Test
    public void testGet_ReturnsSameAccessorOnSubsequentCalls() {
        PropertyAccessor<TestObject, ?> propertyAccessor = PropertyAccessorCache.get(TestObject.class,
                                                                                     TestObject.STRING_PROPERTY);

        assertThat(propertyAccessor.getPropertyName(), is(TestObject.STRING_PROPERTY));
        assertThat(PropertyAccessorCache.get(TestObject.class, TestObject.STRING_PROPERTY),
                   is(sameInstance(propertyAccessor)));
    }


}
