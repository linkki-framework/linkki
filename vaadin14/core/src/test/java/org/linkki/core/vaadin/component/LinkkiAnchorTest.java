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
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.linkki.core.vaadin.component.base.LinkkiAnchor;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class LinkkiAnchorTest {

    @Test
    public void testAnchor_default() {
        LinkkiAnchor anchor = new LinkkiAnchor();
        assertThat(anchor.getChildren().findAny().isPresent(), is(false));
        assertThat(anchor.getText(), is(""));
    }

    @Test
    public void testAnchor_withTextOnly() {
        LinkkiAnchor anchor = new LinkkiAnchor();
        anchor.setText("Click me!");
        assertThat(anchor.getChildren().findFirst().isPresent(), is(false));
        assertThat(anchor.getText(), is("Click me!"));
    }

    @Test
    public void testAnchor_withIconOnly() {
        LinkkiAnchor anchor = new LinkkiAnchor();
        anchor.setIcon(VaadinIcon.ABACUS);

        assertThat(anchor.getText(), is(""));
        Optional<Component> firstChild = anchor.getChildren().findFirst();
        assertThat(firstChild.isPresent(), is(true));
        assertThat(firstChild.get(), is(instanceOf(Icon.class)));
    }

    @Test
    public void testAnchor_withTextAndIcon() {
        LinkkiAnchor anchor = new LinkkiAnchor();
        anchor.setText("Click here for alarm");
        anchor.setIcon(VaadinIcon.ALARM);

        assertThat(anchor.getText(), is("Click here for alarm"));
        Optional<Component> firstChild = anchor.getChildren().findFirst();
        assertThat(firstChild.isPresent(), is(true));
        assertThat(firstChild.get(), is(instanceOf(Icon.class)));
    }

    @Test
    public void testAnchor_removeIcon() {
        LinkkiAnchor anchor = new LinkkiAnchor();
        anchor.setIcon(VaadinIcon.ALARM);

        Optional<Component> firstChild = anchor.getChildren().findFirst();
        assertThat(firstChild.isPresent(), is(true));
        assertThat(firstChild.get(), is(instanceOf(Icon.class)));

        anchor.setIcon(null);

        Optional<Component> iconComponent = anchor.getChildren().findFirst();
        assertThat(iconComponent.isPresent(), is(false));
    }

}
