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
package org.linkki.core.ui.section.annotations.aspect;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class AvailableValuesProviderTest {

    @Test
    public void testBooleanPrimitiveToValues() {
        assertThat(AvailableValuesProvider.booleanPrimitiveToValues().size(), is(2));
    }

    @Test
    public void testBooleanToValues() {
        assertThat(AvailableValuesProvider.booleanWrapperToValues().size(), is(3));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEnumToValues() {
        assertThat(AvailableValuesProvider.enumToValues(TestEnum.class, false).size(), is(3));
        assertThat(AvailableValuesProvider.enumToValues(TestEnum.class, true).size(), is(4));
        assertThat(AvailableValuesProvider.enumToValues(TestEnum.class, true).get(0), is(nullValue()));

        assertThat(AvailableValuesProvider.enumToValues(TestEnum.VALUE1.getClass().asSubclass(Enum.class), false)
                .size(), is(3));
    }


    private enum TestEnum {
        VALUE1,
        VALUE2,
        VALUE3;
    }
}
