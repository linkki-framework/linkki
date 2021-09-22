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

package org.linkki.core.ui.creation.table;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.grid.Grid;

public class AbstractGridComponentWrapperTest {

    /**
     * LIN-2184: Checks if setting the page length to 0 works as intended.
     */
    @Test
    public void testSetPageLength_Zero_Once() {

        Grid<Object> componentMock = new Grid<Object>();

        AbstractGridComponentWrapper<Object> wrapper = new AbstractGridComponentWrapper<Object>(componentMock) {

            private static final long serialVersionUID = 1L;

            @Override
            public void setItems(List<Object> actualItems) {
                // intended to be empty
            }
        };

        wrapper.setPageLength(0);

        assertThat(wrapper.getComponent().isAllRowsVisible(), is(true));
    }

    /**
     * LIN-2184: Checks if setting the page length more than once gives the same result.
     */
    @Test
    public void testSetPageLength_Zero_Twice() {

        Grid<Object> componentMock = new Grid<Object>();

        AbstractGridComponentWrapper<Object> wrapper = new AbstractGridComponentWrapper<Object>(componentMock) {

            private static final long serialVersionUID = 1L;

            @Override
            public void setItems(List<Object> actualItems) {
                // intended to be empty
            }
        };

        wrapper.setPageLength(0);
        wrapper.setPageLength(0);

        assertThat(wrapper.getComponent().isAllRowsVisible(), is(true));
    }

}
