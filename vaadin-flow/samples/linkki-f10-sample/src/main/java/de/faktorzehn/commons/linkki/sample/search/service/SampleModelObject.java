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
package de.faktorzehn.commons.linkki.sample.search.service;

import java.time.LocalDate;

public class SampleModelObject {

    private String partnerNummber;
    private String name;
    private LocalDate geburtsdatum;
    private String geschlecht;
    private String adresse;
    private String link;

    public SampleModelObject(String partnerNummber, String name, LocalDate geburtsdatum, String geschlecht,
            String adresse,
            String link) {
        this.partnerNummber = partnerNummber;
        this.name = name;
        this.geburtsdatum = geburtsdatum;
        this.geschlecht = geschlecht;
        this.adresse = adresse;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getPartnerNummber() {
        return partnerNummber;
    }

    public LocalDate getGeburtsdatum() {
        return geburtsdatum;
    }

    public String getGeschlecht() {
        return geschlecht;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getLink() {
        return link;
    }

}
