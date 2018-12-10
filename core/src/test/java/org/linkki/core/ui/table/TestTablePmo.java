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
package org.linkki.core.ui.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.linkki.core.ButtonPmo;
import org.linkki.core.ui.section.annotations.UISection;

@UISection(caption = TestTablePmo.CAPTION)
public class TestTablePmo implements ContainerPmo<@NonNull TestRowPmo> {

    public static final String CAPTION = "container";

    private final List<@NonNull TestRowPmo> pmos = new ArrayList<>();

    private int pageLength = ContainerPmo.DEFAULT_PAGE_LENGTH;

    @Nullable
    private TableFooterPmo footerPmo;

    public TestRowPmo addItem() {
        TestRowPmo columnPmo = new TestRowPmo(this);
        pmos.add(columnPmo);
        return columnPmo;
    }

    public void deleteItem(TestRowPmo columnPmo) {
        pmos.remove(columnPmo);
    }

    @Override
    public List<TestRowPmo> getItems() {
        return pmos;
    }

    @Override
    public Optional<ButtonPmo> getAddItemButtonPmo() {
        return Optional.of(ButtonPmo.newAddButton(this::addItem));
    }

    @Override
    public int getPageLength() {
        return pageLength;
    }

    public void setPageLength(int pageLength) {
        this.pageLength = pageLength;
    }

    @Override
    public Optional<TableFooterPmo> getFooterPmo() {
        return Optional.ofNullable(footerPmo);
    }

    public void setFooterPmo(TableFooterPmo footerPmo) {
        this.footerPmo = footerPmo;
    }
}