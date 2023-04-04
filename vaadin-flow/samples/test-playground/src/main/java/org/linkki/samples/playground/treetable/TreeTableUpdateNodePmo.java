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

package org.linkki.samples.playground.treetable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.defaults.columnbased.pmo.HierarchicalRowPmo;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.samples.playground.treetable.TreeTableUpdateNodePmo.SimpleTreeNodeRowPmo;

import com.vaadin.flow.component.icon.VaadinIcon;

public class TreeTableUpdateNodePmo implements ContainerPmo<SimpleTreeNodeRowPmo> {

    private List<SimpleTreeNodeRowPmo> items = Arrays
            .asList(new SimpleTreeNodeRowPmo("first parent node"), new SimpleTreeNodeRowPmo("second parent row"),
                    new SimpleTreeNodeRowPmo("a third one"));

    @Override
    public List<SimpleTreeNodeRowPmo> getItems() {
        return items;
    }

    public class SimpleTreeNodeRowPmo implements HierarchicalRowPmo<SimpleTreeLeafRowPmo> {

        private final String text;
        private List<SimpleTreeLeafRowPmo> children = new ArrayList<>();

        private SimpleTreeNodeRowPmo(String text) {
            this.text = text;
        }

        @UILabel(position = 10)
        public String getText() {
            return text;
        }

        @UIButton(position = 20, icon = VaadinIcon.PLUS_CIRCLE_O, showIcon = true, visible = VisibleType.DYNAMIC)
        public void add() {
            children.add(new SimpleTreeLeafRowPmo("a child node"));
        }

        public boolean isAddVisible() {
            return true;
        }

        @UIButton(position = 30, icon = VaadinIcon.MINUS_CIRCLE_O, enabled = EnabledType.DYNAMIC, showIcon = true, visible = VisibleType.DYNAMIC)
        public void remove() {
            children.stream().findFirst().ifPresent(children::remove);
        }

        public boolean isRemoveVisible() {
            return true;
        }

        public boolean isRemoveEnabled() {
            return !children.isEmpty();
        }

        @Override
        public List<? extends SimpleTreeLeafRowPmo> getChildRows() {
            return children;
        }

    }

    public class SimpleTreeLeafRowPmo extends SimpleTreeNodeRowPmo {

        public SimpleTreeLeafRowPmo(String text) {
            super(text);
        }

        @Override
        public List<? extends SimpleTreeLeafRowPmo> getChildRows() {
            return Collections.emptyList();
        }

        @Override
        public boolean isAddVisible() {
            return false;
        }

        @Override
        public boolean isRemoveVisible() {
            return false;
        }
    }
}
