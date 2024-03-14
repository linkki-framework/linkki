package org.linkki.samples.appsample.view;

import org.linkki.framework.ui.component.Headline;
import org.linkki.samples.appsample.model.BusinessPartnerRepository;
import org.linkki.samples.appsample.page.SearchPage;
import org.linkki.samples.appsample.ui.BusinessPartnerLayout;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("BusinessPartner")
@Route(value = "", layout = BusinessPartnerLayout.class)
public class BusinessPartnerView extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    // tag::businessPartnerViewConstructor[]
    @Autowired
    public BusinessPartnerView(BusinessPartnerRepository repository) {
        Headline headline = new Headline("Partner Search");
        add(headline);
        SearchPage searchPage = new SearchPage(repository);
        searchPage.init();
        add(searchPage);
    }
    // end::businessPartnerViewConstructor[]
}
