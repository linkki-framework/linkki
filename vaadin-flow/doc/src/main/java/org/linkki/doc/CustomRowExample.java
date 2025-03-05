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

package org.linkki.doc;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;

import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UITextField;

public class CustomRowExample {

    // tag::customRowExample_base[]
    class PartnerTablePmo extends SimpleTablePmo<Partner, PartnerRowPmo> {

        protected PartnerTablePmo(Supplier<List<? extends Partner>> modelObjectsSupplier) {
            super(modelObjectsSupplier);
        }

        @Override
        protected PartnerRowPmo createRow(Partner partner) {
            return new PartnerRowPmo(partner);
        }
    }

    class PartnerRowPmo {
        private final Partner partner;

        public PartnerRowPmo(Partner partner) {
            this.partner = partner;
        }

        @UITextField(position = 10, label = "Name")
        public String getName() {
            return partner.getName();
        }

        protected Partner getPartner() {
            return partner;
        }
    }
    // end::customRowExample_base[]


    // tag::customRowExample_custom_table[]
    class CustomPartnerTablePmo extends PartnerTablePmo {

        protected CustomPartnerTablePmo(Supplier<List<? extends Partner>> modelObjectsSupplier) {
            super(modelObjectsSupplier);
        }

        @Override
        protected PartnerRowPmo createRow(Partner partner) {
            return new CustomPartnerRowPmo(partner);
        }

        // Attention:
        // It does not suffice to solely overwrite createRow.
        // getItemPmoClass must be altered to return the new row type.
        @Override
        public Class<? extends PartnerRowPmo> getItemPmoClass() {
            return CustomPartnerRowPmo.class;
        }
    }
    // end::customRowExample_custom_table[]

    // tag::customRowExample_custom_row[]
    class CustomPartnerRowPmo extends PartnerRowPmo {

        public CustomPartnerRowPmo(Partner partner) {
            super(partner);
        }

        @UIDateField(position = 20, label = "Date of Birth")
        public LocalDate getDateOfBirth() {
            return getPartner().getDateOfBirth();
        }
    }

    // end::customRowExample_custom_row[]

}
