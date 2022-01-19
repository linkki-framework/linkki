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

package org.linkki.core.ui.nested.annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.aspects.annotation.BindVisible;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.VerticalAlignment;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.base.LabelComponentFormItem;
import org.linkki.core.vaadin.component.section.BaseSection;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout.FormItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class UINestedComponentIntegrationTest {

    @Test
    public void testCreateNestedComponent() {
        BaseSection component = (BaseSection)VaadinUiCreator.createComponent(new NestedComponentPmo(),
                                                                             new BindingContext());
        List<FormItem> childComponents = component.getSectionContent().getChildren().map(FormItem.class::cast)
                .collect(Collectors.toList());
        assertThat(childComponents.stream().map(i -> getChild(i, 0)).collect(Collectors.toList()),
                   contains(instanceOf(TextField.class), instanceOf(Div.class)));

        FormItem nestedComponentFormItem = childComponents.get(1);
        assertThat(getChild(nestedComponentFormItem, 1).getElement().getText(), is("Name/Vorname"));

        Div nestedComponentWrapper = (Div)getChild(nestedComponentFormItem, 0);
        assertThat(nestedComponentWrapper.getWidth(), is("100px"));
        assertThat(nestedComponentWrapper.getClassName(), is("style1 style2"));

        Component nestedComponent = getChild(nestedComponentWrapper, 0);
        assertThat(nestedComponent, instanceOf(HorizontalLayout.class));
        assertThat(((HorizontalLayout)nestedComponent).getWidth(), is("100px"));

        assertThat(getChild(nestedComponent, 0), instanceOf(TextField.class));
    }

    @Test
    public void testApsectOnNestedComponent() {
        NestedComponentPmo pmo = new NestedComponentPmo();
        BindingContext bindingContext = new BindingContext();
        BaseSection component = (BaseSection)VaadinUiCreator.createComponent(pmo,
                                                                             bindingContext);
        LabelComponentFormItem nestedComponent = (LabelComponentFormItem)getChild(component.getSectionContent(), 1);

        assertThat(nestedComponent.isVisible(), is(true));

        pmo.setVisible(false);
        bindingContext.modelChanged();

        assertThat(nestedComponent.isVisible(), is(false));
    }

    private static Component getChild(Component parent, int i) {
        return parent.getChildren().collect(Collectors.toList()).get(i);
    }

    @UISection
    public static class NestedComponentPmo {

        private boolean visible = true;

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
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        @UIHorizontalLayout(alignment = VerticalAlignment.MIDDLE)
        public static class NamePmo {

            private String name;

            @UITextField(position = 10, label = "")
            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
