package org.linkki.samples.appsample.ui;

import org.linkki.framework.ui.application.ApplicationConfig;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.samples.appsample.view.BusinessPartnerView;
import org.linkki.util.Sequence;

public class BusinessPartnerConfig implements ApplicationConfig {

    @Override
    public BusinessPartnerInfo getApplicationInfo() {
        return new BusinessPartnerInfo();
    }

// tag::searchMenu[]
    @Override
    public Sequence<ApplicationMenuItemDefinition> getMenuItemDefinitions() {
        return Sequence.of(new ApplicationMenuItemDefinition("Search", "appmenu-search", BusinessPartnerView.class));
    }
// end::searchMenu[]

}