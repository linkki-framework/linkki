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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.Nullable;
import org.linkki.core.ButtonPmo;

public class TestContainerPmo implements ContainerPmo<TestRowPmo> {

    private final List<TestRowPmo> items = new LinkedList<>();
    private Optional<TableFooterPmo> footerPmo = Optional.empty();
    private Optional<ButtonPmo> addItemButtonPmo = Optional.empty();

    public TestContainerPmo(TestRowPmo... items) {
        Arrays.stream(items).forEachOrdered(this.items::add);
    }

    @Override
    public List<TestRowPmo> getItems() {
        return items;
    }

    public void addItem(TestRowPmo item) {
        items.add(item);
    }

    @Override
    public Optional<ButtonPmo> getAddItemButtonPmo() {
        return addItemButtonPmo;
    }

    public void setAddItemButtonPmo(@Nullable ButtonPmo addItemButtonPmo) {
        this.addItemButtonPmo = Optional.ofNullable(addItemButtonPmo);
    }

    @Override
    public Optional<TableFooterPmo> getFooterPmo() {
        return footerPmo;
    }

    public void setFooterPmo(@Nullable TableFooterPmo footerPmo) {
        this.footerPmo = Optional.ofNullable(footerPmo);
    }

}
