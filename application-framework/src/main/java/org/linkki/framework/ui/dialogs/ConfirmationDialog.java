/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.framework.ui.dialogs;

import org.linkki.util.handler.Handler;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A dialog to present an information to the user, who has to confirm it with OK.
 */
public class ConfirmationDialog extends OkCancelDialog {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new dialog.
     * 
     * @param caption The caption.
     * @param content A component containing the question to ask.
     * @param okHandler A function that is executed when the OK button was pressed.
     */
    public ConfirmationDialog(String caption, Component content, Handler okHandler) {
        super(caption, content, okHandler, ButtonOption.OK_ONLY);
    }

    /**
     * Calls the {@link #ok()} method as a confirmation dialog does only allow confirmation. The
     * cancel method is called if the user closes the dialog using the close button instead of the
     * OK button.
     */
    @Override
    protected void cancel() {
        ok();
    }

    /**
     * Opens the dialog.
     * 
     * @param caption The caption.
     * @param infoText The information text for the user.
     */
    public static ConfirmationDialog open(String caption, String infoText) {
        return open(caption, infoText, Handler.NOP_HANDLER);
    }

    /**
     * Opens the dialog.
     * 
     * @param caption The caption.
     * @param infoText The information text for the user.
     * @param okHandler A function that is executed when the OK button was pressed.
     */
    public static ConfirmationDialog open(String caption,
            String infoText,
            Handler okHandler) {
        Label infoLabel = new Label();
        infoLabel.setValue(infoText);
        infoLabel.setStyleName(ValoTheme.LABEL_H3);
        infoLabel.setContentMode(ContentMode.HTML);
        return open(caption, infoLabel, okHandler);
    }

    /**
     * Opens the dialog.
     * 
     * @param caption The caption.
     * @param content A component that is rendered as content
     */
    public static ConfirmationDialog open(String caption, Component content) {
        return open(caption, content, Handler.NOP_HANDLER);
    }

    /**
     * Opens the dialog.
     * 
     * @param caption The caption.
     * @param content A component that is rendered as content
     * @param okHandler A function that is executed when the OK button was pressed.
     */
    public static ConfirmationDialog open(String caption,
            Component content,
            Handler okHandler) {
        ConfirmationDialog d = new ConfirmationDialog(caption, content, okHandler);
        d.open();
        return d;
    }

}
