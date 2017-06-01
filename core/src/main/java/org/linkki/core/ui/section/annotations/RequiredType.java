package org.linkki.core.ui.section.annotations;

/**
 * Defines how the required state (whether a value must be entered/selected) of an UI component is
 * bound.
 */
public enum RequiredType {

    /** The UI component is always required. */
    REQUIRED,

    /** The UI component is required if the component is {@link EnabledType enabled}. */
    REQUIRED_IF_ENABLED,

    /** The UI component is optional. */
    NOT_REQUIRED,

    /**
     * The required state is read from the PMO by invoking a method named
     * is[PropertyName]Required().
     */
    DYNAMIC,

}
