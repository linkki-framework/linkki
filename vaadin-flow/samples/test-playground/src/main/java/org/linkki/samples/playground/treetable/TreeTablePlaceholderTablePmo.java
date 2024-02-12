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

package org.linkki.samples.playground.treetable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.defaults.columnbased.pmo.HierarchicalRowPmo;
import org.linkki.core.defaults.columnbased.pmo.SimpleItemSupplier;
import org.linkki.core.ui.aspects.annotation.BindPlaceholder;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.playground.treetable.TreeTablePlaceholderTablePmo.TreeGridPlaceholderRowPmo;

@BindPlaceholder("Placeholder")
@UISection
public class TreeTablePlaceholderTablePmo implements ContainerPmo<TreeGridPlaceholderRowPmo> {

    private final AtomicInteger counter;
    private final List<Integer> models;
    private final SimpleItemSupplier<TreeGridPlaceholderRowPmo, Integer> itemSupplier;

    public TreeTablePlaceholderTablePmo() {
        this.counter = new AtomicInteger();
        this.models = new ArrayList<>();
        this.itemSupplier = new SimpleItemSupplier<>(() -> models, TreeGridPlaceholderRowPmo::new);
    }

    @Override
    public List<TreeGridPlaceholderRowPmo> getItems() {
        return itemSupplier.get();
    }

    @UIButton(position = -20, caption = "add")
    @SectionHeader
    public void add() {
        models.add(counter.getAndIncrement());
    }

    @UIButton(position = -10, caption = "delete")
    @SectionHeader
    public void delete() {
        if (!models.isEmpty()) {
            models.remove(0);
        }
    }

    public static class TreeGridPlaceholderRowPmo implements HierarchicalRowPmo<TreeGridPlaceholderRowPmo> {

        private Integer value;
        private boolean hasChildren;

        public TreeGridPlaceholderRowPmo(Integer value) {
            this(value, true);
        }

        public TreeGridPlaceholderRowPmo(Integer value, boolean hasChildren) {
            this.value = value;
            this.hasChildren = hasChildren;
        }

        @UILabel(position = 10, label = "Column")
        public String getValue() {
            return String.valueOf(value);
        }

        @Override
        public List<? extends TreeGridPlaceholderRowPmo> getChildRows() {
            return hasChildren ? List.of(new TreeGridPlaceholderRowPmo(1, false),
                                         new TreeGridPlaceholderRowPmo(2, false))
                    : List.of();
        }
    }
}
