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
