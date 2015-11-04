/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

public enum TestEnum {
    ONE("1"),
    TWO("2"),
    THREE("3");

    private String id;

    TestEnum(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name();
    }
}