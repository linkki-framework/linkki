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
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;

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
        LinkkiSection section = new LinkkiSection("caption", true, 1);
        assertThat(section.isClosed(), is(false));

        section.close();

        assertThat(section.isClosed(), is(true));
        assertThat(section.isOpen(), is(false));
    }

    @Test
    void testClose_AlreadyClosed() {
        LinkkiSection section = new LinkkiSection("caption", true, 1);
        section.close();
        assertThat(section.isClosed(), is(true));

        section.close();

        assertThat(section.isClosed(), is(true));
        assertThat(section.isOpen(), is(false));
    }

    @Test
    void testOpen() {
        LinkkiSection section = new LinkkiSection("caption", true, 1);
        section.close();
        assertThat(section.isOpen(), is(false));

        section.open();

        assertThat(section.isClosed(), is(false));
        assertThat(section.isOpen(), is(true));
    }

    @Test
    void testOpen_AlreadyOpen() {
        LinkkiSection section = new LinkkiSection("caption", true, 1);
        assertThat(section.isOpen(), is(true));

        section.open();

        assertThat(section.isClosed(), is(false));
        assertThat(section.isOpen(), is(true));
    }

    @Test
    void testSwitchOpenStatus() {
        LinkkiSection section = new LinkkiSection("caption", true, 1);

        section.close();

        assertThat(section.getContentWrapper().isVisible(), is(false));
        assertThat(getCloseToggle(section).getIcon().getElement().getAttribute("icon"),
                   is("vaadin:angle-right"));

        section.open();

        assertThat(section.getContentWrapper().isVisible(), is(true));
        assertThat(getCloseToggle(section).getIcon().getElement().getAttribute("icon"),
                   is("vaadin:angle-down"));
    }

    @Test
    void testHeader_Caption() {
        LinkkiSection section = new LinkkiSection("CAP", false, 1);
        H4 captionLabel = getCaptionLabel(section);

        assertThat(captionLabel.isVisible(), is(true));
        assertThat(captionLabel.getText(), is("CAP"));
    }

    @Test
    void testHeader_CloseButton_DefaultShouldBeInvisible() {
        LinkkiSection section = new LinkkiSection("caption");

        assertThat(getCloseToggle(section).isVisible(), is(false));
    }

    @Test
    void testHeader_CloseButton_CloseableShouldResultInCloseButton() {
        LinkkiSection section = new LinkkiSection("caption", true, 1);

        assertThat(getCloseToggle(section).isVisible(), is(true));
    }

    @Test
    void testHeader_CloseButton_NotCloseableShouldHideCloseButton() {
        LinkkiSection section = new LinkkiSection("caption", false, 1);

        assertThat(getCloseToggle(section).isVisible(), is(false));
    }

    @Test
    void testHeader_CloseButtonStyle() {
        LinkkiSection section = new LinkkiSection("", true, 1);

        assertThat(getCloseToggle(section).getThemeNames()
                .contains(ButtonVariant.LUMO_TERTIARY_INLINE.getVariantName()),
                   is(true));
    }

    @Test
    void testSetCaption() {
        LinkkiSection section = new LinkkiSection("CAP", false, 1);
        H4 captionLabel = getCaptionLabel(section);

        assertThat(captionLabel.isVisible(), is(true));
        assertThat(captionLabel.getText(), is("CAP"));

        section.setCaption("TION");

        assertThat(captionLabel.isVisible(), is(true));
        assertThat(captionLabel.getText(), is("TION"));
    }

    @Test
    void testSetCaption_Null() {
        LinkkiSection section = new LinkkiSection("CAP", false, 1);
        H4 captionLabel = getCaptionLabel(section);

        assertThat(captionLabel.isVisible(), is(true));
        assertThat(captionLabel.getText(), is("CAP"));

        section.setCaption(null);

        assertThat(captionLabel.isVisible(), is(false));
    }

    @Test
    void testSetCaption_Empty() {
        LinkkiSection section = new LinkkiSection("CAP", false, 1);
        H4 captionLabel = getCaptionLabel(section);

        assertThat(captionLabel.isVisible(), is(true));
        assertThat(captionLabel.getText(), is("CAP"));

        section.setCaption("");

        assertThat(captionLabel.isVisible(), is(false));
    }

    @Test
    void testAddHeaderButton() {
        LinkkiSection section = new LinkkiSection("CAP", true, 1);

        assertThat(section.getHeaderComponents(), hasSize(1)); // caption

        Button button1 = new Button();
        section.addHeaderButton(button1);

        assertThat(section.getHeaderComponents(), hasSize(2));
        assertThat(section.getHeaderComponents().get(1), is(button1));

        Button button2 = new Button();
        section.addHeaderButton(button2);

        assertThat(section.getHeaderComponents(), hasSize(3));
        assertThat(section.getHeaderComponents().get(1), is(button2));
        assertThat(section.getHeaderComponents().get(2), is(button1));

        section.setCaption("UPDATE");

        assertThat(section.getHeaderComponents(), hasSize(3));
        assertThat(section.getHeaderComponents().get(1), is(button2));
        assertThat(section.getHeaderComponents().get(2), is(button1));
    }

    @Test
    void testHeaderButton_Style() {
        LinkkiSection section = new LinkkiSection("CAP", true, 1);
        Button button1 = new Button();

        section.addHeaderButton(button1);

        assertThat(button1.getThemeNames().contains(ButtonVariant.LUMO_TERTIARY_INLINE.getVariantName()), is(true));
    }

    @Test
    void testAddHeaderComponent_ComponentShouldBeAddedBeforeCloseButton() {
        LinkkiSection section = new LinkkiSection("Caption");

        section.addHeaderComponent(new Div());

        assertThat(section.getHeaderComponents(), hasSize(2));
        assertThat(section.getHeaderComponents().get(1), is(instanceOf(Div.class)));
    }

    @Test
    void testAddHeaderComponent_AddDefaultStyleToButton() {
        LinkkiSection section = new LinkkiSection("Caption");

        section.addHeaderComponent(new Button());

        assertThat(section.getHeaderComponents(), hasSize(2));
        assertThat(section.getHeaderComponents().get(1).getElement().getThemeList(),
                   Matchers.contains(ButtonVariant.LUMO_TERTIARY_INLINE.getVariantName()));
    }

    private H4 getCaptionLabel(LinkkiSection linkkiSection) {
        return (H4)linkkiSection.getHeaderComponents().get(0);
    }

    private Button getCloseToggle(LinkkiSection section) {
        return (Button)section.getChildren()
                .filter(c -> LinkkiSection.SLOT_CLOSE_TOGGLE.contentEquals(c.getElement().getAttribute("slot")))
                .findFirst().get();
    }

}
