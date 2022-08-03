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
package org.linkki.samples.playground.binding.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.linkki.samples.playground.binding.model.Country;

public final class CountryService {

    private static final List<Country> COUNTRIES;

    static {
        List<Country> countries = new ArrayList<>(3);
        countries.add(new Country("DEU", "Germany"));
        countries.add(new Country("AUT", "Austria"));
        countries.add(new Country("ITA", "Italy"));

        countries.sort(Comparator.comparing(Country::getName));

        COUNTRIES = Collections.unmodifiableList(countries);
    }

    private CountryService() {
        throw new IllegalStateException("Utility class");
    }

    public static List<Country> getCountries() {
        return COUNTRIES;
    }

}
