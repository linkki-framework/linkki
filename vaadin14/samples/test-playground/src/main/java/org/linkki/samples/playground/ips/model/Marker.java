package org.linkki.samples.playground.ips.model;

import org.faktorips.runtime.model.annotation.IpsDocumented;
import org.faktorips.runtime.model.annotation.IpsEnumType;
import org.faktorips.runtime.IMarker;
import java.util.Map;
import java.util.HashMap;
import org.faktorips.runtime.model.annotation.IpsEnumAttribute;

/**
 * @generated
 */
@IpsDocumented(bundleName = "org.linkki.samples.playground.ips.model.model-label-and-descriptions", defaultLocale = "en")
@IpsEnumType(name = "Marker", attributeNames = { "id", "name", "requiredInformationMissing",
        "technicalConstraintViolated" })
public enum Marker implements IMarker {

    /**
     * @generated
     */
    REQUIRED_INFORMATION_MISSING(
            "R",
            "Required Information Missing",
            true,
            false),

    /**
     * @generated
     */
    TECHNICAL_CONSTRAINT_VIOLATED(
            "T",
            "Technical Constraint Violated",
            false,
            true);

    /**
     * This map is used to have high performance access to the values by ID.
     *
     * @generated
     */
    private static final Map<String, Marker> ID_MAP;
    /**
     * In this static block the id map is initialized with all the values in this enum.
     *
     * @generated
     */
    static {
        ID_MAP = new HashMap<String, Marker>();
        for (Marker value : values()) {
            ID_MAP.put(value.id, value);
        }
    }

    /**
     * @generated
     */
    private final String id;
    /**
     * @generated
     */
    private final String name;
    /**
     * @generated
     */
    private final boolean requiredInformationMissing;
    /**
     * @generated
     */
    private final boolean technicalConstraintViolated;

    /**
     * Creates a new instance of Marker.
     *
     * @generated
     */
    private Marker(String id, String name, boolean requiredInformationMissing, boolean technicalConstraintViolated) {
        this.id = id;
        this.name = name;
        this.requiredInformationMissing = requiredInformationMissing;
        this.technicalConstraintViolated = technicalConstraintViolated;
    }

    /**
     * Returns the enumeration value for the specified parameter <code>id</code>. Returns <code>null</code>
     * if no corresponding enumeration value is found, or if the parameter <code>id</code> is
     * <code>null</code>.
     *
     * @generated
     */
    public static final Marker getValueById(String id) {
        return ID_MAP.get(id);
    }

    /**
     * Returns the enumeration value for the specified parameter <code>name</code>. Returns
     * <code>null</code> if no corresponding enumeration value is found, or if the parameter
     * <code>name</code> is <code>null</code>.
     *
     * @generated
     */
    public static final Marker getValueByName(String name) {
        for (Marker currentValue : values()) {
            if (currentValue.name.equals(name)) {
                return currentValue;
            }
        }
        return null;
    }

    /**
     * Returns the enumeration value for the specified parameter <code>id</code>. If no corresponding enum
     * value is found for the given parameter, an {@link IllegalArgumentException} is thrown.
     *
     * @throws IllegalArgumentException if no corresponding enum value is found
     *
     * @generated
     */
    public static final Marker getExistingValueById(String id) {
        if (ID_MAP.containsKey(id)) {
            return ID_MAP.get(id);
        } else {
            throw new IllegalArgumentException("No enum value with id " + id);
        }
    }

    /**
     * Returns the enumeration value for the specified parameter <code>name</code>. If no corresponding enum
     * value is found for the given parameter, an {@link IllegalArgumentException} is thrown.
     *
     * @throws IllegalArgumentException if no corresponding enum value is found
     *
     * @generated
     */
    public static final Marker getExistingValueByName(String name) {
        for (Marker currentValue : values()) {
            if (currentValue.name.equals(name)) {
                return currentValue;
            }
        }
        throw new IllegalArgumentException("No enum value with name " + name);
    }

    /**
     * Returns <code>true</code> if the provided parameter value identifies a value of this enumeration.
     *
     * @generated
     */
    public static final boolean isValueById(String id) {
        return getValueById(id) != null;
    }

    /**
     * Returns <code>true</code> if the provided parameter value identifies a value of this enumeration.
     *
     * @generated
     */
    public static final boolean isValueByName(String name) {
        return getValueByName(name) != null;
    }


    /**
     * Returns the value of the attribute id.
     * 
     * @generated
     */
    @IpsEnumAttribute(name = "id", identifier = true, unique = true)
    public String getId() {
        return id;
    }

    /**
     * Returns the value of the attribute name.
     * 
     * @generated
     */
    @IpsEnumAttribute(name = "name", unique = true, displayName = true)
    public String getName() {
        return name;
    }

    /**
     * Returns the value of the attribute requiredInformationMissing.
     * 
     * @generated
     */
    @IpsEnumAttribute(name = "requiredInformationMissing")
    public boolean isRequiredInformationMissing() {
        return requiredInformationMissing;
    }

    /**
     * Returns the value of the attribute technicalConstraintViolated.
     * 
     * @generated
     */
    @IpsEnumAttribute(name = "technicalConstraintViolated")
    public boolean isTechnicalConstraintViolated() {
        return technicalConstraintViolated;
    }

    /**
     * {@inheritDoc}
     *
     * @generated
     */
    @Override
    public String toString() {
        return "Marker: " + id + '(' + name + ')';
    }
}
