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

abstract class AbstractPersonRowPmo {

    @UITableColumn(expandRatio = 0F)
    @UILabel(position = 10, label = "Province")
    public String getProvince() {
        return "";
    }

    @UITableColumn(expandRatio = 0.1F)
    @UILabel(position = 20, label = "City")
    public String getCity() {
        return "";
    }

    @UITableColumn(expandRatio = 0F)
    @UILabel(position = 30, label = "Zip Code")
    public String getZipCode() {
        return "";
    }

    @UITableColumn(expandRatio = 0.15F)
    @UILabel(position = 40, label = "Address")
    public String getAddress() {
        return "";
    }

    @UITableColumn(expandRatio = 0.075F)
    @UILabel(position = 50, label = "Last Name")
    public String getLastName() {
        return "";
    }

    @UITableColumn(expandRatio = 0.075F)
    @UILabel(position = 60, label = "First Name")
    public String getFirstName() {
        return "";
    }

    @UITableColumn(expandRatio = 0.1F)
    @UILabel(position = 70, label = "Company")
    public String getCompany() {
        return "";
    }

    @UITableColumn(expandRatio = 0.075F)
    @UILabel(position = 80, label = "Phone")
    public String getPhone1() {
        return "";
    }

    @UITableColumn(expandRatio = 0.075F)
    @UILabel(position = 90, label = "2nd Phone")
    public String getPhone2() {
        return "";
    }

    @UITableColumn(expandRatio = 0.15F)
    @UILabel(position = 100, label = "Email")
    public String getEmail() {
        return "";
    }

    @UITableColumn(expandRatio = 0.2F)
    @UILabel(position = 110, label = "Website")
    public String getWeb() {
        return "";
    }

}