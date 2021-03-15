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

package org.linkki.core.vaadin.component;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.vaadin.component.base.LinkkiAnchor;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class LinkkiAnchorTest {

    @Test
    public void testAnchor() {
        LinkkiAnchor anchor = new LinkkiAnchor();

        assertThat(anchor.getElement().getText(), is(""));
        List<Component> children = anchor.getChildren().collect(Collectors.toList());
        assertThat(children, hasSize(0));
    }

    @Test
    public void testSetText() {
        LinkkiAnchor anchor = new LinkkiAnchor();

        anchor.setText("test");

        assertThat(anchor.getText(), is("test"));
        assertThat(anchor.getElement().getText(), is("test"));
        List<Component> children = anchor.getChildren().collect(Collectors.toList());
        assertThat(children, hasSize(0));
    }

    @Test
    public void testSetText_KeepsExistingIcon() {
        LinkkiAnchor anchor = new LinkkiAnchor();
        anchor.setIcon(VaadinIcon.ARCHIVE);

        anchor.setText("test");

        assertThat(anchor.getElement().getText(), is("test"));
        List<Component> children = anchor.getChildren().collect(Collectors.toList());
        assertThat(children, contains(instanceOf(Icon.class)));
        assertThat(((Icon)children.get(0)).getElement().getAttribute("icon"), is("vaadin:archive"));
    }

    @Test
    public void testSetIcon() {
        LinkkiAnchor anchor = new LinkkiAnchor();

        anchor.setIcon(VaadinIcon.ABACUS);

        assertThat(anchor.getIcon(), is(VaadinIcon.ABACUS));
        List<Component> children = anchor.getChildren().collect(Collectors.toList());
        assertThat(children, contains(instanceOf(Icon.class)));
        assertThat(((Icon)children.get(0)).getElement().getAttribute("icon"), is("vaadin:abacus"));
    }

    @Test
    public void testSetIcon_RemovesPreviousIcon() {
        LinkkiAnchor anchor = new LinkkiAnchor();
        anchor.setIcon(VaadinIcon.ABACUS);

        anchor.setIcon(null);

        List<Component> children = anchor.getChildren().collect(Collectors.toList());
        assertThat(children, hasSize(0));
    }

    @Test
    public void testSetIcon_KeepsExistingText() {
        LinkkiAnchor anchor = new LinkkiAnchor();
        anchor.setText("test");

        anchor.setIcon(VaadinIcon.ARCHIVE);

        assertThat(anchor.getElement().getText(), is("test"));
        List<Component> children = anchor.getChildren().collect(Collectors.toList());
        assertThat(children, contains(instanceOf(Icon.class)));
        assertThat(((Icon)children.get(0)).getElement().getAttribute("icon"), is("vaadin:archive"));
    }

}
