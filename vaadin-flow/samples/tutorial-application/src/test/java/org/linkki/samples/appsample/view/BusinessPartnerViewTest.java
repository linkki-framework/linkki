/*
 * Copyright Faktor Zehn GmbH. Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package org.linkki.samples.appsample.view;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.core.ui.test.WithLocale;
import org.linkki.samples.appsample.model.InMemoryBusinessPartnerRepository;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;

// tag::businessPartnerViewTest[]
@ExtendWith(KaribuUIExtension.class)
@WithLocale
public class BusinessPartnerViewTest {

    // end::businessPartnerViewTest[]

    // tag::testSearchFieldWithResult[]
    @Test
    void testSearchFieldWithResult() {
        var businessPartnerView = new BusinessPartnerView(InMemoryBusinessPartnerRepository.newSampleRepository());

        var searchField = _get(businessPartnerView, TextField.class, ss -> ss.withId("searchText"));
        KaribuUtils.Fields.setValue(searchField, "John");

        var searchButton = _get(businessPartnerView, Button.class, ss -> ss.withId("search"));
        searchButton.click();

        var resultTable = KaribuUtils.Grids.get(businessPartnerView);
        assertThat(resultTable.getColumns()).hasSize(3);

        var nameColumnValues = KaribuUtils.Grids.getTextContentsInColumn(resultTable, "name");
        var addressColumnValues = KaribuUtils.Grids.getTextContentsInColumn(resultTable, "firstAddress");
        assertThat(nameColumnValues).containsExactly("John Doe");
        assertThat(addressColumnValues).containsExactly("Im Zollhafen 15/17\n50678 Cologne\nGermany");
    }
    // end::testSearchFieldWithResult[]

    // tag::testSearchFieldWithNoResult[]
    @Test
    void testSearchFieldWithNoResult() {
        var businessPartnerView = new BusinessPartnerView(InMemoryBusinessPartnerRepository.newSampleRepository());

        var searchField = _get(businessPartnerView, TextField.class, ss -> ss.withId("searchText"));
        KaribuUtils.Fields.setValue(searchField, "non-existent");

        var searchButton = _get(businessPartnerView, Button.class, ss -> ss.withId("search"));
        searchButton.click();

        var resultTable = KaribuUtils.Grids.get(businessPartnerView);
        assertThat(resultTable.getColumns()).hasSize(3);

        var nameColumnValues = KaribuUtils.Grids.getTextContentsInColumn(resultTable, "name");
        var addressColumnValues = KaribuUtils.Grids.getTextContentsInColumn(resultTable, "firstAddress");
        assertThat(nameColumnValues).isEmpty();
        assertThat(addressColumnValues).isEmpty();
    }
    // end::testSearchFieldWithNoResult[]
}