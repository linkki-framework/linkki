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
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

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
import org.linkki.core.ui.nls.NlsText;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.ComponentAnnotationReader;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.core.uicreation.layout.LayoutDefinitionCreator;
import org.linkki.core.uicreation.layout.LinkkiLayout;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;
import org.linkki.core.uiframework.UiFramework;
import org.linkki.util.BeanUtils;
import org.linkki.util.handler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

/**
 * Creates a {@link Grid} component.
 * <p>
 * If the value aspect is of type {@link CompletableFuture}, the items of this table are set
 * asynchronously when the future is completed. Otherwise, the items are set immediately.
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
            GridVariant.LUMO_NO_BORDER
    };

    class UITableComponentDefinitionCreator implements ComponentDefinitionCreator<UITableComponent> {

        @Override
        public LinkkiComponentDefinition create(UITableComponent annotation, AnnotatedElement annotatedElement) {
            return new GridComponentDefinition(annotation.variants());
        }
    }

    class UITableComponentAspectDefinitionCreator implements AspectDefinitionCreator<UITableComponent> {

        @Override
        public LinkkiAspectDefinition create(UITableComponent annotation) {
            return new GridItemsAspectDefinition();
        }
    }

    class GridItemsAspectDefinition implements LinkkiAspectDefinition {

        private static final Logger LOGGER = LoggerFactory.getLogger(GridItemsAspectDefinition.class);

        private static final String NAME = LinkkiAspectDefinition.VALUE_ASPECT_NAME;
        private static final String ATTR_HAS_ITEMS = "has-items";
        private static final String ATTR_LOADING = "items-loading";
        private static final String ATTR_HAS_ERRORS = "has-errors";
        private static final String CSS_PROPERTY_ERROR_MESSAGES = "--error-message";
        private static final String MSG_CODE_ERROR_MESSAGES = "GridItemsAspectDefinition.error";

        @Override
        public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
            @SuppressWarnings("unchecked")
            var grid = (Grid<Object>)componentWrapper.getComponent();
            var ui = UI.getCurrent();
            var locale = UiFramework.getLocale();
            if (CompletableFuture.class.isAssignableFrom(propertyDispatcher.getValueClass())) {
                if (!ui.getPushConfiguration().getPushMode().isEnabled()) {
                    if (LOGGER.isWarnEnabled()) {
                        LOGGER.warn(("""
                                CompletableFuture is used to retrieve table items with %s although server push is not \
                                enabled.

                                This will cause update to be not reflected in the UI immediately. (%s)""")
                                .formatted(UITableComponent.class.getSimpleName(),
                                           Optional.ofNullable(propertyDispatcher.getBoundObject())
                                                   .map(Object::getClass)
                                                   .map(Class::getName)
                                                   .orElse("null") + "#"
                                                   + propertyDispatcher.getProperty()));
                    }
                }
                return () -> {
                    grid.getElement().setAttribute(ATTR_LOADING, true);
                    grid.getElement().setAttribute(ATTR_HAS_ERRORS, false);
                    grid.getElement().getStyle().remove(CSS_PROPERTY_ERROR_MESSAGES);
                    @SuppressWarnings("unchecked")
                    var future = (CompletableFuture<List<Object>>)getAspectValue(propertyDispatcher);
                    future.whenComplete(onFutureComplete(ui, grid, locale));
                };
            } else {
                return () -> {
                    List<Object> items = getAspectValue(propertyDispatcher);
                    setItems(grid, items);
                };
            }
        }

        private BiConsumer<List<Object>, Throwable> onFutureComplete(UI ui, Grid<Object> grid, Locale locale) {
            return (items, throwable) -> ui.access(() -> {
                if (throwable != null) {
                    LOGGER.error("An error occurred when retrieving table items", throwable);
                    setItems(grid, List.of());
                    grid.getElement().setAttribute(ATTR_HAS_ERRORS, true);
                    grid.getElement().getStyle()
                            .set(CSS_PROPERTY_ERROR_MESSAGES,
                                 "\"" + NlsText.getString(MSG_CODE_ERROR_MESSAGES, locale) + "\"");
                } else {
                    setItems(grid, items);
                }
                grid.getElement().removeAttribute(ATTR_LOADING);
            });
        }

        private <T> T getAspectValue(PropertyDispatcher propertyDispatcher) {
            return requireNonNull(propertyDispatcher
                    .pull(Aspect.of(NAME)), "List of items must not be null");
        }

        private void setItems(Grid<Object> grid, List<Object> newItems) {
            grid.setItems(newItems);
            grid.getElement().setAttribute(ATTR_HAS_ITEMS, !newItems.isEmpty());
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
