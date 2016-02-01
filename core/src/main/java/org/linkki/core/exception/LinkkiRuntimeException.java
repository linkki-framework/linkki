/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.exception;

public class LinkkiRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 2782433427960406450L;

    public LinkkiRuntimeException(String message) {
        super(message);
    }

    public LinkkiRuntimeException(Throwable cause) {
        super(cause);
    }

    public LinkkiRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
