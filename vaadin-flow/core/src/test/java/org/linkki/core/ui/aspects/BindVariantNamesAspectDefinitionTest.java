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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.dom.impl.ThemeListImpl;

class BindVariantNamesAspectDefinitionTest {

    PropertyDispatcher dispatcher = new PropertyDispatcherFactory()
            .createDispatcherChain(new Object(),
                                   BoundProperty.empty(),
                                   PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

    @Test
    void testSetVariantNames_Button() {
        var component = new Button();
        assertThat(component.getThemeNames(), is(empty()));

        String theme = "jurassic-park";
        apply(component, theme);

        assertThat(component.getThemeNames(), contains(theme));
    }

    @Test
    void testSetVariantNames_Button_MultipleThemes() {
        var component = new Button();
        assertThat(component.getThemeNames(), is(empty()));

        String[] themes = {"cosmic-horror", "whoop-dee-doo"};
        apply(component, themes);

        assertThat(component.getThemeNames(), containsInAnyOrder(themes));
    }

    @Test
    void testSetVariantNames_Anchor() {
        // Anchor does not support themes
        var component = new Anchor();
        assertThat(component.getElement().getAttribute(ThemeListImpl.THEME_ATTRIBUTE_NAME), nullValue());

        apply(component, "ultimate-boredom");

        assertThat(component.getElement().getAttribute(ThemeListImpl.THEME_ATTRIBUTE_NAME), nullValue());
    }

    void apply(Component component, String... themes) {
        new BindVariantNamesAspectDefinition(themes).createUiUpdater(dispatcher, new NoLabelComponentWrapper(component)).apply();
    }

}
