/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.section;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

import de.faktorzehn.ipm.web.EditAction;
import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.binding.BindingContext;
import de.faktorzehn.ipm.web.binding.FieldBinding;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyBehaviorProvider;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcherFactory;
import de.faktorzehn.ipm.web.ui.section.annotations.FieldDescriptor;
import de.faktorzehn.ipm.web.ui.section.annotations.SectionLayout;
import de.faktorzehn.ipm.web.ui.section.annotations.UIAnnotationReader;
import de.faktorzehn.ipm.web.ui.section.annotations.UISection;

/**
 * Object holding the state about PMO and binding context, as well as the property dispatcher in
 * use, while creating a section for them. Intended to be used only once.
 *
 * @author widmaier
 */
public class SectionCreationContext {

    private static final PropertyDispatcherFactory DISPATCHER_FACTORY = new PropertyDispatcherFactory();

    private final PresentationModelObject pmo;
    private final BindingContext bindingContext;
    private final PropertyBehaviorProvider propertyBehaviorProvider;

    private PropertyDispatcher propertyDispatcher;

    public SectionCreationContext(PresentationModelObject pmo, BindingContext bindingContext,
            PropertyBehaviorProvider propertyBehaviorProvider) {
        this.pmo = pmo;
        this.bindingContext = bindingContext;
        this.propertyBehaviorProvider = propertyBehaviorProvider;
    }

    /* package private for testing */
    protected PropertyDispatcher createDefaultDispatcher() {
        return DISPATCHER_FACTORY.defaultDispatcherChain(pmo, propertyBehaviorProvider);
    }

    protected PresentationModelObject getPmo() {
        return pmo;
    }

    protected Object getModelObject() {
        return pmo.getModelObject();
    }

    public BaseSection createSection() {
        BaseSection section = createEmptySection();
        createFields(section);
        return section;
    }

    private BaseSection createEmptySection() {
        BaseSection section;
        UISection sectionDefinition = pmo.getClass().getAnnotation(UISection.class);
        checkNotNull(sectionDefinition, "PMO " + pmo.getClass() + " must be annotated with @UISection!");
        Optional<EditAction> editAction = pmo.getEditAction();
        if (sectionDefinition.layout() == SectionLayout.COLUMN) {
            section = new FormSection(sectionDefinition.caption(), sectionDefinition.closeable(), editAction);
        } else {
            section = new HorizontalSection(sectionDefinition.caption(), sectionDefinition.closeable(), editAction);
        }
        return section;
    }

    private void createFields(BaseSection section) {
        UIAnnotationReader annotationReader = new UIAnnotationReader(getPmo().getClass());
        for (FieldDescriptor fieldDesc : annotationReader.getFields()) {
            SectionCreationContext.LabelField lf = createLabelAndField(section, fieldDesc);
            bindField(lf.field, fieldDesc, lf.label);
        }
    }

    private SectionCreationContext.LabelField createLabelAndField(BaseSection section, FieldDescriptor fieldDesc) {
        Component component = fieldDesc.newComponent();
        String labelText = fieldDesc.getLabelText();
        Label label = section.add(labelText, component);
        return new LabelField(label, (Field<?>)component);
    }

    private void bindField(Field<?> vaadinField, FieldDescriptor fieldDesc, Label label) {
        FieldBinding<?> fieldBinding = FieldBinding.create(bindingContext, fieldDesc.getPropertyName(), label,
                                                           vaadinField, getPropertyDispatcher());
        bindingContext.add(fieldBinding);
    }

    protected PropertyDispatcher getPropertyDispatcher() {
        if (propertyDispatcher == null) {
            propertyDispatcher = createDefaultDispatcher();
        }
        return propertyDispatcher;
    }

    private static class LabelField {
        public Label label;
        public Field<?> field;

        LabelField(Label label, Field<?> field) {
            this.label = label;
            this.field = field;
        }
    }

}