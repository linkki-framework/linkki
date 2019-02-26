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

package org.linkki.samples.treetable.fixed.pmo;

import java.util.List;

import org.linkki.core.ui.table.HierarchicalRowPmo;

// tag::hierarchical-row-pmo[]
public abstract class SummarizingPersonRowPmo extends AbstractPersonRowPmo
        implements HierarchicalRowPmo<AbstractPersonRowPmo> {

    private final List<? extends AbstractPersonRowPmo> childRows;

    public SummarizingPersonRowPmo(List<? extends AbstractPersonRowPmo> childRows) {
        this.childRows = childRows;
    }

    @Override
    public List<? extends AbstractPersonRowPmo> getChildRows() {
        return childRows;
    }
    // end::hierarchical-row-pmo[]

    @Override
    public String getLastName() {
        return "";
    }

    @Override
    public String getFirstName() {
        return "";
    }

    @Override
    public String getCompany() {
        return "";
    }

    @Override
    public String getPhone1() {
        return "";
    }

    @Override
    public String getPhone2() {
        return "";
    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public String getWeb() {
        return "";
    }

}
