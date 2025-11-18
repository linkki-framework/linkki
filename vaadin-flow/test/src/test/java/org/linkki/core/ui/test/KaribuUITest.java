/*
 * Copyright Faktor Zehn GmbH. Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package org.linkki.core.ui.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serial;
import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

class KaribuUITest {

    @AfterEach
    void tearDown() {
        KaribuUI.tearDown();

        assertThat(UI.getCurrent()).isNull();
        assertThat(System.getProperty("vaadin.productionMode")).isNull();
    }

    @MethodSource("getTestLocaleArguments")
    @ParameterizedTest
    void testLocale(Locale locale, Locale expectedLocale) {
        KaribuUI.setUp(KaribuConfiguration.withDefaults().setLocale(locale));

        assertThat(UI.getCurrent().getLocale()).isEqualTo(expectedLocale);

        KaribuUtils.UI.refresh();

        assertThat(UI.getCurrent().getLocale()).isEqualTo(expectedLocale);
    }

    private static Stream<Arguments.ArgumentSet> getTestLocaleArguments() {
        return Stream.of(Arguments.argumentSet("en", Locale.ENGLISH, Locale.ENGLISH),
                         Arguments.argumentSet("de", Locale.GERMAN, Locale.GERMAN),
                         Arguments.argumentSet("no locale", null, Locale.ROOT));
    }

    @Test
    void testAddRoute() {
        KaribuUI.setUp(KaribuConfiguration.withDefaults().addRoute(MyView.class, MyView::new));

        UI.getCurrent().navigate(MyView.class);

        assertThat(UI.getCurrent().getCurrentView()).isInstanceOf(MyView.class);
    }

    @ValueSource(booleans = { true, false })
    @ParameterizedTest
    void testProductionMode(boolean productionMode) {
        KaribuUI.setUp(KaribuConfiguration.withDefaults().setProductionMode(productionMode));

        assertThat(System.getProperty("vaadin.productionMode")).isEqualTo(productionMode ? "true" : null);
    }

    @Route
    private static class MyView extends VerticalLayout {
        @Serial
        private static final long serialVersionUID = 2000472994321994678L;
    }
}