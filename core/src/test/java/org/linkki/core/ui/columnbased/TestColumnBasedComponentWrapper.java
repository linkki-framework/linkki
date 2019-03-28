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

package org.linkki.core.ui.columnbased;

import java.util.List;
import java.util.Optional;

import org.linkki.core.ui.TestComponentWrapper;
import org.linkki.core.ui.components.WrapperType;
import org.linkki.core.ui.table.TableFooterPmo;

public class TestColumnBasedComponentWrapper<ROW> extends TestComponentWrapper
        implements ColumnBasedComponentWrapper<ROW> {

    private static final long serialVersionUID = 1L;

    public TestColumnBasedComponentWrapper(TestColumnBasedComponent<ROW> component) {
        super(component);
    }

    @Override
    public WrapperType getType() {
        return ColumnBasedComponentWrapper.COLUMN_BASED_TYPE;
    }

    @Override
    public void updateFooter(Optional<TableFooterPmo> footerPmo) {
        getComponent().setFooterPmo(footerPmo);
    }

    @Override
    public TestColumnBasedComponent<ROW> getComponent() {
        @SuppressWarnings("unchecked")
        TestColumnBasedComponent<ROW> columnBasedComponent = (TestColumnBasedComponent<ROW>)super.getComponent();
        return columnBasedComponent;
    }

    @Override
    public void setPageLength(int pageLength) {
        getComponent().setPageLength(pageLength);
    }

    @Override
    public void setItems(List<ROW> actualItems) {
        getComponent().setItems(actualItems);
    }

}