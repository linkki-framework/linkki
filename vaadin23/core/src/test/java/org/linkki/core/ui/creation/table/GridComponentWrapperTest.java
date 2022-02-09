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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.grid.Grid;

public class GridComponentWrapperTest {

    @Test
    void testHasItemsAttribute() {
        GridComponentWrapper<Integer> componentWrapper = new GridComponentWrapper<>(new Grid<>());

        assertThat(componentWrapper.getComponent().getElement().hasAttribute("has-items")).isFalse();

        componentWrapper.setItems(Arrays.asList(1, 2, 3));

        assertThat(componentWrapper.getComponent().getElement().hasAttribute("has-items")).isTrue();

        componentWrapper.setItems(Collections.emptyList());

        assertThat(componentWrapper.getComponent().getElement().hasAttribute("has-items")).isFalse();
    }

}
