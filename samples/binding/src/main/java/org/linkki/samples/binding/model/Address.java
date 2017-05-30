package org.linkki.samples.binding.model;

public class Address {

    private String street;
    private String zip;
    private String city;
    private Country country;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String asSingleLineString() {
        StringBuilder builder = new StringBuilder(50);
        if (country != null) {
            builder.append(country.getIsoA3()).append(" - ");
        }
        if (zip != null) {
            builder.append(zip);
        }
        if (city != null) {
            appendSpaceIfNeeded(builder).append(city).append(',');
        }
        if (street != null) {
            appendSpaceIfNeeded(builder).append(street);
        }

        return builder.toString();
    }

    private static StringBuilder appendSpaceIfNeeded(StringBuilder builder) {

        if (builder.length() > 0) {
            builder.append(' ');
        }

        return builder;
    }
}
