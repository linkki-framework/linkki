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

import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.layout.annotation.UICssLayout;
import org.linkki.framework.ui.dialogs.OkCancelDialog;
import org.linkki.framework.ui.notifications.NotificationUtil;

@UICssLayout
public class OkCancelDialogHandlerPmo {

    public static final String SHOW_DIALOG_BUTTON_ID = "showDialog";
    public static final String MESSAGE_OK = "OK";
    public static final String MESSAGE_CANCEL = "Cancel";

    @UIButton(position = 10, caption = "Open dialog")
    public void showDialog() {
        OkCancelDialog.builder("OkCancelDialog")
                .okHandler(this::onOk)
                .cancelHandler(this::onCancel)
                .build()
                .open();
    }

    private void onCancel() {
        NotificationUtil.showInfo(MESSAGE_CANCEL, "");
    }

    private void onOk() {
        NotificationUtil.showInfo(MESSAGE_OK, "");
    }

}
