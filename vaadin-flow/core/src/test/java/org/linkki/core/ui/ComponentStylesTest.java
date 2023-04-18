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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ClassList;
import com.vaadin.flow.dom.Style;

class ComponentStylesTest {

    @Test
    void testSetFormItemLabelWidth_Null() {
        assertDoesNotThrow(() -> {
            ComponentStyles.setFormItemLabelWidth(null, "100%");
        });
    }

    @Test
    void testSetFormItemLabelWidth_Dialog() {
        var dialog = new Dialog();

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
    void testSetOverflowAuto_Null() {
        assertDoesNotThrow(() -> {
            ComponentStyles.setOverflowAuto(null);
        });
    }

    @Test
    void testSetOverflowAuto_Dialog() {
        var dialog = new Dialog();

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

    // Simulate OkCancelDialog for testing
    private static class DialogWithStyleOnContentLayout extends Composite<Dialog> {

        private static final long serialVersionUID = 1L;
        private final VerticalLayout contentLayout;

        public DialogWithStyleOnContentLayout() {
            contentLayout = new VerticalLayout();
            getContent().add(contentLayout);
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
