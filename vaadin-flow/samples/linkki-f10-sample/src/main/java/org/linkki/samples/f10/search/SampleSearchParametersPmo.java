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
package org.linkki.samples.f10.search;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import de.faktorzehn.commons.linkki.search.annotation.UISearchCriteriaGroup;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.annotation.BindAutoFocus;
import org.linkki.core.ui.aspects.annotation.BindVisible;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UITextField;

import com.vaadin.flow.component.button.ButtonVariant;

import org.linkki.samples.f10.search.service.SampleSearchParameters;

// tag::sampleSearchParametersPmo[]
public class SampleSearchParametersPmo {

    public static final String SECTOR_FINANCE = "Finanz und Versicherung";
    public static final String SECTOR_TELECOM = "Telekommunikation";

    private final Supplier<SampleSearchParameters> searchParameters;

    private boolean showMore;

    public SampleSearchParametersPmo(Supplier<SampleSearchParameters> searchParameters) {
        this.searchParameters = searchParameters;
        this.showMore = false;
    }

    @ModelObject
    public SampleSearchParameters getSearchParameters() {
        return searchParameters.get();
    }

    // @BindPlaceholder("z.B. B1230984EK")
    @BindAutoFocus
    @UITextField(position = 10, label = "Partnernummer",
            modelAttribute = SampleSearchParameters.PROPERTY_PARTNER_NUMBER)
    public void partnerNumber() {
        // model binding
    }
    // end::sampleSearchParametersPmo[]

    @UISearchCriteriaGroup(position = 30)
    public BasicInfoPmo getBasicInfo() {
        return new BasicInfoPmo(this::getSearchParameters);
    }

    @BindVisible
    @UISearchCriteriaGroup(position = 50, caption = "Kontaktdaten")
    public ContactInfoPmo getAddress() {
        return new ContactInfoPmo(this::getSearchParameters);
    }

    public boolean isAddressVisible() {
        return showMore;
    }

    @BindVisible
    @UISearchCriteriaGroup(position = 60, caption = "Bankdaten")
    public BankInfoPmo getBankInfo() {
        return new BankInfoPmo(this::getSearchParameters);
    }

    public boolean isBankInfoVisible() {
        return showMore;
    }

    @UIButton(position = Integer.MAX_VALUE, captionType = CaptionType.DYNAMIC, variants = {
            ButtonVariant.LUMO_TERTIARY })
    public void showMore() {
        showMore = !showMore;
    }

    public String getShowMoreCaption() {
        return showMore ? "Weniger Parameter anzeigen" : "Mehr Parameter zeigen";
    }

    public static class BasicInfoPmo {

        private final Supplier<SampleSearchParameters> searchParameters;

        public BasicInfoPmo(Supplier<SampleSearchParameters> searchParameters) {
            this.searchParameters = searchParameters;
        }

        @UITextField(position = 10, label = "Name",
                modelAttribute = SampleSearchParameters.PROPERTY_NAME)
        public void name() {
            // model binding
        }

        @UITextField(position = 20, label = "Vorname",
                modelAttribute = SampleSearchParameters.PROPERTY_GIVEN_NAME)
        public void givenName() {
            // model binding
        }

        @UIDateField(position = 30, label = "Geburtsdatum",
                modelAttribute = SampleSearchParameters.PROPERTY_DATE_OF_BIRTH)
        public void dateOfBirth() {
            // model binding
        }

        @UIComboBox(position = 40, label = "Industriesektor",
                modelAttribute = SampleSearchParameters.PROPERTY_SECTOR,
                content = AvailableValuesType.DYNAMIC)
        public void sector() {
            // model binding
        }

        public List<String> getSectorAvailableValues() {
            return Arrays.asList(SECTOR_FINANCE, SECTOR_TELECOM);
        }

        @UIComboBox(position = 50, label = "Klassifizierung",
                modelAttribute = SampleSearchParameters.PROPERTY_CLASSIFICATION,
                content = AvailableValuesType.DYNAMIC)
        public void classification() {
            // model binding
        }

        public List<String> getClassificationAvailableValues() {
            return List.of("A 01 Landwirtschaft und Jagd");
        }

        @ModelObject
        public SampleSearchParameters getSearchParameters() {
            return searchParameters.get();
        }
    }

    public static class ContactInfoPmo {

        private final Supplier<SampleSearchParameters> searchParameters;

        public ContactInfoPmo(Supplier<SampleSearchParameters> searchParameters) {
            this.searchParameters = searchParameters;
        }

        @UITextField(position = 10, label = "PLZ",
                modelAttribute = SampleSearchParameters.PROPERTY_POSTAL_CODE)
        public void postalCode() {
            // model binding
        }

        @UITextField(position = 20, label = "Ort",
                modelAttribute = SampleSearchParameters.PROPERTY_CITY)
        public void cty() {
            // model binding
        }

        @UITextField(position = 30, label = "Straße und Hausnummer",
                modelAttribute = SampleSearchParameters.PROPERTY_STREE_AND_NUMBER)
        public void streetAndNumber() {
            // model binding
        }

        @UITextField(position = 40, label = "Email-Adresse",
                modelAttribute = SampleSearchParameters.PROPERTY_EMAIL)
        public void email() {
            // model binding
        }

        @UITextField(position = 50, label = "Telefonnummer",
                modelAttribute = SampleSearchParameters.PROPERTY_PHONE)
        public void phone() {
            // model binding
        }

        @ModelObject
        public SampleSearchParameters getSearchParameters() {
            return searchParameters.get();
        }
    }

    enum PartnerKindFilter {
        ALL("All"),
        NATURAL_PERSON("Natural Person"),
        LEGAL_ENTITY("Legal Entity");

        private String name;

        PartnerKindFilter(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class BankInfoPmo {

        private final Supplier<SampleSearchParameters> searchParameters;

        public BankInfoPmo(Supplier<SampleSearchParameters> searchParameters) {
            this.searchParameters = searchParameters;
        }

        @UITextField(position = 10, label = "IBAN",
                modelAttribute = SampleSearchParameters.PROPERTY_IBAN)
        public void iban() {
            // model binding
        }

        @ModelObject
        public SampleSearchParameters getSearchParameters() {
            return searchParameters.get();
        }
    }

}
