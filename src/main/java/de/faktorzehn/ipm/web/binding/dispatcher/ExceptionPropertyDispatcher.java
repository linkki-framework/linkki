/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding.dispatcher;

import java.text.MessageFormat;
import java.util.List;

import org.faktorips.runtime.MessageList;

import de.faktorzehn.ipm.web.PresentationModelObject;

/**
 * {@link PropertyDispatcher} that throws exception on every method call except
 * {@link #isReadonly(String)} which returns <code>true</code> and {@link #getMessages(String)},
 * which returns an empty {@link MessageList}.
 *
 * Serves as a last resort fallback to simplify exception creation in other dispatchers.
 *
 * @author widmaier
 */
public final class ExceptionPropertyDispatcher implements PropertyDispatcher {

    private final Object domainModelObject;
    private final PresentationModelObject pmo;

    /**
     * @param domainModelObject used for error messages
     * @param pmo used for error messages
     */
    public ExceptionPropertyDispatcher(Object domainModelObject, PresentationModelObject pmo) {
        this.domainModelObject = domainModelObject;
        this.pmo = pmo;
    }

    @Override
    public void prepareUpdateUI() {
        // do nothing
    }

    @Override
    public Class<?> getValueClass(String property) {
        throw new IllegalArgumentException(getExceptionText(property, "find getter method for"));
    }

    private String getExceptionText(String property, String action) {
        return MessageFormat.format("Cannot {0} property \"{1}\" (object {2}, pmo {3})", action, property,
                                    domainModelObject, pmo);
    }

    @Override
    public Object getValue(String property) {
        throw new IllegalArgumentException(getExceptionText(property, "read"));
    }

    @Override
    public void setValue(String property, Object value) {
        throw new IllegalArgumentException(getExceptionText(property, "write"));
    }

    @Override
    public boolean isReadonly(String property) {
        return true;
    }

    @Override
    public boolean isEnabled(String property) {
        throw new IllegalArgumentException(getExceptionText(property, "get enabled state for"));
    }

    @Override
    public boolean isVisible(String property) {
        throw new IllegalArgumentException(getExceptionText(property, "get visibility for"));
    }

    @Override
    public boolean isRequired(String property) {
        throw new IllegalArgumentException(getExceptionText(property, "get required state for"));
    }

    @Override
    public List<?> getAvailableValues(String property) {
        throw new IllegalArgumentException(getExceptionText(property, "get available values for"));
    }

    /**
     * Returns an empty {@link MessageList} for all properties.
     */
    @Override
    public MessageList getMessages(String property) {
        return new MessageList();
    }

    @Override
    public PresentationModelObject getPmo() {
        return pmo;
    }
}
