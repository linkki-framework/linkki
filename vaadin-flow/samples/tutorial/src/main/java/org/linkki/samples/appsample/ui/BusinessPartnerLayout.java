package org.linkki.samples.appsample.ui;

import org.linkki.framework.ui.application.ApplicationLayout;
import org.linkki.samples.appsample.model.BusinessPartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class BusinessPartnerLayout extends ApplicationLayout {

    private static final long serialVersionUID = 1L;

    @Autowired
    public BusinessPartnerLayout(BusinessPartnerRepository repository) {
        super(new BusinessPartnerConfig());
    }
}