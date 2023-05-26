/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.core.ui.creation.section;

import java.lang.reflect.Method;
import java.util.Optional;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.pmo.ButtonPmo;
import org.linkki.core.pmo.PresentationModelObject;
import org.linkki.core.ui.creation.table.GridComponentCreator;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.wrapper.FormItemComponentWrapper;
import org.linkki.core.ui.wrapper.LabelComponentWrapper;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.ui.wrapper.VaadinComponentWrapper;
import org.linkki.core.uicreation.ComponentAnnotationReader;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;
import org.linkki.core.vaadin.component.base.LabelComponentFormItem;
import org.linkki.core.vaadin.component.section.BaseSection;
import org.linkki.core.vaadin.component.section.GridSection;
import org.linkki.core.vaadin.component.section.LinkkiSection;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeLabel;

/**
 * Defines how UI components are added to an {@link LinkkiSection}.
 * 
 * @see SectionComponentDefiniton SectionComponentDefiniton for the creation of the section
 */
public enum SectionLayoutDefinition implements LinkkiLayoutDefinition {

    /**
     * Uses {@link FormItemComponentWrapper FormItemComponentWrappers} for section content. Labels are
     * shown aside of the components.
     */
    DEFAULT {
        @Override
        protected VaadinComponentWrapper createComponentWrapperAndAddComponentToSection(BaseSection section, 
                                                                                        NativeLabel label,
                                                                                        Component component) {
            LabelComponentFormItem formItem = new LabelComponentFormItem(component, label);
            FormItemComponentWrapper wrapper = new FormItemComponentWrapper(formItem);
            section.addContent(formItem);
            return wrapper;
        }
    },
    /**
     * Uses {@link LabelComponentWrapper} for section content. Labels are shown on top of the
     * components.
     */
    LABEL_ON_TOP {
        @Override
        protected VaadinComponentWrapper createComponentWrapperAndAddComponentToSection(BaseSection section,
                                                                                        NativeLabel label, 
                                                                                        Component component) {
            VaadinComponentWrapper componentWrapper = new LabelComponentWrapper(component, WrapperType.FIELD);
            section.addContent(componentWrapper.getComponent());
            return componentWrapper;
        }
    };

    /**
     * {@inheritDoc}
     * <p>
     * The parent component must be an {@link LinkkiSection}.
     * 
     * @throws ClassCastException if the parent component is not an {@link LinkkiSection}.
     */
    @Override
    public void createChildren(Object parentComponent, Object pmo, BindingContext bindingContext) {
        createHeaderContent((LinkkiSection)parentComponent, pmo, bindingContext);
        if (pmo instanceof ContainerPmo) {
            createTable(parentComponent, pmo, bindingContext);
        } else {
            createSectionContent(parentComponent, pmo, bindingContext);
        }
    }

    private void createHeaderContent(LinkkiSection section, Object pmo, BindingContext bindingContext) {
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

    private void addHeaderComponent(Method method, LinkkiSection section, Object pmo, BindingContext bindingContext) {
        NoLabelComponentWrapper wrapper = UiCreator.createUiElement(method, pmo, bindingContext,
                                                                    c -> new NoLabelComponentWrapper((Component)c,
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
        UiCreator.createUiElement(method, pmo, bindingContext,
                                  c -> createComponentWrapperAndAddComponentToSection(section, new NativeLabel(),
                                                                                      (Component)c));
    }

    /**
     * Creates the component wrapper for the given {@link NativeLabel} and {@link Component} and adds it to
     * the given {@link BaseSection}.
     * <p>
     * Note that it is necessary to add the component directly to the section in this method. In case of
     * a {@link FormItemComponentWrapper}, the {@link LabelComponentFormItem} has to be added to the
     * section, and not the given {@link Component} itself. However, it is not possible to retrieve the
     * created {@link LabelComponentFormItem} from the {@link FormItemComponentWrapper} after creation.
     * Thus, the {@link LabelComponentFormItem} has to be added to the section before it is returned.
     */
    protected abstract VaadinComponentWrapper createComponentWrapperAndAddComponentToSection(BaseSection section,
                                                                                             NativeLabel label, 
                                                                                             Component component);

    private void createTable(Object parentComponent, Object pmo, BindingContext bindingContext) {
        GridSection section = (GridSection)parentComponent;
        Grid<?> grid = GridComponentCreator.createGrid((ContainerPmo<?>)pmo, bindingContext);
        section.setGrid(grid);
    }

}