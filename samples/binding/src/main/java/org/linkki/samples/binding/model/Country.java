package org.linkki.samples.binding.model;

public final class Country {

    private final String isoA3;
    private final String name;

    public Country(String isoA3, String name) {
        this.isoA3 = isoA3;
        this.name = name;
    }

    public String getIsoA3() {
        return isoA3;
    }

    public String getName() {
        return name;
    }
}
