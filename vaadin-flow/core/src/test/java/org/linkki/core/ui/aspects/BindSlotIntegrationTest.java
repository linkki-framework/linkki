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
 *
 */

package org.linkki.core.ui.aspects;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.ui.aspects.annotation.BindSlot;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.ui.wrapper.VaadinComponentWrapper;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.dom.Element;

class BindSlotIntegrationTest {

    private static final String SLOT_TEXT = "text";
    private static final String SLOT_BUTTON = "button";

    @Test
    void testBindSlot() {
        List<Element> elements = UiCreator.createUiElements(new SampleButtonPmo(), new BindingContext(),
                                                            c -> new NoLabelComponentWrapper(
                                                                    (Component)c, WrapperType.COMPONENT))
                .map(VaadinComponentWrapper::getComponent)
                .map(Component::getElement)
                .collect(Collectors.toList());

        assertThat(elements).hasSize(2);

        elements.forEach(element -> {
            String slotName = element.getTag().equals("linkki-text") ? SLOT_TEXT : SLOT_BUTTON;
            assertThat(element.getAttribute("slot")).isEqualTo(slotName);
        });
    }

    @UIVerticalLayout
    private static class SampleButtonPmo {

        @BindSlot(SLOT_TEXT)
        @UILabel(position = 10)
        public String getText() {
            return StringUtils.EMPTY;
        }

        @BindSlot(SLOT_BUTTON)
        @UIButton(position = 20)
        public void button() {
            // click
        }

    }
}
