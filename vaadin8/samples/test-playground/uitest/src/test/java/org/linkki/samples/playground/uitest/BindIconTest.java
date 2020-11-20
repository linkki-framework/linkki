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

package org.linkki.samples.playground.uitest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.allelements.Direction;

import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.LinkElement;

public class BindIconTest extends AbstractUiTest {


    @Test
    public void testBindIcon() {
        LinkElement link = $(LinkElement.class).id("link");
        assertThat(link.getText(), is("Link to Dynamic Annotations"));

        ComboBoxElement comboBox = $(ComboBoxElement.class).id("enumValueComboBox");
        comboBox.selectByText(Direction.RIGHT.getName());

        assertThat(link.getText(), is((char)Direction.RIGHT.getIcon().getCodepoint() + "Link to Dynamic Annotations"));
    }

}
