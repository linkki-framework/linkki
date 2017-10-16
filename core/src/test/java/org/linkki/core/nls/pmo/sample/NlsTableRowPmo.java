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
package org.linkki.core.nls.pmo.sample;

import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.UITableColumn;
import org.linkki.core.ui.section.annotations.UITextField;

public class NlsTableRowPmo {

    @UITableColumn(width = 100)
    @UITextField(position = 0, label = "1")
    public String getValue1() {
        return "1";
    }

    @UITableColumn(expandRatio = 2.0f)
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
        // nothing to do
    }


}
