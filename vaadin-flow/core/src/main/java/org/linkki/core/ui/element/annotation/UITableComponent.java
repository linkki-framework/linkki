/*
 * Copyright Faktor Zehn GmbH.
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

package org.linkki.core.ui.element.annotation;

import static org.linkki.util.Objects.requireNonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.BindingDescriptor;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.staticvalue.StaticValueNlsService;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.aspects.LabelAspectDefinition;
import org.linkki.core.ui.creation.table.GridColumnWrapper;
import org.linkki.core.ui.creation.table.GridComponentDefinition;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.ComponentAnnotationReader;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.core.uicreation.layout.LayoutDefinitionCreator;
import org.linkki.core.uicreation.layout.LinkkiLayout;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;
import org.linkki.util.BeanUtils;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

/**
 * Creates a {@link Grid} component.
 * <p>
 * If {@link UI#getPushConfiguration() push} is enabled, the items are fetch asynchronously.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBoundProperty(BoundPropertyCreator.SimpleMemberNameBoundPropertyCreator.class)
@LinkkiAspect(UITableComponent.UITableComponentAspectDefinitionCreator.class)
@LinkkiComponent(UITableComponent.UITableComponentDefinitionCreator.class)
@LinkkiLayout(UITableComponent.UITableComponentLayoutDefinitionCreator.class)
@LinkkiPositioned
public @interface UITableComponent {

    @LinkkiPositioned.Position
    int position();

    /**
     * Defines the rows in the table. Must match the returned items, i.e. must be either the same
     * class or a super class of the row items.
     */
    // This method is technically necessary as the generic type of the items aspect is erased at
    // runtime.
    Class<?> rowPmoClass();

    GridVariant[] variants() default {
            GridVariant.LUMO_WRAP_CELL_CONTENT,
            GridVariant.LUMO_COMPACT,
            GridVariant.LUMO_NO_BORDER };

    class UITableComponentDefinitionCreator implements ComponentDefinitionCreator<UITableComponent> {

        @Override
        public LinkkiComponentDefinition create(UITableComponent annotation, AnnotatedElement annotatedElement) {
            return new GridComponentDefinition(annotation.variants());
        }
    }

    class UITableComponentAspectDefinitionCreator implements AspectDefinitionCreator<UITableComponent> {

        @Override
        public LinkkiAspectDefinition create(UITableComponent annotation) {
            return new GridItemsDefinition();
        }
    }

    class GridItemsDefinition implements LinkkiAspectDefinition {

        private static final String NAME = LinkkiAspectDefinition.VALUE_ASPECT_NAME;

        @Override
        public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
            @SuppressWarnings("unchecked")
            var grid = (Grid<Object>)componentWrapper.getComponent();
            var ui = UI.getCurrent();
            if (ui.getPushConfiguration().getPushMode().isEnabled()) {
                return () -> CompletableFuture
                        .supplyAsync(() -> getAspectValue(propertyDispatcher))
                        .whenComplete((items, throwable) -> ui.access(() -> setItems(grid, items)));
            } else {
                return () -> setItems(grid, getAspectValue(propertyDispatcher));
            }
        }

        private List<Object> getAspectValue(PropertyDispatcher propertyDispatcher) {
            return requireNonNull(propertyDispatcher
                    .pull(Aspect.of(NAME)), "List of items must not be null");
        }

        private void setItems(Grid<Object> grid, List<Object> newItems) {
            grid.setItems(newItems);
            grid.getElement().setAttribute("has-items", !newItems.isEmpty());
        }
    }

    class UITableComponentLayoutDefinitionCreator implements LayoutDefinitionCreator<UITableComponent> {

        @Override
        public LinkkiLayoutDefinition create(UITableComponent annotation, AnnotatedElement annotatedElement) {
            return new TableComponentLayoutDefinition(annotation.rowPmoClass());
        }
    }

    class TableComponentLayoutDefinition implements LinkkiLayoutDefinition {

        private final Class<?> rowPmoClass;

        public TableComponentLayoutDefinition(Class<?> rowPmoClass) {
            this.rowPmoClass = rowPmoClass;
        }

        @Override
        public void createChildren(Object parentComponent, Object pmo, BindingContext bindingContext) {
            var grid = (Grid<?>)parentComponent;
            ComponentAnnotationReader.getComponentDefinitionMethods(rowPmoClass)
                    .forEach(method -> {
                        var column = createColumn(grid, method, bindingContext);

                        var bindingDescriptor = BindingDescriptor.forMethod(method);
                        column.setKey(bindingDescriptor.getBoundProperty().getPmoProperty());
                        bindingContext.bind(rowPmoClass, bindingDescriptor, new GridColumnWrapper(column));
                    });
        }

        private Grid.Column<?> createColumn(Grid<?> grid, Method method, BindingContext bindingContext) {
            var column = grid.addComponentColumn(row -> UiCreator
                    .<Component, NoLabelComponentWrapper> createUiElement(method, row, bindingContext,
                                                                          NoLabelComponentWrapper::new)
                    .getComponent());
            column.setHeader(getHeaderText(rowPmoClass, method));
            return column;
        }

        private String getHeaderText(Class<?> pmoRow, Method method) {
            return StaticValueNlsService.getInstance().getString(pmoRow, BeanUtils.getPropertyName(method),
                                                                 LabelAspectDefinition.NAME,
                                                                 LinkkiAspectDefinition.DERIVED_BY_LINKKI);
        }
    }

}
