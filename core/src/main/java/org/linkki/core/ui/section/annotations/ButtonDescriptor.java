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

import javax.annotation.Nullable;

import org.linkki.core.binding.ButtonBinding;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class ButtonDescriptor extends ElementDescriptor {

    private final String methodName;

    public ButtonDescriptor(UIButtonDefinition buttonAnnotation, String methodName, Class<?> pmoClass,
            List<LinkkiAspectDefinition> aspectDefs) {
        super(buttonAnnotation, pmoClass, aspectDefs);
        this.methodName = requireNonNull(methodName, "methodName must not be null");
    }

    @Override
    protected UIButtonDefinition getBindingDefinition() {
        return (UIButtonDefinition)super.getBindingDefinition();
    }

    @Override
    public String getModelPropertyName() {
        return methodName;
    }

    @Override
    public String getModelObjectName() {
        return ModelObject.DEFAULT_NAME;
    }

    @Override
    public ButtonBinding createBinding(PropertyDispatcher propertyDispatcher,
            Handler modelChanged,
            Component component,
            @Nullable Label label) {
        requireNonNull(propertyDispatcher, "propertyDispatcher must not be null");
        requireNonNull(modelChanged, "updateUi must not be null");
        requireNonNull(component, "component must not be null");

        return new ButtonBinding(label, (Button)component, propertyDispatcher, modelChanged, true, getAspectDefinitions());
    }

    @Override
    public String getPmoPropertyName() {
        return methodName;
    }

    public CaptionType captionType() {
        return getBindingDefinition().captionType();
    }

    public String caption() {
        return getBindingDefinition().caption();
    }
}
