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
package org.linkki.core.ui.section.descriptor;

import static java.util.Objects.requireNonNull;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.ComponentBinding;
import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.nls.pmo.PmoLabelType;
import org.linkki.core.nls.pmo.PmoNlsService;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.section.annotations.BindingDefinition;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Component;

/**
 * Holds information about a bound UI element (such as the PMO and model property name) and on how
 * to create and display such an UI element.
 */
public class ElementDescriptor extends BindingDescriptor {

    private final String pmoPropertyName;
    private final PmoNlsService pmoNlsService;
    private final Class<?> pmoClass;
    private final BindingDefinition bindingDefinition;

    public ElementDescriptor(BindingDefinition bindingDefinition, String pmoPropertyName, Class<?> pmoClass,
            List<LinkkiAspectDefinition> aspectDefinitions) {
        super(aspectDefinitions);
        this.bindingDefinition = requireNonNull(bindingDefinition, "bindingDefinition must not be null");
        this.pmoPropertyName = requireNonNull(pmoPropertyName, "pmoPropertyName must not be null");
        this.pmoClass = requireNonNull(pmoClass, "pmoClass must not be null");
        pmoNlsService = PmoNlsService.get();
    }

    protected BindingDefinition getBindingDefinition() {
        return bindingDefinition;
    }

    /** The position of the UI element in its parent/container. */
    public int getPosition() {
        return getBindingDefinition().position();
    }

    /**
     * @return the label text as defined by the {@link PmoNlsService}, with fall back to the label
     *         defined in the annotation and further to the property name.
     */
    @SuppressWarnings("null")
    public String getLabelText() {
        if (!getBindingDefinition().showLabel()) {
            return "";
        }

        String label = getBindingDefinition().label();
        if (StringUtils.isEmpty(label)) {
            label = StringUtils.capitalize(getModelPropertyName());
        }
        return pmoNlsService.getLabel(PmoLabelType.PROPERTY_LABEL, pmoClass, getPmoPropertyName(), label);
    }

    /** Creates a new Vaadin UI component for this UI element. */
    public Component newComponent() {
        return getBindingDefinition().newComponent();
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

    @Override
    public ElementBinding createBinding(PropertyDispatcher propertyDispatcher,
            Handler modelChanged,
            ComponentWrapper componentWrapper) {
        requireNonNull(propertyDispatcher, "propertyDispatcher must not be null");
        requireNonNull(modelChanged, "modelChanged must not be null");
        requireNonNull(componentWrapper, "component must not be null");
        return new ComponentBinding(componentWrapper, propertyDispatcher, modelChanged,
                getAspectDefinitions());
    }

}
