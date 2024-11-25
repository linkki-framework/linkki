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
package org.linkki.core.vaadin.component.section;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;

class LinkkiSectionTest {

    @Test
    void testisClosed_DefaultShouldBeFalse() {
        LinkkiSection section = new LinkkiSection("caption");

        assertThat(section.isClosed()).isFalse();
    }

    @Test
    void testisOpen_DefaultShouldBeTrue() {
        LinkkiSection section = new LinkkiSection("caption");

        assertThat(section.isOpen()).isTrue();
    }

    @Test
    void testClose() {
        LinkkiSection section = new LinkkiSection("caption", true, 1);
        assertThat(section.isClosed()).isFalse();

        section.close();

        assertThat(section.isClosed()).isTrue();
        assertThat(section.isOpen()).isFalse();
    }

    @Test
    void testClose_AlreadyClosed() {
        LinkkiSection section = new LinkkiSection("caption", true, 1);
        section.close();
        assertThat(section.isClosed()).isTrue();

        section.close();

        assertThat(section.isClosed()).isTrue();
        assertThat(section.isOpen()).isFalse();
    }

    @Test
    void testOpen() {
        LinkkiSection section = new LinkkiSection("caption", true, 1);
        section.close();
        assertThat(section.isOpen()).isFalse();

        section.open();

        assertThat(section.isClosed()).isFalse();
        assertThat(section.isOpen()).isTrue();
    }

    @Test
    void testOpen_AlreadyOpen() {
        LinkkiSection section = new LinkkiSection("caption", true, 1);
        assertThat(section.isOpen()).isTrue();

        section.open();

        assertThat(section.isClosed()).isFalse();
        assertThat(section.isOpen()).isTrue();
    }

    @Test
    void testSwitchOpenStatus() {
        LinkkiSection section = new LinkkiSection("caption", true, 1);

        section.close();

        assertThat(section.getContentWrapper().isVisible()).isFalse();
        assertThat(getCloseToggle(section).getIcon().getElement().getAttribute("icon")).isEqualTo("vaadin:angle-right");

        section.open();

        assertThat(section.getContentWrapper().isVisible()).isTrue();
        assertThat(getCloseToggle(section).getIcon().getElement().getAttribute("icon"))
                .isEqualTo("vaadin:angle-down");
    }

    @Test
    void testHeader_Caption() {
        LinkkiSection section = new LinkkiSection("CAP", false, 1);
        H4 captionLabel = getCaptionLabel(section);

        assertThat(captionLabel.isVisible()).isTrue();
        assertThat(captionLabel.getText()).isEqualTo("CAP");
    }

    @Test
    void testHeader_CloseButton_DefaultShouldBeInvisible() {
        LinkkiSection section = new LinkkiSection("caption");

        assertThat(getCloseToggle(section).isVisible()).isFalse();
    }

    @Test
    void testHeader_CloseButton_CloseableShouldResultInCloseButton() {
        LinkkiSection section = new LinkkiSection("caption", true, 1);

        assertThat(getCloseToggle(section).isVisible()).isTrue();
    }

    @Test
    void testHeader_CloseButton_NotCloseableShouldHideCloseButton() {
        LinkkiSection section = new LinkkiSection("caption", false, 1);

        assertThat(getCloseToggle(section).isVisible()).isFalse();
    }

    @Test
    void testHeader_CloseButtonStyle() {
        LinkkiSection section = new LinkkiSection("", true, 1);

        assertThat(getCloseToggle(section).getThemeNames()
                .contains(ButtonVariant.LUMO_TERTIARY_INLINE.getVariantName())).isTrue();
    }

    @Test
    void testSetCaption() {
        LinkkiSection section = new LinkkiSection("CAP", false, 1);
        H4 captionLabel = getCaptionLabel(section);

        assertThat(captionLabel.isVisible()).isTrue();
        assertThat(captionLabel.getText()).isEqualTo("CAP");

        section.setCaption("TION");

        assertThat(captionLabel.isVisible()).isTrue();
        assertThat(captionLabel.getText()).isEqualTo("TION");
    }

    @Test
    void testSetCaption_Null() {
        LinkkiSection section = new LinkkiSection("CAP", false, 1);
        H4 captionLabel = getCaptionLabel(section);

        assertThat(captionLabel.isVisible()).isTrue();
        assertThat(captionLabel.getText()).isEqualTo("CAP");

        section.setCaption(null);

        assertThat(captionLabel.isVisible()).isFalse();
    }

    @Test
    void testSetCaption_Empty() {
        LinkkiSection section = new LinkkiSection("CAP", false, 1);
        H4 captionLabel = getCaptionLabel(section);

        assertThat(captionLabel.isVisible()).isTrue();
        assertThat(captionLabel.getText()).isEqualTo("CAP");

        section.setCaption("");

        assertThat(captionLabel.isVisible()).isFalse();
    }

