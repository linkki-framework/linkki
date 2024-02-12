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

package org.linkki.samples.playground.ts.dialogs;

import org.linkki.core.ui.ComponentStyles;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.framework.ui.dialogs.PmoBasedDialogFactory;

@UISection(caption = "Dialog with custom label width")
public class SetFormItemLabelWidthDialogPmo {

    private String width = "240px";

    @UIButton(position = 40, label = "Opens a dialog with a custom label width", caption = "Open dialog")
    public void showDialog() {
        var dialog = new PmoBasedDialogFactory().newOkDialog("Custom label width dialog", new LabelTextfieldPmo());
        ComponentStyles.setFormItemLabelWidth(dialog, width);
        dialog.open();
    }

    @UISection
    public static class LabelTextfieldPmo {

        @UITextField(position = 10, label = "This label should be in one line")
        public String getText() {
            return "Ich bin ein Text!";
        }
    }
}
