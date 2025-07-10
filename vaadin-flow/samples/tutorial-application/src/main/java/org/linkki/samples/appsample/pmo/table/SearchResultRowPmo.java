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
package org.linkki.samples.appsample.pmo.table;

import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UILink;
import org.linkki.samples.appsample.model.BusinessPartner;

// tag::searchRow[]
public class SearchResultRowPmo {

    private final BusinessPartner partner;

    public SearchResultRowPmo(BusinessPartner partner) {
        this.partner = partner;
    }

    @UILabel(position = 10, label = "Name")
    public String getName() {
        return partner.getName();
    }

    @UILabel(position = 20, label = "First Address")
    public String getFirstAddress() {
        return partner.getFirstAddress();
    }

    // tag::url[]
    @UILink(position = 30, caption = "Show Details")
    public String getDetails() {
        // end::searchRow[]
        return "PartnerDetails/" + partner.getUuid().toString();
        // tag::searchRow2[]
    }
    // end::url[]
}
// end::searchRow2[]
