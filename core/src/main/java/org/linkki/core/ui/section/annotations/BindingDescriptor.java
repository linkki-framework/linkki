/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public abstract class BindingDescriptor {

    private final BindingDefinition bindingDefinition;
    private final UIToolTipDefinition toolTipDefinition;

    public BindingDescriptor(BindingDefinition bindingDefinition, UIToolTipDefinition toolTipDefinition) {
        this.bindingDefinition = bindingDefinition;
        this.toolTipDefinition = toolTipDefinition;
    }

    protected BindingDefinition getBindingDefinition() {
        return bindingDefinition;
    }

    public EnabledType enabled() {
        return bindingDefinition.enabled();
    }

    public VisibleType visible() {
        return bindingDefinition.visible();
    }

    public RequiredType required() {
        return bindingDefinition.required();
    }

    public AvailableValuesType availableValues() {
        return bindingDefinition.availableValues();
    }

    /**
     * The name of the property that the bound UI element displays. For an UI element that accesses
     * the field of a model/PMO class, this is the name of that field. For an UI element that
     * invokes a method (i.e. a button) this is the name of the method.
     */
    public abstract String getModelPropertyName();

    /**
     * The name of the model object containing the {@link #getModelPropertyName() property} if the
     * bound PMO itself does not contain the property. The PMO has to have a
     * {@link ModelObject @ModelObject} annotation with that name on the method that returns the
     * model object.
     */
    public abstract String getModelObjectName();

    /**
     * Creates a binding with the given dispatcher, the given handler for updating the UI and the
     * given UI components using the binding information from this descriptor.
     */
    public abstract ElementBinding createBinding(PropertyDispatcher propertyDispatcher,
            Handler updateUi,
            Component component,
            @Nullable Label label);

    /**
     * The name of the property from the pmo
     */
    public abstract String getPmoPropertyName();


    /**
     * The tooltip of the UI element
     */
    public String getToolTip() {
        return toolTipDefinition.text();
    }

    /**
     * The type of the tooltip
     */
    @CheckForNull
    public ToolTipType getToolTipType() {
        return toolTipDefinition.toolTipType();
    }
}