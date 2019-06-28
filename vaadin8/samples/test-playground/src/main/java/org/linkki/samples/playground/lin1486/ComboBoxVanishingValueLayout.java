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

package org.linkki.samples.playground.lin1486;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.samples.playground.ui.SidebarSheetDefinition;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.ui.VerticalLayout;

public class ComboBoxVanishingValueLayout extends VerticalLayout implements SidebarSheetDefinition {

    public static final String ID = "LIN-1486";

    private static final long serialVersionUID = 5943058450629856446L;

    public ComboBoxVanishingValueLayout() {
        BindingContext bindingContext = new BindingContext();
        addComponent(PmoBasedSectionFactory
                .createAndBindSection(new ComboBoxVanishingValuePmo(bindingContext::modelChanged), bindingContext));
    }

    @Override
    public String getSidebarSheetName() {
        return ID;
    }

    @Override
    public Resource getSidebarSheetIcon() {
        return VaadinIcons.MAGIC;
    }

    @Override
    public String getSidebarSheetId() {
        return ID;
    }
}
