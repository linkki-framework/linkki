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

import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.ips.decimalfield.UIDecimalField;
import org.linkki.samples.playground.ips.model.IpsModelObject;

@UISection(caption = "Faktor-IPS with linkki")
public class IpsPmo {

    // tag::ModelObject[]
    @ModelObject
    private final IpsModelObject modelObject;
    // end::ModelObject[]

    public IpsPmo(IpsModelObject modelObject) {
        this.modelObject = modelObject;
    }

    // tag::getString[]
    @UITextField(position = 0, modelAttribute = IpsModelObject.PROPERTY_STRING)
    public void getString() {
        // model binding
    }
    // end::getString[]

    @UIDecimalField(position = 10, label = "In Range [0..100/0.5]", required = RequiredType.REQUIRED, modelAttribute = IpsModelObject.PROPERTY_DECIMAL)
    public void decimal() {
        // model binding
    }

}