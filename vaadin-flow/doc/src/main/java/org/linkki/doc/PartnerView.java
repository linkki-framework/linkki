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
package org.linkki.doc;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.ui.creation.VaadinUiCreator;

// tag::layoutWithPMO[]
public class PartnerView extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private final BindingManager bindingManager = new DefaultBindingManager();

    private final Partner partner;

    public PartnerView(Partner partner) {
        this.partner = partner;

        // elements that are always visible can be instantiated in the constructor
        createContent();
    }

    public void createContent() {
        add(VaadinUiCreator.createComponent(
                                            new PartnerSectionPmo(partner),
                                            bindingManager.getContext(this.getClass())));
    }

}
// end::layoutWithPMO[]