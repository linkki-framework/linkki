/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.samples.playground.ips.model;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.faktorips.runtime.IRuntimeRepository;
import org.faktorips.runtime.IValidationContext;
import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;
import org.faktorips.runtime.MsgReplacementParameter;
import org.faktorips.runtime.ObjectProperty;
import org.faktorips.runtime.Severity;
import org.faktorips.runtime.annotation.IpsGenerated;
import org.faktorips.runtime.internal.AbstractModelObject;
import org.faktorips.runtime.internal.IpsStringUtils;
import org.faktorips.runtime.model.annotation.IpsAllowedValues;
import org.faktorips.runtime.model.annotation.IpsAttribute;
import org.faktorips.runtime.model.annotation.IpsAttributeSetter;
import org.faktorips.runtime.model.annotation.IpsAttributes;
import org.faktorips.runtime.model.annotation.IpsDocumented;
import org.faktorips.runtime.model.annotation.IpsPolicyCmptType;
import org.faktorips.runtime.model.annotation.IpsValidationRule;
import org.faktorips.runtime.model.annotation.IpsValidationRules;
import org.faktorips.runtime.model.type.AttributeKind;
import org.faktorips.runtime.model.type.ValueSetKind;
import org.faktorips.runtime.util.MessagesHelper;
import org.faktorips.values.Decimal;
import org.faktorips.runtime.model.annotation.IpsDefaultValue;
import org.faktorips.valueset.DecimalRange;
import org.faktorips.valueset.OrderedValueSet;
import org.faktorips.valueset.IntegerRange;
import org.faktorips.valueset.UnrestrictedValueSet;
import org.faktorips.valueset.ValueSet;
import org.w3c.dom.Element;

/**
 * Implementation for IpsModelObject.
 *
 * @generated
 */
@IpsPolicyCmptType(name = "IpsModelObject")
@IpsAttributes({ "decimal", "string", "unrestrictedInclNull", "unrestrictedExclNull", "emptyValueSet",
        "enumerationValueSet", "integerEnumerationValueSet", "integerRangeValueSet", "booleanValueSet",
        "emptyStringValueSet" })
@IpsValidationRules({ "checkDecimal" })
@IpsDocumented(bundleName = "org.linkki.samples.playground.ips.model.model-label-and-descriptions",
        defaultLocale = "en")
public class IpsModelObject extends AbstractModelObject {

    /**
     * Error code for rule checkDecimal.
     *
     * @generated
     */
    public static final String MSG_CODE_CHECK_DECIMAL = "INVALID_DECIMAL";

    /**
     * The name of the property decimal.
     *
     * @generated
     */
    public static final String PROPERTY_DECIMAL = "decimal";
    /**
     * Max allowed range for the property decimal.
     *
     * @generated
     */
    public static final DecimalRange MAX_ALLOWED_RANGE_FOR_DECIMAL = DecimalRange
            .valueOf(Decimal.valueOf("0"), Decimal.valueOf("100"), Decimal.valueOf("0.5"), false);
    /**
     * The default value for decimal.
     *
     * @generated
     */
    @IpsDefaultValue("decimal")
    public static final Decimal DEFAULT_VALUE_FOR_DECIMAL = Decimal.valueOf("42");

    /**
     * The name of the property string.
     *
     * @generated
     */
    // tag::PROPERTY_STRING[]
    public static final String PROPERTY_STRING = "string";


    /**
     * Max allowed values for property string.
     *
     * @generated
     */
    public static final ValueSet<String> MAX_ALLOWED_VALUES_FOR_STRING = new UnrestrictedValueSet<>(true);

    /**
     * The default value for string.
     *
     * @generated
     */
    @IpsDefaultValue("string")
    public static final String DEFAULT_VALUE_FOR_STRING = "";

    /**
     * The name of the property unrestrictedInclNull.
     *
     * @generated
     */
    public static final String PROPERTY_UNRESTRICTEDINCLNULL = "unrestrictedInclNull";

