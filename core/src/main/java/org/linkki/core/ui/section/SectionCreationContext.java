/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkNotNull;
import static java.util.Objects.requireNonNull;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.linkki.core.ButtonPmo;
import org.linkki.core.PresentationModelObject;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ButtonPmoBinding;
import org.linkki.core.ui.section.annotations.ElementDescriptor;
import org.linkki.core.ui.section.annotations.SectionLayout;
import org.linkki.core.ui.section.annotations.UIAnnotationReader;
import org.linkki.core.ui.section.annotations.UISection;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * Object holding the state about PMO and binding context, as well as the property dispatcher in
 * use, while creating a section for them. Intended to be used only once.
 *
 * @author widmaier
 */
public class SectionCreationContext {

    private final PresentationModelObject pmo;
    private final BindingContext bindingContext;

    public SectionCreationContext(@Nonnull PresentationModelObject pmo, @Nonnull BindingContext bindingContext) {
        this.pmo = requireNonNull(pmo, "PresentationModelObject must not be null");
        this.bindingContext = requireNonNull(bindingContext, "BindingContext must not be null");
    }

    protected PresentationModelObject getPmo() {
        return pmo;
    }

    protected Object getModelObject() {
        return pmo.getModelObject();
    }

    public BaseSection createSection() {
        BaseSection section = createEmptySection();
        createUiElements(section);
        return section;
    }

    private BaseSection createEmptySection() {
        BaseSection section;
        UISection sectionDefinition = pmo.getClass().getAnnotation(UISection.class);
        checkNotNull(sectionDefinition, "PMO " + pmo.getClass() + " must be annotated with @UISection!");
        Optional<Button> editButton = createEditButton(pmo.getEditButtonPmo());
        if (sectionDefinition.layout() == SectionLayout.COLUMN) {
            section = new FormSection(sectionDefinition.caption(), sectionDefinition.closeable(), editButton,
                    sectionDefinition.columns());
        } else {
            section = new HorizontalSection(sectionDefinition.caption(), sectionDefinition.closeable(), editButton);
        }
        return section;
    }

    private Optional<Button> createEditButton(Optional<ButtonPmo> buttonPmo) {
        return buttonPmo.map(b -> ButtonPmoBinding.createBoundButton(bindingContext, b));
    }

    private void createUiElements(BaseSection section) {
        UIAnnotationReader annotationReader = new UIAnnotationReader(getPmo().getClass());
        for (ElementDescriptor uiElement : annotationReader.getUiElements()) {
            LabelComponent lf = createLabelAndComponent(section, uiElement);
            bindUiElement(lf.component, uiElement, lf.label);
        }
    }

    private LabelComponent createLabelAndComponent(BaseSection section, ElementDescriptor uiElement) {
        Component component = uiElement.newComponent();
        String labelText = uiElement.getLabelText();
        Label label = section.add(labelText, component);
        return new LabelComponent(label, component);
    }

    private void bindUiElement(Component component, ElementDescriptor uiElement, Label label) {
        uiElement.createBinding(bindingContext, pmo, label, component);
    }

    private static class LabelComponent {

        private final Label label;
        private final Component component;

        LabelComponent(Label label, Component component) {
            this.label = label;
            this.component = component;
        }
    }

}