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

package org.linkki.framework.ui.application;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.ui.converters.LinkkiConverterFactory;
import org.linkki.core.ui.converters.LocalDateToDateConverter;
import org.linkki.core.ui.converters.LocalDateToStringConverter;
import org.linkki.util.Sequence;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.ConverterFactory;
import com.vaadin.data.util.converter.StringToBigDecimalConverter;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

import edu.umd.cs.findbugs.annotations.NonNull;

@RunWith(MockitoJUnitRunner.class)
public class LinkkiUiTest {

    
    @Mock
    private VaadinRequest request;

    
    @Mock
    private VaadinSession vaadinSession;

    
    @Captor
    private ArgumentCaptor<ConverterFactory> converterFactoryCaptor;

    private TestApplicationConfig config = new TestApplicationConfig();

    private LinkkiUi linkkiUi = new LinkkiUi(config);


    private void initUi() {
        UI.setCurrent(linkkiUi);
        VaadinSession.setCurrent(vaadinSession);

        linkkiUi.init(request);
    }

    @After
    public void cleanUpCurrentUi() {
        UI.setCurrent(null);
        VaadinSession.setCurrent(null);
    }

    
    @Test
    public void testInit() {
        initUi();

        assertThat(UI.getCurrent().getContent(), is(linkkiUi.getApplicationLayout()));
    }

    @Test
    public void testSetsConverters_NoConverter() {
        config.converters = Sequence.empty();
        initUi();

        verify(vaadinSession).setConverterFactory(converterFactoryCaptor.capture());
        
        @NonNull
        ConverterFactory converterFactory = converterFactoryCaptor.getValue();
        assertThat(converterFactory, is(instanceOf(LinkkiConverterFactory.class)));

        Converter<Date, LocalDate> converter = converterFactory
                .createConverter(Date.class, LocalDate.class);
        assertThat(converter, is(nullValue()));
        Converter<String, BigDecimal> converter2 = converterFactory
                .createConverter(String.class, BigDecimal.class);
        assertThat(converter2, is(instanceOf(StringToBigDecimalConverter.class)));
        Converter<String, LocalDate> converter3 = converterFactory
                .createConverter(String.class, LocalDate.class);
        assertThat(converter3, is(nullValue()));
    }

    @Test
    public void testSetsConverters_DefaultConverters() {
        config.converters = LinkkiConverterFactory.DEFAULT_JAVA_8_DATE_CONVERTERS;
        initUi();

        verify(vaadinSession).setConverterFactory(converterFactoryCaptor.capture());
        
        @NonNull
        ConverterFactory converterFactory = converterFactoryCaptor.getValue();
        assertThat(converterFactory, is(instanceOf(LinkkiConverterFactory.class)));

        Converter<Date, LocalDate> converter = converterFactory
                .createConverter(Date.class, LocalDate.class);
        assertThat(converter, is(instanceOf(LocalDateToDateConverter.class)));
        Converter<String, BigDecimal> converter2 = converterFactory
                .createConverter(String.class, BigDecimal.class);
        assertThat(converter2, is(instanceOf(StringToBigDecimalConverter.class)));
        Converter<String, LocalDate> converter3 = converterFactory
                .createConverter(String.class, LocalDate.class);
        assertThat(converter3, is(instanceOf(LocalDateToStringConverter.class)));
    }

    @Test
    public void testSetsConverters_SomeConverters() {
        config.converters = Sequence.of(new LocalDateToDateConverter());
        initUi();

        verify(vaadinSession).setConverterFactory(converterFactoryCaptor.capture());
        
        @NonNull
        ConverterFactory converterFactory = converterFactoryCaptor.getValue();
        assertThat(converterFactory, is(instanceOf(LinkkiConverterFactory.class)));

        Converter<Date, LocalDate> converter = converterFactory
                .createConverter(Date.class, LocalDate.class);
        assertThat(converter, is(instanceOf(LocalDateToDateConverter.class)));
        Converter<String, BigDecimal> converter2 = converterFactory
                .createConverter(String.class, BigDecimal.class);
        assertThat(converter2, is(instanceOf(StringToBigDecimalConverter.class)));
        Converter<String, LocalDate> converter3 = converterFactory
                .createConverter(String.class, LocalDate.class);
        assertThat(converter3, is(nullValue()));
    }

}
