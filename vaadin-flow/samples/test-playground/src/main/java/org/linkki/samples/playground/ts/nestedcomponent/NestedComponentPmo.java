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
package org.linkki.samples.playground.ts.nestedcomponent;

import java.util.List;

import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.aspects.annotation.BindStyleNames;
import org.linkki.core.ui.aspects.annotation.BindVisible;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.VerticalAlignment;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.nested.annotation.UINestedComponent;

import com.vaadin.flow.theme.lumo.LumoUtility;

// tag::nestedcomponentpmo[]
@UISection(caption = "@UINestedComponent")
public class NestedComponentPmo {

    private String title;
    private final AddressPmo firstAddress = new AddressPmo("First Address",
            new AddressPmo.CityPmo("12345", "First city"),
            "First street");
    private final AddressPmo secondAddress = new AddressPmo("Second Address",
            new AddressPmo.CityPmo("67890", "Second city"),
            "Second street");
    private final List<AddressPmo> addresses = List.of(firstAddress, secondAddress);

    @UITextField(position = 0, label = "Non nested component")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @UINestedComponent(position = 10, label = "Nested horizontal layout", width = "")
    public NamePmo getNameLayout() {
        return new NamePmo();
    }

    @UICheckBox(position = 20,
            label = "Toggle visibility of nested PMO with collection", caption = "Has second address")
    public boolean isSecondNestedComponentVisible() {
        return addresses.get(1).isVisible();
    }

    public void setSecondNestedComponentVisible(boolean visible) {
        addresses.get(1).visible = visible;
    }

    // end::nestedcomponentpmo[]

    // tag::nestedcomponentcollectionpmo[]
    @BindStyleNames({ LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Gap.LARGE })
    @UINestedComponent(position = 80, label = "Nested PMO with Collection")
    public List<AddressPmo> getNestedPmos() {
        return addresses;
    }
    // end::nestedcomponentcollectionpmo[]

    // tag::innercomponentpmo[]
    @UIHorizontalLayout(alignment = VerticalAlignment.MIDDLE)
    public static class NamePmo {

        private String name;
        private String vorname;

        @UITextField(position = 10, label = "")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @UILabel(position = 20)
        public String getDivider() {
            return "/";
        }

        @UITextField(position = 30, label = "")
        public String getVorname() {
            return vorname;
        }

        public void setVorname(String vorname) {
            this.vorname = vorname;
        }
    }

    // end::innercomponentpmo[]

    @BindVisible
    @BindCaption
    @UISection
    public static class AddressPmo {

        private final String caption;
        private final CityPmo city;
        private final String street;
        private boolean visible = true;

        public AddressPmo(String caption, CityPmo city, String street) {
            this.caption = caption;
            this.city = city;
            this.street = street;
        }

        public String getCaption() {
            return caption;
        }

        public boolean isVisible() {
            return visible;
        }

        @UILabel(position = 10, label = "Street")
        public String getStreet() {
            return street;
        }

        @UINestedComponent(position = 20, label = "Zip/City")
        public CityPmo getCity() {
            return city;
        }

        @UIHorizontalLayout
        public static class CityPmo {
            private final String zip;
            private final String city;

            public CityPmo(String zip, String city) {
                this.zip = zip;
                this.city = city;
            }

            @UILabel(position = 20)
            public String getZip() {
                return zip;
            }

            @UILabel(position = 30)
            public String getCity() {
                return city;
            }
        }
    }
}
