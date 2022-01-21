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
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.style.LinkkiTheme;
import org.linkki.core.vaadin.component.ComponentFactory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

class LinkkiSectionTest {

    @Test
    void testisClosed_DefaultShouldBeFalse() {
        LinkkiSection section = new LinkkiSection("caption");

        assertThat(section.isClosed(), is(false));
    }

    @Test
    void testisOpen_DefaultShouldBeTrue() {
        LinkkiSection section = new LinkkiSection("caption");

        assertThat(section.isOpen(), is(true));
    }

    @Test
    void testClose() {
        LinkkiSection section = new LinkkiSection("caption", true);
        assertThat(section.isClosed(), is(false));

        section.close();

        assertThat(section.isClosed(), is(true));
        assertThat(section.isOpen(), is(false));
    }

    @Test
    void testClose_AlreadyClosed() {
        LinkkiSection section = new LinkkiSection("caption", true);
        section.close();
        assertThat(section.isClosed(), is(true));

        section.close();

        assertThat(section.isClosed(), is(true));
        assertThat(section.isOpen(), is(false));
    }

    @Test
    void testOpen() {
        LinkkiSection section = new LinkkiSection("caption", true);
        section.close();
        assertThat(section.isOpen(), is(false));

        section.open();

        assertThat(section.isClosed(), is(false));
        assertThat(section.isOpen(), is(true));
    }

    @Test
    void testOpen_AlreadyOpen() {
        LinkkiSection section = new LinkkiSection("caption", true);
        assertThat(section.isOpen(), is(true));

        section.open();

        assertThat(section.isClosed(), is(false));
        assertThat(section.isOpen(), is(true));
    }

    @Test
    void testSwitchOpenStatus() {
        LinkkiSection section = new LinkkiSection("caption", true);

        section.close();

        assertThat(section.getContentWrapper().isVisible(), is(false));
        assertThat(((Button)getHeader(section).getComponentAt(1)).getIcon().getElement().getAttribute("icon"),
                   is("vaadin:angle-right"));

        section.open();

        assertThat(section.getContentWrapper().isVisible(), is(true));
        assertThat(((Button)getHeader(section).getComponentAt(1)).getIcon().getElement().getAttribute("icon"),
                   is("vaadin:angle-down"));
    }

    @Test
    void testHeader_Caption() {
        LinkkiSection section = new LinkkiSection("CAP", false);
        HorizontalLayout header = getHeader(section);
        H4 captionLabel = getCaptionLabel(header);

        assertThat(header.isVisible(), is(true));
        assertThat(captionLabel.isVisible(), is(true));
        assertThat(captionLabel.getText(), is("CAP"));
    }

    @Test
    void testHeader_CloseButton_DefaultShouldBeInvisible() {
        LinkkiSection section = new LinkkiSection("caption");
        HorizontalLayout header = getHeader(section);

        assertThat(header.getComponentAt(1).isVisible(), is(false));
    }

    @Test
    void testHeader_CloseButton_CloseableShouldResultInCloseButton() {
        LinkkiSection section = new LinkkiSection("caption", true);
        HorizontalLayout header = getHeader(section);

        assertThat(header.getComponentAt(1).isVisible(), is(true));
    }

    @Test
    void testHeader_CloseButton_NotCloseableShouldHideCloseButton() {
        LinkkiSection section = new LinkkiSection("caption", false);
        HorizontalLayout header = getHeader(section);

        assertThat(header.getComponentAt(1).isVisible(), is(false));
    }

    @Test
    void testHeader_CloseButtonStyle() {
        LinkkiSection section = new LinkkiSection("", true);

        HorizontalLayout header = getHeader(section);
        Button closeButton = (Button)header.getComponentAt(1);

        assertThat(closeButton.getClassName(), containsString(LinkkiTheme.BUTTON_TEXT));
    }

    @Test
    void testHeader_AddHeaderButton() {
        LinkkiSection section = new LinkkiSection("", false);
        HorizontalLayout header = getHeader(section);
        H4 captionLabel = getCaptionLabel(header);

        assertThat(header.isVisible(), is(false));
        assertThat(captionLabel.isVisible(), is(false));

        Button button = ComponentFactory.newButton();
        section.addHeaderButton(button);

        assertThat(header.isVisible(), is(true));
        assertThat(captionLabel.isVisible(), is(false));
    }

