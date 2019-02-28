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
package org.linkki.core.ui.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.linkki.core.ButtonPmo;
import org.linkki.core.ButtonPmoBuilder;
import org.linkki.core.ui.section.annotations.UISection;

@UISection(caption = TestTablePmo.CAPTION)
public class TestTablePmo implements ContainerPmo<@NonNull TestRowPmo> {

    public static final String CAPTION = "container";

    private final List<@NonNull TestRowPmo> pmos;
    private int pageLength;
    @Nullable
    private TableFooterPmo footerPmo;

    public TestTablePmo() {
        this(new ArrayList<>(), ContainerPmo.DEFAULT_PAGE_LENGTH, null);
    }

    public TestTablePmo(TestRowPmo... rowPmos) {
        this(new ArrayList<>(Arrays.asList(rowPmos)), ContainerPmo.DEFAULT_PAGE_LENGTH, null);
    }

    public TestTablePmo(int pageLength) {
        this(new ArrayList<>(), pageLength, null);
    }

    public TestTablePmo(TableFooterPmo footerPmo) {
        this(new ArrayList<>(), ContainerPmo.DEFAULT_PAGE_LENGTH, footerPmo);
    }

    public TestTablePmo(List<@NonNull TestRowPmo> rowPmos, int pageLength, @Nullable TableFooterPmo footerPmo) {
        this.pmos = rowPmos;
        this.pageLength = pageLength;
        this.footerPmo = footerPmo;
    }

    @Override
    public List<TestRowPmo> getItems() {
        return pmos;
    }

    @Override
    public Optional<ButtonPmo> getAddItemButtonPmo() {
        return Optional.of(ButtonPmoBuilder.newAddButton(() -> pmos.add(new TestRowPmo())));
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

    public void setFooterPmo(@Nullable TableFooterPmo footerPmo) {
        this.footerPmo = footerPmo;
    }
}