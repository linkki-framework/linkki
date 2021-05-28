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

package org.linkki.samples.playground.treetable.fixed;

import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.ui.table.column.annotation.UITableColumn.CollapseMode;

public abstract class AbstractPersonRowPmo {

    @UITableColumn(flexGrow = 150)
    @UILabel(position = 40, label = "Address", htmlContent = true)
    public abstract String getAddress();

    @UITableColumn(flexGrow = 75)
    @UILabel(position = 50, label = "Last Name")
    public abstract String getLastName();

    @UITableColumn(flexGrow = 75, collapsible = CollapseMode.COLLAPSIBLE)
    @UILabel(position = 60, label = "First Name")
    public abstract String getFirstName();

    @UITableColumn(flexGrow = 10, collapsible = CollapseMode.COLLAPSIBLE)
    @UILabel(position = 70, label = "Company")
    public abstract String getCompany();

    @UITableColumn(flexGrow = 75, collapsible = CollapseMode.COLLAPSIBLE)
    @UILabel(position = 80, label = "Phone")
    public abstract String getPhone1();

    @UITableColumn(flexGrow = 75, collapsible = CollapseMode.INITIALLY_COLLAPSED)
    @UILabel(position = 90, label = "2nd Phone")
    public abstract String getPhone2();

    @UITableColumn(flexGrow = 150)
    @UILabel(position = 100, label = "Email")
    public abstract String getEmail();

    @UITableColumn(flexGrow = 20, collapsible = CollapseMode.COLLAPSIBLE)
    @UILabel(position = 110, label = "Website")
    public abstract String getWeb();

}