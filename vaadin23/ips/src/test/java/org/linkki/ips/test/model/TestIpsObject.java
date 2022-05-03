package org.linkki.ips.test.model;

import org.faktorips.runtime.model.annotation.IpsPolicyCmptType;
import org.faktorips.runtime.model.annotation.IpsAttributes;
import org.faktorips.runtime.model.annotation.IpsDocumented;
import org.faktorips.runtime.internal.AbstractModelObject;
import org.faktorips.valueset.ValueSet;
import org.faktorips.valueset.UnrestrictedValueSet;
import org.faktorips.runtime.model.annotation.IpsDefaultValue;
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
import org.faktorips.runtime.annotation.IpsGenerated;

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
     * The name of the property foo.
     *
     * @generated
     */
    public static final String PROPERTY_FOO = "foo";


    /**
     * Max allowed values for property foo.
     *
     * @generated
     */
    public static final ValueSet<String> MAX_ALLOWED_VALUES_FOR_FOO = new UnrestrictedValueSet<>(true);


    /**
     * The default value for foo.
     *
     * @generated
     */
    @IpsDefaultValue("foo")
    public static final String DEFAULT_VALUE_FOR_FOO = null;


    /**
     * The name of the property valueSetInclNull.
     *
     * @generated
     */
    public static final String PROPERTY_VALUESETINCLNULL = "valueSetInclNull";


    /**
     * Max allowed values for property valueSetInclNull.
     *
     * @generated
     */
    public static final ValueSet<Boolean> MAX_ALLOWED_VALUES_FOR_VALUE_SET_INCL_NULL = new UnrestrictedValueSet<>(true);


    /**
     * The default value for valueSetInclNull.
     *
     * @generated
     */
    @IpsDefaultValue("valueSetInclNull")
    public static final Boolean DEFAULT_VALUE_FOR_VALUE_SET_INCL_NULL = null;


    /**
     * The name of the property valueSetExclNull.
     *
     * @generated
     */
    public static final String PROPERTY_VALUESETEXCLNULL = "valueSetExclNull";


    /**
     * Max allowed values for property valueSetExclNull.
     *
     * @generated
     */
    public static final ValueSet<Boolean> MAX_ALLOWED_VALUES_FOR_VALUE_SET_EXCL_NULL = new UnrestrictedValueSet<>(
            false);


    /**
     * The default value for valueSetExclNull.
     *
     * @generated
     */
    @IpsDefaultValue("valueSetExclNull")
    public static final boolean DEFAULT_VALUE_FOR_VALUE_SET_EXCL_NULL = true;


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
     * The default value for emptyValueSet.
     *
     * @generated
     */
    @IpsDefaultValue("emptyValueSet")
    public static final MonthDay DEFAULT_VALUE_FOR_EMPTY_VALUE_SET = null;


    /**
     * Member variable for foo.
     *
     * @generated
     */
    private String foo = DEFAULT_VALUE_FOR_FOO;


    /**
     * Member variable for valueSetInclNull.
     *
     * @generated
     */
    private Boolean valueSetInclNull = DEFAULT_VALUE_FOR_VALUE_SET_INCL_NULL;


    /**
     * Member variable for valueSetExclNull.
     *
     * @generated
     */
    private boolean valueSetExclNull = DEFAULT_VALUE_FOR_VALUE_SET_EXCL_NULL;


    /**
     * Member variable for emptyValueSet.
     *
     * @generated
     */
    private MonthDay emptyValueSet = DEFAULT_VALUE_FOR_EMPTY_VALUE_SET;


    /**
     * Creates a new TestIpsObject.
     *
     * @generated
     */
    @IpsGenerated
    public TestIpsObject() {
        super();
    }


    /**
     * Returns the set of allowed values for the property foo.
     *
     * @generated
     */
    @IpsAllowedValues("foo")
    @IpsGenerated
    public ValueSet<String> getSetOfAllowedValuesForFoo(IValidationContext context) {
        return MAX_ALLOWED_VALUES_FOR_FOO;
    }


    /**
     * Returns the foo.
     *
     * @generated
     */
    @IpsAttribute(name = "foo", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.AllValues)
    @IpsGenerated
    public String getFoo() {
        return foo;
    }

    /**
     * Sets the value of attribute foo.
     *
     * @generated
     */
    @IpsAttributeSetter("foo")
    @IpsGenerated
    public void setFoo(String newValue) {
        this.foo = newValue;
    }


    /**
     * Returns the set of allowed values for the property valueSetInclNull.
     *
     * @generated
     */
    @IpsAllowedValues("valueSetInclNull")
    @IpsGenerated
    public ValueSet<Boolean> getSetOfAllowedValuesForValueSetInclNull(IValidationContext context) {
        return MAX_ALLOWED_VALUES_FOR_VALUE_SET_INCL_NULL;
    }


    /**
     * Returns the valueSetInclNull.
     *
     * @generated
     */
    @IpsAttribute(name = "valueSetInclNull", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.AllValues)
    @IpsGenerated
    public Boolean getValueSetInclNull() {
        return valueSetInclNull;
    }


    /**
     * Sets the value of attribute valueSetInclNull.
     *
     * @generated
     */
    @IpsAttributeSetter("valueSetInclNull")
    @IpsGenerated
    public void setValueSetInclNull(Boolean newValue) {
        this.valueSetInclNull = newValue;
    }


    /**
     * Returns the set of allowed values for the property valueSetExclNull.
     *
     * @restrainedmodifiable
     */
    @IpsAllowedValues("valueSetExclNull")
    @IpsGenerated
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
    @IpsGenerated
    public boolean isValueSetExclNull() {
        return valueSetExclNull;
    }


    /**
     * Sets the value of attribute valueSetExclNull.
     *
     * @generated
     */
    @IpsAttributeSetter("valueSetExclNull")
    @IpsGenerated
    public void setValueSetExclNull(boolean newValue) {
        this.valueSetExclNull = newValue;
    }


    /**
     * Returns the set of allowed values for the property emptyValueSet.
     *
     * @generated
     */
    @IpsAllowedValues("emptyValueSet")
    @IpsGenerated
    public OrderedValueSet<MonthDay> getAllowedValuesForEmptyValueSet(IValidationContext context) {
        return MAX_ALLOWED_VALUES_FOR_EMPTY_VALUE_SET;
    }


    /**
     * Returns the emptyValueSet.
     *
     * @generated
     */
    @IpsAttribute(name = "emptyValueSet", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.Enum)
    @IpsGenerated
    public MonthDay getEmptyValueSet() {
        return emptyValueSet;
    }


    /**
     * Sets the value of attribute emptyValueSet.
     *
     * @generated
     */
    @IpsAttributeSetter("emptyValueSet")
    @IpsGenerated
    public void setEmptyValueSet(MonthDay newValue) {
        this.emptyValueSet = newValue;
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
        doInitFoo(propMap);
        doInitValueSetInclNull(propMap);
        doInitValueSetExclNull(propMap);
        doInitEmptyValueSet(propMap);
    }

    /**
     * @generated
     */
    @IpsGenerated
    private void doInitFoo(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_FOO)) {
            this.foo = propMap.get(PROPERTY_FOO);
        }
    }

    /**
     * @generated
     */
    @IpsGenerated
    private void doInitValueSetInclNull(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_VALUESETINCLNULL)) {
            this.valueSetInclNull = IpsStringUtils.isEmpty(propMap.get(PROPERTY_VALUESETINCLNULL)) ? null
                    : Boolean.valueOf(propMap.get(PROPERTY_VALUESETINCLNULL));
        }
    }


    /**
     * @generated
     */
    @IpsGenerated
    private void doInitValueSetExclNull(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_VALUESETEXCLNULL)) {
            this.valueSetExclNull = Boolean.valueOf(propMap.get(PROPERTY_VALUESETEXCLNULL)).booleanValue();
        }
    }


    /**
     * @generated
     */
    @IpsGenerated
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


}
