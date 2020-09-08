package org.linkki.core.defaults.ui.aspects.types;

/**
 * Defines whether a link's target attribute is determined statically or dynamically from a call to the
 * PMO's {@code get[PropertyName]Target()} method.
 */
public enum TargetType {
    /** The link's target attribute is defined statically. */
    STATIC,
    /** The link's target attribute is defined by the PMO's {@code get[PropertyName]Target()} method. */
    DYNAMIC
}