/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding.aspect;

/**
 * Provides default implementation for all aspects. All methods return <code>true</code>. Subclasses
 * may override to veto behavior.
 *
 * @author widmaier
 */
public abstract class DefaultPropertyBehavior implements InjectablePropertyBehavior {

    @Override
    public boolean isWritable(String property) {
        return true;
    }

    @Override
    public boolean isVisible(String property) {
        return true;
    }

    @Override
    public boolean isShowValidationMessages(String property) {
        return true;
    }

}
