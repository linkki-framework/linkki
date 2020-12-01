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
package org.linkki.core.ui.creation.table;

import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.ui.table.column.annotation.UITableColumn.CollapseMode;

public class TestRowPmo {

    public static final String PROPERTY_VALUE_1 = "value1";
    public static final String PROPERTY_VALUE_2 = "value2";
    public static final String PROPERTY_VALUE_3 = "value3";
    public static final String PROPERTY_DELETE = "delete";

    private final Object modelObject = new Object();

    @UITableColumn(width = 100, collapsible = CollapseMode.COLLAPSIBLE)
    @UITextField(position = 0, label = "1")
    public String getValue1() {
        return "1";
    }

    @UITableColumn(expandRatio = 2.0f, collapsible = CollapseMode.INITIALLY_COLLAPSED)
    @UITextField(position = 1, label = "2")
    public String getValue2() {
        return "2";
    }

    @UITextField(position = 2, label = "3")
    public String getValue3() {
        return "3";
    }

    @UIButton(position = 3)
    public void delete() {
        // does nothing
    }

    @ModelObject
    public Object getModelObject() {
        return modelObject;
    }

}