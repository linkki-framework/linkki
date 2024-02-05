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

package org.linkki.core.ui.table.aspects.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.InheritedAspect;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.ui.table.aspects.GridSelectionAspectDefinition;
import org.linkki.core.ui.table.aspects.annotation.BindTableSelection.TableSelectionAspectDefinitionCreator;

import com.vaadin.flow.component.grid.Grid;

/**
 * Binds the selection of a table row to the aspect {@value GridSelectionAspectDefinition#SELECTION_ASPECT_NAME}. In
 * addition, the double click invokes the aspect {@value GridSelectionAspectDefinition#DOUBLE_CLICK_ASPECT_NAME}.
 */
@InheritedAspect
@LinkkiAspect(TableSelectionAspectDefinitionCreator.class)
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindTableSelection {

    /**
     * If <code>true</code>, the table will be selectable only for visual reasons and the methods mentioned in
     * {@link #selectionMode()} are no longer required.
     */
    boolean visualOnly() default false;

    /**
     * Controls the selection mode of the created table.
     * <ul>
     * <li> {@link Grid.SelectionMode#SINGLE} - for single selection. requires:
     * <ul>
     *     <li>ROW getSelection()</li>
     *     <li>void setSelection(ROW)</li>
     *     <li>void onDoubleClick()</li>
     * </ul>
     * </li>
     * <li> {@link Grid.SelectionMode#MULTI} - for multi selection. requires:
     * <ul>
     *    <li>{@literal Set<ROW>} getSelection()</li>
     *    <li>void setSelection({@literal Set<ROW>})</li>
     * </ul>
     * </li>
     * <li> {@link Grid.SelectionMode#NONE} - for no selection.</li>
     * </ul>
     */
    Grid.SelectionMode selectionMode() default Grid.SelectionMode.SINGLE;

    class TableSelectionAspectDefinitionCreator implements AspectDefinitionCreator<BindTableSelection> {
        @Override
        public LinkkiAspectDefinition create(BindTableSelection annotation) {
            return new GridSelectionAspectDefinition(annotation.visualOnly(), annotation.selectionMode());
        }
    }
}
