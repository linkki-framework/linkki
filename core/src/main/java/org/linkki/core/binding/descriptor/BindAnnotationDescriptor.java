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
package org.linkki.core.binding.descriptor;

import java.util.List;

import org.linkki.core.binding.ComponentBinding;
import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.property.BoundProperty;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.util.handler.Handler;

/**
 * Descriptor for {@link Bind @Bind}.
 */
public class BindAnnotationDescriptor extends BindingDescriptor {

    private final BoundProperty boundProperty;

    public BindAnnotationDescriptor(BoundProperty boundProperty,
            List<LinkkiAspectDefinition> aspectDefinitions) {
        super(aspectDefinitions);
        this.boundProperty = boundProperty;
    }

    @Override
    public String getModelPropertyName() {
        return boundProperty.getModelAttribute();
    }

    @Override
    public String getModelObjectName() {
        return boundProperty.getModelObject();
    }

    @Override
    public ElementBinding createBinding(PropertyDispatcher propertyDispatcher,
            Handler modelChanged,
            ComponentWrapper componentWrapper) {
        return new ComponentBinding(componentWrapper, propertyDispatcher, modelChanged,
                getAspectDefinitions());
    }

    @Override
    public String getPmoPropertyName() {
        return boundProperty.getPmoProperty();
    }

}
