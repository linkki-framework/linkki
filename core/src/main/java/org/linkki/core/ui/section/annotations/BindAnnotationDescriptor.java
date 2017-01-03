/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import org.linkki.core.binding.ButtonBinding;
import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.FieldBinding;
import org.linkki.core.binding.LabelBinding;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.section.annotations.adapters.BindAnnotationAdapter;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

public class BindAnnotationDescriptor extends BindingDescriptor {

    public BindAnnotationDescriptor(Bind annotation, UIToolTipDefinition toolTipDefinition) {
        super(new BindAnnotationAdapter(annotation), toolTipDefinition);
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
            Handler updateUi,
            Component component,
            Label label) {
        if (component instanceof Field<?>) {
            return new FieldBinding<>(label, (AbstractField<?>)component, propertyDispatcher, updateUi);
        } else if (component instanceof Button) {
            return new ButtonBinding(label, (Button)component, propertyDispatcher, updateUi, false);
        } else if (component instanceof Label) {
            return new LabelBinding(label, (Label)component, propertyDispatcher);
        } else {
            throw new IllegalArgumentException("Cannot create a binding for component " + component);
        }
    }

    @Override
    public String getPmoPropertyName() {
        return getBindingDefinition().getPmoPropertyName();
    }

}
