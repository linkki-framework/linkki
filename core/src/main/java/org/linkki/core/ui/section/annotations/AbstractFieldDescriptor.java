/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.core.ui.section.annotations;

import static java.util.Objects.requireNonNull;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;

/**
 * Holds all information about a field, which are the property name as well as the settings for
 * visibility, enabled-state etc. The given property name is only used as fallback if there is
 * {@link UIFieldDefinition#modelAttribute()} is not set.
 */
public abstract class AbstractFieldDescriptor extends ElementDescriptor {

    private final String pmoPropertyName;

    /**
     * Constructs a new field description.
     *
     * @param fieldDef field definition that holds given annotated properties
     * @param pmoPropertyName name of the corresponding method in the PMO
     * @param pmoClass presentation model object class type
     */
    public AbstractFieldDescriptor(UIFieldDefinition fieldDef, String pmoPropertyName, Class<?> pmoClass,
            List<LinkkiAspectDefinition> aspectDefinitions) {
        super(fieldDef, pmoClass, aspectDefinitions);
        this.pmoPropertyName = requireNonNull(pmoPropertyName, "pmoPropertyName must not be null");
    }

    @Override
    protected UIFieldDefinition getBindingDefinition() {
        return (UIFieldDefinition)super.getBindingDefinition();
    }

    /**
     * Property derived from the "modelAttribute" property defined by the annotation. If no
     * "modelAttribute" exists, derives the property name from the name of the annotated method.
     */
    @Override
    public String getModelPropertyName() {
        if (StringUtils.isEmpty(getBindingDefinition().modelAttribute())) {
            return getPmoPropertyName();
        }
        return getBindingDefinition().modelAttribute();
    }

    @Override
    public String getModelObjectName() {
        return getBindingDefinition().modelObject();
    }

    @Override
    public String getPmoPropertyName() {
        return pmoPropertyName;
    }

    @Override
    public String toString() {
        return "FieldDescriptor [getBindingDefinition()=" + getBindingDefinition() + ", fallbackPropertyName="
                + getPmoPropertyName()
                + "]";
    }
}
