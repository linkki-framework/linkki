/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import org.linkki.core.ButtonPmo;
import org.linkki.core.PresentationModelObject;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ButtonPmoBinding;
import org.linkki.core.ui.section.annotations.ElementDescriptor;
import org.linkki.core.ui.section.annotations.SectionID;
import org.linkki.core.ui.section.annotations.SectionLayout;
import org.linkki.core.ui.section.annotations.UIAnnotationReader;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.util.BeanUtils;

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

    private final Object pmo;
    private final BindingContext bindingContext;

    public SectionCreationContext(Object pmo, BindingContext bindingContext) {
        requireNonNull(pmo, "pmo must not be null");
        requireNonNull(bindingContext, "bindingContext must not be null");
        this.pmo = pmo;
        this.bindingContext = bindingContext;
    }

    protected Object getPmo() {
        return pmo;
    }

    public BaseSection createSection() {
        BaseSection section = createEmptySection();
        createUiElements(section);
        return section;
    }

    private BaseSection createEmptySection() {
        BaseSection section;
        UISection sectionDefinition = requireNonNull(pmo.getClass()
                .getAnnotation(UISection.class), () -> "PMO " + pmo.getClass() + " must be annotated with @UISection!");
        Optional<Button> editButton = createEditButton(getEditButtonPmo());
        if (sectionDefinition.layout() == SectionLayout.COLUMN) {
            section = new FormSection(sectionDefinition.caption(), sectionDefinition.closeable(), editButton,
                    sectionDefinition.columns());
        } else {
            section = new HorizontalSection(sectionDefinition.caption(), sectionDefinition.closeable(), editButton);
        }
        section.setId(getSectionId());
        return section;
    }

    private String getSectionId() {
        Optional<Method> idMethod = BeanUtils.getMethod(pmo.getClass(), (m) -> m.isAnnotationPresent(SectionID.class));
        return idMethod.map(this::getId).orElse(pmo.getClass().getSimpleName());
    }

    public String getId(Method m) {
        try {
            return (String)m.invoke(pmo);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<ButtonPmo> getEditButtonPmo() {
        return (pmo instanceof PresentationModelObject) ? ((PresentationModelObject)pmo).getEditButtonPmo()
                : Optional.empty();
    }

    private Optional<Button> createEditButton(Optional<ButtonPmo> buttonPmo) {
        return buttonPmo.map(b -> ButtonPmoBinding.createBoundButton(bindingContext, b));
    }

    private void createUiElements(BaseSection section) {
        UIAnnotationReader annotationReader = new UIAnnotationReader(getPmo().getClass());
        for (ElementDescriptor uiElement : annotationReader.getUiElements()) {
            LabelComponent lf = createLabelAndComponent(section, uiElement);
            bindUiElement(uiElement, lf.component, lf.label);
        }
    }

    private LabelComponent createLabelAndComponent(BaseSection section, ElementDescriptor uiElement) {
        Component component = uiElement.newComponent();
        String labelText = uiElement.getLabelText();
        Label label = section.add(labelText, component);
        return new LabelComponent(label, component);
    }

    private void bindUiElement(ElementDescriptor elementDescriptor, Component component, Label label) {
        component.setId(elementDescriptor.getPmoPropertyName());
        bindingContext.bind(pmo, elementDescriptor, component, label);
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