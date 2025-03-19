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
package org.linkki.search.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator.EmptyPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.defaults.columnbased.aspects.ColumnBasedComponentFooterAspectDefinition;
import org.linkki.core.defaults.columnbased.pmo.TableFooterPmo;
import org.linkki.core.ui.creation.table.AbstractGridComponentWrapper;
import org.linkki.core.ui.creation.table.GridComponentDefinition;
import org.linkki.core.ui.creation.table.GridComponentWrapper;
import org.linkki.core.ui.creation.table.GridLayoutDefinition;
import org.linkki.core.ui.table.aspects.GridSelectionAspectDefinition;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.layout.LayoutDefinitionCreator;
import org.linkki.core.uicreation.layout.LinkkiLayout;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.provider.ListDataProvider;

/**
 * A table that displays search results
 *
 * @since 2.8.0
 */
@Retention(RUNTIME)
@Target(ElementType.TYPE)
@LinkkiComponent(UISearchTable.GridComponentDefinitionCreator.class)
@LinkkiLayout(UISearchTable.GridLayoutDefinitionCreator.class)
@LinkkiBoundProperty(EmptyPropertyCreator.class)
@LinkkiAspect(UISearchTable.TableAspectDefinitionCreator.class)
public @interface UISearchTable {

    GridVariant[] variants() default { GridVariant.LUMO_WRAP_CELL_CONTENT,
            GridVariant.LUMO_COMPACT,
            GridVariant.LUMO_NO_BORDER };

    class GridComponentDefinitionCreator implements ComponentDefinitionCreator<UISearchTable> {

        @Override
        public LinkkiComponentDefinition create(UISearchTable annotation, AnnotatedElement annotatedElement) {
            return new GridComponentDefinition(annotation.variants());
        }
    }

    class GridLayoutDefinitionCreator implements LayoutDefinitionCreator<UISearchTable> {

        @Override
        public LinkkiLayoutDefinition create(UISearchTable annotation, AnnotatedElement annotatedElement) {
            return new GridLayoutDefinition();
        }
    }

    class TableAspectDefinitionCreator implements AspectDefinitionCreator<UISearchTable> {

        @Override
        public LinkkiAspectDefinition create(UISearchTable annotation) {
            return new CompositeAspectDefinition(
                    new TablePageLengthAspectDefinition(),
                    new TableItemsAspectDefinition(),
                    new TableFooterAspectDefinition(),
                    new GridSelectionAspectDefinition() {

                        @Override
                        public boolean supports(WrapperType type) {
                            return true;
                        }
                    });
        }
    }

    class TableFooterAspectDefinition extends ModelToUiAspectDefinition<Optional<TableFooterPmo>> {

        public static final String NAME = "footerPmo";

        @Override
        public Aspect<Optional<TableFooterPmo>> createAspect() {
            return Aspect.of(NAME);
        }

        /**
         * Copied from {@link ColumnBasedComponentFooterAspectDefinition}
         */
        @Override
        public Consumer<Optional<TableFooterPmo>> createComponentValueSetter(ComponentWrapper componentWrapper) {
            return footerPmo -> {
                final Grid<?> grid = (Grid<?>)componentWrapper.getComponent();
                if (footerPmo.isPresent()) {
                    for (Column<?> column : grid.getColumns()) {
                        column.setFooter(footerPmo.get().getFooterText(column.getKey()));
                    }
                }
            };
        }

    }

    class TableItemsAspectDefinition implements LinkkiAspectDefinition {

        public static final String NAME = "items";

        /**
         * Copied from {@link GridComponentWrapper#setItems(List)}
         */
        @SuppressWarnings("unchecked")
        @Override
        public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
            ((Grid<Object>)componentWrapper.getComponent()).setItems(new ListDataProvider<>(new ArrayList<>()));
            return () -> {
                Collection<?> newItems = propertyDispatcher.pull(Aspect.of(NAME));
                var listDataProvider = (ListDataProvider<Object>)((Grid<Object>)componentWrapper
                        .getComponent()).getDataProvider();
                var backendList = listDataProvider.getItems();
                if (!Objects.equals(newItems, backendList)) {
                    backendList.clear();
                    backendList.addAll(newItems);
                    listDataProvider.refreshAll();
                }
            };
        }
    }

    class TablePageLengthAspectDefinition extends ModelToUiAspectDefinition<Integer> {

        public static final String NAME = "pageLength";

        @Override
        public Aspect<Integer> createAspect() {
            return Aspect.of(NAME);
        }

        /**
         * Copied from {@link AbstractGridComponentWrapper#setPageLength(int)}
         */
        @Override
        public Consumer<Integer> createComponentValueSetter(ComponentWrapper componentWrapper) {
            return pageLength -> {
                var table = (Grid<?>)componentWrapper.getComponent();
                if (pageLength < 1 && !table.isAllRowsVisible()) {
                    table.setAllRowsVisible(true);
                } else if (pageLength >= 1) {
                    if (table.isAllRowsVisible()) {
                        table.setAllRowsVisible(false);
                    }
                    if (table.getPageSize() != pageLength) {
                        table.setPageSize(pageLength);
                    }
                    var headerAndFooter = 1 + table.getFooterRows().size();
                    table.setHeight((pageLength + headerAndFooter) * 3 + "em");
                }
            };
        }
    }
}
