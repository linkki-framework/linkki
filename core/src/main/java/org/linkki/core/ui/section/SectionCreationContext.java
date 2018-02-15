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
package org.linkki.core.ui.section;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import org.linkki.core.ButtonPmo;
import org.linkki.core.PresentationModelObject;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ButtonPmoBinding;
import org.linkki.core.nls.pmo.PmoLabelType;
import org.linkki.core.nls.pmo.PmoNlsService;
import org.linkki.core.ui.section.annotations.SectionID;
import org.linkki.core.ui.section.annotations.SectionLayout;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.adapters.UISectionDefinition;
import org.linkki.core.ui.section.descriptor.ElementDescriptor;
import org.linkki.core.ui.section.descriptor.PropertyElementDescriptors;
import org.linkki.core.ui.section.descriptor.UIAnnotationReader;
import org.linkki.util.BeanUtils;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * Object holding the state about PMO and binding context, as well as the property dispatcher in
 * use, while creating a section for them. Intended to be used only once.
 * <p>
 * Only used for {@link FormSection}.
 */
public class SectionCreationContext {

    private PmoNlsService pmoNlsService;

    private final Object pmo;
    private final BindingContext bindingContext;


    public SectionCreationContext(Object pmo, BindingContext bindingContext) {
        this.pmo = requireNonNull(pmo, "pmo must not be null");
        this.bindingContext = requireNonNull(bindingContext, "bindingContext must not be null");
        pmoNlsService = PmoNlsService.get();
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
        UISection sectionDefinition = UISectionDefinition.from(pmo);

        Optional<Button> editButton = createEditButton(getEditButtonPmo());
        SectionLayout layout = sectionDefinition.layout();
        String caption = pmoNlsService.getLabel(PmoLabelType.SECTION_CAPTION, pmo.getClass(), null,
                                                sectionDefinition.caption());

        BaseSection section = createSection(sectionDefinition, editButton, layout, caption);

        section.setId(getSectionId());
        return section;
    }

    private BaseSection createSection(UISection sectionDefinition,
            Optional<Button> editButton,
            SectionLayout layout,
            String caption) {
        switch (layout) {
            case COLUMN:
                return new FormSection(caption,
                        sectionDefinition.closeable(),
                        editButton,
                        sectionDefinition.columns());
            case HORIZONTAL:
                return new HorizontalSection(caption,
                        sectionDefinition.closeable(),
                        editButton);
            case CUSTOM:
                return new CustomLayoutSection(pmo.getClass().getSimpleName(), caption,
                        sectionDefinition.closeable(),
                        editButton);
            default:
                throw new IllegalStateException("unknown SectionLayout#" + layout);
        }
    }

    private String getSectionId() {
        Optional<Method> idMethod = BeanUtils.getMethod(pmo.getClass(), (m) -> m.isAnnotationPresent(SectionID.class));
        return idMethod.map(this::getIdFromSectionIdMethod).orElse(pmo.getClass().getSimpleName());
    }

    private String getIdFromSectionIdMethod(Method m) {
        try {
            return requireNonNull((String)m.invoke(pmo), "The method annotated with @" + SectionID.class.getSimpleName()
                    + " must not return null.");
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
        for (PropertyElementDescriptors elementDescriptors : annotationReader.getUiElements()) {
            ElementDescriptor uiElement = elementDescriptors.getDescriptor(pmo);
            LabelComponent lf = createLabelAndComponent(section, uiElement);
            bindUiElement(uiElement, lf.component, lf.label);
        }
    }

    private LabelComponent createLabelAndComponent(BaseSection section, ElementDescriptor uiElement) {
        Component component = uiElement.newComponent();
        String labelText = uiElement.getLabelText();
        String pmoPropertyName = uiElement.getPmoPropertyName();
        Label label = new Label(labelText);
        section.add(pmoPropertyName, label, component);
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
            this.label = requireNonNull(label, "label must not be null");
            this.component = requireNonNull(component, "component must not be null");
        }
    }

    @UISection
    private static final class DefaultUISection {
        // Default UISection class if there is no @UISection annotation.
        // This way, the default values from the @UISection annotation get used.
    }

}