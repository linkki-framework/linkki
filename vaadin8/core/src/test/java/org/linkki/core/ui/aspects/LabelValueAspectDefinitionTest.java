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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.function.Consumer;

import org.junit.After;
import org.junit.Test;
import org.linkki.core.ui.bind.MockUi;
import org.linkki.core.ui.converters.LinkkiConverterRegistry;
import org.linkki.core.ui.wrapper.LabelComponentWrapper;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

public class LabelValueAspectDefinitionTest {

    @After
    public void cleanUpUi() {
        UI.setCurrent(null);
        VaadinSession.setCurrent(null);
    }

    @Test
    public void testCreateComponentValueSetter_SetsString() {
        Label label = new Label();
        Consumer<Object> valueSetter = new LabelValueAspectDefinition()
                .createComponentValueSetter(new LabelComponentWrapper(label));

        valueSetter.accept("foo");

        assertThat(label.getValue(), is("foo"));
    }

    @Test
    public void testCreateComponentValueSetter_UsesToString() {
        Label label = new Label();
        Consumer<Object> valueSetter = new LabelValueAspectDefinition()
                .createComponentValueSetter(new LabelComponentWrapper(label));

        valueSetter.accept(new Object() {
            @Override
            public String toString() {
                return "bar";
            }
        });

        assertThat(label.getValue(), is("bar"));
    }

    @Test
    public void testCreateComponentValueSetter_UsesStandardConverter() {
        Label label = new Label();
        Consumer<Object> valueSetter = new LabelValueAspectDefinition()
                .createComponentValueSetter(new LabelComponentWrapper(label));

        valueSetter.accept(Integer.valueOf(123456));

        // default is Locale.GERMAN
        assertThat(label.getValue(), is("123.456"));
    }

    @Test
    public void testCreateComponentValueSetter_UsesStandardConverter_DependingOnUiLocale() {
        Label label = new Label();
        Consumer<Object> valueSetter = new LabelValueAspectDefinition()
                .createComponentValueSetter(new LabelComponentWrapper(label));

        UI ui = MockUi.mockUi();
        when(ui.getLocale()).thenReturn(Locale.US);

        valueSetter.accept(Integer.valueOf(123456));

        assertThat(label.getValue(), is("123,456"));
    }

    @Test
    public void testCreateComponentValueSetter_UsesCustomConverter() {
        Label label = new Label();
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

        assertThat(label.getValue(), is("Foo"));
    }

    private enum FooBar {
        FOO,
        BAR;
    }

}
