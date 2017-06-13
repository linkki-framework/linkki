package org.linkki.samples.messages.model;

import java.time.LocalDate;

public class User {

    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_PASSWORD = "password";
    public static final String PROPERTY_EMAIL = "email";
    public static final String PROPERTY_BIRTHDAY = "birthday";

    private String name;
    private String password;
    private String email;
    private LocalDate birthday;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

}
