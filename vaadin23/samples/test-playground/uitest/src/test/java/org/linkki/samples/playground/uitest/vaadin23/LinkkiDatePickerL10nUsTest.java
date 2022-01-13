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

package org.linkki.samples.playground.uitest.vaadin23;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.uitest.AbstractUiTest;
import org.linkki.samples.playground.uitest.extensions.DriverExtension;
import org.openqa.selenium.Keys;

import com.vaadin.flow.component.datepicker.testbench.DatePickerElement;

@DriverExtension.Configuration(locale = "en")
public class LinkkiDatePickerL10nUsTest extends AbstractUiTest {

    @Test
    public void testNoDotsShort() {
        DatePickerElement datePicker = $(DatePickerElement.class).id("date");
        datePicker.clear();

        datePicker.sendKeys("030120" + Keys.ENTER);

        assertThat(datePicker.getDate(), is(LocalDate.of(2020, 3, 1)));
    }

    @Test
    public void testNoDotsLong() {
        DatePickerElement datePicker = $(DatePickerElement.class).id("date");
        datePicker.clear();

        datePicker.sendKeys("05071850" + Keys.ENTER);

        assertThat(datePicker.getDate(), is(LocalDate.of(1850, 5, 7)));
    }

    @Test
    public void testDotsShort() {
        DatePickerElement datePicker = $(DatePickerElement.class).id("date");
        datePicker.clear();

        datePicker.sendKeys("6/4/73" + Keys.ENTER);

        assertThat(datePicker.getDate(), is(LocalDate.of(1973, 6, 4)));
    }

    @Test
    public void testDotsLong() {
        DatePickerElement datePicker = $(DatePickerElement.class).id("date");
        datePicker.clear();

        datePicker.sendKeys("07/08/2232" + Keys.ENTER);

        assertThat(datePicker.getDate(), is(LocalDate.of(2232, 7, 8)));
    }

}