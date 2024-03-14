package org.linkki.samples.appsample.page;

import java.util.ArrayList;
import java.util.List;

import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.vaadin.component.page.AbstractPage;
import org.linkki.samples.appsample.model.BusinessPartner;
import org.linkki.samples.appsample.model.BusinessPartnerRepository;
import org.linkki.samples.appsample.pmo.section.SearchSectionPmo;
import org.linkki.samples.appsample.pmo.table.SearchResultTablePmo;

// tag::searchPageInit[]
public class SearchPage extends AbstractPage // <1>
{
    // end::searchPageInit[]
    private static final long serialVersionUID = 1L;

    // tag::searchPageInit[]
    private final BindingManager bindingManager = new DefaultBindingManager();

    // end::searchPageInit[]

    // tag::search[]
    private final BusinessPartnerRepository partnerRepository;
    private final List<BusinessPartner> foundPartners = new ArrayList<>();

    // end::search[]

    // tag::searchPageInit[]
    public SearchPage(BusinessPartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    @Override
    protected BindingManager getBindingManager() {
        return bindingManager;
    }

    // end::searchPageInit[]

    // tag::searchPageInit[]
    // tag::addTable[]
    @Override
    public void createContent() {
        addSection(new SearchSectionPmo(this::search));
        // end::searchPageInit[]
        addSection(new SearchResultTablePmo(() -> foundPartners));
        // tag::searchPageInit[]
    }
    // end::addTable[]
    // end::searchPageInit[]

    // tag::search[]
    public void search(String searchText) {
        var searchResults = partnerRepository.findBusinessPartners(searchText);
        foundPartners.clear();
        foundPartners.addAll(searchResults);
    }
    // end::search[]
}
