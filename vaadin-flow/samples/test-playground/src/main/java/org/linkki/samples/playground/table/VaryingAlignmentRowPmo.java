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

package org.linkki.samples.playground.table;

import org.linkki.core.ui.aspects.types.TextAlignment;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UILink;
import org.linkki.core.ui.table.column.annotation.UITableColumn;

public class VaryingAlignmentRowPmo {

    private boolean checked = true;

    @UITableColumn(flexGrow = 1, textAlign = TextAlignment.DEFAULT)
    @UILabel(position = 0, label = "@UILabel - DEFAULT alignment")
    public String getLabel() {
        return "Label text";
    }

    @UITableColumn(flexGrow = 1, textAlign = TextAlignment.LEFT)
    @UILink(position = 10, label = "@UILink - START alignment", caption = "Link")
    public String getLink() {
        return "https://linkki-framework.org/";
    }

    @UITableColumn(flexGrow = 1, textAlign = TextAlignment.CENTER)
    @UICheckBox(position = 20, label = "@UICheckBox - CENTER alignment")
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @UITableColumn(flexGrow = 1, textAlign = TextAlignment.RIGHT)
    @UIButton(position = 30, label = "@UIButton - END alignment")
    public void button() {
        // do nothing
    }

}
