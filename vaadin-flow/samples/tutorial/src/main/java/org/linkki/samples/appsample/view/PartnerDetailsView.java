package org.linkki.samples.appsample.view;

import java.util.Optional;
import java.util.UUID;

import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;
import org.linkki.samples.appsample.model.BusinessPartner;
import org.linkki.samples.appsample.model.BusinessPartnerRepository;
import org.linkki.samples.appsample.page.AddressPage;
import org.linkki.samples.appsample.page.BasicDataPage;
import org.linkki.samples.appsample.ui.BusinessPartnerLayout;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

@Route(value = "PartnerDetails", layout = BusinessPartnerLayout.class)
// tag::hasUrlParameter[]
public class PartnerDetailsView extends LinkkiTabLayout implements HasUrlParameter<String> {

    // end::hasUrlParameter[]

    private static final long serialVersionUID = 1L;

    // tag::retrievePartner1[]
    private final BusinessPartnerRepository partnerRepository;
    private Optional<BusinessPartner> currentPartner;

    @Autowired
    // tag::addTabSheets1[]
    public PartnerDetailsView(BusinessPartnerRepository partnerRepository) {
        // end::retrievePartner1[]
        // end::addTabSheets1[]
        super(Orientation.VERTICAL);
        this.getElement().getThemeList().add(THEME_VARIANT_SOLID);
        // tag::retrievePartner2[]
        this.partnerRepository = partnerRepository;
        this.currentPartner = Optional.empty();
        // end::retrievePartner2[]

        // tag::addTabSheets2[]
        addTabSheets(
                LinkkiTabSheet.builder("error").caption(VaadinIcon.WARNING.create()).content(this::createErrorLayout)
                        .visibleWhen(() -> currentPartner.isEmpty()).build(),
                LinkkiTabSheet.builder("basic-data").caption(VaadinIcon.USER.create())
                        .content(() -> currentPartner.map(this::createBasicDataPage).orElseGet(Div::new))
                        .visibleWhen(() -> currentPartner.isPresent()).build(),
                LinkkiTabSheet.builder("address").caption(VaadinIcon.HOME.create())
                        .content(() -> currentPartner.map(this::createAddressPage).orElseGet(Div::new))
                        .visibleWhen(() -> currentPartner.isPresent()).build());
        // tag::retrievePartner2[]
    }
    // end::addTabSheets2[]

    // tag::hasUrlParameter[]
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        // end::hasUrlParameter[]
        currentPartner = Optional.ofNullable(parameter).map(UUID::fromString)
                .map(partnerRepository::getBusinessPartner);
    }
    // end::retrievePartner2[]

    // tag::createComponentError[]
    private Component createErrorLayout() {
        return new Div(new Text("No partner ID was provided or no partner could be found with the given ID"));
    }
    // end::createComponentError[]

    // tag::createBasicDataPage[]
    private Component createBasicDataPage(BusinessPartner partner) {
        BasicDataPage basicDataPage = new BasicDataPage(partner);
        basicDataPage.init();
        return basicDataPage;
    }
    // end::createBasicDataPage[]

    // tag::createAddressPage[]
    private Component createAddressPage(BusinessPartner partner) {
        AddressPage addressPage = new AddressPage(partnerRepository, partner);
        addressPage.init();
        return addressPage;
    }
    // end::createAddressPage[]

}
