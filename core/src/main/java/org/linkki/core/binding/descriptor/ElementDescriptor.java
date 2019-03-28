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
package org.linkki.core.binding.descriptor;

import java.util.List;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;

/**
 * A {@link BindingDescriptor} using {@link BindingDefinition}.
 */
public class ElementDescriptor extends BindingDescriptor {

    private final LinkkiComponentDefinition componentDefinition;
    private final BoundProperty boundProperty;

    public ElementDescriptor(LinkkiComponentDefinition componentDefinition, BoundProperty boundPropery,
            List<LinkkiAspectDefinition> aspectDefinitions) {
        super(aspectDefinitions);
        this.componentDefinition = componentDefinition;
        this.boundProperty = boundPropery;
    }

    /** The position of the UI element in its parent/container. */
    public int getPosition() {
        return componentDefinition.getPosition();
    }

    /** Creates a new UI component for this UI element. */
    public Object newComponent(Object pmo) {
        return componentDefinition.createComponent(pmo);
    }

    @Override
    public BoundProperty getBoundProperty() {
        return boundProperty;
    }

    @Override
    public String toString() {
        return "ElementDescriptor [propertyName =" + getPmoPropertyName() + "]";
    }
}
