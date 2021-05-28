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

import org.linkki.samples.playground.table.dynamicfields.DynamicFieldsSection;
import org.linkki.samples.playground.table.selection.SelectableTableSection;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class TablePage extends VerticalLayout {

    public static final String ID = "Table";

    private static final long serialVersionUID = 1L;

    public TablePage() {
        add(TableWithValidationSection.create());
        add(SelectableTableSection.create());
        addAndExpand(DynamicFieldsSection.create());

    }
}
