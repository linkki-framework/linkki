package org.linkki.core.ui.section.annotations;

/** The type how the enabled state of an UI component is bound. */
public enum EnabledType {

    /** The UI component is always enabled. */
    ENABLED,

    /** The UI component is always disabled. */
    DISABLED,

    /**
     * The enabled state is read from the PMO by invoking a method named is[PropertyName]Enabled().
     */
    DYNAMIC;

}
