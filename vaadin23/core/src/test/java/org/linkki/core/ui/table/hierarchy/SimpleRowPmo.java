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

package org.linkki.core.ui.table.hierarchy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.linkki.core.defaults.columnbased.pmo.HierarchicalRowPmo;
import org.linkki.core.ui.element.annotation.UITextField;

public class SimpleRowPmo implements HierarchicalRowPmo<SimpleRowPmo> {

    private final String content;
    private final List<SimpleRowPmo> children = new ArrayList<>();

    private SimpleRowPmo(String text) {
        this.content = text;
    }

    @UITextField(position = 0, label = "Content")
    public String getContent() {
        return content;
    }

    @Override
    public List<? extends SimpleRowPmo> getChildRows() {
        return children;
    }

    @Override
    public String toString() {
        return "SimpleRowPmo(" + content + ")";
    }

    public SimpleRowPmo createChild(String text) {
        SimpleRowPmo child = new SimpleRowPmo(text);
        children.add(child);
        return child;
    }

    public void removeChild(String text) {
        Iterator<SimpleRowPmo> iterator = children.iterator();
        while (iterator.hasNext()) {
            SimpleRowPmo child = iterator.next();
            if (child.content.equals(text)) {
                iterator.remove();
                return;
            }
        }

        throw new IllegalArgumentException("No child with the given content present");
    }

    public static SimpleRowPmo create(String text) {
        return new SimpleRowPmo(text);
    }

}
