package org.linkki.ips.test.model;

import org.faktorips.runtime.model.annotation.IpsPolicyCmptType;
import org.faktorips.runtime.model.annotation.IpsAttributes;
import org.faktorips.runtime.model.annotation.IpsDocumented;
import org.faktorips.runtime.internal.AbstractModelObject;
import org.faktorips.valueset.ValueSet;
import org.faktorips.valueset.UnrestrictedValueSet;
import org.faktorips.runtime.model.annotation.IpsAttribute;
import org.faktorips.runtime.model.type.AttributeKind;
import org.faktorips.runtime.model.type.ValueSetKind;
import org.faktorips.runtime.model.annotation.IpsAttributeSetter;
import org.faktorips.runtime.model.annotation.IpsAllowedValues;
import java.util.Map;
import org.faktorips.runtime.IRuntimeRepository;
import org.w3c.dom.Element;
import org.faktorips.runtime.MessageList;
import org.faktorips.runtime.IValidationContext;

/**
 * Implementation for TestIpsObject.
 * 
 * @generated
 */
@IpsPolicyCmptType(name = "TestIpsObject")
@IpsAttributes({ "foo", "unrestrictedInclNull", "unrestrictedExclNull" })
@IpsDocumented(bundleName = "org.linkki.ips.test.model.testmodel-label-and-descriptions", defaultLocale = "en")
public class TestIpsObject extends AbstractModelObject {


    /**
     * The name of the property foo.
     * 
     * @generated
     */
    public static final String PROPERTY_FOO = "foo";


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
    public static final ValueSet<String> MAX_ALLOWED_VALUES_FOR_UNRESTRICTED_EXCL_NULL = new UnrestrictedValueSet<String>(
            false);


    /**
     * Member variable for foo.
     * 
     * @generated
     */
    private String foo = null;


    /**
     * Member variable for unrestrictedInclNull.
     * 
     * @generated
     */
    private String unrestrictedInclNull = null;


    /**
     * Member variable for unrestrictedExclNull.
     * 
     * @generated
     */
    private String unrestrictedExclNull = null;


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
        doInitUnrestrictedInclNull(propMap);
        doInitUnrestrictedExclNull(propMap);
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