    /**
     * Max allowed values for property unrestrictedInclNull.
     *
     * @generated
     */
    public static final ValueSet<String> MAX_ALLOWED_VALUES_FOR_UNRESTRICTED_INCL_NULL = new UnrestrictedValueSet<>(
            true);

    /**
     * The default value for unrestrictedInclNull.
     *
     * @generated
     */
    @IpsDefaultValue("unrestrictedInclNull")
    public static final String DEFAULT_VALUE_FOR_UNRESTRICTED_INCL_NULL = "not required";

    /**
     * The name of the property unrestrictedExclNull.
     *
     * @generated
     */
    public static final String PROPERTY_UNRESTRICTEDEXCLNULL = "unrestrictedExclNull";

    /**
     * Max allowed values for property unrestrictedExclNull.
     *
     * @generated
     */
    public static final ValueSet<String> MAX_ALLOWED_VALUES_FOR_UNRESTRICTED_EXCL_NULL = new UnrestrictedValueSet<>(
            false);

    /**
     * The default value for unrestrictedExclNull.
     *
     * @generated
     */
    @IpsDefaultValue("unrestrictedExclNull")
    public static final String DEFAULT_VALUE_FOR_UNRESTRICTED_EXCL_NULL = "required";

    /**
     * The name of the property emptyValueSet.
     *
     * @generated
     */
    public static final String PROPERTY_EMPTYVALUESET = "emptyValueSet";

    /**
     * Max allowed values for property emptyValueSet.
     *
     * @generated
     */
    public static final OrderedValueSet<Marker> MAX_ALLOWED_VALUES_FOR_EMPTY_VALUE_SET = new OrderedValueSet<>(false,
            null);
    /**
     * The default value for emptyValueSet.
     *
     * @generated
     */
    @IpsDefaultValue("emptyValueSet")
    public static final Marker DEFAULT_VALUE_FOR_EMPTY_VALUE_SET = null;

    /**
     * The name of the property enumerationValueSet.
     *
     * @generated
     */
    public static final String PROPERTY_ENUMERATIONVALUESET = "enumerationValueSet";

    /**
     * Max allowed values for property enumerationValueSet.
     *
     * @generated
     */
    public static final OrderedValueSet<Marker> MAX_ALLOWED_VALUES_FOR_ENUMERATION_VALUE_SET = new OrderedValueSet<>(
            false, null, Marker.REQUIRED_INFORMATION_MISSING, Marker.TECHNICAL_CONSTRAINT_VIOLATED);

    /**
     * The default value for enumerationValueSet.
     *
     * @generated
     */
    @IpsDefaultValue("enumerationValueSet")
    public static final Marker DEFAULT_VALUE_FOR_ENUMERATION_VALUE_SET = null;

    /**
     * The name of the property integerEnumerationValueSet.
     *
     * @generated
     */
    public static final String PROPERTY_INTEGERENUMERATIONVALUESET = "integerEnumerationValueSet";

