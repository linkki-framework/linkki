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

package org.linkki.framework.ui.component;

import static org.assertj.core.api.Assertions.assertThat;

import com.vaadin.flow.component.grid.Grid;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.validation.message.MessageList;

import com.vaadin.flow.component.html.NativeLabel;

class MessageUiComponentsTest {

    @Test
    void testCreateMessageTable() {
        var component = MessageUiComponents.createMessageTable("Title", MessageList::new, new BindingContext());

        var children = component.getChildren().toList();
        assertThat(children).hasSize(2);
        assertThat(children.get(0)).isInstanceOf(NativeLabel.class)
                .satisfies(c -> assertThat(((NativeLabel)c).getText()).isEqualTo("Title"));
        assertThat(children.get(1)).isInstanceOf(Grid.class);
    }
}
