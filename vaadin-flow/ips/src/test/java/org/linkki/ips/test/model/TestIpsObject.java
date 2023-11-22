package org.linkki.ips.test.model;

import java.time.MonthDay;
import java.util.Map;

import org.faktorips.runtime.IRuntimeRepository;
import org.faktorips.runtime.IValidationContext;
import org.faktorips.runtime.MessageList;
import org.faktorips.runtime.annotation.IpsGenerated;
import org.faktorips.runtime.internal.AbstractModelObject;
import org.faktorips.runtime.internal.IpsStringUtils;
import org.faktorips.runtime.model.annotation.IpsAllowedValues;
import org.faktorips.runtime.model.annotation.IpsAttribute;
import org.faktorips.runtime.model.annotation.IpsAttributeSetter;
import org.faktorips.runtime.model.annotation.IpsAttributes;
import org.faktorips.runtime.model.annotation.IpsDefaultValue;
import org.faktorips.valueset.DerivedValueSet;
import org.faktorips.runtime.model.annotation.IpsDocumented;
import org.faktorips.runtime.model.annotation.IpsPolicyCmptType;
import org.faktorips.runtime.model.type.AttributeKind;
import org.faktorips.runtime.model.type.ValueSetKind;
import org.faktorips.valueset.DoubleRange;
import org.faktorips.valueset.IntegerRange;
import org.faktorips.valueset.OrderedValueSet;
import org.faktorips.valueset.UnrestrictedValueSet;
import org.faktorips.valueset.ValueSet;
import org.w3c.dom.Element;

/**
 * Implementation for TestIpsObject.
 *
 * @generated
 */
