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

package org.linkki.framework.ui.application;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.linkki.util.Sequence;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;

class ApplicationHeaderTest {

    @Test
    void testCreateUserMenu() {
        ApplicationHeader header = new ApplicationHeader(new TestApplicationConfig().getApplicationInfo(),
                Sequence.empty());
        MenuBar menuBar = new MenuBar();

        header.addUserMenu("user", menuBar);

        assertThat(menuBar.getItems(), hasSize(1));
        assertThat(menuBar.getItems().get(0).getText(), is("user"));
        assertThat(menuBar.getItems().get(0).getChildren().findFirst().get(), is(Matchers.instanceOf(Icon.class)));
    }
}
