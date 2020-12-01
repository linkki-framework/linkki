package org.linkki.core.ui.creation.section;

import java.lang.reflect.Method;
import java.util.Optional;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.pmo.ButtonPmo;
import org.linkki.core.pmo.PresentationModelObject;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.wrapper.CaptionComponentWrapper;
import org.linkki.core.ui.wrapper.LabelComponentWrapper;
import org.linkki.core.uicreation.ComponentAnnotationReader;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;
import org.linkki.core.vaadin.component.section.AbstractSection;
import org.linkki.core.vaadin.component.section.BaseSection;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * Defines how UI components are added to an {@link AbstractSection}.
 * 
 * @see SectionComponentDefiniton SectionComponentDefiniton for the creation of the section
 */
public enum SectionLayoutDefinition implements LinkkiLayoutDefinition {

    /**
     * The default uses {@link LabelComponentWrapper LabelComponentWrappers} for section content.
     */
    DEFAULT,

    /**
     * Uses {@link CaptionComponentWrapper CaptionComponentWrappers} for section content.
     */
    CAPTION_ONLY {
        @Override
        void addSectionComponent(Method method, BaseSection section, Object pmo, BindingContext bindingContext) {
            CaptionComponentWrapper wrapper = UiCreator.createUiElement(method, pmo, bindingContext,
                                                                        c -> new CaptionComponentWrapper((Component)c,
                                                                                WrapperType.FIELD));

            Component component = wrapper.getComponent();
            section.add(component.getId(), new Label(), wrapper.getComponent());
        }
    };

    /**
     * {@inheritDoc}
     * <p>
     * The parent component must be an {@link AbstractSection}.
     * 
     * @throws ClassCastException if the parent component is not an {@link AbstractSection}.
     */
    @Override
    public void createChildren(Object parentComponent, Object pmo, BindingContext bindingContext) {
        createHeaderContent((AbstractSection)parentComponent, pmo, bindingContext);
        createSectionContent(parentComponent, pmo, bindingContext);
    }

    private void createHeaderContent(AbstractSection section, Object pmo, BindingContext bindingContext) {
        ComponentAnnotationReader.getComponentDefinitionMethods(pmo.getClass())
                .filter(method -> method.isAnnotationPresent(SectionHeader.class))
                .forEach(method -> addHeaderComponent(method, section, pmo, bindingContext));

        getHeaderButtonPmo(pmo)
                .map(b -> ButtonPmoBinder.createBoundButton(bindingContext, b))
                .ifPresent(section::addHeaderButton);
    }

    private Optional<ButtonPmo> getHeaderButtonPmo(Object pmo) {
        if (pmo instanceof PresentationModelObject) {
            return ((PresentationModelObject)pmo).getEditButtonPmo();
        } else if (pmo instanceof ContainerPmo<?>) {
            return ((ContainerPmo<?>)pmo).getAddItemButtonPmo();
        } else {
            return Optional.empty();
        }
    }

    private void addHeaderComponent(Method method, AbstractSection section, Object pmo, BindingContext bindingContext) {
        CaptionComponentWrapper wrapper = UiCreator.createUiElement(method, pmo, bindingContext,
                                                                    c -> new CaptionComponentWrapper((Component)c,
                                                                            WrapperType.COMPONENT));

        section.addHeaderComponent(wrapper.getComponent());
    }

    private void createSectionContent(Object parentComponent, Object pmo, BindingContext bindingContext) {
        BaseSection section = (BaseSection)parentComponent;
        ComponentAnnotationReader.getComponentDefinitionMethods(pmo.getClass())
                .filter(method -> !method.isAnnotationPresent(SectionHeader.class))
                .forEach(method -> addSectionComponent(method, section, pmo, bindingContext));
    }

    void addSectionComponent(Method method, BaseSection section, Object pmo, BindingContext bindingContext) {
        Label label = new Label();
        LabelComponentWrapper wrapper = UiCreator.createUiElement(method, pmo, bindingContext,
                                                                  c -> new LabelComponentWrapper(label,
                                                                          (Component)c));

        Component component = wrapper.getComponent();
        section.add(component.getId(), label, wrapper.getComponent());
    }

}