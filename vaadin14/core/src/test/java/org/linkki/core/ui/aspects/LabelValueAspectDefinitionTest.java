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
package org.linkki.core.ui.aspects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.function.Consumer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.ui.converters.LinkkiConverterRegistry;
import org.linkki.core.ui.mock.MockUi;
import org.linkki.core.ui.wrapper.LabelComponentWrapper;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.server.VaadinSession;

public class LabelValueAspectDefinitionTest {

    @AfterEach
    public void cleanUpUi() {
        UI.setCurrent(null);
        VaadinSession.setCurrent(null);
    }

    @Test
    public void testCreateComponentValueSetter_SetsString() {
        Span label = new Span();
        Consumer<Object> valueSetter = new LabelValueAspectDefinition()
                .createComponentValueSetter(new LabelComponentWrapper(label));

        valueSetter.accept("foo");

        assertThat(label.getText(), is("foo"));
    }

    @Test
    public void testCreateComponentValueSetter_UsesToString() {
        Span label = new Span();
        Consumer<Object> valueSetter = new LabelValueAspectDefinition()
                .createComponentValueSetter(new LabelComponentWrapper(label));

        valueSetter.accept(new Object() {
            @Override
            public String toString() {
                return "bar";
            }
        });

        assertThat(label.getText(), is("bar"));
    }

    @Test
    public void testCreateComponentValueSetter_UsesStandardConverter() {
        Span label = new Span();
        Consumer<Object> valueSetter = new LabelValueAspectDefinition()
                .createComponentValueSetter(new LabelComponentWrapper(label));

        valueSetter.accept(Integer.valueOf(123456));

        // default is Locale.GERMAN
        assertThat(label.getText(), is("123.456"));
    }

    @Test
    public void testCreateComponentValueSetter_UsesStandardConverter_DependingOnUiLocale() {
        Span label = new Span();
        Consumer<Object> valueSetter = new LabelValueAspectDefinition()
                .createComponentValueSetter(new LabelComponentWrapper(label));

        UI ui = MockUi.mockUi();
        when(ui.getLocale()).thenReturn(Locale.US);

        valueSetter.accept(Integer.valueOf(123456));

        assertThat(label.getText(), is("123,456"));
    }

    @Test
    public void testCreateComponentValueSetter_UsesCustomConverter() {
        Span label = new Span();
        Consumer<Object> valueSetter = new LabelValueAspectDefinition()
                .createComponentValueSetter(new LabelComponentWrapper(label));


        LinkkiConverterRegistry converterRegistry = LinkkiConverterRegistry.DEFAULT
                .with(new Converter<String, FooBar>() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Result<FooBar> convertToModel(String value,
                            ValueContext context) {
                        return Result.ok(FooBar.valueOf(value));
                    }

                    @Override
                    public String convertToPresentation(FooBar value,
                            ValueContext context) {
                        return value == FooBar.FOO ? "Foo" : "Bar";
                    }
                });
        VaadinSession vaadinSession = mock(VaadinSession.class);
        when(vaadinSession.getAttribute(LinkkiConverterRegistry.class)).thenReturn(converterRegistry);
        VaadinSession.setCurrent(vaadinSession);

        valueSetter.accept(FooBar.FOO);

        assertThat(label.getText(), is("Foo"));
    }

    private enum FooBar {
        FOO,
        BAR;
    }

}
