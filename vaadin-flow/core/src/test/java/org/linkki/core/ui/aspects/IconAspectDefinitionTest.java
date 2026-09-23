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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.types.IconType;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.vaadin.component.ComponentFactory;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.dom.Element;

class IconAspectDefinitionTest {

    @Test
    void testCreateComponentValueSetter_Button() {
        var iconAspectDefinition = new IconAspectDefinition(IconType.STATIC, VaadinIcon.AIRPLANE);
        var button = ComponentFactory.newButton();
        var componentWrapper = new NoLabelComponentWrapper(button);

        Consumer<VaadinIcon> componentValueSetter = iconAspectDefinition.createComponentValueSetter(componentWrapper);

        componentValueSetter.accept(VaadinIcon.ACADEMY_CAP);
        assertThat(button.isIconAfterText(), is(false));
        assertThat(getIconAttribute(button.getIcon().getElement()),
                   is(getIconAttribute(VaadinIcon.ACADEMY_CAP.create().getElement())));
    }

    @Test
    void testCreateComponentValueSetter_Link() {
        var iconAspectDefinition = new IconAspectDefinition(IconType.STATIC, VaadinIcon.AIRPLANE);
        var anchor = ComponentFactory.newLink("");

        assertThat(anchor.getIcon(), is(nullValue()));

        var componentWrapper = new NoLabelComponentWrapper(anchor);

        Consumer<VaadinIcon> componentValueSetter = iconAspectDefinition.createComponentValueSetter(componentWrapper);

        componentValueSetter.accept(VaadinIcon.CONNECT);
        assertThat(anchor.getIcon(), is(VaadinIcon.CONNECT));
    }

    private String getIconAttribute(Element icon) {
        if (icon == null) {
            return null;
        }
        return icon.getAttribute("icon");
    }
}
