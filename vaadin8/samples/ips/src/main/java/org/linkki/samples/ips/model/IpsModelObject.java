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
package org.linkki.samples.ips.model;

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
import org.w3c.dom.Element;

/**
 * Implementation for IpsModelObject.
 * 
 * @generated
 */
@IpsPolicyCmptType(name = "IpsModelObject")
@IpsAttributes({ "decimal", "string" })
@IpsValidationRules({ "checkDecimal" })
@IpsDocumented(bundleName = "org.linkki.samples.ips.model.model-label-and-descriptions", defaultLocale = "en")
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
     * The name of the property string.
     * 
     * @generated
     */
    // tag::PROPERTY_STRING[]
    public static final String PROPERTY_STRING = "string";
    // end::PROPERTY_STRING[]
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
     * @customizedAnnotations ADDED
     */
    @SuppressWarnings("unused")
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
    // tag::getString[]
    @IpsAttribute(name = "string", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.AllValues)
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
    public void setString(String newValue) {
        this.string = newValue;
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
     * Validates the object (but not it's children). Returns <code>true</code> if this object should
     * continue validating, <code>false</code> else.
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
     * @param ml list to which validation errors are added
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
            ml.add(createMessageForRuleCheckDecimal(context, getRangeForDecimal(context), getDecimal()));
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
        MessagesHelper messageHelper = new MessagesHelper("org.linkki.samples.ips.model.internal.validation-messages",
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
