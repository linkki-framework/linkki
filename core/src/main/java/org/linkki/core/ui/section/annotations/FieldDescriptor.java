/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nullable;

import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.FieldBinding;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * Holds all information about a field, which are the property name as well as the settings for
 * visibility, enabled-state etc. The given property name is only used as fallback if there is
 * {@link UIFieldDefinition#modelAttribute()} is not set.
 */
public class FieldDescriptor extends AbstractFieldDescriptor {

    /**
     * Constructs a new field description.
     *
     * @param fieldDef field definition that holds given annotated properties
     * @param toolTipDefinition text and type of the tooltip
     * @param pmoPropertyName name of the corresponding method in the PMO
     * @param pmoClass presentation model object class type
     */
    public FieldDescriptor(UIFieldDefinition fieldDef, UIToolTipDefinition toolTipDefinition, String pmoPropertyName,
            Class<?> pmoClass) {
        super(fieldDef, toolTipDefinition, pmoPropertyName, pmoClass);
    }

    @Override
    public ElementBinding createBinding(PropertyDispatcher propertyDispatcher,
            Handler updateUi,
            Component component,
            @Nullable Label label) {
        requireNonNull(propertyDispatcher, "propertyDispatcher must not be null");
        requireNonNull(updateUi, "updateUi must not be null");
        requireNonNull(component, "component must not be null");
        return new FieldBinding<>(label, (AbstractField<?>)component, propertyDispatcher, updateUi);
    }

}
