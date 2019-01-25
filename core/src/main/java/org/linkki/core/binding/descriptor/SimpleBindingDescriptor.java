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

import java.util.Arrays;
import java.util.List;

import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.property.BoundProperty;

/**
 * A simple {@link BindingDescriptor} that only describes the bound property to be used with the
 * configured {@link LinkkiAspectDefinition aspect definitions}.
 * <p>
 * If the {@code pmoPropertyName} is an empty String, the binding of aspects happens via
 * {@code get<Aspect>()} methods.
 * 
 * @see LinkkiAspectDefinition
 */
public class SimpleBindingDescriptor extends BindingDescriptor {

    private final BoundProperty boundProperty;

    public SimpleBindingDescriptor(String pmoPropertyName, LinkkiAspectDefinition... aspectDefs) {
        this(pmoPropertyName, Arrays.asList(aspectDefs));
    }

    public SimpleBindingDescriptor(String pmoPropertyName, List<? extends LinkkiAspectDefinition> aspectDefs) {
        this(new BoundProperty(pmoPropertyName), aspectDefs);
    }

    public SimpleBindingDescriptor(String pmoPropertyName, String modelPropertyName, String modelObjectName,
            List<? extends LinkkiAspectDefinition> aspectDefs) {
        this(new BoundProperty(pmoPropertyName).withModelObject(modelObjectName).withModelAttribute(modelPropertyName),
                aspectDefs);
    }

    public SimpleBindingDescriptor(BoundProperty boundProperty,
            List<? extends LinkkiAspectDefinition> aspectDefs) {
        super(aspectDefs);
        this.boundProperty = boundProperty;
    }

    @Override
    public String getPmoPropertyName() {
        return boundProperty.getPmoProperty();
    }

    @Override
    public String getModelPropertyName() {
        return boundProperty.getModelAttribute();
    }

    @Override
    public String getModelObjectName() {
        return boundProperty.getModelObject();
    }

}
