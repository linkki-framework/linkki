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

package de.faktorzehn.commons.linkki.ui.menu;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

@SuppressWarnings("deprecation")
class SingleItemMenuBarTest {

    @Test
    void testSingleItemMenuBar() {
        SingleItemMenuBar menuBar = new SingleItemMenuBar("caption");

        assertThat(menuBar.getCaption(), is("caption"));
        assertThat(menuBar.getIcon(), is(nullValue()));
        assertThat(menuBar.getItems().size(), is(1));
        MenuItem menuItem = menuBar.getItems().get(0);
        assertThat(menuItem.getText(), is("caption"));
        assertThat(menuItem.getChildren().count(), is(0L));
    }

    @Test
    void testSingleItemMenuBar_WithIcon() {
        SingleItemMenuBar menuBar = new SingleItemMenuBar("caption", VaadinIcon.AMBULANCE);

        assertThat(menuBar.getCaption(), is("caption"));
        assertThat(menuBar.getIcon(), is(VaadinIcon.AMBULANCE));
        assertThat(menuBar.getItems().size(), is(1));
        MenuItem menuItem = menuBar.getItems().get(0);
        assertThat(menuItem.getText(), is("caption"));
        Icon icon = (Icon)menuItem.getChildren().collect(Collectors.toList()).get(0);
        assertThat(icon.getElement().getAttribute("icon"), is("vaadin:ambulance"));
    }

    @Test
    void testSetCaption() {
        SingleItemMenuBar menuBar = new SingleItemMenuBar("old caption");

        menuBar.setCaption("new caption");

        assertThat(menuBar.getCaption(), is("new caption"));
        assertThat(menuBar.getItems().size(), is(1));
        MenuItem menuItem = menuBar.getItems().get(0);
        assertThat(menuItem.getText(), is("new caption"));
    }

    @Test
    void testSetIcon() {
        SingleItemMenuBar menuBar = new SingleItemMenuBar("caption", VaadinIcon.NEWSPAPER);

        menuBar.setIcon(VaadinIcon.RSS);

        assertThat(menuBar.getIcon(), is(VaadinIcon.RSS));
        assertThat(menuBar.getItems().size(), is(1));
        MenuItem menuItem = menuBar.getItems().get(0);
        Icon icon = (Icon)menuItem.getChildren().collect(Collectors.toList()).get(0);
        assertThat(icon.getElement().getAttribute("icon"), is("vaadin:rss"));
    }
}