    @Test
    void testAddHeaderButton() {
        LinkkiSection section = new LinkkiSection("CAP", true, 1);

        assertThat(section.getHeaderComponents()).hasSize(1); // caption

        Button button1 = new Button();
        section.addHeaderButton(button1);

        assertThat(section.getHeaderComponents()).hasSize(2);
        assertThat(section.getHeaderComponents().get(1)).isSameAs(button1);

        Button button2 = new Button();
        section.addHeaderButton(button2);

        assertThat(section.getHeaderComponents()).hasSize(3);
        assertThat(section.getHeaderComponents().get(1)).isSameAs(button2);
        assertThat(section.getHeaderComponents().get(2)).isSameAs(button1);

        section.setCaption("UPDATE");

        assertThat(section.getHeaderComponents()).hasSize(3);
        assertThat(section.getHeaderComponents().get(1)).isSameAs(button2);
        assertThat(section.getHeaderComponents().get(2)).isSameAs(button1);
    }

    @Test
    void testHeaderButton_Style() {
        LinkkiSection section = new LinkkiSection("CAP", true, 1);
        Button button1 = new Button();

        section.addHeaderButton(button1);

        assertThat(button1.getThemeNames().contains(ButtonVariant.LUMO_TERTIARY_INLINE.getVariantName())).isTrue();
    }

    @Test
    void testAddHeaderComponent_ComponentShouldBeAddedBeforeCloseButton() {
        LinkkiSection section = new LinkkiSection("Caption");

        section.addHeaderComponent(new Div());

        assertThat(section.getHeaderComponents()).hasSize(2);
        assertThat(section.getHeaderComponents().get(1)).isInstanceOf(Div.class);
    }

    @Test
    void testAddHeaderComponent_AddDefaultStyleToButton() {
        LinkkiSection section = new LinkkiSection("Caption");

        section.addHeaderComponent(new Button());

        assertThat(section.getHeaderComponents()).hasSize(2);
        assertThat(section.getHeaderComponents().get(1).getElement().getThemeList())
                .contains(ButtonVariant.LUMO_TERTIARY_INLINE.getVariantName());
    }

    @Test
    void testAddRightHeaderComponent_WithoutComponents() {
        LinkkiSection section = new LinkkiSection("Caption");

        var headerComponents = getHeaderComponents(section, LinkkiSection.SLOT_RIGHT_HEADER_COMPONENTS);

        assertThat(headerComponents).isEmpty();
    }

    @Test
    void testAddRightHeaderComponent_WithComponents() {
        LinkkiSection section = new LinkkiSection("Caption");
        section.addRightHeaderComponent(new Button("test1"));
        section.addRightHeaderComponent(new Button("test2"));

        var headerComponents = getHeaderComponents(section, LinkkiSection.SLOT_RIGHT_HEADER_COMPONENTS);

        assertThat(headerComponents).hasSize(2);
    }

    @Test
    void testGetPlaceholder_Null() {
        var section = new LinkkiSection("Caption");
        assertThat(section.getPlaceholder()).isEmpty();
        assertThat(section.getStyle().get(LinkkiSection.CSS_PROPERTY_PLACEHOLDER)).isNull();
    }

    @Test
    void testGetPlaceholder_Empty() {
        var section = new LinkkiSection("Caption");
        section.setPlaceholder("");
        assertThat(section.getPlaceholder()).isEmpty();
        assertThat(section.getStyle().get(LinkkiSection.CSS_PROPERTY_PLACEHOLDER)).isEqualTo("''");
    }

    @Test
    void testSetPlaceholder() {
        var placeholder = "I am the placeholder text";
        var section = new LinkkiSection("Caption");

        section.setPlaceholder(placeholder);

        assertThat(section.getPlaceholder()).isEqualTo(placeholder);
        assertThat(section.getStyle().get(LinkkiSection.CSS_PROPERTY_PLACEHOLDER)).contains(placeholder);
    }

    private H4 getCaptionLabel(LinkkiSection linkkiSection) {
        return (H4)linkkiSection.getHeaderComponents().get(0);
    }

    private Button getCloseToggle(LinkkiSection section) {
        return (Button)getHeaderComponents(section, LinkkiSection.SLOT_CLOSE_TOGGLE).get(0);
    }

    private List<Component> getHeaderComponents(LinkkiSection section, String slot) {
        return section.getChildren()
                .filter(c -> slot.contentEquals(c.getElement().getAttribute("slot")))
                .collect(Collectors.toList());
    }
}