    @Test
    void testSetCaption() {
        LinkkiSection section = new LinkkiSection("CAP", false);
        HorizontalLayout header = getHeader(section);
        H4 captionLabel = getCaptionLabel(header);

        assertThat(header.isVisible(), is(true));
        assertThat(captionLabel.isVisible(), is(true));
        assertThat(captionLabel.getText(), is("CAP"));

        section.setCaption("TION");

        assertThat(header.isVisible(), is(true));
        assertThat(captionLabel.isVisible(), is(true));
        assertThat(captionLabel.getText(), is("TION"));
    }

    @Test
    void testSetCaption_Null() {
        LinkkiSection section = new LinkkiSection("CAP", false);
        HorizontalLayout header = getHeader(section);
        H4 captionLabel = getCaptionLabel(header);

        assertThat(header.isVisible(), is(true));
        assertThat(captionLabel.isVisible(), is(true));
        assertThat(captionLabel.getText(), is("CAP"));

        section.setCaption(null);

        assertThat(header.isVisible(), is(false));
        assertThat(captionLabel.isVisible(), is(false));
    }

    @Test
    void testSetCaption_Empty() {
        LinkkiSection section = new LinkkiSection("CAP", false);
        HorizontalLayout header = getHeader(section);
        H4 captionLabel = getCaptionLabel(header);

        assertThat(header.isVisible(), is(true));
        assertThat(captionLabel.isVisible(), is(true));
        assertThat(captionLabel.getText(), is("CAP"));

        section.setCaption("");

        assertThat(header.isVisible(), is(false));
        assertThat(captionLabel.isVisible(), is(false));
    }

    @Test
    void testAddHeaderButton() {
        LinkkiSection section = new LinkkiSection("CAP", true);
        HorizontalLayout header = getHeader(section);

        assertThat(header.getComponentCount(), is(2)); // caption label and close button. line is css
        assertThat(header.isVisible(), is(true));

        Button button1 = new Button();
        section.addHeaderButton(button1);

        assertThat(header.getComponentCount(), is(3));
        assertThat(header.getComponentAt(1), is(button1));

        Button button2 = new Button();
        section.addHeaderButton(button2);

        assertThat(header.getComponentCount(), is(4));
        assertThat(header.getComponentAt(1), is(button2));
        assertThat(header.getComponentAt(2), is(button1));

        section.setCaption("UPDATE");

        assertThat(header.getComponentCount(), is(4));
        assertThat(header.getComponentAt(1), is(button2));
        assertThat(header.getComponentAt(2), is(button1));
    }

    @Test
    void testHeaderButton_Style() {
        LinkkiSection section = new LinkkiSection("CAP", true);
        Button button1 = new Button();

        section.addHeaderButton(button1);

        assertThat(button1.getClassName(), containsString(LinkkiTheme.BUTTON_TEXT));
    }

    @Test
    void testAddHeaderComponent_ComponentShouldBeAddedBeforeCloseButton() {
        LinkkiSection section = new LinkkiSection("Caption");

        section.addHeaderComponent(new Div());

        assertThat(getHeader(section).getComponentCount(), is(3));
        assertThat(getHeader(section).getComponentAt(1), is(instanceOf(Div.class)));
    }

    @Test
    void testAddHeaderComponent_AddDefaultStyleToButton() {
        LinkkiSection section = new LinkkiSection("Caption");

        section.addHeaderComponent(new Button());

        assertThat(getHeader(section).getComponentCount(), is(3));
        assertThat(getHeader(section).getComponentAt(1).getElement().getThemeList(),
                   Matchers.contains(ButtonVariant.LUMO_TERTIARY_INLINE.getVariantName()));
    }

    @Test
    void testAddHeaderComponent_InvisibleHeaderShouldBecomeVisible() {
        LinkkiSection section = new LinkkiSection("");
        assertThat(getHeader(section).isVisible(), is(false));

        section.addHeaderComponent(new Div());

        assertThat(getHeader(section).isVisible(), is(true));
    }

    private HorizontalLayout getHeader(LinkkiSection linkkiSection) {
        return (HorizontalLayout)linkkiSection.getComponentAt(0);
    }

    private H4 getCaptionLabel(HorizontalLayout header) {
        H4 captionLabel = (H4)header.getComponentAt(0);
        return captionLabel;
    }

}
