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
package org.linkki.framework.ui.component;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.Text;

class CompositeH2Test {

    @Test
    void testSetText() {
        var h2 = new CompositeH2("First Title");
        Text subtitle = new Text("Subtitle");
        h2.add(subtitle);
        assertThat(h2.getText()).isEqualTo("First Title");
        assertThat(h2.getChildren().count()).isEqualTo(2);

        h2.setText("Title");

        assertThat(h2.getText()).isEqualTo("Title");
        var children = h2.getChildren().toList();
        assertThat(children).hasSize(2);
        assertThat(children.get(1)).isEqualTo(subtitle);
    }
}