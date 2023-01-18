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

package org.linkki.samples.playground.dialogs;

import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.framework.ui.dialogs.PmoBasedDialogFactory;

public class VerticalLayoutContentDialog {

    @UISection(caption = "Dialog with an UIVerticalLayout")
    public static class VerticalLayoutContentDialogPmo {

        @UIButton(position = 0, label = "Opens a dialog with an UIVerticalLayout", caption = "Open dialog")
        public void button() {
            // tag::openDialog[]
            new PmoBasedDialogFactory().openOkDialog("Dialog containing @UIVerticalLayout",
                                                     new VerticalLayoutTestContentPmo());
            // end::openDialog[]
        }

    }

    // tag::verticalLayout[]
    @UIVerticalLayout
    private static class VerticalLayoutTestContentPmo {
        @UILabel(position = 0)
        public String getText() {
            return "Some text";
        }
    }
    // end::verticalLayout[]
}
