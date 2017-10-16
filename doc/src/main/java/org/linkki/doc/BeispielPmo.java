package org.linkki.doc;

//tag::class[]
public class BeispielPmo {

    private String surname;
    private String salutation;

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getNameForUI() {
        return String.join(" ", salutation, surname);
    }
}
//end::class[]
