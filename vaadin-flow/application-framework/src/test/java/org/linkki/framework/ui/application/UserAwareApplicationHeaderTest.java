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
package org.linkki.framework.ui.application;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.util.Sequence;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;

@ExtendWith(KaribuUIExtension.class)
class UserAwareApplicationHeaderTest {

    private UserAwareApplicationHeader header;

    @BeforeEach
    void init() {
        header = createTestApplicationHeader();
    }

    @Test
    void testCreateUserMenu() {
        MenuBar menuBar = new MenuBar();

        header.addUserMenu(menuBar);

        assertThat(menuBar.getItems()).hasSize(1);
        assertThat(menuBar.getItems().get(0).getText()).isEqualTo("user");
        assertThat(menuBar.getItems().get(0).getChildren().findFirst())
                .hasValueSatisfying(c -> assertThat(c).isInstanceOf(Icon.class));
    }

    @Test
    void testGetUsername() {
        assertThat(header.getUsername()).isEqualTo("user");
    }

    @Test
    void testCreateRightMenuBar() {
        MenuBar menuBar = header.createRightMenuBar();

        assertThat(menuBar.getItems()).hasSize(2);
        assertThat(menuBar.getItems().get(0).getText()).isEmpty();
        assertThat(menuBar.getItems().get(0).getChildren().findFirst())
                .hasValueSatisfying(c -> assertThat(c).isInstanceOf(Icon.class));
        assertThat(menuBar.getItems().get(1).getText()).isEqualTo("user");
        assertThat(menuBar.getItems().get(1).getId()).hasValue("appmenu-user");
        assertThat(menuBar.getItems().get(1).getSubMenu().getItems().get(0).getId()).hasValue("appmenu-logout");
    }

    @Test
    void testGetLogoutUrl() {
        UI.getCurrent().getInternals().setContextRoot("test/");

        assertThat(header.getLogoutUrl()).isEqualTo("test/logout");
    }

    private UserAwareApplicationHeader createTestApplicationHeader() {
        var header = new UserAwareApplicationHeader(new ExampleApplicationInfo(),
                Sequence.empty()) {
            private static final long serialVersionUID = 1L;

            @Override
            public String getUsername() {
                return "user";
            }
        };
        UI.getCurrent().add(header);
        return header;
    }
}