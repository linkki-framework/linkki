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

import java.util.List;

import javax.annotation.Nullable;

import org.linkki.core.binding.ButtonBinding;
import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.FieldBinding;
import org.linkki.core.binding.LabelBinding;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.binding.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.section.annotations.adapters.BindAnnotationAdapter;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

public class BindAnnotationDescriptor extends BindingDescriptor {

    public BindAnnotationDescriptor(Bind annotation, List<LinkkiAspectDefinition> aspectDefinitions) {
        super(new BindAnnotationAdapter(annotation), aspectDefinitions);
    }

    @Override
    protected BindAnnotationAdapter getBindingDefinition() {
        return (BindAnnotationAdapter)super.getBindingDefinition();
    }

    @Override
    public String getModelPropertyName() {
        // We currently do not support a model property name in the binding annotation
        return getPmoPropertyName();
    }

    @Override
    public String getModelObjectName() {
        // We currently do not support multiple model objects for PMOs that are bound to a view
        // annotated with @Bind
        return ModelObject.DEFAULT_NAME;
    }

    @Override
    public ElementBinding createBinding(PropertyDispatcher propertyDispatcher,
            Handler modelChanged,
            Component component,
            @Nullable Label label) {

        if (component instanceof Field<?>) {
            return new FieldBinding<>(label, (AbstractField<?>)component, propertyDispatcher, modelChanged,
                    getAspectDefinitions());
        } else if (component instanceof Button) {
            return new ButtonBinding(label, (Button)component, propertyDispatcher, modelChanged, false,
                    getAspectDefinitions());
        } else if (component instanceof Label) {
            return new LabelBinding(label, (Label)component, propertyDispatcher, getAspectDefinitions());
        } else {
            throw new IllegalArgumentException("Cannot create a binding for component " + component);
        }
    }

    @Override
    public String getPmoPropertyName() {
        return getBindingDefinition().getPmoPropertyName();
    }

}
