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
package org.linkki.core.ui.table.column.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.ui.aspects.types.TextAlignment;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.table.aspects.ColumnSortableAspectDefinition;
import org.linkki.core.ui.table.aspects.ColumnTextAlignAspectDefinition;
import org.linkki.core.ui.table.column.annotation.UITableColumn.TableColumnAspectDefinitionCreator;
import org.linkki.core.ui.table.column.aspects.ColumnCollapseAspectDefinition;
import org.linkki.core.ui.table.column.aspects.ColumnFlexGrowAspectDefinition;
import org.linkki.core.ui.table.column.aspects.ColumnWidthAspectDefinition;

import com.vaadin.flow.component.grid.Grid.Column;

/**
 * Annotation that allows to customize the column for a PMO's field/method that is rendered in a
 * table column.
 * 
 * @implNote This annotation is <em>not</em> required for a field/method to be rendered in a column.
 *           All fields/methods with one of the {@code @UI...} annotations
 *           ({@link UITextField @UITextField}, {@link UICheckBox @UICheckBox} etc.) are rendered
 *           automatically. This annotation allows optional customization and can be omitted if no
 *           customization is needed.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiAspect(TableColumnAspectDefinitionCreator.class)
public @interface UITableColumn {

    static final int UNDEFINED_WIDTH = -1;
    static final int UNDEFINED_FLEX_GROW = -1;

    /**
     * Configures the width in pixels for the column.
     * 
     * @implSpec The default value of -1 means that the column can be sized freely by the layout.
     * 
     * @see Column#setWidth(String)
     */
    int width() default UNDEFINED_WIDTH;

    /**
     * Defines how much the column should grow relative to other columns when there is extra space
     * available.
     * <p>
     * The returned integer defines how many parts of the excess space should be taken for this
     * column. If not set, the flex-grow is set to {@value UNDEFINED_FLEX_GROW}.
     * <p>
     * Note that if {@link #width()} is set, flex-grow will take effect in addition to
     * {@link #width()}. The column then has at least the defined width. If there is extra space, it
     * takes the defined part additionally.
     * 
     * @see Column#setFlexGrow(int)
     */
    int flexGrow() default UNDEFINED_FLEX_GROW;

    /**
     * Configures whether a column can be collapsed and whether it initially is.
     * <p>
     * If there are any collapsible columns in a table, a right aligned header menu is displayed in
     * the corresponding section with which the user can toggle the visibility of a collapsible
     * column.
     * <p>
     * A column can only be collapsible if the table is created inside of a section.
     * 
     * @implNote Table columns are by default not collapsible.
     */
    CollapseMode collapsible() default CollapseMode.NOT_COLLAPSIBLE;

    /**
     * Configures the alignment of text inside the table column, as well as the header and footer.
     * <p>
     * This does not affect the alignment of text inside fields.
     */
    TextAlignment textAlign() default TextAlignment.DEFAULT;

    /**
     * Configures whether a column can be sorted. The annotated method
     * <ul>
     * <li>must be a getter method (model objects/model attributes are not supported)</li>
     * <li>must return a type that is {@link Comparable}
     * </ul>
     */
    boolean sortable() default false;

    /**
     * Defines whether a column can be collapsed and whether it initially is.
     */
    public enum CollapseMode {
        /**
         * The column is always shown.
         */
        NOT_COLLAPSIBLE(false, false),
        /**
         * The column is collapsible (if the table is part of a section) and is initially visible.
         */
        COLLAPSIBLE(true, false),
        /**
         * The column is collapsible (if the table is part of a section) and is initially collapsed.
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
            int flexGrow = annotation.flexGrow();
            int width = annotation.width();
            // flexGrow needs to be set before width is set, as the width aspect should set the
            // flexGrow
            // to 0 if the flexGrow is undefined in the annotation.
            return new CompositeAspectDefinition(
                    new ColumnCollapseAspectDefinition(annotation.collapsible()),
                    new ColumnFlexGrowAspectDefinition(flexGrow),
                    new ColumnWidthAspectDefinition(width),
                    new ColumnTextAlignAspectDefinition(annotation.textAlign()),
                    new ColumnSortableAspectDefinition(annotation.sortable()));
        }
    }
}
