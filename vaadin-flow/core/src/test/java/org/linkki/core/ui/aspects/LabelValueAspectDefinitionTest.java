/*
 * Copyright Faktor Zehn GmbH.
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
package org.linkki.core.ui.aspects;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.ui.converters.LinkkiConverterRegistry;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.util.HtmlContent;
import org.linkki.core.vaadin.component.base.LinkkiText;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.server.VaadinSession;

@ExtendWith(KaribuUIExtension.class)
public class LabelValueAspectDefinitionTest {

    @Test
    public void testCreateComponentValueSetter_SetsString() {
        LinkkiText label = new LinkkiText();
        Consumer<Object> valueSetter = new LabelValueAspectDefinition()
                .createComponentValueSetter(new NoLabelComponentWrapper(label));

        valueSetter.accept("foo");

        assertThat(label.getText(), is("foo"));
        assertThat(label.getElement().getProperty("innerHTML"), is(nullValue()));
    }

    @Test
    public void testCreateComponentValueSetter_UsesToString() {
        LinkkiText label = new LinkkiText();
        Consumer<Object> valueSetter = new LabelValueAspectDefinition()
                .createComponentValueSetter(new NoLabelComponentWrapper(label));

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
        UI.getCurrent().setLocale(Locale.GERMAN);
        LinkkiText label = new LinkkiText();
        Consumer<Object> valueSetter = new LabelValueAspectDefinition()
                .createComponentValueSetter(new NoLabelComponentWrapper(label));

        valueSetter.accept(Integer.valueOf(123456));

        assertThat(label.getText(), is("123.456"));
    }

    @Test
    public void testCreateComponentValueSetter_UsesStandardConverter_DependingOnUiLocale() {
        UI.getCurrent().setLocale(Locale.US);
        LinkkiText label = new LinkkiText();
        Consumer<Object> valueSetter = new LabelValueAspectDefinition()
                .createComponentValueSetter(new NoLabelComponentWrapper(label));

        valueSetter.accept(Integer.valueOf(123456));

        assertThat(label.getText(), is("123,456"));
    }

    @Test
    public void testCreateComponentValueSetter_UsesCustomConverter() {
        LinkkiText label = new LinkkiText();
        Consumer<Object> valueSetter = new LabelValueAspectDefinition()
                .createComponentValueSetter(new NoLabelComponentWrapper(label));

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
        VaadinSession.getCurrent().setAttribute(LinkkiConverterRegistry.class, converterRegistry);

        valueSetter.accept(FooBar.FOO);

        assertThat(label.getText(), is("Foo"));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCreateComponentValueSetter_HtmlContent_deprecated() {
        LinkkiText label = new LinkkiText();
        Consumer<Object> valueSetter = new LabelValueAspectDefinition(true)
                .createComponentValueSetter(new NoLabelComponentWrapper(label));

        valueSetter.accept("<i>foo</i>");

        assertThat(label.getText(), is("<i>foo</i>"));

        valueSetter = new LabelValueAspectDefinition(false)
                .createComponentValueSetter(new NoLabelComponentWrapper(label));

        valueSetter.accept("foo");

        assertThat(label.getText(), is("foo"));
    }

    @Test
    public void testCreateComponentValueSetter_HtmlContent() {
        LinkkiText label = new LinkkiText();
        Consumer<Object> valueSetter = new LabelValueAspectDefinition()
                .createComponentValueSetter(new NoLabelComponentWrapper(label));
        var htmlContent = HtmlContent.builder().tag("i", "foo").build();

        valueSetter.accept(htmlContent);

        var linkkiTextSpan = label.getElement().getChildren().findFirst().orElseThrow();
        var innerHTML = linkkiTextSpan.getProperty("innerHTML");
        assertThat(innerHTML, is("<i>foo</i>"));

    }

    private enum FooBar {
        FOO,
        BAR;
    }

}
