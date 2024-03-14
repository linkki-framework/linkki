package org.linkki.samples.appsample.pmo.table;

import java.util.List;
import java.util.function.Supplier;

import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.appsample.model.BusinessPartner;

// tag::searchTable[]
@UISection(caption = "Search Results")
public class SearchResultTablePmo extends SimpleTablePmo<BusinessPartner, SearchResultRowPmo> {

    public SearchResultTablePmo(Supplier<List<? extends BusinessPartner>> modelObjectsSupplier) {
        super(modelObjectsSupplier);
    }

    @Override
    protected SearchResultRowPmo createRow(BusinessPartner partner) {
        return new SearchResultRowPmo(partner);
    }
}
// end::searchTable[]