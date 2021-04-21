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
import org.faktorips.valueset.DecimalRange;
import org.faktorips.valueset.OrderedValueSet;
import org.faktorips.valueset.UnrestrictedValueSet;
import org.faktorips.valueset.ValueSet;
import org.w3c.dom.Element;

/**
 * Implementation for IpsModelObject.
 *
 * @generated
 */
@IpsPolicyCmptType(name = "IpsModelObject")
@IpsAttributes({ "decimal", "string", "unrestrictedInclNull", "unrestrictedExclNull", "emptyValueSet" })
@IpsValidationRules({ "checkDecimal" })
@IpsDocumented(bundleName = "org.linkki.samples.playground.ips.model.model-label-and-descriptions", defaultLocale = "en")
public class IpsModelObject extends AbstractModelObject {

    /**
     * The name of the Faktor-IPS package of this policy component type.
     *
     * @generated
     */
    public static final String IPS_PACKAGE = "";

    /**
     * The unqualified name of this policy component type.
     *
     * @generated
     */
    public static final String SIMPLE_NAME = "IpsModelObject";

    /**
     * The qualified name of this policy component type.
     *
     * @generated
     */
    public static final String QUALIFIED_NAME = IPS_PACKAGE + '.' + SIMPLE_NAME;

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
     * The name of the property string.
     *
     * @generated
     */
    public static final String PROPERTY_STRING = "string";


    /**
     * The name of the property unrestrictedInclNull.
     *
     * @generated
     */
    public static final String PROPERTY_UNRESTRICTEDINCLNULL = "unrestrictedInclNull";

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
     * Member variable for decimal.
     *
     * @generated
     */
    private Decimal decimal = Decimal.valueOf("42");
    /**
     * Member variable for string.
     *
     * @generated
     */
    private String string = null;


    /**
     * Member variable for unrestrictedInclNull.
     *
     * @generated
     */
    private String unrestrictedInclNull = "not required";

    /**
     * Member variable for unrestrictedExclNull.
     *
     * @generated
     */
    private String unrestrictedExclNull = "required";

    /**
     * Member variable for emptyValueSet.
     *
     * @generated
     */
    private Marker emptyValueSet = null;


    /**
     * Creates a new IpsModelObject.
     *
     * @generated
     */
    public IpsModelObject() {
        super();
    }


    /**
     * Returns the range of allowed values for the property decimal.
     *
     * @generated
     * @param context validation context
     */
    @IpsAllowedValues("decimal")
    public DecimalRange getRangeForDecimal(IValidationContext context) {
        return MAX_ALLOWED_RANGE_FOR_DECIMAL;
    }

    /**
     * Returns the decimal.
     *
     * @generated
     */
    @IpsAttribute(name = "decimal", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.Range)
    public Decimal getDecimal() {
        return decimal;
    }

    /**
     * Sets the value of attribute decimal.
     *
     * @generated
     */
    @IpsAttributeSetter("decimal")
    public void setDecimal(Decimal newValue) {
        this.decimal = newValue;
    }

    /**
     * Returns the string.
     *
     * @generated
     */
    @IpsAttribute(name = "string", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.AllValues)
    public String getString() {
        return string;
    }

    /**
     * Sets the value of attribute string.
     *
     * @generated
     */
    @IpsAttributeSetter("string")
    public void setString(String newValue) {
        this.string = newValue;
    }


    /**
     * Returns the unrestrictedInclNull.
     *
     * @generated
     */
    @IpsAttribute(name = "unrestrictedInclNull", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.AllValues)
    public String getUnrestrictedInclNull() {
        return unrestrictedInclNull;
    }


    /**
     * Sets the value of attribute unrestrictedInclNull.
     *
     * @generated
     */
    @IpsAttributeSetter("unrestrictedInclNull")
    public void setUnrestrictedInclNull(String newValue) {
        this.unrestrictedInclNull = newValue;
    }


    /**
     * Returns the set of allowed values for the property unrestrictedExclNull.
     *
     * @generated
     * @param context validation context
     */
    @IpsAllowedValues("unrestrictedExclNull")
    public ValueSet<String> getSetOfAllowedValuesForUnrestrictedExclNull(IValidationContext context) {
        return MAX_ALLOWED_VALUES_FOR_UNRESTRICTED_EXCL_NULL;
    }


    /**
     * Returns the unrestrictedExclNull.
     *
     * @generated
     */
    @IpsAttribute(name = "unrestrictedExclNull", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.AllValues)
    public String getUnrestrictedExclNull() {
        return unrestrictedExclNull;
    }


