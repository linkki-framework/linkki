package org.linkki.ips.test.model;

import org.faktorips.runtime.model.annotation.IpsPolicyCmptType;
import org.faktorips.runtime.model.annotation.IpsAttributes;
import org.faktorips.runtime.model.annotation.IpsDocumented;
import org.faktorips.runtime.model.annotation.IpsAttribute;
import org.faktorips.runtime.model.type.AttributeKind;
import org.faktorips.runtime.model.type.ValueSetKind;
import org.faktorips.runtime.internal.AbstractModelObject;
import org.w3c.dom.Element;
import org.faktorips.runtime.MessageList;
import org.faktorips.runtime.IValidationContext;

/**
 * Implementation for TestIpsObject2.
 *
 * @generated
 */
@IpsPolicyCmptType(name = "TestIpsObject2")
@IpsAttributes({ "foo" })
@IpsDocumented(bundleName = "org.linkki.ips.test.model.testmodel-label-and-descriptions", defaultLocale = "en")
public class TestIpsObject2 extends TestIpsObject {


    /**
     * Creates a new TestIpsObject2.
     *
     * @generated
     */
    public TestIpsObject2() {
        super();
        setFoo(null);
    }


    /**
     * Returns the foo.
     *
     * @generated
     */
    @IpsAttribute(name = "foo", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.AllValues)
    @Override
    public String getFoo() {
        return super.getFoo();
    }


    /**
     * Initializes the object with the configured defaults.
     *
     * @restrainedmodifiable
     */
    @Override
    public void initialize() {
        super.initialize();
        // begin-user-code
        // end-user-code
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
     * Validates the object (but not its children). Returns <code>true</code> if this object should
     * continue validating, <code>false</code> otherwise.
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
