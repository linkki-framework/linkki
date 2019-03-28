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

package org.linkki.core.ui.columnbased;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.linkki.core.ui.TestUiLayoutComponent;
import org.linkki.core.ui.table.TableFooterPmo;

public class TestColumnBasedComponent<ROW> extends TestUiLayoutComponent {

    private Optional<TableFooterPmo> footerPmo = Optional.empty();
    private int pageLength;
    private List<ROW> items = Collections.emptyList();
    private int columnsOnLastFooterUpdate;

    public Optional<TableFooterPmo> getFooterPmo() {
        return footerPmo;
    }

    public void setFooterPmo(Optional<TableFooterPmo> footerPmo) {
        this.footerPmo = footerPmo;
        columnsOnLastFooterUpdate = getChildren().size();
    }

    public int getPageLength() {
        return pageLength;
    }

    public void setPageLength(int pageLength) {
        this.pageLength = pageLength;
    }

    public List<ROW> getItems() {
        return items;
    }

    public void setItems(List<ROW> items) {
        this.items = items;
    }

    public int getColumnsOnLastFooterUpdate() {
        return columnsOnLastFooterUpdate;
    }

}