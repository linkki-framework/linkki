/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations.adapters;

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