    /**
     * Max allowed values for property integerEnumerationValueSet.
     *
     * @generated
     */
    public static final OrderedValueSet<Integer> MAX_ALLOWED_VALUES_FOR_INTEGER_ENUMERATION_VALUE_SET = new OrderedValueSet<>(
            true, null, Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4),
            Integer.valueOf(5), null);

    /**
     * The default value for integerEnumerationValueSet.
     *
     * @generated
     */
    @IpsDefaultValue("integerEnumerationValueSet")
    public static final Integer DEFAULT_VALUE_FOR_INTEGER_ENUMERATION_VALUE_SET = null;

    /**
     * The name of the property integerRangeValueSet.
     *
     * @generated
     */
    public static final String PROPERTY_INTEGERRANGEVALUESET = "integerRangeValueSet";

    /**
     * Max allowed range for the property integerRangeValueSet.
     *
     * @generated
     */
    public static final IntegerRange MAX_ALLOWED_RANGE_FOR_INTEGER_RANGE_VALUE_SET = IntegerRange
            .valueOf(Integer.valueOf("0"), Integer.valueOf(100), Integer.valueOf(5), true);

    /**
     * The default value for integerRangeValueSet.
     *
     * @generated
     */
    @IpsDefaultValue("integerRangeValueSet")
    public static final Integer DEFAULT_VALUE_FOR_INTEGER_RANGE_VALUE_SET = null;

    /**
     * The name of the property booleanValueSet.
     *
     * @generated
     */
    public static final String PROPERTY_BOOLEANVALUESET = "booleanValueSet";

    /**
     * Max allowed values for property booleanValueSet.
     *
     * @generated
     */
    public static final ValueSet<Boolean> MAX_ALLOWED_VALUES_FOR_BOOLEAN_VALUE_SET = new UnrestrictedValueSet<>(false);

    /**
     * The default value for booleanValueSet.
     *
     * @generated
     */
    @IpsDefaultValue("booleanValueSet")
    public static final Boolean DEFAULT_VALUE_FOR_BOOLEAN_VALUE_SET = null;

    /**
     * The name of the property emptyStringValueSet.
     *
     * @generated
     */
    public static final String PROPERTY_EMPTYSTRINGVALUESET = "emptyStringValueSet";

    /**
     * Max allowed values for property emptyStringValueSet.
     *
     * @generated
     */
    public static final OrderedValueSet<String> MAX_ALLOWED_VALUES_FOR_EMPTY_STRING_VALUE_SET = new OrderedValueSet<>(
            false, "");

    /**
     * The default value for emptyStringValueSet.
     *
     * @generated
     */
    @IpsDefaultValue("emptyStringValueSet")
    public static final String DEFAULT_VALUE_FOR_EMPTY_STRING_VALUE_SET = "";

    // end::PROPERTY_STRING[]

    /**
     * Member variable for decimal.
     *
     * @generated
     */
    private Decimal decimal = DEFAULT_VALUE_FOR_DECIMAL;
    /**
     * Member variable for string.
     *
     * @generated
     */
    private String string = DEFAULT_VALUE_FOR_STRING;


    /**
     * Member variable for unrestrictedInclNull.
     *
     * @generated
     */
    private String unrestrictedInclNull = DEFAULT_VALUE_FOR_UNRESTRICTED_INCL_NULL;

    /**
     * Member variable for unrestrictedExclNull.
     *
     * @generated
     */
    private String unrestrictedExclNull = DEFAULT_VALUE_FOR_UNRESTRICTED_EXCL_NULL;

    /**
     * Member variable for emptyValueSet.
     *
     * @generated
     */
    private Marker emptyValueSet = DEFAULT_VALUE_FOR_EMPTY_VALUE_SET;


    /**
     * Member variable for enumerationValueSet.
     *
     * @generated
     */
    private Marker enumerationValueSet = DEFAULT_VALUE_FOR_ENUMERATION_VALUE_SET;

    /**
     * Member variable for integerEnumerationValueSet.
     *
     * @generated
     */
    private Integer integerEnumerationValueSet = DEFAULT_VALUE_FOR_INTEGER_ENUMERATION_VALUE_SET;

    /**
     * Member variable for integerRangeValueSet.
     *
     * @generated
     */
    private Integer integerRangeValueSet = DEFAULT_VALUE_FOR_INTEGER_RANGE_VALUE_SET;

    /**
     * Member variable for booleanValueSet.
     *
     * @generated
     */
    private Boolean booleanValueSet = DEFAULT_VALUE_FOR_BOOLEAN_VALUE_SET;

    /**
     * Member variable for emptyStringValueSet.
     *
     * @generated
     */
    private String emptyStringValueSet = DEFAULT_VALUE_FOR_EMPTY_STRING_VALUE_SET;

    /**
     * Creates a new IpsModelObject.
     *
     * @generated
     */
    @IpsGenerated
    public IpsModelObject() {
        super();
    }


    /**
     * Returns the range of allowed values for the property decimal.
     *
     * @generated
     */
    @IpsAllowedValues("decimal")
    @IpsGenerated
    public ValueSet<Decimal> getAllowedValuesForDecimal() {
        return MAX_ALLOWED_RANGE_FOR_DECIMAL;
    }


    /**
     * Returns the decimal.
     *
     * @generated
     */
    @IpsAttribute(name = "decimal", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.Range)
    @IpsGenerated
    public Decimal getDecimal() {
        return decimal;
    }

    /**
     * Sets the value of attribute decimal.
     *
     * @generated
     */
    @IpsAttributeSetter("decimal")
    @IpsGenerated
    public void setDecimal(Decimal newValue) {
        this.decimal = newValue;
    }

    /**
     * Returns the set of allowed values for the property string.
     *
     * @generated
     */
    @IpsAllowedValues("string")
    @IpsGenerated
    public ValueSet<String> getAllowedValuesForString() {
        return MAX_ALLOWED_VALUES_FOR_STRING;
    }


    /**
     * Returns the string.
     *
     * @generated
     */
    // tag::getString[]
    @IpsAttribute(name = "string", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.AllValues)
    @IpsGenerated
    public String getString() {
        return string;
    }
    // end::getString[]

    /**
     * Sets the value of attribute string.
     *
     * @generated
     */
    @IpsAttributeSetter("string")
    @IpsGenerated
    public void setString(String newValue) {
        this.string = newValue;
    }


    /**
     * Returns the set of allowed values for the property unrestrictedInclNull.
     *
     * @generated
     */
    @IpsAllowedValues("unrestrictedInclNull")
    @IpsGenerated
    public ValueSet<String> getAllowedValuesForUnrestrictedInclNull() {
        return MAX_ALLOWED_VALUES_FOR_UNRESTRICTED_INCL_NULL;
    }


    /**
     * Returns the unrestrictedInclNull.
     *
     * @generated
     */
    @IpsAttribute(name = "unrestrictedInclNull", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.AllValues)
    @IpsGenerated
    public String getUnrestrictedInclNull() {
        return unrestrictedInclNull;
    }


    /**
     * Sets the value of attribute unrestrictedInclNull.
     *
     * @generated
     */
    @IpsAttributeSetter("unrestrictedInclNull")
    @IpsGenerated
    public void setUnrestrictedInclNull(String newValue) {
        this.unrestrictedInclNull = newValue;
    }


    /**
     * Returns the set of allowed values for the property unrestrictedExclNull.
     *
     * @generated
     */
    @IpsAllowedValues("unrestrictedExclNull")
    @IpsGenerated
    public ValueSet<String> getAllowedValuesForUnrestrictedExclNull() {
        return MAX_ALLOWED_VALUES_FOR_UNRESTRICTED_EXCL_NULL;
    }


    /**
     * Returns the unrestrictedExclNull.
     *
     * @generated
     */
    @IpsAttribute(name = "unrestrictedExclNull", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.AllValues)
    @IpsGenerated
    public String getUnrestrictedExclNull() {
        return unrestrictedExclNull;
    }


    /**
     * Sets the value of attribute unrestrictedExclNull.
     *
     * @generated
     */
    @IpsAttributeSetter("unrestrictedExclNull")
    @IpsGenerated
    public void setUnrestrictedExclNull(String newValue) {
        this.unrestrictedExclNull = newValue;
    }


    /**
     * Returns the set of allowed values for the property emptyValueSet.
     *
     * @generated
     */
    @IpsAllowedValues("emptyValueSet")
    @IpsGenerated
    public ValueSet<Marker> getAllowedValuesForEmptyValueSet() {
        return MAX_ALLOWED_VALUES_FOR_EMPTY_VALUE_SET;
    }


    /**
     * Returns the emptyValueSet.
     *
     * @generated
     */
    @IpsAttribute(name = "emptyValueSet", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.Enum)
    @IpsGenerated
    public Marker getEmptyValueSet() {
        return emptyValueSet;
    }


    /**
     * Sets the value of attribute emptyValueSet.
     *
     * @generated
     */
    @IpsAttributeSetter("emptyValueSet")
    @IpsGenerated
    public void setEmptyValueSet(Marker newValue) {
        this.emptyValueSet = newValue;
    }


    /**
     * Returns the set of allowed values for the property enumerationValueSet.
     *
     * @generated
     */
    @IpsAllowedValues("enumerationValueSet")
    @IpsGenerated
    public ValueSet<Marker> getAllowedValuesForEnumerationValueSet() {
        return MAX_ALLOWED_VALUES_FOR_ENUMERATION_VALUE_SET;
    }


    /**
     * Returns the enumerationValueSet.
     *
     * @generated
     */
    @IpsAttribute(name = "enumerationValueSet", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.Enum)
    @IpsGenerated
    public Marker getEnumerationValueSet() {
        return enumerationValueSet;
    }


    /**
     * Sets the value of attribute enumerationValueSet.
     *
     * @generated
     */
    @IpsAttributeSetter("enumerationValueSet")
    @IpsGenerated
    public void setEnumerationValueSet(Marker newValue) {
        this.enumerationValueSet = newValue;
    }


    /**
     * Returns the set of allowed values for the property integerEnumerationValueSet.
     *
     * @generated
     */
    @IpsAllowedValues("integerEnumerationValueSet")
    @IpsGenerated
    public ValueSet<Integer> getAllowedValuesForIntegerEnumerationValueSet() {
        return MAX_ALLOWED_VALUES_FOR_INTEGER_ENUMERATION_VALUE_SET;
    }


    /**
     * Returns the integerEnumerationValueSet.
     *
     * @generated
     */
    @IpsAttribute(name = "integerEnumerationValueSet", kind = AttributeKind.CHANGEABLE,
            valueSetKind = ValueSetKind.Enum)
    @IpsGenerated
    public Integer getIntegerEnumerationValueSet() {
        return integerEnumerationValueSet;
    }


    /**
     * Sets the value of attribute integerEnumerationValueSet.
     *
     * @generated
     */
    @IpsAttributeSetter("integerEnumerationValueSet")
    @IpsGenerated
    public void setIntegerEnumerationValueSet(Integer newValue) {
        this.integerEnumerationValueSet = newValue;
    }


    /**
     * Returns the range of allowed values for the property integerRangeValueSet.
     *
     * @generated
     */
    @IpsAllowedValues("integerRangeValueSet")
    @IpsGenerated
    public ValueSet<Integer> getAllowedValuesForIntegerRangeValueSet() {
        return MAX_ALLOWED_RANGE_FOR_INTEGER_RANGE_VALUE_SET;
    }


    /**
     * Returns the integerRangeValueSet.
     *
     * @generated
     */
    @IpsAttribute(name = "integerRangeValueSet", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.Range)
    @IpsGenerated
    public Integer getIntegerRangeValueSet() {
        return integerRangeValueSet;
    }


    /**
     * Sets the value of attribute integerRangeValueSet.
     *
     * @generated
     */
    @IpsAttributeSetter("integerRangeValueSet")
    @IpsGenerated
    public void setIntegerRangeValueSet(Integer newValue) {
        this.integerRangeValueSet = newValue;
    }


    /**
     * Returns the set of allowed values for the property booleanValueSet.
     *
     * @generated
     */
    @IpsAllowedValues("booleanValueSet")
    @IpsGenerated
    public ValueSet<Boolean> getAllowedValuesForBooleanValueSet() {
        return MAX_ALLOWED_VALUES_FOR_BOOLEAN_VALUE_SET;
    }


    /**
     * Returns the booleanValueSet.
     *
     * @generated
     */
    @IpsAttribute(name = "booleanValueSet", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.AllValues)
    @IpsGenerated
    public Boolean getBooleanValueSet() {
        return booleanValueSet;
    }


    /**
     * Sets the value of attribute booleanValueSet.
     *
     * @generated
     */
    @IpsAttributeSetter("booleanValueSet")
    @IpsGenerated
    public void setBooleanValueSet(Boolean newValue) {
        this.booleanValueSet = newValue;
    }


    /**
     * Returns the set of allowed values for the property emptyStringValueSet.
     *
     * @generated
     */
    @IpsAllowedValues("emptyStringValueSet")
    @IpsGenerated
    public ValueSet<String> getAllowedValuesForEmptyStringValueSet() {
        return MAX_ALLOWED_VALUES_FOR_EMPTY_STRING_VALUE_SET;
    }


    /**
     * Returns the emptyStringValueSet.
     *
     * @generated
     */
    @IpsAttribute(name = "emptyStringValueSet", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.Enum)
    @IpsGenerated
    public String getEmptyStringValueSet() {
        return emptyStringValueSet;
    }


    /**
     * Sets the value of attribute emptyStringValueSet.
     *
     * @generated
     */
    @IpsAttributeSetter("emptyStringValueSet")
    @IpsGenerated
    public void setEmptyStringValueSet(String newValue) {
        this.emptyStringValueSet = newValue;
    }


    /**
     * Initializes the object with the configured defaults.
     *
     * @restrainedmodifiable
     */
    @IpsGenerated
    public void initialize() {
        // begin-user-code
        // end-user-code
    }


    /**
     * {@inheritDoc}
     *
     * @generated
     */
    @Override
    @IpsGenerated
    protected void initPropertiesFromXml(Map<String, String> propMap, IRuntimeRepository productRepository) {
        super.initPropertiesFromXml(propMap, productRepository);
        doInitDecimal(propMap);
        doInitString(propMap);
        doInitUnrestrictedInclNull(propMap);
        doInitUnrestrictedExclNull(propMap);
        doInitEmptyValueSet(propMap);
        doInitEnumerationValueSet(propMap);
        doInitIntegerEnumerationValueSet(propMap);
        doInitIntegerRangeValueSet(propMap);
        doInitBooleanValueSet(propMap);
        doInitEmptyStringValueSet(propMap);
    }

    /**
     * @generated
     */
    @IpsGenerated
    private void doInitDecimal(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_DECIMAL)) {
            this.decimal = Decimal.valueOf(propMap.get(PROPERTY_DECIMAL));
        }
    }

    /**
     * @generated
     */
    @IpsGenerated
    private void doInitString(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_STRING)) {
            this.string = propMap.get(PROPERTY_STRING);
        }
    }

    /**
     * @generated
     */
    @IpsGenerated
    private void doInitUnrestrictedInclNull(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_UNRESTRICTEDINCLNULL)) {
            this.unrestrictedInclNull = propMap.get(PROPERTY_UNRESTRICTEDINCLNULL);
        }
    }


    /**
     * @generated
     */
    @IpsGenerated
    private void doInitUnrestrictedExclNull(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_UNRESTRICTEDEXCLNULL)) {
            this.unrestrictedExclNull = propMap.get(PROPERTY_UNRESTRICTEDEXCLNULL);
        }
    }


    /**
     * @generated
     */
    @IpsGenerated
    private void doInitEmptyValueSet(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_EMPTYVALUESET)) {
            this.emptyValueSet = IpsStringUtils.isEmpty(propMap.get(PROPERTY_EMPTYVALUESET)) ? null
                    : Marker.getValueById(propMap.get(PROPERTY_EMPTYVALUESET));
        }
    }


    /**
     * @generated
     */
    @IpsGenerated
    private void doInitEnumerationValueSet(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_ENUMERATIONVALUESET)) {
            this.enumerationValueSet = IpsStringUtils.isEmpty(propMap.get(PROPERTY_ENUMERATIONVALUESET)) ? null
                    : Marker.getValueById(propMap.get(PROPERTY_ENUMERATIONVALUESET));
        }
    }


    /**
     * @generated
     */
    @IpsGenerated
    private void doInitIntegerEnumerationValueSet(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_INTEGERENUMERATIONVALUESET)) {
            this.integerEnumerationValueSet = IpsStringUtils.isEmpty(propMap.get(PROPERTY_INTEGERENUMERATIONVALUESET))
                    ? null
                    : Integer.valueOf(propMap.get(PROPERTY_INTEGERENUMERATIONVALUESET));
        }
    }


    /**
     * @generated
     */
    @IpsGenerated
    private void doInitIntegerRangeValueSet(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_INTEGERRANGEVALUESET)) {
            this.integerRangeValueSet = IpsStringUtils.isEmpty(propMap.get(PROPERTY_INTEGERRANGEVALUESET)) ? null
                    : Integer.valueOf(propMap.get(PROPERTY_INTEGERRANGEVALUESET));
        }
    }


    /**
     * @generated
     */
    @IpsGenerated
    private void doInitBooleanValueSet(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_BOOLEANVALUESET)) {
            this.booleanValueSet = IpsStringUtils.isEmpty(propMap.get(PROPERTY_BOOLEANVALUESET)) ? null
                    : Boolean.valueOf(propMap.get(PROPERTY_BOOLEANVALUESET));
        }
    }


    /**
     * @generated
     */
    @IpsGenerated
    private void doInitEmptyStringValueSet(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_EMPTYSTRINGVALUESET)) {
            this.emptyStringValueSet = propMap.get(PROPERTY_EMPTYSTRINGVALUESET);
        }
    }


    /**
     * {@inheritDoc}
     *
     * @generated
     */
    @Override
    @IpsGenerated
    protected AbstractModelObject createChildFromXml(Element childEl) {
        AbstractModelObject newChild = super.createChildFromXml(childEl);
        if (newChild != null) {
            return newChild;
        }
        return null;
    }


    /**
     * Validates the object (but not its children). Returns <code>true</code> if this object should
     * continue validating, <code>false</code> otherwise.
     *
     * @generated
     */
    @Override
    @IpsGenerated
    public boolean validateSelf(MessageList ml, IValidationContext context) {
        if (!super.validateSelf(ml, context)) {
            return STOP_VALIDATION;
        }
        if (!checkDecimal(ml, context)) {
            return STOP_VALIDATION;
        }
        return CONTINUE_VALIDATION;
    }

    /**
     * Validates the object's children.
     *
     * @generated
     */
    @Override
    @IpsGenerated
    public void validateDependants(MessageList ml, IValidationContext context) {
        super.validateDependants(ml, context);
    }


    /**
     *
     * Executes the rule checkDecimal and adds a message to the given list if the object is invalid.
     *
     * @param ml list to which validation errors are added
     * @param context the validation context
     * @return <code>true</code>, if the validation should be continued, <code>false</code> if it should
     *         be stopped after processing this rule.
     *
     * @restrainedmodifiable
     */
    @IpsValidationRule(name = "checkDecimal", msgCode = MSG_CODE_CHECK_DECIMAL, severity = Severity.ERROR)
    @IpsGenerated
    protected boolean checkDecimal(MessageList ml, IValidationContext context) {
        if (getAllowedValuesForDecimal().isEmpty() && getDecimal() == Decimal.NULL) {
            return CONTINUE_VALIDATION;
        }
        if (!getAllowedValuesForDecimal().contains(getDecimal())) {

            // begin-user-code
            ml.add(createMessageForRuleCheckDecimal(context, null, null));
            // end-user-code
        }
        return CONTINUE_VALIDATION;
    }

    /**
     * Creates a message to indicate that the rule checkDecimal has found an invalid state.
     *
     * @generated
     */
    @IpsGenerated
    protected Message createMessageForRuleCheckDecimal(IValidationContext context, Object range, Object actual) {
        List<ObjectProperty> invalidObjectProperties = Arrays.asList(
                                                                     new ObjectProperty(this, PROPERTY_DECIMAL));
        MsgReplacementParameter[] replacementParameters = new MsgReplacementParameter[] {
                new MsgReplacementParameter("range", range),
                new MsgReplacementParameter("actual", actual)
        };
        MessagesHelper messageHelper = new MessagesHelper("org.linkki.samples.playground.ips.model.validation-messages",
                getClass().getClassLoader(), Locale.ENGLISH);
        String msgText = messageHelper.getMessage("IpsModelObject-checkDecimal", context.getLocale(), range, actual);

        Message.Builder builder = new Message.Builder(msgText, Severity.ERROR)
                .code(MSG_CODE_CHECK_DECIMAL)
                .invalidObjects(invalidObjectProperties)
                .replacements(replacementParameters)
                .markers(Marker.TECHNICAL_CONSTRAINT_VIOLATED);
        return builder.create();
    }

}
