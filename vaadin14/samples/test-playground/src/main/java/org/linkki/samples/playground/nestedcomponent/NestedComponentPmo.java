/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.samples.playground.nestedcomponent;

import java.time.LocalDate;

import org.linkki.core.defaults.ui.aspects.types.AlignmentType;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UIRadioButtons;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.VerticalAlignment;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.nested.annotation.UINestedComponent;

// tag::nestedcomponentpmo[]
@UISection(caption = "@UINestedComponent")
public class NestedComponentPmo {

    private LocalDate birthday;
    private String title;
    private Color favoriteColor = Color.RED;

    @UITextField(position = 0, label = "Anrede")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @UINestedComponent(position = 10, label = "Name/Vorname", width = "")
    public NamePmo getNameLayout() {
        return new NamePmo();
    }

    @UIDateField(position = 20, label = "Geburtsdatum")
    public LocalDate getBirthday() {
        return birthday;
    }
    // end::nestedcomponentpmo[]

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    @UIRadioButtons(position = 30, label = "Lieblingsfarbe", buttonAlignment = AlignmentType.HORIZONTAL)
    public Color getFavoriteColor() {
        return favoriteColor;
    }

    public void setFavoriteColor(Color color) {
        favoriteColor = color;
    }

    @UINestedComponent(position = 40, label = "")
    public InfoPmo getInfoText() {
        return new InfoPmo();
    }

    @UINestedComponent(position = 60, label = "")
    public AddressPmo getAddress() {
        return new AddressPmo();
    }

    @UINestedComponent(position = 100)
    public ButtonPmo getButtons() {
        return new ButtonPmo();
    }

    @UISection(caption = "Information")
    private static class InfoPmo {

        @UILabel(position = 10)
        public String getText() {
            return "Hier steht ein Text.";
        }

        @UILabel(position = 20)
        public String getText2() {
            return "Hier auch.";
        }

    }

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

        @UILabel(position = 20, label = "")
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
    @UIHorizontalLayout(alignment = VerticalAlignment.MIDDLE)
    public static class ButtonPmo {

        @UIButton(position = 10, caption = "Submit")
        public void doStuff() {
            // just kidding
        }

        @UIButton(position = 20, caption = "Reset")
        public void doEvenMoreStuff() {
            // as if
        }
    }

    @UISection(caption = "Adresse")
    public static class AddressPmo {

        private CityPmo city = new CityPmo();
        private String street;
        private String country;


        @UITextField(position = 10, label = "Straße")
        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        @UINestedComponent(position = 20, label = "PLZ/Stadt")
        public CityPmo getCity() {
            return city;
        }

        @UITextField(position = 40, label = "Land")
        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        @UIHorizontalLayout
        public static class CityPmo {
            private String zip;
            private String city;

            @UITextField(position = 20, label = "", width = "20em")
            public String getZip() {
                return zip;
            }

            public void setZip(String zip) {
                this.zip = zip;
            }

            @UITextField(position = 30, label = "", width = "40%")
            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }
        }
    }

    public static enum Color {
        RED,
        GREEN,
        BLUE;
    }
}
