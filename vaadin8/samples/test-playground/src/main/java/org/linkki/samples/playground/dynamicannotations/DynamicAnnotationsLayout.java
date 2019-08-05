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

package org.linkki.samples.playground.dynamicannotations;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.samples.playground.ui.SidebarSheetDefinition;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.ui.VerticalLayout;

public class DynamicAnnotationsLayout extends VerticalLayout implements SidebarSheetDefinition {

    public static final String ID = "Dynamic Annotations";

    private static final long serialVersionUID = 1L;

    public DynamicAnnotationsLayout() {
        BindingContext bindingContext = new BindingContext();
        addComponent(PmoBasedSectionFactory
                .createAndBindSection(new DynamicCaptionWithoutButtonPmo(), bindingContext));
        addComponent(PmoBasedSectionFactory
                .createAndBindSection(new DynamicCaptionWithEditButtonPmo(), bindingContext));
        addComponent(PmoBasedSectionFactory
                .createAndBindSection(new DynamicCaptionWithCloseButtonPmo(), bindingContext));

        addComponent(PmoBasedSectionFactory
                .createAndBindSection(new DynamicTooltipPmo(), bindingContext));
    }

    @Override
    public String getSidebarSheetName() {
        return ID;
    }

    @Override
    public Resource getSidebarSheetIcon() {
        return VaadinIcons.FLIGHT_TAKEOFF;
    }

    @Override
    public String getSidebarSheetId() {
        return ID;
    }
}
