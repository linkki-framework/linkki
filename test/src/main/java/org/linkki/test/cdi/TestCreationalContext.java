package org.linkki.test.cdi;
/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

import javax.enterprise.context.spi.CreationalContext;

import org.apache.commons.lang3.NotImplementedException;

/**
 * A {@link CreationalContext} implementation for tests.
 */
public class TestCreationalContext<T> implements CreationalContext<T> {

    @Override
    public void push(T incompleteInstance) {
        throw new NotImplementedException("");
    }

    @Override
    public void release() {
        throw new NotImplementedException("");
    }

}
