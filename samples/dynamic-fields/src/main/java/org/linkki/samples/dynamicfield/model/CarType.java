package org.linkki.samples.dynamicfield.model;

public enum CarType {

    STANDARD,
    PREMIUM;


    /**
     * Method is called by linkki for presentation.
     *
     * @return captialized name of the enum value
     */
    public String getName() {
        String name = name();
        return name.substring(0, 1) + name.substring(1).toLowerCase();
    }
}
