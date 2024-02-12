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

import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.framework.ui.dialogs.PmoBasedDialogFactory;

@UISection(caption = "Dialog with a custom size")
public class DialogWithCustomSizePmo {

    private String width = "500px";
    private String height = "500px";

    @UITextField(position = 10, label = "Dialog width")
    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    @UITextField(position = 20, label = "Dialog height")
    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    @UIButton(position = 40, label = "Opens a dialog with a custom size", caption = "Open dialog")
    public void showDialog() {
        var dialog = new PmoBasedDialogFactory().newOkDialog("Dialog with a custom size");
        dialog.setSize(width, height);
        dialog.open();
    }
}
