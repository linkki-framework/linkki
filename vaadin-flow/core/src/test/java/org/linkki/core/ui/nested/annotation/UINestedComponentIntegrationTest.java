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

package org.linkki.core.ui.nested.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.pmo.SectionID;
import org.linkki.core.ui.aspects.annotation.BindVisible;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.VerticalAlignment;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.base.LabelComponentFormItem;
import org.linkki.core.vaadin.component.section.LinkkiSection;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

class UINestedComponentIntegrationTest {

    @Test
    void testCreateNestedComponent() {
        var section = VaadinUiCreator.createComponent(new NestedComponentPmo(),
                                                      new BindingContext());
        assertThat(getChildrenOfSection(section))
                .map(LabelComponentFormItem::getComponent)
                .map(c -> c.getId().get())
                .containsExactly("labelAheadOfNestedComponent", "nameLayout");

        var firstChildComponent = getChildInSection(section, 1);
        assertThat(firstChildComponent)
                .extracting(LabelComponentFormItem::getLabel)
                .extracting(NativeLabel::getText)
                .isEqualTo("Name/Vorname");

        var nestedComponentWrapper = (Div)firstChildComponent.getComponent();
        assertThat(nestedComponentWrapper.getWidth()).isEqualTo("100px");
        assertThat(nestedComponentWrapper.getClassName()).isEqualTo("style1 style2");

        var nestedComponent = nestedComponentWrapper.getComponentAt(0);
        assertThat(nestedComponent).isInstanceOf(HorizontalLayout.class);
        var horizontalLayout = (HorizontalLayout)nestedComponent;
        assertThat(horizontalLayout.getWidth()).isEqualTo("100%");
        assertThat(horizontalLayout.getComponentAt(0)).isInstanceOf(TextField.class);
    }

    @Test
    void testAspectOnNestedComponent() {
        var pmo = new NestedComponentPmo();
        var bindingContext = new BindingContext();
        var section = VaadinUiCreator.createComponent(pmo, bindingContext);
        var nestedComponent = getChildInSection(section, 1);

        assertThat(nestedComponent.isVisible()).isTrue();

        pmo.setNameLayoutVisible(false);
        bindingContext.modelChanged();

        assertThat(nestedComponent.isVisible()).isFalse();
    }

    @Test
    void testNestedComponent_EmptyWidth() {
        var section = VaadinUiCreator.createComponent(new NestedComponentWidthEmptyWidthPmo(),
                                                      new BindingContext());
        var wrapper = getChildInSection(section, 0);

        var nestedComponentWrapper = (Div)wrapper.getComponent();
        assertThat(nestedComponentWrapper.getWidth()).isEmpty();

        var nestedComponent = nestedComponentWrapper.getComponentAt(0);
        assertThat(nestedComponent).isInstanceOf(HorizontalLayout.class);
        assertThat(((HasSize)nestedComponent).getWidth()).isNull();
    }

    @Test
    void testCreateNestedComponent_MultiplePmos() {
        var pmo = new MultipleNestedPmo();
        var bindingContext = new BindingContext();

        var section = VaadinUiCreator.createComponent(pmo, bindingContext);

        var nestedComponentWithEmptyCollection = getChildInSection(section, 1).getComponent();
        assertThat(nestedComponentWithEmptyCollection.getChildren()).isEmpty();

        var nestedComponentWithCollectionWrapper = getChildInSection(section, 0).getComponent();
        var nestedComponents = nestedComponentWithCollectionWrapper.getChildren().toList();
        assertThat(nestedComponents)
                .allSatisfy(c -> assertThat(c).isInstanceOf(HorizontalLayout.class))
                .map(c -> c.getId().get())
                .containsExactly("first", "second");
        assertThat(nestedComponents)
                .map(c -> c.getChildren().findFirst().get())
                .allSatisfy(c -> assertThat(c).isInstanceOf(TextField.class))
                .map(c -> ((TextField)c).getValue())
                .as("Values should be filled correctly initially")
                .containsExactly("First Pmo", "Second Pmo");

        pmo.getMultiplePmos().get(0).setName("New");
        bindingContext.modelChanged();

        var firstNestedComponent = (HorizontalLayout)nestedComponents.get(0);
        var textFieldInNestedComponent = (TextField)firstNestedComponent.getComponentAt(0);
        assertThat(textFieldInNestedComponent.getValue()).isEqualTo("New");
    }

    private static LabelComponentFormItem getChildInSection(Component component, int index) {
        var section = (LinkkiSection)component;
        return (LabelComponentFormItem)section.getContentWrapper().getComponentAt(index);
    }

    private static Stream<LabelComponentFormItem> getChildrenOfSection(Component component) {
        var section = (LinkkiSection)component;
        return section.getContentWrapper().getChildren().map(LabelComponentFormItem.class::cast);
    }

    @UISection
    public static class NestedComponentPmo {

        private boolean nameLayoutVisible = true;

        @UITextField(position = 10)
        public String getLabelAheadOfNestedComponent() {
            return "";
        }

        @BindVisible
        @UINestedComponent(position = 20, label = "Name/Vorname", width = "100px", styleNames = { "style1", "style2" })
        public NamePmo getNameLayout() {
            return new NamePmo();
        }

        public boolean isNameLayoutVisible() {
            return nameLayoutVisible;
        }

        public void setNameLayoutVisible(boolean nameLayoutVisible) {
            this.nameLayoutVisible = nameLayoutVisible;
        }
    }

    @UISection
    public static class MultipleNestedPmo {

        private List<NamePmo> nestedPmos = List.of(new NamePmo("first", "First Pmo"),
                                                   new NamePmo("second", "Second Pmo"));

        @UINestedComponent(position = 30, label = "Multiple Pmos")
        public List<NamePmo> getMultiplePmos() {
            return nestedPmos;
        }

        @UINestedComponent(position = 40)
        public List<NamePmo> getEmptyCollection() {
            return List.of();
        }
    }

    @UISection
    public static class NestedComponentWidthEmptyWidthPmo {

        @UINestedComponent(position = 20, label = "Name", width = "")
        public NamePmo getNameLayout() {
            return new NamePmo();
        }

    }

    @UIHorizontalLayout(alignment = VerticalAlignment.MIDDLE)
    public static class NamePmo {

        private final String id;
        private String name;

        public NamePmo() {
            this.id = getClass().getSimpleName();
            this.name = "";
        }

        public NamePmo(String id, String name) {
            this.name = name;
            this.id = id;
        }

        @UITextField(position = 10, label = "")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @SectionID
        public String getId() {
            return id;
        }
    }
}
