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
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.linkki.samples.playground.uitest.AbstractUiTest;
import org.linkki.samples.playground.uitest.extensions.DriverExtension;
import org.openqa.selenium.Keys;

import com.vaadin.flow.component.datepicker.testbench.DatePickerElement;
import com.vaadin.flow.component.datepicker.testbench.DatePickerElement.OverlayContentElement;

@DriverExtension.Configuration(locale = "en")
public class DatePickerEnglishTest extends AbstractUiTest {

    @ParameterizedTest
    @MethodSource("dates")
    public void testDateFormats(String input, LocalDate expectedDate) {
        DatePickerElement datePicker = $(DatePickerElement.class).id("date");
        datePicker.clear();

        datePicker.sendKeys(input + Keys.ENTER);
        LocalDate actualDate = datePicker.getDate();

        assertThat(actualDate, is(expectedDate));
    }

    private static Stream<Arguments> dates() {
        return Stream.of(
                         Arguments.of("030120", LocalDate.of(2020, 3, 1)),
                         Arguments.of("05071850", LocalDate.of(1850, 5, 7)),
                         Arguments.of("6/4/73", LocalDate.of(1973, 6, 4)),
                         Arguments.of("9/1/1654", LocalDate.of(1654, 9, 1)),
                         Arguments.of("11/25/22", LocalDate.of(2022, 11, 25)),
                         Arguments.of("07/08/2232", LocalDate.of(2232, 7, 8)));
    }

    @Test
    public void testButtonLocalization() {
        DatePickerElement datePicker = $(DatePickerElement.class).id("date");
        datePicker.clear();
        datePicker.open();
        OverlayContentElement overlay = datePicker.getOverlayContent();

        String todayText = overlay.getTodayButton().getText().trim();

        assertThat(todayText, is("Today"));
    }

}