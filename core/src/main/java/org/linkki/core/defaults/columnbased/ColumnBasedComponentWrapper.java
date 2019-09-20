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

package org.linkki.core.defaults.columnbased;

import java.util.List;
import java.util.Optional;

import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.defaults.columnbased.pmo.TableFooterPmo;

/**
 * {@link ComponentWrapper} for column based UI components.
 * 
 * @param <ROW> the row presentation model objects that are bound to the cells in one row
 */
public interface ColumnBasedComponentWrapper<ROW> extends ComponentWrapper {

    static final long serialVersionUID = 1L;

    WrapperType COLUMN_BASED_TYPE = WrapperType.of("columnBased", WrapperType.COMPONENT);

    @Override
    public default WrapperType getType() {
        return ColumnBasedComponentWrapper.COLUMN_BASED_TYPE;
    }

    /**
     * Updates the columns' footers. If the given {@link Optional}&lt;{@link TableFooterPmo}&gt; is
     * {@link Optional#empty() empty}, no footer will be visible, otherwise the column footers will be
     * set according to {@link TableFooterPmo#getFooterText(String)}.
     */
    void updateFooter(Optional<TableFooterPmo> footerPmo);

    /**
     * Sets the page length.
     */
    void setPageLength(int pageLength);

    /**
     * Sets the items displayed in the component.
     */
    void setItems(List<ROW> actualItems);
}