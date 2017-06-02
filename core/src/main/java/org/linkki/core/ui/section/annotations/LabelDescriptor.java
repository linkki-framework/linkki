/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nullable;

import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.LabelBinding;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class LabelDescriptor extends AbstractFieldDescriptor {

    public LabelDescriptor(UIFieldDefinition labelDefinition, UIToolTipDefinition toolTipDefinition,
            String pmoPropertyName, Class<?> pmoClass) {
        super(labelDefinition, toolTipDefinition, pmoPropertyName, pmoClass);
    }

    @Override
    public ElementBinding createBinding(PropertyDispatcher propertyDispatcher,
            Handler updateUi,
            Component component,
            @Nullable Label label) {
        requireNonNull(propertyDispatcher, "propertyDispatcher must not be null");
        requireNonNull(updateUi, "updateUi must not be null");
        requireNonNull(component, "component must not be null");
        return new LabelBinding(label, (Label)component, propertyDispatcher);
    }

}
