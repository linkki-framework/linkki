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
package org.linkki.core.ui.table.column.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.table.column.annotation.UITableColumn.TableColumnAspectDefinitionCreator;
import org.linkki.core.ui.table.column.aspects.ColumnCollapseAspectDefinition;
import org.linkki.core.ui.table.column.aspects.ColumnExpandRatioAspectDefinition;
import org.linkki.core.ui.table.column.aspects.ColumnWidthAspectDefinition;


/**
 * Annotation that allows to customize the column for a PMO's field/method that is rendered in a table
 * column.
 * 
 * @implNote This annotation is <em>not</em> required for a field/method to be rendered in a column. All
 *           fields/methods with one of the {@code @UI...} annotations
 *           ({@link UITextField @UITextField}, {@link UICheckBox @UICheckBox} etc.) are rendered
 *           automatically. This annotation allows optional customization and can be omitted if no
 *           customization is needed.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiAspect(TableColumnAspectDefinitionCreator.class)
public @interface UITableColumn {

    static final int UNDEFINED_WIDTH = -1;
    static final float UNDEFINED_EXPAND_RATIO = -1.0f;

    /**
     * Configures the width in pixels for the column.
     * 
     * @implSpec The default value of -1 means that the column can be sized freely by the layout.
     * 
     * @implNote This attribute is mutually exclusive with {@link #expandRatio()}.
     * 
     * @see com.vaadin.v7.ui.Table#setColumnWidth(Object, int)
     */
    @SuppressWarnings("javadoc")
    int width() default UNDEFINED_WIDTH;

    /**
     * Configures the expand ratio for the column.
     * 
     * @implSpec The expand ratio defines what part of excess available space the layout allots to this
     *           column.
     * 
     * @implNote This attribute is mutually exclusive with {@link #width()}.
     * 
     * @see com.vaadin.v7.ui.Table#setColumnExpandRatio(Object, float)
     */
    @SuppressWarnings("javadoc")
    float expandRatio() default UNDEFINED_EXPAND_RATIO;

    /**
     * Configures whether a column can be collapsed and whether it initially is.
     * 
     * @implNote Table columns are by default not collapsible.
     */
    CollapseMode collapsible() default CollapseMode.NOT_COLLAPSIBLE;

    /**
     * Defines whether a column can be collapsed and whether it initially is.
     */
    public enum CollapseMode {
        /**
         * The column is always shown.
         */
        NOT_COLLAPSIBLE(false, false),
        /**
         * The column is collapsible and is initially visible.
         */
        COLLAPSIBLE(true, false),
        /**
         * The column is collapsible and is initially collapsed.
         */
        INITIALLY_COLLAPSED(true, true);

        private final boolean collapsible;
        private final boolean initiallyCollapsed;

        private CollapseMode(boolean collapsible, boolean initiallyCollapsed) {
            this.collapsible = collapsible;
            this.initiallyCollapsed = initiallyCollapsed;
        }

        public boolean isCollapsible() {
            return collapsible;
        }

        public boolean isInitiallyCollapsed() {
            return initiallyCollapsed;
        }
    }

    static class TableColumnAspectDefinitionCreator implements AspectDefinitionCreator<UITableColumn> {

        @Override
        public LinkkiAspectDefinition create(UITableColumn annotation) {
            float expandRatio = annotation.expandRatio();
            int width = annotation.width();
            if (expandRatio != UNDEFINED_EXPAND_RATIO && width != UNDEFINED_WIDTH) {
                throw new IllegalStateException("The attributes \"" +
                        ColumnWidthAspectDefinition.NAME + "\" and \"" + ColumnExpandRatioAspectDefinition.NAME
                        + "\" should not be both defined in a @" + UITableColumn.class.getSimpleName()
                        + " annotation.");
            }
            return new CompositeAspectDefinition(
                    new ColumnCollapseAspectDefinition(annotation.collapsible()),
                    new ColumnExpandRatioAspectDefinition(expandRatio),
                    new ColumnWidthAspectDefinition(width));
        }

    }

}
