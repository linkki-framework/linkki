package org.linkki.core.ui.creation.section;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.section.Sections;
import org.linkki.core.ui.wrapper.LabelComponentWrapper;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;
import org.linkki.core.vaadin.component.section.BaseSection;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * Defines how UI components are added to a {@link BaseSection}.
 * 
 * @see SectionComponentDefiniton SectionComponentDefiniton for the creation of the section
 */
public enum SectionLayoutDefinition implements LinkkiLayoutDefinition {

    DEFAULT;

    /**
     * {@inheritDoc}
     * <p>
     * The parent component must be a {@link BaseSection}.
     * 
     * @throws ClassCastException if the parent component is no {@link BaseSection}.
     */
    @Override
    public void createChildren(Object parentComponent, Object pmo, BindingContext bindingContext) {
        BaseSection section = (BaseSection)parentComponent;
        Sections.getEditButtonPmo(pmo)
                .map(b -> ButtonPmoBinder.createBoundButton(bindingContext, b))
                .ifPresent(section::addHeaderButton);
        UiCreator.createUiElements(pmo, bindingContext,
                                   c -> new LabelComponentWrapper(new Label(), (Component)c))
                .forEach(w -> add(section, w));
    }

    private void add(BaseSection section, LabelComponentWrapper wrapper) {
        Label label = wrapper.getLabelComponent().get();
        Component component = wrapper.getComponent();
        section.add(component.getId(), label, component);
    }

}