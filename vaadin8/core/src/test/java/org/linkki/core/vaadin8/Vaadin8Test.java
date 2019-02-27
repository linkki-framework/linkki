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

package org.linkki.core.vaadin8;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Test;
import org.linkki.core.binding.MockUi;
import org.linkki.core.ui.UiFramework;

import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class Vaadin8Test {

    @After
    public void cleanUpUi() {
        UI.setCurrent(null);
    }

    @Test
    public void testUiFrameworkIsVaadin8() {
        assertThat(UiFramework.get(), is(instanceOf(Vaadin8.class)));
    }

    @Test
    public void testGetUiLocale() {
        assertThat(UI.getCurrent(), is(nullValue()));
        assertThat(UiFramework.getLocale(), is(Locale.GERMAN));

        UI ui = MockUi.mockUi();
        when(ui.getLocale()).thenReturn(Locale.ITALIAN);
        assertThat(UiFramework.getLocale(), is(Locale.ITALIAN));

    }

    @Test
    public void testGetChildComponents() {
        Component component1 = new Label("first label");
        Component component2 = new Label("second label");
        HasComponents componentWithChildren = new VerticalLayout(component1, component2);

        Component[] arr = { component1, component2 };
        assertThat(UiFramework.get().getChildComponents(componentWithChildren).toArray(), is(arr));
    }

    public void testGetChildComponents_noComponents() {
        String noComponent = "no Component";
        assertThat(UiFramework.get().getChildComponents(noComponent), is(Stream.empty()));
    }

}
