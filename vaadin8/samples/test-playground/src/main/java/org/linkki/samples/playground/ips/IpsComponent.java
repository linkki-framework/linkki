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
package org.linkki.samples.playground.ips;

import org.faktorips.runtime.ValidationContext;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.ui.converters.LinkkiConverterRegistry;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.uiframework.UiFramework;
import org.linkki.core.vaadin.component.section.AbstractSection;
import org.linkki.ips.binding.dispatcher.IpsPropertyDispatcherFactory;
import org.linkki.ips.messages.MessageConverter;
import org.linkki.samples.playground.ips.model.IpsModelObject;
import org.linkki.samples.playground.ui.SidebarSheetDefinition;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.VerticalLayout;

public class IpsComponent extends VerticalLayout implements SidebarSheetDefinition {

    public static final String ID = "Ips";

    private static final long serialVersionUID = 1L;

    public IpsComponent() {

        VaadinSession.getCurrent().setAttribute(LinkkiConverterRegistry.class, new LinkkiConverterRegistry());

        IpsModelObject ipsModelObject = new IpsModelObject();
        IpsPmo pmo = new IpsPmo(ipsModelObject);

        ValidationService validationService = // tag::createValidationService[]
                () -> MessageConverter.convert(ipsModelObject.validate(new ValidationContext(UiFramework.getLocale())));
        // end::createValidationService[]

        // tag::createBindingManager[]
        BindingManager bindingManager = new DefaultBindingManager(validationService,
                PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER, new IpsPropertyDispatcherFactory());
        // end::createBindingManager[]

        AbstractSection section = new PmoBasedSectionFactory().createSection(pmo,
                                                                             bindingManager.getContext(getClass()));

        addComponent(section);
    }

    @Override
    public String getSidebarSheetName() {
        return ID;
    }

    @Override
    public Resource getSidebarSheetIcon() {
        return VaadinIcons.INFO;
    }

    @Override
    public String getSidebarSheetId() {
        return ID;
    }

}