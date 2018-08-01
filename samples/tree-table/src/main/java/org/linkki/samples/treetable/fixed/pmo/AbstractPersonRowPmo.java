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

package org.linkki.samples.treetable.fixed.pmo;

import org.linkki.core.ui.section.annotations.UILabel;
import org.linkki.core.ui.section.annotations.UITableColumn;

public abstract class AbstractPersonRowPmo {

    @UITableColumn(expandRatio = 0.15F)
    @UILabel(position = 40, label = "Address", htmlContent = true)
    public abstract String getAddress();

    @UITableColumn(expandRatio = 0.075F)
    @UILabel(position = 50, label = "Last Name")
    public abstract String getLastName();

    @UITableColumn(expandRatio = 0.075F)
    @UILabel(position = 60, label = "First Name")
    public abstract String getFirstName();

    @UITableColumn(expandRatio = 0.1F)
    @UILabel(position = 70, label = "Company")
    public abstract String getCompany();

    @UITableColumn(expandRatio = 0.075F)
    @UILabel(position = 80, label = "Phone")
    public abstract String getPhone1();

    @UITableColumn(expandRatio = 0.075F)
    @UILabel(position = 90, label = "2nd Phone")
    public abstract String getPhone2();

    @UITableColumn(expandRatio = 0.15F)
    @UILabel(position = 100, label = "Email")
    public abstract String getEmail();

    @UITableColumn(expandRatio = 0.2F)
    @UILabel(position = 110, label = "Website")
    public abstract String getWeb();

}