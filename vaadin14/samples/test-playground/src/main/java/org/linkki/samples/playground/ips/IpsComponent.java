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
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.uiframework.UiFramework;
import org.linkki.core.vaadin.component.section.AbstractSection;
import org.linkki.ips.binding.dispatcher.IpsPropertyDispatcherFactory;
import org.linkki.ips.messages.MessageConverter;
import org.linkki.samples.playground.ips.model.IpsModelObject;

import com.vaadin.flow.component.html.Div;

public class IpsComponent extends Div {

    private static final long serialVersionUID = 1L;

    public IpsComponent() {
        init();
    }

    protected void init() {

        IpsModelObject ipsModelObject = new IpsModelObject();
        IpsPmo pmo = new IpsPmo(ipsModelObject);

        ValidationService validationService = () -> MessageConverter
                .convert(ipsModelObject.validate(new ValidationContext(UiFramework.getLocale())));

        BindingManager bindingManager = new DefaultBindingManager(validationService,
                PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER, new IpsPropertyDispatcherFactory());

        AbstractSection section = new PmoBasedSectionFactory().createSection(pmo,
                                                                             bindingManager.getContext(getClass()));

        add(section);
    }
}