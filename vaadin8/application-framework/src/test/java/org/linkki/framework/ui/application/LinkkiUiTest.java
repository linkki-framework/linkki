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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.ui.converters.LinkkiConverterRegistry;
import org.linkki.framework.ui.application.menu.ApplicationMenu;
import org.linkki.util.Sequence;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vaadin.data.Converter;
import com.vaadin.data.converter.StringToBigDecimalConverter;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

import edu.umd.cs.findbugs.annotations.NonNull;

@ExtendWith(MockitoExtension.class)
public class LinkkiUiTest {

    @Mock
    private VaadinRequest request;

    @Mock
    private VaadinSession vaadinSession;

    @Captor
    private ArgumentCaptor<LinkkiConverterRegistry> converterRegistryCaptor;

    private TestApplicationConfig config = new TestApplicationConfig();

    private LinkkiUi linkkiUi = new LinkkiUi(config);

    private void initUi() {
        UI.setCurrent(linkkiUi);
        VaadinSession.setCurrent(vaadinSession);

        linkkiUi.init(request);
    }

    @Test
    public void testInit() {
        initUi();

        assertThat(UI.getCurrent().getContent(), is(linkkiUi.getApplicationLayout()));
    }

    @Test
    public void testLayoutInitializedWithCorrectLocale() {
        linkkiUi.setLocale(Locale.CANADA);

        initUi();

        String menuItem = ((ApplicationMenu)((ApplicationHeader)linkkiUi.getApplicationLayout().getComponent(0))
                .getComponent(0)).getItems().get(0).getText();
        assertThat(menuItem, is(Locale.CANADA.getDisplayCountry()));
    }

    @Test
    public void testSetsConverters_DefaultConverter() {
        config.converters = Sequence.empty();
        initUi();

        verify(vaadinSession).setAttribute(Mockito.eq(LinkkiConverterRegistry.class),
                                           converterRegistryCaptor.capture());

        @NonNull
        LinkkiConverterRegistry converterRegistry = converterRegistryCaptor.getValue();
        assertThat(converterRegistry, is(instanceOf(LinkkiConverterRegistry.class)));

        Converter<String, BigDecimal> converter2 = converterRegistry
                .findConverter(String.class, BigDecimal.class);
        assertThat(converter2, is(instanceOf(StringToBigDecimalConverter.class)));
        // TODO weitere testen
    }

}
