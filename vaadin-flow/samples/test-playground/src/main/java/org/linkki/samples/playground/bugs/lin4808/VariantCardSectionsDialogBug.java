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

package org.linkki.samples.playground.bugs.lin4808;

import java.util.List;

import org.linkki.core.ui.aspects.annotation.BindVariantNames;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.nested.annotation.UINestedComponent;
import org.linkki.core.ui.theme.LinkkiTheme;
import org.linkki.framework.ui.dialogs.PmoBasedDialogFactory;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class VariantCardSectionsDialogBug extends VerticalLayout {

    public static final String LIN_4808 = "LIN-4808";
    public static final String CAPTION = LIN_4808 + " :: Card sections in dialog";

    public static final String BUTTON_CAPTION_OPEN_DIALOG_PMO = "Open dialog with card styled section";
    public static final String BUTTON_CAPTION_OPEN_DIALOG_NESTED_PMOS = "Open dialog with nested card styled sections";
    public static final String DIALOG_CAPTION_PMO = "Dialog with card styled section";
    public static final String DIALOG_CAPTION_NESTED_PMOS = "Dialog with nested card styled sections";

    private static final long serialVersionUID = 1L;

    public VariantCardSectionsDialogBug() {
        add(new H4(CAPTION));
        add(new Div(
                """
                        LinkkiTheme.VARIANT_CARD_SECTIONS does not work in dialogs since the migration to Vaadin 25.
                        Click the buttons below to open dialogs containing sections with card styling."""));

        add(new Button(BUTTON_CAPTION_OPEN_DIALOG_PMO, e -> openPmoDialog()));
        add(new Button(BUTTON_CAPTION_OPEN_DIALOG_NESTED_PMOS, e -> openNestedPmoDialog()));
    }

    private void openPmoDialog() {
        var dialog = new PmoBasedDialogFactory()
                .openOkCancelDialog(DIALOG_CAPTION_PMO, Handler.NOP_HANDLER,
                                    new ContentPmo());
        dialog.setWidth("800px");
        dialog.getElement().getThemeList().add(LinkkiTheme.VARIANT_CARD_SECTIONS);
    }

    private void openNestedPmoDialog() {
        var dialog = new PmoBasedDialogFactory()
                .openOkCancelDialog(DIALOG_CAPTION_NESTED_PMOS, Handler.NOP_HANDLER,
                                    new NestedContentPmo());
        dialog.setWidth("800px");
    }

    @UISection(caption = "Section with card style")
    public static class ContentPmo {

        private String value = "Section";

        @UITextField(position = 10, label = "Value")
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @UIButton(position = 20, caption = "Click me")
        public void click() {
            // does nothing
        }
    }

    @UISection(caption = "Section without card style")
    public static class NestedContentPmo {

        @BindVariantNames({
                LinkkiTheme.VARIANT_CARD_SECTIONS, })
        @UINestedComponent(position = 10, styleNames = { LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Gap.LARGE })
        public List<ChildPmo> getChildPmos() {
            return List.of(new ChildPmo("Section 1"), new ChildPmo("Section 2 "));
        }
    }

    @UISection(caption = "Nested section with card style")
    public static class ChildPmo {

        private String value;

        public ChildPmo(String value) {
            this.value = value;
        }

        @UITextField(position = 10, label = "Value")
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @UIButton(position = 20, caption = "Click me")
        public void click() {
            // does nothing
        }
    }
}
