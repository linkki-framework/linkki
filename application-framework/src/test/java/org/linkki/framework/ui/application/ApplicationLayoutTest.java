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
package org.linkki.framework.ui.application;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.ui.converters.LinkkiConverterFactory;
import org.linkki.core.ui.converters.LocalDateToDateConverter;
import org.linkki.framework.state.ApplicationConfig;
import org.linkki.util.Sequence;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.ConverterFactory;
import com.vaadin.data.util.converter.StringToBigDecimalConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationLayoutTest {

    @SuppressWarnings("null")
    @Mock
    private UI ui;

    @SuppressWarnings("null")
    @Mock
    private Page page;

    @SuppressWarnings("null")
    @Mock
    private ApplicationHeader header;

    @SuppressWarnings("null")
    @Mock
    private ApplicationFooter footer;

    @SuppressWarnings("null")
    @Mock
    private ApplicationConfig config;

    @SuppressWarnings("null")
    @Mock
    private ViewProvider viewProvider;

    @SuppressWarnings("null")
    private ApplicationLayout applicationLayout;

    @SuppressWarnings("null")
    @Captor
    private ArgumentCaptor<ConverterFactory> converterFactoryCaptor;

    private VaadinSession vaadinSession = mock(VaadinSession.class);

    private void setUpApplicationLayout() {
        applicationLayout = ApplicationLayout.create()
                .withHeader(header)
                .withFooter(footer)
                .build();

        when(ui.getPage()).thenReturn(page);
    }

    @Test
    public void testGetCurrentView_empty() {
        setUpApplicationLayout();

        Component currentView = applicationLayout.getCurrentView();

        assertThat(currentView, is(ApplicationLayout.EMPTY_VIEW));
    }

    @Test
    public void testGetCurrentView() {
        setUpApplicationLayout();
        View view = mock(View.class, withSettings().extraInterfaces(Component.class));
        applicationLayout.showView(view);

        Component currentView = applicationLayout.getCurrentView();

        assertThat(currentView, is(view));
    }

    @Test
    public void testShowView() {
        setUpApplicationLayout();
        assertThat(applicationLayout.getComponentCount(), is(3));
        assertThat(applicationLayout.getComponent(1), is(ApplicationLayout.EMPTY_VIEW));

        View view = mock(View.class, withSettings().extraInterfaces(Component.class));
        applicationLayout.showView(view);

        assertThat(applicationLayout.getComponentCount(), is(3));
        assertThat(applicationLayout.getComponent(1), is(view));

        View view2 = mock(View.class, withSettings().extraInterfaces(Component.class));
        applicationLayout.showView(view2);

        assertThat(applicationLayout.getComponentCount(), is(3));
        assertThat(applicationLayout.getComponent(1), is(view2));
    }

    @Test
    public void testShowView_NoFooter() {
        applicationLayout = ApplicationLayout.create()
                .withHeader(header)
                .build();
        when(ui.getPage()).thenReturn(page);

        assertThat(applicationLayout.getComponentCount(), is(2));
        assertThat(applicationLayout.getComponent(1), is(ApplicationLayout.EMPTY_VIEW));

        View view = mock(View.class, withSettings().extraInterfaces(Component.class));
        applicationLayout.showView(view);

        assertThat(applicationLayout.getComponentCount(), is(2));
        assertThat(applicationLayout.getComponent(1), is(view));

        View view2 = mock(View.class, withSettings().extraInterfaces(Component.class));
        applicationLayout.showView(view2);

        assertThat(applicationLayout.getComponentCount(), is(2));
        assertThat(applicationLayout.getComponent(1), is(view2));
    }

    @Test
    public void testSetsConverters_NoConverter() {
        VaadinSession.setCurrent(vaadinSession);

        applicationLayout = ApplicationLayout.create()
                .withHeader(header)
                .withFooter(footer)
                .withConverters(Sequence.empty())
                .build();

        verify(vaadinSession).setConverterFactory(converterFactoryCaptor.capture());
        assertThat(converterFactoryCaptor.getValue(), is(instanceOf(LinkkiConverterFactory.class)));

        Converter<Date, LocalDate> converter = converterFactoryCaptor.getValue()
                .createConverter(Date.class, LocalDate.class);
        assertThat(converter, is(nullValue()));
        Converter<String, BigDecimal> converter2 = converterFactoryCaptor.getValue()
                .createConverter(String.class, BigDecimal.class);
        assertThat(converter2, is(instanceOf(StringToBigDecimalConverter.class)));
    }

    @Test
    public void testSetsConverters_DefaultConverters() {
        VaadinSession.setCurrent(vaadinSession);

        applicationLayout = ApplicationLayout.create()
                .withHeader(header)
                .withFooter(footer)
                .build();

        verify(vaadinSession).setConverterFactory(converterFactoryCaptor.capture());
        assertThat(converterFactoryCaptor.getValue(), is(instanceOf(LinkkiConverterFactory.class)));

        Converter<Date, LocalDate> converter = converterFactoryCaptor.getValue()
                .createConverter(Date.class, LocalDate.class);
        assertThat(converter, is(instanceOf(LocalDateToDateConverter.class)));
        Converter<String, BigDecimal> converter2 = converterFactoryCaptor.getValue()
                .createConverter(String.class, BigDecimal.class);
        assertThat(converter2, is(instanceOf(StringToBigDecimalConverter.class)));
    }

    @Test
    public void testSetsConverters_SomeConverters() {
        VaadinSession.setCurrent(vaadinSession);

        applicationLayout = ApplicationLayout.create()
                .withHeader(header)
                .withFooter(footer)
                .withConverters(Sequence.of(new LocalDateToDateConverter()))
                .build();

        verify(vaadinSession).setConverterFactory(converterFactoryCaptor.capture());
        assertThat(converterFactoryCaptor.getValue(), is(instanceOf(LinkkiConverterFactory.class)));

        Converter<Date, LocalDate> converter = converterFactoryCaptor.getValue()
                .createConverter(Date.class, LocalDate.class);
        assertThat(converter, is(instanceOf(LocalDateToDateConverter.class)));
        Converter<String, BigDecimal> converter2 = converterFactoryCaptor.getValue()
                .createConverter(String.class, BigDecimal.class);
        assertThat(converter2, is(instanceOf(StringToBigDecimalConverter.class)));
    }

}
