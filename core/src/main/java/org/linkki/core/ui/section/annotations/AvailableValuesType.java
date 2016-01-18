package org.linkki.core.ui.section.annotations;

/**
 * The type of available values of a property. Either dynamic or static.
 *
 * @author widmaier
 */
public enum AvailableValuesType {

    /**
     * Static tries to retrieve the values from an enum class. Only works if the returned type of
     * the property is an {@link Enum}
     */
    STATIC,

    /**
     * When retrieving the content for the input field a method called
     * get[PropertyName]AvailableValues() is called.
     */
    DYNAMIC;

}
