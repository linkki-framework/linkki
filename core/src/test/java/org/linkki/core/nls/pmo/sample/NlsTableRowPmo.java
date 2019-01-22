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
package org.linkki.core.nls.pmo.sample;

import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.UITableColumn;
import org.linkki.core.ui.section.annotations.UITextField;

public class NlsTableRowPmo {

    public static final String PROPERTY_VALUE1 = "value1";
    public static final String PROPERTY_VALUE2 = "value2";
    public static final String PROPERTY_VALUE3 = "value3";
    public static final String PROPERTY_DELETE = "delete";

    public static final String PMO_LABEL = "not translated label";

    @UITableColumn(width = 100)
    @UITextField(position = 0, label = PMO_LABEL)
    public String getValue1() {
        return "1";
    }

    @UITableColumn(expandRatio = 2.0f)
    @UITextField(position = 1, label = PMO_LABEL)
    public String getValue2() {
        return "2";
    }

    @UITextField(position = 2, label = PMO_LABEL)
    public String getValue3() {
        return "3";
    }

    @UIButton(position = 3)
    public void delete() {
        // nothing to do
    }


}
