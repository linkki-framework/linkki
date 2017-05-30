package org.linkki.samples.binding.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.linkki.samples.binding.model.Country;

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

    public static List<Country> getCountries() {
        return COUNTRIES;
    }
}
