package org.linkki.ips.test.model;

import org.faktorips.runtime.model.annotation.IpsPolicyCmptType;
import org.faktorips.runtime.model.annotation.IpsAttributes;
import org.faktorips.runtime.model.annotation.IpsDocumented;
import org.faktorips.runtime.internal.AbstractModelObject;
import org.faktorips.valueset.ValueSet;
import org.faktorips.valueset.UnrestrictedValueSet;
import org.faktorips.valueset.OrderedValueSet;
import java.time.MonthDay;
import org.faktorips.runtime.model.annotation.IpsAttribute;
import org.faktorips.runtime.model.type.AttributeKind;
import org.faktorips.runtime.model.type.ValueSetKind;
import org.faktorips.runtime.model.annotation.IpsAttributeSetter;
import org.faktorips.runtime.model.annotation.IpsAllowedValues;
import java.util.Map;
import org.faktorips.runtime.IRuntimeRepository;
import org.faktorips.runtime.internal.IpsStringUtils;
import org.w3c.dom.Element;
import org.faktorips.runtime.MessageList;
import org.faktorips.runtime.IValidationContext;

/**
 * Implementation for TestIpsObject.
 *
 * @generated
 */
@IpsPolicyCmptType(name = "TestIpsObject")
@IpsAttributes({ "foo", "valueSetInclNull", "valueSetExclNull", "emptyValueSet" })
@IpsDocumented(bundleName = "org.linkki.ips.test.model.testmodel-label-and-descriptions", defaultLocale = "en")
public class TestIpsObject extends AbstractModelObject {


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
    public static final String SIMPLE_NAME = "TestIpsObject";


    /**
     * The qualified name of this policy component type.
     *
     * @generated
     */
    public static final String QUALIFIED_NAME = IPS_PACKAGE + '.' + SIMPLE_NAME;


    /**
     * The name of the property foo.
     *
     * @generated
     */
    public static final String PROPERTY_FOO = "foo";


    /**
     * The name of the property valueSetInclNull.
     *
     * @generated
     */
    public static final String PROPERTY_VALUESETINCLNULL = "valueSetInclNull";


    /**
     * The name of the property valueSetExclNull.
     *
     * @generated
     */
    public static final String PROPERTY_VALUESETEXCLNULL = "valueSetExclNull";


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
    public static final OrderedValueSet<MonthDay> MAX_ALLOWED_VALUES_FOR_EMPTY_VALUE_SET = new OrderedValueSet<>(false,
            null);


    /**
     * Member variable for foo.
     *
     * @generated
     */
    private String foo = null;


    /**
     * Member variable for valueSetInclNull.
     *
     * @generated
     */
    private Boolean valueSetInclNull = null;


    /**
     * Member variable for valueSetExclNull.
     *
     * @generated
     */
    private boolean valueSetExclNull = true;


    /**
     * Member variable for emptyValueSet.
     *
     * @generated
     */
    private MonthDay emptyValueSet = null;


    /**
     * Creates a new TestIpsObject.
     *
     * @generated
     */
    public TestIpsObject() {
        super();
    }


    /**
     * Returns the foo.
     *
     * @generated
     */
    @IpsAttribute(name = "foo", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.AllValues)
    public String getFoo() {
        return foo;
    }

    /**
     * Sets the value of attribute foo.
     *
     * @generated
     */
    @IpsAttributeSetter("foo")
    public void setFoo(String newValue) {
        this.foo = newValue;
    }


    /**
     * Returns the valueSetInclNull.
     *
     * @generated
     */
    @IpsAttribute(name = "valueSetInclNull", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.AllValues)
    public Boolean getValueSetInclNull() {
        return valueSetInclNull;
    }


    /**
     * Sets the value of attribute valueSetInclNull.
     *
     * @generated
     */
    @IpsAttributeSetter("valueSetInclNull")
    public void setValueSetInclNull(Boolean newValue) {
        this.valueSetInclNull = newValue;
    }


    /**
     * Returns the set of allowed values for the property valueSetExclNull.
     *
     * @restrainedmodifiable
     */
    @IpsAllowedValues("valueSetExclNull")
    public ValueSet<Boolean> getSetOfAllowedValuesForValueSetExclNull(IValidationContext context) {
        // begin-user-code
        return new UnrestrictedValueSet<Boolean>(false);
        // end-user-code
    }


    /**
     * Returns the valueSetExclNull.
     *
     * @generated
     */
    @IpsAttribute(name = "valueSetExclNull", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.AllValues)
    public boolean isValueSetExclNull() {
        return valueSetExclNull;
    }


    /**
     * Sets the value of attribute valueSetExclNull.
     *
     * @generated
     */
    @IpsAttributeSetter("valueSetExclNull")
    public void setValueSetExclNull(boolean newValue) {
        this.valueSetExclNull = newValue;
    }


    /**
     * Returns the set of allowed values for the property emptyValueSet.
     *
     * @generated
     */
    @IpsAllowedValues("emptyValueSet")
    public OrderedValueSet<MonthDay> getAllowedValuesForEmptyValueSet(IValidationContext context) {
        return MAX_ALLOWED_VALUES_FOR_EMPTY_VALUE_SET;
    }


    /**
     * Returns the emptyValueSet.
     *
     * @generated
     */
    @IpsAttribute(name = "emptyValueSet", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.Enum)
    public MonthDay getEmptyValueSet() {
        return emptyValueSet;
    }


    /**
     * Sets the value of attribute emptyValueSet.
     *
     * @generated
     */
    @IpsAttributeSetter("emptyValueSet")
    public void setEmptyValueSet(MonthDay newValue) {
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
        doInitFoo(propMap);
        doInitValueSetInclNull(propMap);
        doInitValueSetExclNull(propMap);
        doInitEmptyValueSet(propMap);
    }

    /**
     * @generated
     */
    private void doInitFoo(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_FOO)) {
            this.foo = propMap.get(PROPERTY_FOO);
        }
    }

    /**
     * @generated
     */
    private void doInitValueSetInclNull(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_VALUESETINCLNULL)) {
            this.valueSetInclNull = IpsStringUtils.isEmpty(propMap.get(PROPERTY_VALUESETINCLNULL)) ? null
                    : Boolean.valueOf(propMap.get(PROPERTY_VALUESETINCLNULL));
        }
    }


    /**
     * @generated
     */
    private void doInitValueSetExclNull(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_VALUESETEXCLNULL)) {
            this.valueSetExclNull = Boolean.valueOf(propMap.get(PROPERTY_VALUESETEXCLNULL)).booleanValue();
        }
    }


    /**
     * @generated
     */
    private void doInitEmptyValueSet(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_EMPTYVALUESET)) {
            this.emptyValueSet = IpsStringUtils.isEmpty(propMap.get(PROPERTY_EMPTYVALUESET)) ? null
                    : MonthDay.parse(propMap.get(PROPERTY_EMPTYVALUESET));
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


}
