package org.linkki.samples.binding.model;

public class Person {

    private String firstname;
    private String lastname;

    private Address address;

    public Person() {
        this.address = new Address();
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        StringBuilder builder = new StringBuilder(30);
        if (firstname != null) {
            builder.append(firstname);
        }
        if (lastname != null) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(lastname);
        }

        return builder.toString();
    }
}
