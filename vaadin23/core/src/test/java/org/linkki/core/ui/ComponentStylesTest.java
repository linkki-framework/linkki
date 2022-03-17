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

package org.linkki.core.ui;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.core.vaadin.component.section.LinkkiSection;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ClassList;
import com.vaadin.flow.dom.Style;

class ComponentStylesTest {

    @Test
    void testSetFormItemLabelWidth_NoHasStyle() {
        var dialog = new Dialog();
        assertThat(dialog).isNotInstanceOf(HasStyle.class);

        ComponentStyles.setFormItemLabelWidth(dialog, "100%");

        assertThat(dialog.getElement().getStyle().get("--linkki-form-item-label-width")).isEqualTo("100%");
    }

    @Test
    void testSetFormItemLabelWidth_HasStyle() {
        var dialog = new DialogWithStyleOnContentLayout();

        ComponentStyles.setFormItemLabelWidth(dialog, "100%");

        assertThat(dialog.getElement().getStyle().get("--linkki-form-item-label-width")).isNull();
        assertThat(dialog.getContentLayout().getStyle().get("--linkki-form-item-label-width")).isEqualTo("100%");
    }

    @Test
    void testSetOverflowAuto_NoHasStyle() {
        var dialog = new Dialog();
        assertThat(dialog).isNotInstanceOf(HasStyle.class);

        ComponentStyles.setOverflowAuto(dialog);

        assertThat(dialog.getElement().getStyle().get("overflow")).isEqualTo("auto");
    }

    @Test
    void testSetOverFlowAuto_HasStyle() {
        var dialog = new DialogWithStyleOnContentLayout();

        ComponentStyles.setOverflowAuto(dialog);

        assertThat(dialog.getElement().getStyle().get("overflow")).isNull();
        assertThat(dialog.getContentLayout().getStyle().get("overflow")).isEqualTo("auto");
    }

    @Test
    void testSetCardLikeSections_NoHasStyle() {
        var dialog = new Dialog();
        assertThat(dialog).isNotInstanceOf(HasStyle.class);

        ComponentStyles.setCardLikeSections(dialog);

        assertThat(dialog.getElement().getClassList()).contains(LinkkiSection.CLASS_SECTION_STYLE_CARD);
    }

    @Test
    void testSetCardLikeSections_HasStyle() {
        var dialog = new DialogWithStyleOnContentLayout();

        ComponentStyles.setCardLikeSections(dialog);

        assertThat(dialog.getElement().getClassList()).doesNotContain(LinkkiSection.CLASS_SECTION_STYLE_CARD);
        assertThat(dialog.getClassNames()).contains(LinkkiSection.CLASS_SECTION_STYLE_CARD);
    }

    private static class DialogWithStyleOnContentLayout extends Dialog implements HasStyle {

        private static final long serialVersionUID = 1L;
        private final VerticalLayout contentLayout;

        public DialogWithStyleOnContentLayout() {
            contentLayout = new VerticalLayout();
            add(contentLayout);
        }

        public VerticalLayout getContentLayout() {
            return contentLayout;
        }

        @Override
        public Style getStyle() {
            return contentLayout.getStyle();
        }

        @Override
        public void setClassName(String className) {
            contentLayout.setClassName(className);
        }

        @Override
        public String getClassName() {
            return contentLayout.getClassName();
        }

        @Override
        public ClassList getClassNames() {
            return contentLayout.getClassNames();
        }
    }
}
