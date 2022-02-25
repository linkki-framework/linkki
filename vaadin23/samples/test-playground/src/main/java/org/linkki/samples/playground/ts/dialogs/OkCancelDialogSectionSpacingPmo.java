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

package org.linkki.samples.playground.ts.dialogs;

import java.time.LocalDate;

import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.framework.ui.dialogs.PmoBasedDialogFactory;

@UIVerticalLayout
public class OkCancelDialogSectionSpacingPmo {

    @UIButton(position = 10, caption = "Open Dialog")
    public void openDialog() {
        new PmoBasedDialogFactory()
                .newOkDialog("Dialog with spacing between sections", new SimpleSectionPmo1(),
                             new SimpleSectionPmo2(), new SimpleSectionPmo3())
                .open();
    }

    @UISection(caption = "Section 1")
    static class SimpleSectionPmo1 extends SimpleSectionPmo {
        // no content needed
    }

    @UISection(caption = "Second section")
    static class SimpleSectionPmo2 extends SimpleSectionPmo {
        // no content needed
    }

    @UISection(caption = "Another section")
    static class SimpleSectionPmo3 extends SimpleSectionPmo {
        // no content needed
    }

    abstract static class SimpleSectionPmo {

        private String text;
        private LocalDate date;

        @UITextField(position = 10, label = "@UITextField")
        public String getTextField() {
            return text;
        }

        public void setTextField(String text) {
            this.text = text;
        }

        @UIDateField(position = 20, label = "@UIDateField")
        public LocalDate getDateField() {
            return date;
        }

        public void setDateField(LocalDate date) {
            this.date = date;
        }
    }
}