@IpsPolicyCmptType(name = "TestIpsObject")
@IpsAttributes({ "foo", "valueSetInclNull", "valueSetExclNull", "emptyValueSet", "valueSetRangeOfIntInclNull",
        "valueSetRangeOfIntExclNull", "valueSetIntUnrestricted", "valueSetNotDiscrete" })
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
    @IpsAllowedValues("foo")
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
    @IpsAllowedValues("valueSetInclNull")
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
    @IpsAllowedValues("valueSetExclNull")
    public static final ValueSet<Boolean> MAX_ALLOWED_VALUES_FOR_VALUE_SET_EXCL_NULL = new DerivedValueSet<>();


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
    @IpsAllowedValues("emptyValueSet")
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
     * The name of the property valueSetRangeOfIntInclNull.
     *
     * @generated
     */
    public static final String PROPERTY_VALUESETRANGEOFINTINCLNULL = "valueSetRangeOfIntInclNull";


    /**
     * Max allowed range for the property valueSetRangeOfIntInclNull.
     *
     * @generated
     */
    @IpsAllowedValues("valueSetRangeOfIntInclNull")
    public static final IntegerRange MAX_ALLOWED_RANGE_FOR_VALUE_SET_RANGE_OF_INT_INCL_NULL = IntegerRange
            .valueOf(Integer.valueOf("0"), Integer.valueOf(5), Integer.valueOf(1), true);


    /**
     * The default value for valueSetRangeOfIntInclNull.
     *
     * @generated
     */
    @IpsDefaultValue("valueSetRangeOfIntInclNull")
    public static final Integer DEFAULT_VALUE_FOR_VALUE_SET_RANGE_OF_INT_INCL_NULL = null;


    /**
     * The name of the property valueSetRangeOfIntExclNull.
     *
     * @generated
     */
    public static final String PROPERTY_VALUESETRANGEOFINTEXCLNULL = "valueSetRangeOfIntExclNull";


    /**
     * Max allowed range for the property valueSetRangeOfIntExclNull.
     *
     * @generated
     */
    @IpsAllowedValues("valueSetRangeOfIntExclNull")
    public static final IntegerRange MAX_ALLOWED_RANGE_FOR_VALUE_SET_RANGE_OF_INT_EXCL_NULL = IntegerRange
            .valueOf(Integer.valueOf("0"), Integer.valueOf(5), Integer.valueOf(1), false);


    /**
     * The default value for valueSetRangeOfIntExclNull.
     *
     * @generated
     */
    @IpsDefaultValue("valueSetRangeOfIntExclNull")
    public static final Integer DEFAULT_VALUE_FOR_VALUE_SET_RANGE_OF_INT_EXCL_NULL = null;


    /**
     * The name of the property valueSetIntUnrestricted.
     *
     * @generated
     */
    public static final String PROPERTY_VALUESETINTUNRESTRICTED = "valueSetIntUnrestricted";


    /**
     * Max allowed values for property valueSetIntUnrestricted.
     *
     * @generated
     */
    @IpsAllowedValues("valueSetIntUnrestricted")
    public static final ValueSet<Integer> MAX_ALLOWED_VALUES_FOR_VALUE_SET_INT_UNRESTRICTED = new UnrestrictedValueSet<>(
            true);


    /**
     * The default value for valueSetIntUnrestricted.
     *
     * @generated
     */
    @IpsDefaultValue("valueSetIntUnrestricted")
    public static final Integer DEFAULT_VALUE_FOR_VALUE_SET_INT_UNRESTRICTED = null;


    /**
     * The name of the property valueSetNotDiscrete.
     *
     * @generated
     */
    public static final String PROPERTY_VALUESETNOTDISCRETE = "valueSetNotDiscrete";


    /**
     * Max allowed range for the property valueSetNotDiscrete.
     *
     * @generated
     */
    @IpsAllowedValues("valueSetNotDiscrete")
    public static final DoubleRange MAX_ALLOWED_RANGE_FOR_VALUE_SET_NOT_DISCRETE = DoubleRange
            .valueOf(Double.valueOf(1), Double.valueOf(100), (Double)null, false);


    /**
     * The default value for valueSetNotDiscrete.
     *
     * @generated
     */
    @IpsDefaultValue("valueSetNotDiscrete")
    public static final Double DEFAULT_VALUE_FOR_VALUE_SET_NOT_DISCRETE = null;


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
     * Member variable for valueSetRangeOfIntInclNull.
     *
     * @generated
     */
    private Integer valueSetRangeOfIntInclNull = DEFAULT_VALUE_FOR_VALUE_SET_RANGE_OF_INT_INCL_NULL;


    /**
     * Member variable for valueSetRangeOfIntExclNull.
     *
     * @generated
     */
    private Integer valueSetRangeOfIntExclNull = DEFAULT_VALUE_FOR_VALUE_SET_RANGE_OF_INT_EXCL_NULL;


    /**
     * Member variable for valueSetIntUnrestricted.
     *
     * @generated
     */
    private Integer valueSetIntUnrestricted = DEFAULT_VALUE_FOR_VALUE_SET_INT_UNRESTRICTED;


    /**
     * Member variable for valueSetNotDiscrete.
     *
     * @generated
     */
    private Double valueSetNotDiscrete = DEFAULT_VALUE_FOR_VALUE_SET_NOT_DISCRETE;


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
    public ValueSet<String> getAllowedValuesForFoo() {
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
    public ValueSet<Boolean> getAllowedValuesForValueSetInclNull() {
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
    public ValueSet<Boolean> getAllowedValuesForValueSetExclNull() {
        // begin-user-code
        return new UnrestrictedValueSet<>(false);
        // end-user-code
    }

    /**
     * Returns the valueSetExclNull.
     *
     * @generated
     */
    @IpsAttribute(name = "valueSetExclNull", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.Derived,
            primitive = true)
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
    public ValueSet<MonthDay> getAllowedValuesForEmptyValueSet() {
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
     * Returns the range of allowed values for the property valueSetRangeOfIntInclNull.
     *
     * @generated
     */
    @IpsAllowedValues("valueSetRangeOfIntInclNull")
    @IpsGenerated
    public ValueSet<Integer> getAllowedValuesForValueSetRangeOfIntInclNull() {
        return MAX_ALLOWED_RANGE_FOR_VALUE_SET_RANGE_OF_INT_INCL_NULL;
    }


    /**
     * Returns the valueSetRangeOfIntInclNull.
     *
     * @generated
     */
    @IpsAttribute(name = "valueSetRangeOfIntInclNull", kind = AttributeKind.CHANGEABLE,
            valueSetKind = ValueSetKind.Range)
    @IpsGenerated
    public Integer getValueSetRangeOfIntInclNull() {
        return valueSetRangeOfIntInclNull;
    }


    /**
     * Sets the value of attribute valueSetRangeOfIntInclNull.
     *
     * @generated
     */
    @IpsAttributeSetter("valueSetRangeOfIntInclNull")
    @IpsGenerated
    public void setValueSetRangeOfIntInclNull(Integer newValue) {
        this.valueSetRangeOfIntInclNull = newValue;
    }


    /**
     * Returns the range of allowed values for the property valueSetRangeOfIntExclNull.
     *
     * @generated
     */
    @IpsAllowedValues("valueSetRangeOfIntExclNull")
    @IpsGenerated
    public ValueSet<Integer> getAllowedValuesForValueSetRangeOfIntExclNull() {
        return MAX_ALLOWED_RANGE_FOR_VALUE_SET_RANGE_OF_INT_EXCL_NULL;
    }


    /**
     * Returns the valueSetRangeOfIntExclNull.
     *
     * @generated
     */
    @IpsAttribute(name = "valueSetRangeOfIntExclNull", kind = AttributeKind.CHANGEABLE,
            valueSetKind = ValueSetKind.Range)
    @IpsGenerated
    public Integer getValueSetRangeOfIntExclNull() {
        return valueSetRangeOfIntExclNull;
    }


    /**
     * Sets the value of attribute valueSetRangeOfIntExclNull.
     *
     * @generated
     */
    @IpsAttributeSetter("valueSetRangeOfIntExclNull")
    @IpsGenerated
    public void setValueSetRangeOfIntExclNull(Integer newValue) {
        this.valueSetRangeOfIntExclNull = newValue;
    }


    /**
     * Returns the set of allowed values for the property valueSetIntUnrestricted.
     *
     * @generated
     */
    @IpsAllowedValues("valueSetIntUnrestricted")
    @IpsGenerated
    public ValueSet<Integer> getAllowedValuesForValueSetIntUnrestricted() {
        return MAX_ALLOWED_VALUES_FOR_VALUE_SET_INT_UNRESTRICTED;
    }


    /**
     * Returns the valueSetIntUnrestricted.
     *
     * @generated
     */
    @IpsAttribute(name = "valueSetIntUnrestricted", kind = AttributeKind.CHANGEABLE,
            valueSetKind = ValueSetKind.AllValues)
    @IpsGenerated
    public Integer getValueSetIntUnrestricted() {
        return valueSetIntUnrestricted;
    }


    /**
     * Sets the value of attribute valueSetIntUnrestricted.
     *
     * @generated
     */
    @IpsAttributeSetter("valueSetIntUnrestricted")
    @IpsGenerated
    public void setValueSetIntUnrestricted(Integer newValue) {
        this.valueSetIntUnrestricted = newValue;
    }


    /**
     * Returns the range of allowed values for the property valueSetNotDiscrete.
     *
     * @generated
     */
    @IpsAllowedValues("valueSetNotDiscrete")
    @IpsGenerated
    public ValueSet<Double> getAllowedValuesForValueSetNotDiscrete() {
        return MAX_ALLOWED_RANGE_FOR_VALUE_SET_NOT_DISCRETE;
    }


    /**
     * Returns the valueSetNotDiscrete.
     *
     * @generated
     */
    @IpsAttribute(name = "valueSetNotDiscrete", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.Range)
    @IpsGenerated
    public Double getValueSetNotDiscrete() {
        return valueSetNotDiscrete;
    }


    /**
     * Sets the value of attribute valueSetNotDiscrete.
     *
     * @generated
     */
    @IpsAttributeSetter("valueSetNotDiscrete")
    @IpsGenerated
    public void setValueSetNotDiscrete(Double newValue) {
        this.valueSetNotDiscrete = newValue;
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
        doInitValueSetRangeOfIntInclNull(propMap);
        doInitValueSetRangeOfIntExclNull(propMap);
        doInitValueSetIntUnrestricted(propMap);
        doInitValueSetNotDiscrete(propMap);
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
     * @generated
     */
    @IpsGenerated
    private void doInitValueSetRangeOfIntInclNull(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_VALUESETRANGEOFINTINCLNULL)) {
            this.valueSetRangeOfIntInclNull = IpsStringUtils.isEmpty(propMap.get(PROPERTY_VALUESETRANGEOFINTINCLNULL))
                    ? null
                    : Integer.valueOf(propMap.get(PROPERTY_VALUESETRANGEOFINTINCLNULL));
        }
    }


    /**
     * @generated
     */
    @IpsGenerated
    private void doInitValueSetRangeOfIntExclNull(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_VALUESETRANGEOFINTEXCLNULL)) {
            this.valueSetRangeOfIntExclNull = IpsStringUtils.isEmpty(propMap.get(PROPERTY_VALUESETRANGEOFINTEXCLNULL))
                    ? null
                    : Integer.valueOf(propMap.get(PROPERTY_VALUESETRANGEOFINTEXCLNULL));
        }
    }


    /**
     * @generated
     */
    @IpsGenerated
    private void doInitValueSetIntUnrestricted(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_VALUESETINTUNRESTRICTED)) {
            this.valueSetIntUnrestricted = IpsStringUtils.isEmpty(propMap.get(PROPERTY_VALUESETINTUNRESTRICTED)) ? null
                    : Integer.valueOf(propMap.get(PROPERTY_VALUESETINTUNRESTRICTED));
        }
    }


    /**
     * @generated
     */
    @IpsGenerated
    private void doInitValueSetNotDiscrete(Map<String, String> propMap) {
        if (propMap.containsKey(PROPERTY_VALUESETNOTDISCRETE)) {
            this.valueSetNotDiscrete = IpsStringUtils.isEmpty(propMap.get(PROPERTY_VALUESETNOTDISCRETE)) ? null
                    : Double.valueOf(propMap.get(PROPERTY_VALUESETNOTDISCRETE));
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
