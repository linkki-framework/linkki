/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

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
        this.annotatedClass = annotatedClass;
        this.annotatedMethod = annotatedMethod;
        this.columnAnnotation = columnAnnotation;
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
        return columnAnnotation.expandRation();
    }

    public void checkValidConfiguration() {
        if (isCustomWidthDefined() && isCustomExpandRatioDefined()) {
            throw new IllegalStateException(
                    "width and expandRatio cannot be both defined on " + annotatedClass + "." + annotatedMethod);
        }
    }

}
