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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.vaadin.ui.Table;

/**
 * Annotation that allows to customize the column for a PMO's field/method that is rendered in a
 * table column.
 * <p>
 * Note that this annotation is <em>not</em> required for a field/method to be rendered in a column.
 * All fields/methods with one of the {@code @UI...} annotations ({@link UITextField @UITextField},
 * {@link UICheckBox @UICheckBox} etc.) are rendered automatically. This annotation allows optional
 * customization and can be omitted if no customization is needed.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UITableColumn {

    static final int UNDEFINED_WIDTH = -1;
    static final float UNDEFINED_EXPAND_RATIO = -1.0f;

    /**
     * Configures the width in pixels for the column. The default value of -1 means that the column can
     * be sized freely by the layout.
     * <p>
     * This attribute is mutually exclusive with {@link #expandRatio()}.
     * 
     * @see Table#setColumnWidth(Object, int)
     */
    int width() default UNDEFINED_WIDTH;

    /**
     * Configures the expand ratio for the column. The expand ratio defines what part of excess
     * available space the layout allots to this column.
     * <p>
     * This attribute is mutually exclusive with {@link #width()}.
     * 
     * @see Table#setColumnExpandRatio(Object, float)
     */
    float expandRatio() default UNDEFINED_EXPAND_RATIO;

}
