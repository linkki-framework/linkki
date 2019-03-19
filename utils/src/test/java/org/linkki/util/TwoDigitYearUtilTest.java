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

package org.linkki.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

public class TwoDigitYearUtilTest {

    @SuppressWarnings("null")
    private TwoDigitYearUtil convertInternaler;

    @Before
    public void setup() {
        convertInternaler = spy(TwoDigitYearUtil.INSTANCE);
    }

    private void givenCurrentYear(int year) {
        when(convertInternaler.getCurrentYear()).thenReturn(year);
    }

    @Test
    public void testconvertInternal_TwoDigitYearCurrentCentury() {
        givenCurrentYear(2019);
        assertThat((convertInternaler.convertInternal(LocalDate.of(19, 6, 7))), is(LocalDate.of(2019, 6, 7)));
    }

    @Test
    public void testconvertInternal_TwoDigitYearLastCentury() {
        givenCurrentYear(2019);
        assertThat((convertInternaler.convertInternal(LocalDate.of(68, 3, 2))), is(LocalDate.of(1968, 3, 2)));
    }

    @Test
    public void testconvertInternal_MinTwoDigitYearLastCentury() {
        givenCurrentYear(2019);
        assertThat((convertInternaler.convertInternal(LocalDate.of(39, 1, 1))), is(LocalDate.of(1939, 1, 1)));
    }

    @Test
    public void testconvertInternal_MaxTwoDigitYearLastCentury() {
        givenCurrentYear(2019);
        assertThat((convertInternaler.convertInternal(LocalDate.of(99, 12, 31))), is(LocalDate.of(1999, 12, 31)));
    }

    @Test
    public void testconvertInternal_MinTwoDigitYearCurrrentCentury() {
        givenCurrentYear(2019);
        assertThat((convertInternaler.convertInternal(LocalDate.of(0, 1, 1))), is(LocalDate.of(2000, 1, 1)));
    }

    @Test
    public void testconvertInternal_MaxTwoDigitYearCurrrentCentury() {
        givenCurrentYear(2019);
        assertThat((convertInternaler.convertInternal(LocalDate.of(38, 12, 31))), is(LocalDate.of(2038, 12, 31)));
    }

    @Test
    public void testconvertInternal_ThreeDigitYear_NoConversion() {
        givenCurrentYear(2019);
        assertThat((convertInternaler.convertInternal(LocalDate.of(139, 12, 31))), is(LocalDate.of(139, 12, 31)));
    }

    @Test
    public void testconvertInternal_FourDigitYear_NoConversion() {
        givenCurrentYear(2019);
        assertThat((convertInternaler.convertInternal(LocalDate.of(2039, 12, 31))), is(LocalDate.of(2039, 12, 31)));
    }

    @Test
    public void testconvertInternal_TwoDigitYearCurrrentCentury_Future() {
        givenCurrentYear(2091);
        assertThat((convertInternaler.convertInternal(LocalDate.of(80, 2, 24))), is(LocalDate.of(2080, 2, 24)));
    }

    @Test
    public void testconvertInternal_TwoDigitYearNextCentury_Future() {
        givenCurrentYear(2091);
        assertThat((convertInternaler.convertInternal(LocalDate.of(10, 5, 1))), is(LocalDate.of(2110, 5, 1)));
    }

}
