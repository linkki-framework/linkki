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

import java.time.LocalDate;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.core.vaadin.component.section.LinkkiSection;
import org.linkki.samples.appsample.model.InMemoryBusinessPartnerRepository;
import org.linkki.samples.appsample.pmo.section.PartnerDetailsSectionPmo;
import org.linkki.samples.appsample.pmo.table.AddressTablePmo;

import com.github.mvysny.kaributesting.v10.LocatorJ;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

// tag::partnerDetailsViewTest[]
class PartnerDetailsViewTest {

    private final String johnDoesId = "ac54f2f9-7b82-4bc8-ab69-554cac5926f1";

    @RegisterExtension
    KaribuUIExtension karibuUIExtension = KaribuUIExtension
            .withConfiguration(new KaribuUIExtension.KaribuConfiguration()
                    .addRoute(PartnerDetailsView.class,
                              () -> new PartnerDetailsView(InMemoryBusinessPartnerRepository
                                      .newSampleRepository()))
                    .setLocale(Locale.GERMAN));
    // end::partnerDetailsViewTest[]

    // tag::testExistingPartner[]
    @Test
    void testExistingPartner() {
        var partnerDetailsView = UI.getCurrent()
                .navigate(PartnerDetailsView.class, johnDoesId).get();

        var partnerDetailsSection = _get(partnerDetailsView, LinkkiSection.class,
                                         ss -> ss.withId(PartnerDetailsSectionPmo.class.getSimpleName()));

        var name = _get(partnerDetailsSection, TextField.class, ss -> ss.withId("name")).getValue();
        var dateOfBirth = _get(partnerDetailsSection, DatePicker.class, ss -> ss.withId("dateOfBirth")).getValue();
        var note = _get(partnerDetailsSection, TextArea.class, ss -> ss.withId("note")).getValue();

        assertThat(name).isEqualTo("John Doe");
        assertThat(dateOfBirth).isBefore(LocalDate.now());
        assertThat(note).isEmpty();
    }
    // end::testExistingPartner[]

    // tag::testPartnerDoesNotExist[]
    @Test
    void testPartnerDoesNotExist() {
        var partnerDetailsView = UI.getCurrent()
                .navigate(PartnerDetailsView.class, "9999-9999-9999-9999-9999").get();

        var errorText = _get(partnerDetailsView, Text.class);
        assertThat(errorText.getText()).isEqualTo("No partner could be found with the given ID");
    }
    // end::testPartnerDoesNotExist[]

    // tag::testAddAddressValid[]
    @Test
    void testAddAddress_valid() {
        // given
        UI.getCurrent().navigate(PartnerDetailsView.class, johnDoesId);
        _get(Tabs.class).setSelectedIndex(2);
        var addressTable = KaribuUtils.Layouts.getWithPmo(LinkkiSection.class, AddressTablePmo.class);

        // when
        _get(addressTable, Button.class, ss -> ss.withId("addAddressDialogPmo")).click();

        var dialog = _get(Dialog.class);
        var street = _get(dialog, TextField.class, ss -> ss.withId("street"));
        var streetNumber = _get(dialog, TextField.class, ss -> ss.withId("streetNumber"));
        var postalCode = _get(dialog, TextField.class, ss -> ss.withId("postalCode"));
        var city = _get(dialog, TextField.class, ss -> ss.withId("city"));
        var country = _get(dialog, TextField.class, ss -> ss.withId("country"));
        KaribuUtils.Fields.setValue(street, "Mediterranean Avenue");
        KaribuUtils.Fields.setValue(streetNumber, "1");
        KaribuUtils.Fields.setValue(postalCode, "12345");
        KaribuUtils.Fields.setValue(city, "San Francisco");
        KaribuUtils.Fields.setValue(country, "USA");

        KaribuUtils.Dialogs.clickOkButton();

        // then
        LocatorJ._assertNoDialogs();
        var grid = KaribuUtils.Grids.get(addressTable);
        assertThat(KaribuUtils.Grids.getTextContentsInColumn(grid, "street")).contains("Mediterranean Avenue");
        assertThat(KaribuUtils.Grids.getTextContentsInColumn(grid, "streetNumber")).contains("1");
        assertThat(KaribuUtils.Grids.getTextContentsInColumn(grid, "postalCode")).contains("12345");
        assertThat(KaribuUtils.Grids.getTextContentsInColumn(grid, "city")).contains("San Francisco");
        assertThat(KaribuUtils.Grids.getTextContentsInColumn(grid, "country")).contains("USA");
    }
    // end::testAddAddressValid[]

    // tag::testAddAddressInvalid[]
    @Test
    void testAddAddress_invalid() {
        // given
        UI.getCurrent().navigate(PartnerDetailsView.class, johnDoesId);
        _get(Tabs.class).setSelectedIndex(2);
        var addressTablePmo = KaribuUtils.Layouts.getWithPmo(LinkkiSection.class, AddressTablePmo.class);

        // when
        _get(addressTablePmo, Button.class, ss -> ss.withId("addAddressDialogPmo")).click();

        var dialog = _get(Dialog.class);
        var street = _get(dialog, TextField.class, ss -> ss.withId("street"));
        var number = _get(dialog, TextField.class, ss -> ss.withId("streetNumber"));
        var postalCode = _get(dialog, TextField.class, ss -> ss.withId("postalCode"));
        var city = _get(dialog, TextField.class, ss -> ss.withId("city"));

        KaribuUtils.Dialogs.clickOkButton();

        // then
        assertThat(street.getErrorMessage()).isEqualTo("The field Street must not be empty.");
        assertThat(number.getErrorMessage()).isEqualTo("The field Street number must not be empty.");
        assertThat(postalCode.getErrorMessage()).isEqualTo("The field Postal code must not be empty.");
        assertThat(city.getErrorMessage()).isEqualTo("The field City must not be empty.");
    }
    // end::testAddAddressInvalid[]
}
