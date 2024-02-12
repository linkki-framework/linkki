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

import java.time.LocalDate;

import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.table.column.annotation.UITableColumn;

public class SortableRowPmo {

    private String string;
    private final Integer integer;
    private final LocalDate date;

    public SortableRowPmo(String string, Integer integer, LocalDate date) {
        this.string = string;
        this.integer = integer;
        this.date = date;
    }

    @UITableColumn(width = 200, sortable = true)
    @UITextField(position = 0, label = "String")
    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    @UITableColumn(width = 200, sortable = true)
    @UILabel(position = 10, label = "Integer")
    public Integer getInteger() {
        return integer;
    }

    @UITableColumn(width = 200, sortable = true)
    @UILabel(position = 30, label = "LocalDate")
    public LocalDate getDate() {
        return date;
    }
}
