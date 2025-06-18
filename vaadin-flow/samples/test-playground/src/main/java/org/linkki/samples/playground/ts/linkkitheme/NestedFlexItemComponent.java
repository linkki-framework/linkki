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
package org.linkki.samples.playground.ts.linkkitheme;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.aspects.annotation.BindStyleNames;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.nested.annotation.UINestedComponent;
import org.linkki.core.ui.theme.LinkkiTheme;
import org.linkki.core.vaadin.component.section.BaseSection;
import org.linkki.samples.playground.ts.layouts.AbstractBasicElementsLayoutBehaviorPmo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class NestedFlexItemComponent {

    private NestedFlexItemComponent() {
        // Utility class
    }

    public static Component create() {
        BaseSection section = (BaseSection)VaadinUiCreator.createComponent(new OuterPmo(), new BindingContext());
        section.setHeight("300px");
        section.setWidth("100%");
        return section;
    }

    @UISection(layout = SectionLayout.VERTICAL)
    static class OuterPmo {

        @UILabel(position = 10)
        public String getChild1() {
            return "Child 1";
        }

        @BindStyleNames(value = { LinkkiTheme.Flex.BASIS_NONE, LumoUtility.Flex.GROW })
        @UINestedComponent(position = 20)
        public InnerPmo getInnerPmo() {
            return new InnerPmo();
        }

    }

    @UISection(caption = "Child 2")
    static class InnerPmo extends AbstractBasicElementsLayoutBehaviorPmo {
    }
}
