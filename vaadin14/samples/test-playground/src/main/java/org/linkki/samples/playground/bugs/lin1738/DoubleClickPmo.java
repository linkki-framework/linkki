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

package org.linkki.samples.playground.bugs.lin1738;

import java.util.Optional;

import org.linkki.core.pmo.ButtonPmo;
import org.linkki.core.pmo.PresentationModelObject;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.pmo.ButtonPmoBuilder;

import com.vaadin.flow.component.notification.Notification;

@UISection(caption = "LIN-1738")
public class DoubleClickPmo implements PresentationModelObject {

    @UILabel(position = 0)
    public String getDescription() {
        return "Clicking buttons twice in rapid succession should not open two dialogs.";
    }

    @UIButton(position = 10, caption = "Open Dialog")
    public void dialogButton() {
        openDialog();
    }

    @Override
    public Optional<ButtonPmo> getEditButtonPmo() {
        return Optional.of(ButtonPmoBuilder.newEditButton(this::openDialog));
    }

    private void openDialog() {
        Notification.show("TODO Dialoge sind noch nicht implementiert");
        // OkCancelDialog dialog = new PmoBasedDialogFactory().newOkCancelDialog("LIN-1738 Dialog",
        // Handler.NOP_HANDLER);
        // dialog.setWidth("50%");
        // dialog.open();
    }

}
