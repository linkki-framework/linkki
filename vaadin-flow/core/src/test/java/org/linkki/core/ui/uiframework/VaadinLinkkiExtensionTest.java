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

package org.linkki.core.ui.uiframework;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.uiframework.UiFramework;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@ExtendWith(KaribuUIExtension.class)
public class VaadinLinkkiExtensionTest {

    @Test
    public void testUiFrameworkIsVaadinLinkkiExtension() {
        assertThat(UiFramework.get(), is(instanceOf(VaadinLinkkiExtension.class)));
    }

    @Test
    public void testGetUiLocale_Null() {
        UI.setCurrent(null);

        assertThat(UiFramework.getLocale(), is(Locale.GERMAN));
    }

    @Test
    public void testGetUiLocale_Italian() {
        UI.getCurrent().setLocale(Locale.ITALIAN);

        assertThat(UiFramework.getLocale(), is(Locale.ITALIAN));
    }

    @Test
    public void testGetChildComponents() {
        Component component1 = new Span("first text");
        Component component2 = new Span("second text");
        HasComponents componentWithChildren = new VerticalLayout(component1, component2);

        Component[] arr = { component1, component2 };
        assertThat(UiFramework.get().getChildComponents(componentWithChildren).toArray(), is(arr));
    }

    @Test
    public void testIsVisible() {
        var uiComponent = new Span();
        assertThat(UiFramework.isVisible(uiComponent), is(true));

        uiComponent.setVisible(false);
        assertThat(UiFramework.isVisible(uiComponent), is(false));
    }


    public void testGetChildComponents_noComponents() {
        String noComponent = "no Component";
        assertThat(UiFramework.get().getChildComponents(noComponent), is(Stream.empty()));
    }

}
