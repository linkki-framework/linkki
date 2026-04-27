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

package org.linkki.samples.playground.table;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.linkki.core.ui.aspects.annotation.BindPlaceholder;
import org.linkki.core.ui.aspects.types.PlaceholderType;
import org.linkki.core.ui.layout.annotation.UISection;

public abstract class SimplePlaygroundTablePmo extends PlaygroundTablePmo {

    private final int pageLength;

    private SimplePlaygroundTablePmo(int pageLength, List<TableModelObject> modelObjects) {
        super(() -> modelObjects, () -> modelObjects.add(new TableModelObject(modelObjects.size() + 1)),
                modelObjects::remove);
        this.pageLength = pageLength;
    }

    protected SimplePlaygroundTablePmo(int pageLength) {
        this(pageLength, IntStream.range(1, 2)
                .mapToObj(TableModelObject::new)
                .collect(Collectors.toList()));
    }

    @Override
    public int getPageLength() {
        return pageLength;
    }

    // tag::bindPlaceholder-tablePmo[]
    @BindPlaceholder("No rows present.")
    @UISection(caption = "@BindPlaceholder(\"No rows present.\") with fixed page length")
    public static class TableWithPlaceholderPmo extends SimplePlaygroundTablePmo {
        // end::bindPlaceholder-tablePmo[]

        public TableWithPlaceholderPmo() {
            super(3);
        }
    }

    @BindPlaceholder("No rows present.")
    @UISection(caption = "@BindPlaceholder(\"No rows present.\") with page length 0")
    public static class TableWithPlaceholderNoPageLengthPmo extends SimplePlaygroundTablePmo {

        public TableWithPlaceholderNoPageLengthPmo() {
            super(0);
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
            super(3);
        }
    }

    @UISection(caption = "Without @BindPlaceholder")
    public static class TableWithoutPlaceholderPmo extends SimplePlaygroundTablePmo {
        public TableWithoutPlaceholderPmo() {
            super(3);
        }
    }
}
