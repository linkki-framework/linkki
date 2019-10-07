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

import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;

/**
 * PMO for a table whose rows can be selected. In addition, a double click action can be bound to the
 * rows.
 */
@BindTableSelection
public interface SelectableTablePmo<ROW> extends ContainerPmo<ROW> {

    /**
     * Returns the currently selected row.
     */
    ROW getSelection();

    /**
     * Sets the currently selected row.
     */
    void setSelection(ROW selectedRow);

    /**
     * Action that should be executed when a double click is made on a row.
     * 
     * @implSpec As a single click triggers the selection, implementer can assume that
     *           {@link #setSelection(Object)} has already been called before this method is invoked.
     *           Thus, it is safe to assume that the row that is being clicked on is identical to
     *           {@link #getSelection()}.
     */
    void onDoubleClick();
}
