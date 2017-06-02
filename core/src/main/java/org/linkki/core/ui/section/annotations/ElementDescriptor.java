/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.nls.pmo.PmoLabelType;
import org.linkki.core.nls.pmo.PmoNlsService;

import com.vaadin.ui.Component;

/**
 * Holds information about a bound UI element (such as the settings for visibility, enabled-state
 * etc.) and on how to create and display such an UI element.
 */
public abstract class ElementDescriptor extends BindingDescriptor {

    private PmoNlsService pmoNlsService;
    private Class<?> pmoClass;

    public ElementDescriptor(BindingDefinition bindingDefinition, UIToolTipDefinition toolTipDefinition,
            Class<?> pmoClass) {
        super(bindingDefinition, toolTipDefinition);
        this.pmoClass = pmoClass;
        pmoNlsService = PmoNlsService.get();
    }

    @Override
    protected UIElementDefinition getBindingDefinition() {
        return (UIElementDefinition)super.getBindingDefinition();
    }

    /** The position of the UI element in its parent/container. */
    public int getPosition() {
        return getBindingDefinition().position();
    }

    /**
     * @return the label text as defined by the {@link PmoNlsService}, with fall back to the label
     *         defined in the annotation and further to the property name.
     */
    @SuppressWarnings("null")
    public String getLabelText() {
        if (!getBindingDefinition().showLabel()) {
            return "";
        }

        String label = getBindingDefinition().label();
        if (StringUtils.isEmpty(label)) {
            label = StringUtils.capitalize(getModelPropertyName());
        }
        return pmoNlsService.getLabel(PmoLabelType.PROPERTY_LABEL, pmoClass, getPmoPropertyName(), label);
    }


    /** Creates a new Vaadin UI component for this UI element. */
    public Component newComponent() {
        return getBindingDefinition().newComponent();
    }


}
