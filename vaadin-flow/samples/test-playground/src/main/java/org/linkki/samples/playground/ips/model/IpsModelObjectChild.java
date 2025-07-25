/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.linkki.samples.playground.ips.model;

import org.faktorips.runtime.IValidationContext;
import org.faktorips.runtime.MessageList;
import org.faktorips.runtime.annotation.IpsGenerated;
import org.faktorips.runtime.internal.AbstractModelObject;
import org.faktorips.runtime.model.annotation.IpsAllowedValues;
import org.faktorips.runtime.model.annotation.IpsAttribute;
import org.faktorips.runtime.model.annotation.IpsAttributes;
import org.faktorips.runtime.model.annotation.IpsDocumented;
import org.faktorips.runtime.model.annotation.IpsPolicyCmptType;
import org.faktorips.runtime.model.type.AttributeKind;
import org.faktorips.runtime.model.type.ValueSetKind;
import org.faktorips.valueset.UnrestrictedValueSet;
import org.faktorips.valueset.ValueSet;
import org.w3c.dom.Element;
import org.faktorips.runtime.IModelObject;
import java.util.Map;
import java.util.HashMap;

/**
 * Implementation for IpsModelObjectChild.
 *
 * @generated
 */
@IpsPolicyCmptType(name = "IpsModelObjectChild")
@IpsAttributes({ "string" })
@IpsDocumented(bundleName = "org.linkki.samples.playground.ips.model.model-label-and-descriptions",
        defaultLocale = "en")
public class IpsModelObjectChild extends IpsModelObject {

    /**
     * Max allowed values for property string.
     *
     * @generated
     */
    @SuppressWarnings("hiding")
    @IpsAllowedValues("string")
    public static final ValueSet<String> MAX_ALLOWED_VALUES_FOR_STRING = new UnrestrictedValueSet<>(true);

    /**
     * Creates a new IpsModelObjectChild.
     *
     * @generated
     */
    @IpsGenerated
    public IpsModelObjectChild() {
        super();
    }

    /**
     * Returns the set of allowed values for the property string.
     *
     * @generated
     */
    @IpsAllowedValues("string")
    @Override
    @IpsGenerated
    public ValueSet<String> getAllowedValuesForString() {
        return IpsModelObjectChild.MAX_ALLOWED_VALUES_FOR_STRING;
    }

    /**
     * Returns the string.
     *
     * @generated
     */
    @IpsAttribute(name = "string", kind = AttributeKind.CHANGEABLE, valueSetKind = ValueSetKind.AllValues)
    @Override
    @IpsGenerated
    public String getString() {
        return super.getString();
    }

    /**
     * Initializes the object with the configured defaults.
     *
     * @restrainedmodifiable
     */
    @Override
    @IpsGenerated
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
    @IpsGenerated
    protected AbstractModelObject createChildFromXml(Element childEl) {
        AbstractModelObject newChild = super.createChildFromXml(childEl);
        if (newChild != null) {
            return newChild;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @generated
     */
    @Override
    @IpsGenerated
    public IpsModelObjectChild newCopy() {
        Map<IModelObject, IModelObject> copyMap = new HashMap<>();
        IpsModelObjectChild newCopy = newCopyInternal(copyMap);
        copyAssociationsInternal(newCopy, copyMap);
        return newCopy;
    }

    /**
     * Internal copy method with a {@link Map} containing already copied instances.
     *
     * @param copyMap the map contains the copied instances
     *
     * @generated
     */
    @Override
    @IpsGenerated
    public IpsModelObjectChild newCopyInternal(Map<IModelObject, IModelObject> copyMap) {
        IpsModelObjectChild newCopy = (IpsModelObjectChild)copyMap.get(this);
        if (newCopy == null) {
            newCopy = new IpsModelObjectChild();
            copyMap.put(this, newCopy);
            copyProperties(newCopy, copyMap);
        }
        return newCopy;
    }

    /**
     * This method sets all properties in the copy with the values of this object. If there are
     * copied associated objects they are added to the copyMap in {@link #newCopyInternal(Map)}.
     *
     * @param copy The copy object
     * @param copyMap a map containing copied associated objects
     *
     * @generated
     */
    @Override
    @IpsGenerated
    protected void copyProperties(IModelObject copy, Map<IModelObject, IModelObject> copyMap) {
        super.copyProperties(copy, copyMap);
    }

    /**
     * Internal method for setting copied associations. For copied targets, the associations have to
     * be retargeted to the new copied instances. This method have to call
     * {@link #copyAssociationsInternal(IModelObject, Map)} in other instances associated by
     * composite.
     *
     * @param abstractCopy the copy of this policy component
     * @param copyMap the map contains the copied instances
     *
     * @generated
     */
    @Override
    @IpsGenerated
    public void copyAssociationsInternal(IModelObject abstractCopy, Map<IModelObject, IModelObject> copyMap) {
        super.copyAssociationsInternal(abstractCopy, copyMap);
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
