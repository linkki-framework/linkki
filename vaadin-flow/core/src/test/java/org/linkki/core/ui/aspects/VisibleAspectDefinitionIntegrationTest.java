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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.ui.wrapper.VaadinComponentWrapper;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.Component;

class VisibleAspectDefinitionIntegrationTest {

    @Test
    void testInvisibleIfEmpty_null() {
        BindingContext bindingContext = new BindingContext();
        Map<String, Boolean> uiElements = UiCreator
                .createUiElements(new TestInvisibleIfEmptyPmo(),
                                  bindingContext,
                                  c -> new NoLabelComponentWrapper((Component)c))
                .map(c -> c.getComponent())
                .collect(Collectors.toMap(k -> k.getId().get(), v -> v.isVisible()));

        assertThat(uiElements.get("null")).isFalse();
        assertThat(uiElements.get("empty")).isFalse();
        assertThat(uiElements.get("nonEmpty")).isTrue();
    }

    @Test
    void testInvisibleIfEmpty_fromNullToNonEmpty() {
        BindingContext bindingContext = new BindingContext();
        var pmo = new TestInvisibleIfEmptyValueChangesPmo();
        var component = UiCreator
                .createUiElements(pmo,
                                  bindingContext,
                                  c -> new NoLabelComponentWrapper((Component)c))
                .map(VaadinComponentWrapper::getComponent)
                .toList()
                .get(0);

        // value is null -> component is invisible
        assertThat(component.isVisible()).isFalse();

        // value != null -> component should become visible after update
        pmo.setValue("Non Empty");
        bindingContext.modelChanged();
        assertThat(component.isVisible()).isTrue();
    }

    @UISection
    static class TestInvisibleIfEmptyPmo {

        @UILabel(position = 10, visible = VisibleType.INVISIBLE_IF_EMPTY)
        public String getNull() {
            return null;
        }

        @UILabel(position = 20, visible = VisibleType.INVISIBLE_IF_EMPTY)
        public String getEmpty() {
            return null;
        }

        @UILabel(position = 30, visible = VisibleType.INVISIBLE_IF_EMPTY)
        public String getNonEmpty() {
            return "Non Empty";
        }

    }

    @UISection
    static class TestInvisibleIfEmptyValueChangesPmo {

        private String value;

        @UILabel(position = 10, visible = VisibleType.INVISIBLE_IF_EMPTY)
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
