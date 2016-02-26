package org.linkki.core.ui.section.annotations;

/**
 * The type of available values of a property. Either dynamic or static.
 *
 * @author widmaier
 */
public enum AvailableValuesType {

    /**
     * Retrieve the values from an enum class and add the null value. Only works if the returned
     * type of the property is an {@link Enum}.
     */
    ENUM_VALUES_INCL_NULL,

    /**
     * Retrieve the values from an enum class. Only works if the returned type of the property is an
     * {@link Enum}.
     */
    ENUM_VALUES_EXCL_NULL,

    /**
     * When retrieving the content for the input field a method called
     * get[PropertyName]AvailableValues() is called.
     */
    DYNAMIC;

}
