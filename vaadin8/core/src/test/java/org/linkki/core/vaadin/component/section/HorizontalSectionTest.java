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

package org.linkki.core.vaadin.component.section;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class HorizontalSectionTest {

    @Test
    public void testAdd() {
        HorizontalSection section = new HorizontalSection("caption");
        Component component = new TextField();

        Label label = section.add("label", component);

        assertThat(label.getValue(), is("label"));
        assertThat(label.getParent(), is(section.getSectionContent()));
        assertThat(component.getParent(), is(section.getSectionContent()));
    }

    @Test
    public void testAdd_FullWidthComponents() {
        HorizontalSection section = new HorizontalSection("caption");
        Component component1 = new TextField();
        component1.setWidthFull();
        Component component2 = new TextField();
        component2.setWidthFull();

        section.add("component1", component1);
        section.add("component2", component2);

        HorizontalLayout content = (HorizontalLayout)section.getSectionContent();
        assertThat(content.getExpandRatio(component1), is(0.5f));
        assertThat(content.getExpandRatio(component2), is(0.5f));
    }


}
