/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

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
import com.vaadin.ui.HorizontalLayout;

public class AbstractSectionTest {

    static class TestSection extends AbstractSection {

        public TestSection(String caption, boolean closeable, Optional<Button> button) {
            super(caption, closeable, button);
        }

        private static final long serialVersionUID = 1L;

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
