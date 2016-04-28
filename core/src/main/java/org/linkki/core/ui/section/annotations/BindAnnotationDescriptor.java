/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import org.linkki.core.binding.ButtonBinding;
import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.FieldBinding;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.section.annotations.adapters.BindAnnotationAdapter;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

public class BindAnnotationDescriptor implements BindingDescriptor {

    private final BindAnnotationAdapter bindAnnotationAdapter;

    public BindAnnotationDescriptor(BindAnnotationAdapter bindAnnotationAdapter) {
        super();
        this.bindAnnotationAdapter = bindAnnotationAdapter;
    }

    @Override
    public EnabledType enabled() {
        return bindAnnotationAdapter.enabled();
    }

    @Override
    public VisibleType visible() {
        return bindAnnotationAdapter.visible();
    }

    @Override
    public RequiredType required() {
        return bindAnnotationAdapter.required();
    }

    @Override
    public AvailableValuesType availableValues() {
        return bindAnnotationAdapter.availableValues();
    }

    @Override
    public String getModelObjectName() {
        // We currently do not support multiple model objects for PMOs that are bound to a view
        // annotated with @Bind
        return ModelObject.DEFAULT_NAME;
    }

    @Override
    public String getPropertyName() {
        return bindAnnotationAdapter.getPropertyName();
    }

    @Override
    public ElementBinding createBinding(PropertyDispatcher propertyDispatcher,
            Handler updateUi,
            Component component,
            Label label) {
        if (component instanceof Field<?>) {
            return new FieldBinding<>(label, (Field<?>)component, propertyDispatcher, updateUi);
        } else if (component instanceof Button) {
            return new ButtonBinding(label, (Button)component, propertyDispatcher, updateUi);
        } else {
            throw new IllegalArgumentException("Cannot create a binding for component " + component);
        }
    }

}
