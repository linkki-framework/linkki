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
package org.linkki.core.ui.section.annotations;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Method;

/**
 * Holds all information about a table column.
 */
public class TableColumnDescriptor {

    private final Class<?> annotatedClass;
    private final Method annotatedMethod;
    private final UITableColumn columnAnnotation;

    /**
     * Constructs a new table column description with the following parameters.
     * 
     * @param columnAnnotation the annotation that holds the configured values.
     */
    public TableColumnDescriptor(Class<?> annotatedClass, Method annotatedMethod, UITableColumn columnAnnotation) {
        this.annotatedClass = requireNonNull(annotatedClass, "annotatedClass must not be null");
        this.annotatedMethod = requireNonNull(annotatedMethod, "annotatedMethod must not be null");
        this.columnAnnotation = requireNonNull(columnAnnotation, "columnAnnotation must not be null");
    }

    public boolean isCustomWidthDefined() {
        return getWidth() != UITableColumn.UNDEFINED_WIDTH;
    }

    public int getWidth() {
        return columnAnnotation.width();
    }

    public boolean isCustomExpandRatioDefined() {
        return getExpandRatio() != UITableColumn.UNDEFINED_EXPAND_RATIO;
    }

    public float getExpandRatio() {
        return columnAnnotation.expandRatio();
    }

    public void checkValidConfiguration() {
        if (isCustomWidthDefined() && isCustomExpandRatioDefined()) {
            throw new IllegalStateException(
                    "width and expandRatio cannot be both defined on " + annotatedClass + "." + annotatedMethod);
        }
    }

}