    /**
     * Sets the value of attribute unrestrictedExclNull.
     *
     * @generated
     */
    @IpsAttributeSetter("unrestrictedExclNull")
    public void setUnrestrictedExclNull(String newValue) {
        this.unrestrictedExclNull = newValue;
    }


    /**
     * Returns the set of allowed values for the property emptyValueSet.
     *
     * @generated
     * @param context validation context
     */
    @IpsAllowedValues("emptyValueSet")
    public OrderedValueSet<Marker> getAllowedValuesForEmptyValueSet(IValidationContext context) {
        return MAX_ALLOWED_VALUES_FOR_EMPTY_VALUE_SET;
    }


    /**
     * Returns the emptyValueSet.
     *
     * @generated
     */
    @IpsAttribute(name = "emptyValueSet", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.Enum)
    public Marker getEmptyValueSet() {
        return emptyValueSet;
    }


    /**
     * Sets the value of attribute emptyValueSet.
     *
     * @generated
     */
    @IpsAttributeSetter("emptyValueSet")
    public void setEmptyValueSet(Marker newValue) {
        this.emptyValueSet = newValue;
    }


    /**
     * Initializes the object with the configured defaults.
     *
     * @restrainedmodifiable
     */
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
    protected void initPropertiesFromXml(Map<String, String> propMap, IRuntimeRepository productRepository) {
        super.initPropertiesFromXml(propMap, productRepository);
        doInitDecimal(propMap);
        doInitString(propMap);
        doInitUnrestrictedInclNull(propMap);
        doInitUnrestrictedExclNull(propMap);
        doInitEmptyValueSet(propMap);
    }

    /**
     * @generated
     */
    private void doInitDecimal(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_DECIMAL)) {
            this.decimal = Decimal.valueOf(propMap.get(PROPERTY_DECIMAL));
        }
    }

    /**
     * @generated
     */
    private void doInitString(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_STRING)) {
            this.string = propMap.get(PROPERTY_STRING);
        }
    }

    /**
     * @generated
     */
    private void doInitUnrestrictedInclNull(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_UNRESTRICTEDINCLNULL)) {
            this.unrestrictedInclNull = propMap.get(PROPERTY_UNRESTRICTEDINCLNULL);
        }
    }


    /**
     * @generated
     */
    private void doInitUnrestrictedExclNull(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_UNRESTRICTEDEXCLNULL)) {
            this.unrestrictedExclNull = propMap.get(PROPERTY_UNRESTRICTEDEXCLNULL);
        }
    }


    /**
     * @generated
     */
    private void doInitEmptyValueSet(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_EMPTYVALUESET)) {
            this.emptyValueSet = IpsStringUtils.isEmpty(propMap.get(PROPERTY_EMPTYVALUESET)) ? null
                    : Marker.getValueById(propMap.get(PROPERTY_EMPTYVALUESET));
        }
    }


    /**
     * {@inheritDoc}
     *
     * @generated
     */
    @Override
    protected AbstractModelObject createChildFromXml(Element childEl) {
        AbstractModelObject newChild = super.createChildFromXml(childEl);
        if (newChild != null) {
            return newChild;
        }
        return null;
    }


    /**
     * Validates the object (but not its children). Returns <code>true</code> if this object should continue
     * validating, <code>false</code> otherwise.
     *
     * @generated
     */
    @Override
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
    public void validateDependants(MessageList ml, IValidationContext context) {
        super.validateDependants(ml, context);
    }


    /**
     * Executes the rule checkDecimal and adds a message to the given list if the object is invalid.
     * 
     * @param ml      list to which validation errors are added
     * @param context the validation context
     * @return <code>true</code>, if the validation should be continued, <code>false</code> if it should be
     *         stopped after processing this rule.
     *
     * @restrainedmodifiable
     */
    @IpsValidationRule(name = "checkDecimal", msgCode = MSG_CODE_CHECK_DECIMAL, severity = Severity.ERROR)
    protected boolean checkDecimal(MessageList ml, IValidationContext context) {
        if (!getRangeForDecimal(context).contains(getDecimal())) {

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
    protected Message createMessageForRuleCheckDecimal(IValidationContext context, Object range, Object actual) {
        List<ObjectProperty> invalidObjectProperties = Arrays.asList(
                                                                     new ObjectProperty(this, PROPERTY_DECIMAL));
        MsgReplacementParameter[] replacementParameters = new MsgReplacementParameter[] {
                new MsgReplacementParameter("range", range),
                new MsgReplacementParameter("actual", actual)
        };
        MessagesHelper messageHelper = new MessagesHelper(
                "org.linkki.samples.playground.ips.model.internal.validation-messages",
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
