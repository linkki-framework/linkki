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
package org.linkki.samples.playground.customlayout;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.samples.playground.customlayout.pmo.AddressSectionPmo;
import org.linkki.samples.playground.customlayout.pmo.HotelSearchPmo;
import org.linkki.samples.playground.ui.SidebarSheetDefinition;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class CustomLayoutComponent extends VerticalLayout implements SidebarSheetDefinition {

    public static final String ID = "Custom-Layout";

    private static final long serialVersionUID = -3028891029288587709L;

    public CustomLayoutComponent() {
        AddressSectionPmo pmo = new AddressSectionPmo();

        Page.getCurrent().setTitle("Linkki :: Custom Layout Sample");

        BindingContext bindingContext = new BindingContext();

        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        content.setSpacing(false);
        addComponent(content);

        content.addComponent(new PmoBasedSectionFactory()
                .createSection(pmo,
                               bindingContext));


        Label simpleFormLayoutInfo = new Label(
                "<p><hr/><p>Same PMO in a simple FormLayout - it references the same PMO instance, so all fields are in sync<p><hr/>",
                ContentMode.HTML);
        simpleFormLayoutInfo.setWidth("100%");
        content.addComponent(simpleFormLayoutInfo);

        // tag::CustomLayoutUI-create[]
        content.addComponent(FormLayoutCreator.create(pmo, bindingContext));
        // end::CustomLayoutUI-create[]

        Label uiHorizontalLayoutInfo = new Label(
                "<p><hr/><p>Different PMO with custom UIHorizontalLayout annotation - UI elements should align horizontally<p><hr/>",
                ContentMode.HTML);
        uiHorizontalLayoutInfo.setWidth("100%");
        content.addComponent(uiHorizontalLayoutInfo);
        // tag::CustomLayoutAnnotation[]
        HotelSearchPmo zipCityPmo = new HotelSearchPmo();
        content.addComponent(VaadinUiCreator.createComponent(zipCityPmo, bindingContext));
        // end::CustomLayoutAnnotation[]
    }

    @Override
    public String getSidebarSheetName() {
        return ID;
    }

    @Override
    public Resource getSidebarSheetIcon() {
        return VaadinIcons.ABACUS;
    }

    @Override
    public String getSidebarSheetId() {
        return ID;
    }

}