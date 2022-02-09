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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.aspects.annotation.BindPlaceholder;
import org.linkki.core.ui.aspects.types.PlaceholderType;
import org.linkki.core.ui.creation.table.GridComponentCreator;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;

class BindPlaceholderIntegrationTest {

    private final TestWithBindPlaceholderFieldsPmo pmo = new TestWithBindPlaceholderFieldsPmo();
    private final BindingContext bindingContext = new BindingContext();
    private final Map<String, Component> uiElements = UiCreator
            .createUiElements(pmo,
                              bindingContext,
                              c -> new NoLabelComponentWrapper((Component)c))
            .map(c -> c.getComponent())
            .collect(Collectors.toMap(k -> k.getId().get(), v -> v));

    @Test
    void testAspectBindPlaceholderAnnotation_NotBound() {
        TextField textfield = (TextField)uiElements.get("notBoundPlaceholderField");

        String placeholderResult = textfield.getPlaceholder();

        assertThat(placeholderResult).isNull();
    }

    @Test
    void testAspectBindPlaceholderAnnotation_Static() {
        TextField textfield = (TextField)uiElements.get("placeholderFieldStatic");

        String placeholderResult = textfield.getPlaceholder();

        assertThat(placeholderResult).isEqualTo("I am a nice static placeholder");
    }

    @Test
    void testAspectBindPlaceholderAnnotation_DynamicWithValue() {
        TextField textfield = (TextField)uiElements.get("placeholderFieldDynamic");

        pmo.setPlaceholder("I got changed");
        bindingContext.modelChanged();
        String placeholderResult = textfield.getPlaceholder();

        assertThat(placeholderResult).isEqualTo("I got changed");
    }

    @Test
    void testAspectBindPlaceholderAnnotation_DynamicEmptyValue() {
        TextField textfield = (TextField)uiElements.get("placeholderFieldDynamic");

        pmo.setPlaceholder("");
        bindingContext.modelChanged();
        String placeholderResult = textfield.getPlaceholder();

        assertThat(placeholderResult).isEmpty();
    }

    @Test
    void testAspectBindPlaceholderAnnotation_AutoWithValue() {
        TextField textfield = (TextField)uiElements.get("placeholderFieldAutoWithValue");

        String placeholderResult = textfield.getPlaceholder();

        assertThat(placeholderResult).isEqualTo("I am a placeholder");
    }

    @Test
    void testAspectBindPlaceholderAnnotation_AutoEmptyValue() {
        TextField textfield = (TextField)uiElements.get("placeholderFieldAutoEmptyValue");

        String placeholderResult = textfield.getPlaceholder();

        assertThat(placeholderResult).isEqualTo("I am a superior auto placeholder");
    }

    @Test
    void testAspectBindPlaceholderAnnotation_GridPmo_WithoutPlaceholderAnnotation() {
        Grid<Integer> grid = GridComponentCreator.createGrid(new TestGridWithoutBindPlaceholderPmo(),
                                                             new BindingContext());

        assertThat(grid.getElement().hasAttribute("has-placeholder")).isFalse();
        assertThat(grid.getElement().getStyle().get("--placeholder")).isNull();
    }

    @Test
    void testAspectBindPlaceholderAnnotation_GridPmo_WithEmptyPlaceholder() {
        Grid<Integer> grid = GridComponentCreator.createGrid(new TestGridWithEmptyBindPlaceholderPmo(),
                                                             new BindingContext());

        assertThat(grid.getElement().hasAttribute("has-placeholder")).isTrue();
        assertThat(grid.getElement().getStyle().get("--placeholder")).isEqualTo("''");
    }

    @Test
    void testAspectBindPlaceholderAnnotation_GridPmo_WithNonEmptyPlaceholder() {
        Grid<Integer> grid = GridComponentCreator.createGrid(new TestGridWithNonEmptyBindPlaceholderPmo(),
                                                             new BindingContext());

        assertThat(grid.getElement().hasAttribute("has-placeholder")).isTrue();
        assertThat(grid.getElement().getStyle().get("--placeholder")).isEqualTo("'No items'");
    }

    static class TestWithBindPlaceholderFieldsPmo {

        public static final String BUTTON_BINDING = "testFieldBindingButton";

        private String placeholder = "I can be changed";

        @UITextField(position = 10)
        public String getNotBoundPlaceholderField() {
            return "";
        }

        @BindPlaceholder(value = "I am a nice static placeholder", placeholderType = PlaceholderType.STATIC)
        @UITextField(position = 20)
        public String getPlaceholderFieldStatic() {
            return "";
        }

        @BindPlaceholder(placeholderType = PlaceholderType.DYNAMIC)
        @UITextField(position = 50)
        public String getPlaceholderFieldDynamic() {
            return "";
        }

        public String getPlaceholderFieldDynamicPlaceholder() {
            return placeholder;
        }

        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
        }

        @BindPlaceholder(value = "I am a placeholder")
        @UITextField(position = 30)
        public String getPlaceholderFieldAutoWithValue() {
            return "";
        }

        public String getPlaceholderFieldAutoWithValuePlaceholder() {
            return "I am a superior auto placeholder";
        }

        @BindPlaceholder
        @UITextField(position = 40)
        public String getPlaceholderFieldAutoEmptyValue() {
            return "";
        }

        public String getPlaceholderFieldAutoEmptyValuePlaceholder() {
            return "I am a superior auto placeholder";
        }

    }

    @UISection(caption = "TestGridWithoutBindPlaceholder")
    static class TestGridWithoutBindPlaceholderPmo implements ContainerPmo<Integer> {
        @Override
        public List<Integer> getItems() {
            return Collections.emptyList();
        }
    }

    @BindPlaceholder
    @UISection(caption = "TestGridWithEmptyBindPlaceholder")
    static class TestGridWithEmptyBindPlaceholderPmo implements ContainerPmo<Integer> {
        public String getPlaceholder() {
            return "";
        }

        @Override
        public List<Integer> getItems() {
            return Collections.emptyList();
        }
    }

    @BindPlaceholder("No items")
    @UISection(caption = "TestGridWithNonEmptyBindPlaceholder")
    static class TestGridWithNonEmptyBindPlaceholderPmo implements ContainerPmo<Integer> {
        @Override
        public List<Integer> getItems() {
            return Collections.emptyList();
        }
    }
}
