/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.aspect;

/**
 * Provides default implementation for all aspects. All methods return <code>true</code>. Subclasses
 * may override to veto behavior.
 *
 * @author widmaier
 */
public abstract class DefaultPropertyBehavior implements InjectablePropertyBehavior {

    @Override
    public boolean isWritable(Object boundObject, String property) {
        return true;
    }

    @Override
    public boolean isVisible(Object boundObject, String property) {
        return true;
    }

    @Override
    public boolean isShowValidationMessages(Object boundObject, String property) {
        return true;
    }

}
