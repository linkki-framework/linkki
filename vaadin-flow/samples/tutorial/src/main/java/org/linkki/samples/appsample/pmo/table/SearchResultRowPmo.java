package org.linkki.samples.appsample.pmo.table;

import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UILink;
import org.linkki.samples.appsample.model.BusinessPartner;

public class SearchResultRowPmo {

    private final BusinessPartner partner;

    public SearchResultRowPmo(BusinessPartner partner) {
        this.partner = partner;
    }

    // tag::searchRow[]
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
    // end::searchRow2[]
    // end::url[]
}
