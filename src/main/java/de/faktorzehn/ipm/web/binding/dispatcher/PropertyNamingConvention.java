/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding.dispatcher;

import org.apache.commons.lang3.StringUtils;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;

/**
 * Naming convention for properties. Adds suffixes, e.g. "enabled", to the base property, e.g.
 * "premium". To obtain the enabled property for premium, e.g. "premiumEnabled". The respective
 * getter would be called "isPremiumEnabled()".
 *
 * @author widmaier
 */
public class PropertyNamingConvention {

    public static final String ENABLED_PROPERTY_SUFFIX = "enabled";
    public static final String VISIBLE_PROPERTY_SUFFIX = "visible";
    public static final String REQUIRED_PROPERTY_SUFFIX = "required";
    public static final String MESSAGES_PROPERTY_SUFFIX = "messages";
    public static final String AVAILABLE_VALUES_PROPERTY_SUFFIX = "availableValues";

    /**
     * @return the property without change
     * @throws NullPointerException if the given property is <code>null</code>
     */
    public String getValueProperty(String property) {
        Preconditions.checkNotNull(property);
        return property;
    }

    /**
     * @return the capitalized property with the suffix {@link #ENABLED_PROPERTY_SUFFIX}. e.g.
     *         "premium" results in "premiumEnabled".
     * @throws NullPointerException if the given property is <code>null</code>
     */
    public String getEnabledProperty(String property) {
        return checkAndAddSuffix(ENABLED_PROPERTY_SUFFIX, property);
    }

    /**
     * @return the capitalized property with the suffix {@link #VISIBLE_PROPERTY_SUFFIX}. e.g.
     *         "premium" results in "premiumVisible".
     * @throws NullPointerException if the given property is <code>null</code>
     */
    public String getVisibleProperty(String property) {
        return checkAndAddSuffix(VISIBLE_PROPERTY_SUFFIX, property);
    }

    /**
     * @return the capitalized property with the suffix {@link #MESSAGES_PROPERTY_SUFFIX}. e.g.
     *         "premium" results in "premiumMessages".
     * @throws NullPointerException if the given property is <code>null</code>
     */
    public String getMessagesProperty(String property) {
        return checkAndAddSuffix(MESSAGES_PROPERTY_SUFFIX, property);
    }

    /**
     * @return the capitalized property with the suffix {@link #AVAILABLE_VALUES_PROPERTY_SUFFIX}.
     *         e.g. "premium" results in "premiumAvailableValues".
     * @throws NullPointerException if the given property is <code>null</code>
     */
    public String getAvailableValuesProperty(String property) {
        return checkAndAddSuffix(AVAILABLE_VALUES_PROPERTY_SUFFIX, property);
    }

    /**
     * @return the capitalized property with the suffix {@link #REQUIRED_PROPERTY_SUFFIX} . e.g.
     *         "premium" results in "premiumRequired".
     * @throws NullPointerException if the given property is <code>null</code>
     */
    public String getRequiredProperty(String property) {
        return checkAndAddSuffix(REQUIRED_PROPERTY_SUFFIX, property);
    }

    private String checkAndAddSuffix(String suffix, String property) {
        Preconditions.checkNotNull(property);
        return property + StringUtils.capitalize(suffix);
    }

}
