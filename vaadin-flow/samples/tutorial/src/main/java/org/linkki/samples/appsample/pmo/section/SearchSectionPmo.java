package org.linkki.samples.appsample.pmo.section;

import java.util.function.Consumer;

import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

// tag::searchfield[]
@UISection
public class SearchSectionPmo {
    private String searchText = "";

    // end::searchfield[]
    // tag::searchButton[]
    private Consumer<String> searchConsumer;

    public SearchSectionPmo(Consumer<String> searchConsumer) {
        this.searchConsumer = searchConsumer;
    }
    // end::searchButton[]

    // tag::searchfield[]
    @UITextField(position = 10, label = "")
    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
    // end::searchfield[]
    // tag::searchButton[]

    @UIButton(position = 20, caption = "Start Search")
    public void search() {
        searchConsumer.accept(getSearchText());
    }
    // end::searchButton[]
    // tag::searchfield[]
}
// end::searchfield[]
