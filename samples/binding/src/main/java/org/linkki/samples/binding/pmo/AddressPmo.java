package org.linkki.samples.binding.pmo;

import java.util.List;

import org.linkki.samples.binding.model.Address;
import org.linkki.samples.binding.model.Country;
import org.linkki.samples.binding.service.CountryService;

public class AddressPmo {

    private String street;
    private String zip;
    private String city;
    private Country country;


    public AddressPmo(Address address) {
        refreshAddress(address);
    }


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

    public List<Country> getCountryAvailableValues() {
        return CountryService.getCountries();
    }


    public void refreshAddress(Address address) {
        this.street = address.getStreet();
        this.zip = address.getZip();
        this.city = address.getCity();
        this.country = address.getCountry();
    }

}
