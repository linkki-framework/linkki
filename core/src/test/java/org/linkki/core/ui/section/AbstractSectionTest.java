/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.core.ui.section;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Test;
import org.linkki.core.ButtonPmo;
import org.linkki.core.ui.util.ComponentFactory;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

public class AbstractSectionTest {

    static class TestSection extends AbstractSection {

        public TestSection(String caption, boolean closeable, Optional<Button> button) {
            super(caption, closeable, button);
        }

        private static final long serialVersionUID = 1L;

        @Override
        public Component getSectionContent() {
            return null;
        }

    }

    static class TestButton1 implements ButtonPmo {

        static final FontAwesome ICON = FontAwesome.ADJUST;

        @Override
        public void onClick() {
            // Nothing to do
        }

        @Override
        public Resource getButtonIcon() {
            return ICON;
        }

    }

    static class TestButton2 implements ButtonPmo {

        static final FontAwesome ICON = FontAwesome.WON;

        @Override
        public void onClick() {
            // Nothing to do
        }

        @Override
        public Resource getButtonIcon() {
            return ICON;
        }

    }

    @SuppressWarnings("deprecation")
    @Test
    public void testAddHeaderButton_HeaderButtonIsAddedBeforeCloseButton() {
        AbstractSection section = new TestSection("caption", true, Optional.empty());
        HorizontalLayout sectionHeader = (HorizontalLayout)section.getComponent(0);

        assertThat(sectionHeader, is(notNullValue()));
        assertThat(sectionHeader.getComponentCount(), is(3)); // label, close button, line
        assertThat(sectionHeader.getComponent(1), is(instanceOf(Button.class)));
        Button closeButton = (Button)sectionHeader.getComponent(1);
        assertThat(closeButton.getIcon(), is(FontAwesome.ANGLE_DOWN));

        section.addHeaderButton(ComponentFactory.newButton(new TestButton1()));
        assertThat(sectionHeader.getComponentCount(), is(4));
        assertThat(sectionHeader.getComponent(2), is(closeButton));
        Button button1 = (Button)sectionHeader.getComponent(1);
        assertThat(button1.getIcon(), is(TestButton1.ICON));

        section.addHeaderButton(ComponentFactory.newButton(new TestButton2()));
        assertThat(sectionHeader.getComponentCount(), is(5));
        assertThat(sectionHeader.getComponent(3), is(closeButton));
        Button button2 = (Button)sectionHeader.getComponent(2);
        assertThat(button2.getIcon(), is(TestButton2.ICON));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testAddHeaderButton_HeaderButtonIsAddedAtEndIfCloseButtonIsMissing() {
        AbstractSection section = new TestSection("caption", false, Optional.empty());
        HorizontalLayout sectionHeader = (HorizontalLayout)section.getComponent(0);

        assertThat(sectionHeader, is(notNullValue()));
        assertThat(sectionHeader.getComponentCount(), is(2)); // label, line

        section.addHeaderButton(ComponentFactory.newButton(new TestButton1()));
        assertThat(sectionHeader.getComponentCount(), is(3));
        Button button1 = (Button)sectionHeader.getComponent(1);
        assertThat(button1.getIcon(), is(TestButton1.ICON));

        section.addHeaderButton(ComponentFactory.newButton(new TestButton2()));
        assertThat(sectionHeader.getComponentCount(), is(4));
        assertThat(sectionHeader.getComponent(1), is(button1));
        Button button2 = (Button)sectionHeader.getComponent(2);
        assertThat(button2.getIcon(), is(TestButton2.ICON));
    }

}
