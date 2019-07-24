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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.linkki.core.vaadin.component.ComponentFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class AbstractSectionTest {

    static class TestSection extends AbstractSection {

        public TestSection(String caption, boolean closeable) {
            super(caption, closeable);
        }

        private static final long serialVersionUID = 1L;

        public HorizontalLayout getHeader() {
            HorizontalLayout header = (HorizontalLayout)getComponent(0);
            assertThat(header, is(notNullValue()));
            return header;
        }

        @SuppressFBWarnings("NP_NONNULL_RETURN_VIOLATION")
        @Override
        public Component getSectionContent() {
            return null;
        }

    }

    @Test
    public void testHeader_Caption() {
        TestSection section = new TestSection("CAP", false);
        HorizontalLayout header = section.getHeader();
        Label captionLabel = (Label)header.getComponent(0);

        assertThat(header.isVisible(), is(true));
        assertThat(captionLabel.isVisible(), is(true));
        assertThat(captionLabel.getValue(), is("CAP"));
    }

    @Test
    public void testHeader_CloseButton() {
        TestSection section = new TestSection("", true);
        HorizontalLayout header = section.getHeader();
        Label captionLabel = (Label)header.getComponent(0);

        assertThat(header.isVisible(), is(true));
        assertThat(captionLabel.isVisible(), is(false));
    }

    @Test
    public void testHeader_AddHeaderButton() {
        TestSection section = new TestSection("", false);
        HorizontalLayout header = section.getHeader();
        Label captionLabel = (Label)header.getComponent(0);

        assertThat(header.isVisible(), is(false));
        assertThat(captionLabel.isVisible(), is(false));

        Button button = ComponentFactory.newButton();
        section.addHeaderButton(button);

        assertThat(header.isVisible(), is(true));
        assertThat(captionLabel.isVisible(), is(false));
    }

    @Test
    public void testSetCaption() {
        TestSection section = new TestSection("CAP", false);
        HorizontalLayout header = section.getHeader();
        Label captionLabel = (Label)header.getComponent(0);

        assertThat(header.isVisible(), is(true));
        assertThat(captionLabel.isVisible(), is(true));
        assertThat(captionLabel.getValue(), is("CAP"));

        section.setCaption("TION");

        assertThat(header.isVisible(), is(true));
        assertThat(captionLabel.isVisible(), is(true));
        assertThat(captionLabel.getValue(), is("TION"));
    }

    @Test
    public void testSetCaption_Null() {
        TestSection section = new TestSection("CAP", false);
        HorizontalLayout header = section.getHeader();
        Label captionLabel = (Label)header.getComponent(0);

        assertThat(header.isVisible(), is(true));
        assertThat(captionLabel.isVisible(), is(true));
        assertThat(captionLabel.getValue(), is("CAP"));

        section.setCaption(null);

        assertThat(header.isVisible(), is(false));
        assertThat(captionLabel.isVisible(), is(false));
    }

    @Test
    public void testSetCaption_Empty() {
        TestSection section = new TestSection("CAP", false);
        HorizontalLayout header = section.getHeader();
        Label captionLabel = (Label)header.getComponent(0);

        assertThat(header.isVisible(), is(true));
        assertThat(captionLabel.isVisible(), is(true));
        assertThat(captionLabel.getValue(), is("CAP"));

        section.setCaption("");

        assertThat(header.isVisible(), is(false));
        assertThat(captionLabel.isVisible(), is(false));
    }

    @Test
    public void testAddHeaderButton() {
        TestSection section = new TestSection("CAP", true);
        HorizontalLayout header = section.getHeader();

        assertThat(header.getComponentCount(), is(3)); // caption label, close button, line
        assertThat(header.isVisible(), is(true));

        Button button1 = new Button();
        section.addHeaderButton(button1);

        assertThat(header.getComponentCount(), is(4));
        assertThat(header.getComponent(1), is(button1));

        Button button2 = new Button();
        section.addHeaderButton(button2);

        assertThat(header.getComponentCount(), is(5));
        assertThat(header.getComponent(1), is(button2));
        assertThat(header.getComponent(2), is(button1));

        section.setCaption("UPDATE");

        assertThat(header.getComponentCount(), is(5));
        assertThat(header.getComponent(1), is(button2));
        assertThat(header.getComponent(2), is(button1));
    }

}
