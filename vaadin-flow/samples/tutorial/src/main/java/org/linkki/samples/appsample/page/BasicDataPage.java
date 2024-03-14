package org.linkki.samples.appsample.page;

import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.vaadin.component.page.AbstractPage;
import org.linkki.samples.appsample.model.BusinessPartner;
import org.linkki.samples.appsample.pmo.section.PartnerDetailsSectionPmo;

public class BasicDataPage extends AbstractPage {

    private static final long serialVersionUID = 1L;

    private final BindingManager bindingManager = new DefaultBindingManager();

    private final BusinessPartner partner;

    public BasicDataPage(BusinessPartner partner) {
        this.partner = partner;
    }

    @Override
    public void createContent() {
        addSection(new PartnerDetailsSectionPmo(partner));
    }

    @Override
    protected BindingManager getBindingManager() {
        return bindingManager;
    }
}
