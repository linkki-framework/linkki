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

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.ComponentBinding;
import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.util.handler.Handler;

public class BindAnnotationDescriptor extends BindingDescriptor {

    private Bind annotation;

    public BindAnnotationDescriptor(Bind annotation, List<LinkkiAspectDefinition> aspectDefinitions) {
        super(aspectDefinitions);
        this.annotation = annotation;
    }

    @Override
    public String getModelPropertyName() {
        if (StringUtils.isEmpty(annotation.modelAttribute())) {
            return getPmoPropertyName();
        }
        return annotation.modelAttribute();
    }

    @Override
    public String getModelObjectName() {
        return annotation.modelObject();
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
        return annotation.pmoProperty();
    }

}
