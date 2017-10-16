/*
 * Copyright Faktor Zehn AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.linkki.core.ui.components;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

import com.vaadin.data.util.converter.Converter;

public class DoubleFieldTest {

    @Test
    public void testConstructor1() {
        DoubleField field = new DoubleField(Locale.GERMAN);
        assertThat(field.getConverter(), instanceOf(DoubleFieldConverter.class));
        assertThat(field.getConverter().getModelType(), is(Double.class));
    }

    @Test
    public void testConstructor2() {
        DoubleField field = new DoubleField("0.00", Locale.GERMAN);
        Converter<String, Object> converter = field.getConverter();
        assertThat(converter.convertToPresentation(0.2, String.class, null), is("0,20"));
    }

}
