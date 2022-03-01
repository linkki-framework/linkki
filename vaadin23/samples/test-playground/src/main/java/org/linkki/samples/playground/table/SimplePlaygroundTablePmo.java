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

package org.linkki.samples.playground.table;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.linkki.core.ui.aspects.annotation.BindPlaceholder;
import org.linkki.core.ui.aspects.types.PlaceholderType;
import org.linkki.core.ui.layout.annotation.UISection;

public abstract class SimplePlaygroundTablePmo extends PlaygroundTablePmo {

    public SimplePlaygroundTablePmo(List<TableModelObject> modelObjects) {
        super(() -> modelObjects, () -> modelObjects.add(new TableModelObject(modelObjects.size() + 1)),
                o -> modelObjects.remove(o));
    }

    @Override
    public int getPageLength() {
        return 3;
    }

    // tag::bindPlaceholder-tablePmo[]
    @BindPlaceholder("No rows present.")
    @UISection(caption = "@BindPlaceholder(\"No rows present.\")")
    public static class TableWithPlaceholderPmo extends SimplePlaygroundTablePmo {
        // end::bindPlaceholder-tablePmo[]
        public TableWithPlaceholderPmo() {
            super(IntStream.range(1, 2)
                    .mapToObj(TableModelObject::new)
                    .collect(Collectors.toList()));
        }

    }

    @UISection(caption = "Inherited @BindPlaceholder")
    public static class TableWithInheritedPlaceholderPmo extends TableWithPlaceholderPmo {
        public TableWithInheritedPlaceholderPmo() {
            super();
        }
    }

    @BindPlaceholder(value = "", placeholderType = PlaceholderType.STATIC)
    @UISection(caption = "@BindPlaceholder(value = \"\", placeholderType = PlaceholderType.STATIC)")
    public static class TableWithEmptyPlaceholderPmo extends SimplePlaygroundTablePmo {
        public TableWithEmptyPlaceholderPmo() {
            super(IntStream.range(1, 2)
                    .mapToObj(TableModelObject::new)
                    .collect(Collectors.toList()));
        }
    }

    @UISection(caption = "Without @BindPlaceholder")
    public static class TableWithoutPlaceholderPmo extends SimplePlaygroundTablePmo {
        public TableWithoutPlaceholderPmo() {
            super(IntStream.range(1, 2)
                    .mapToObj(TableModelObject::new)
                    .collect(Collectors.toList()));
        }
    }
}
