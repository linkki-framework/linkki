package org.linkki.doc;

import java.time.LocalDate;

public class Partner {

    private String name;
    private LocalDate dateOfBirth;
    private PartnerType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public PartnerType getType() {
        return type;
    }

    public void setType(PartnerType type) {
        this.type = type;
    }

    public enum PartnerType {
        NATURAL_PERSON
    }

}
