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

package org.linkki.core.vaadin.component;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.vaadin.ui.DateField;

public class ComponentFactoryTest {

    private static final String MESSAGE = "Date is out of allowed range";

    @Test
    public void testNewDateField_BelowRange() {
        DateField dateField = ComponentFactory.newDateField();
        dateField.setDateOutOfRangeMessage(MESSAGE);

        dateField.setValue(LocalDate.of(-1, 1, 1));

        assertThat(dateField.getErrorMessage().toString(), is(MESSAGE));
    }

    @Test
    public void testNewDateField_InRangeLowerBound() {
        DateField dateField = ComponentFactory.newDateField();
        dateField.setDateOutOfRangeMessage(MESSAGE);

        dateField.setValue(LocalDate.of(1, 1, 1));

        assertThat(dateField.getErrorMessage(), is(nullValue()));
    }

    @Test
    public void testNewDateField_InRangeUpperBound() {
        DateField dateField = ComponentFactory.newDateField();
        dateField.setDateOutOfRangeMessage(MESSAGE);

        dateField.setValue(LocalDate.of(9999, 12, 31));

        assertThat(dateField.getErrorMessage(), is(nullValue()));
    }

    @Test
    public void testNewDateField_AboveRange() {
        DateField dateField = ComponentFactory.newDateField();
        dateField.setDateOutOfRangeMessage(MESSAGE);

        dateField.setValue(LocalDate.of(10000, 1, 1));

        assertThat(dateField.getErrorMessage().toString(), is(MESSAGE));
    }

}
