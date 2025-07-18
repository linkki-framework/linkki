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
package org.linkki.samples.appsample.page;

import org.linkki.core.binding.dispatcher.behavior.PropertyBehavior;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.vaadin.component.page.AbstractPage;
import org.linkki.samples.appsample.model.Address;
import org.linkki.samples.appsample.model.BusinessPartner;
import org.linkki.samples.appsample.model.BusinessPartnerRepository;
import org.linkki.samples.appsample.pmo.table.AddressTablePmo;

public class AddressPage extends AbstractPage {
    private static final long serialVersionUID = 1L;

    private final BindingManager bindingManager;
    private final BusinessPartner partner;
    private final BusinessPartnerRepository partnerRepository;

    public AddressPage(BusinessPartnerRepository partnerRepository, BusinessPartner partner) {
        // tag::binding[]
        this.bindingManager = new DefaultBindingManager(ValidationService.NOP_VALIDATION_SERVICE,
                PropertyBehaviorProvider.with(PropertyBehavior.readOnly()));
        // end::binding[]
        this.partner = partner;
        this.partnerRepository = partnerRepository;
    }

    // tag::createContent[]
    @Override
    public void createContent() {
        addSection(new AddressTablePmo(partner::getAddresses, this::deleteAddress, this::addAddress));
    }

    // end::createContent[]

    // tag::deleteAddress[]
    public void deleteAddress(Address address) {
        partner.removeAddress(address);
    }
    // end::deleteAddress[]

    // tag::addAddress[]
    public void addAddress(Address address) {
        partner.addAddress(address);
        partnerRepository.saveBusinessPartner(partner);
    }
    // end::addAddress[]

    @Override
    protected BindingManager getBindingManager() {
        return bindingManager;
    }
}
