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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.ui.aspects.types.IconType;
import org.linkki.core.ui.wrapper.LabelComponentWrapper;
import org.linkki.core.vaadin.component.ComponentFactory;
import org.linkki.core.vaadin.component.base.LinkkiAnchor;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.dom.Element;

public class IconAspectDefinitionTest {

    @Test
    public void testCreateComponentValueSetter_Button() {
        IconAspectDefinition iconAspectDefinition = new IconAspectDefinition(IconType.STATIC, VaadinIcon.ABACUS);
        Button button = ComponentFactory.newButton();
        ComponentWrapper componentWrapper = new LabelComponentWrapper(button);

        Consumer<VaadinIcon> componentValueSetter = iconAspectDefinition.createComponentValueSetter(componentWrapper);

        componentValueSetter.accept(VaadinIcon.ACADEMY_CAP);
        assertThat(button.isIconAfterText(), is(false));
        assertThat(getIconAttribute(button.getIcon().getElement()),
                   is(getIconAttribute(VaadinIcon.ACADEMY_CAP.create().getElement())));
    }

    @Test
    public void testCreateComponentValueSetter_Link() {
        IconAspectDefinition iconAspectDefinition = new IconAspectDefinition(IconType.STATIC, VaadinIcon.ABACUS);
        LinkkiAnchor anchor = ComponentFactory.newLink("");

        assertThat(anchor.getIcon(), is(nullValue()));

        ComponentWrapper componentWrapper = new LabelComponentWrapper(anchor);

        Consumer<VaadinIcon> componentValueSetter = iconAspectDefinition.createComponentValueSetter(componentWrapper);

        componentValueSetter.accept(VaadinIcon.ACCORDION_MENU);
        assertThat(anchor.getIcon(), is(VaadinIcon.ACCORDION_MENU));
    }

    private String getIconAttribute(Element icon) {
        if (icon == null) {
            return null;
        }
        return icon.getAttribute("icon");
    }
}
