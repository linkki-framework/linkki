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
package org.linkki.framework.ui.dialogs;

import org.linkki.core.util.HtmlSanitizer;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;

/**
 * A dialog to present an information to the user, who has to confirm it with OK.
 */
public class ConfirmationDialog extends OkCancelDialog {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new dialog.
     * 
     * @param caption the caption
     * @param okHandler a function that is executed when the OK button was pressed
     * @param contentComponents the components containing the information that should be confirmed
     */
    public ConfirmationDialog(String caption, Handler okHandler, Component... contentComponents) {
        super(caption, okHandler, okHandler, ButtonOption.OK_ONLY, contentComponents);
    }

    /**
     * Opens the dialog.
     * 
     * @param caption the caption
     * @param infoText the information text for the user
     */
    public static ConfirmationDialog open(String caption, String infoText) {
        return open(caption, infoText, Handler.NOP_HANDLER);
    }

    /**
     * Opens the dialog.
     * 
     * @param caption the caption
     * @param infoText the information text for the user
     * @param okHandler a function that is executed when the OK button was pressed
     */
    public static ConfirmationDialog open(String caption,
            String infoText,
            Handler okHandler) {
        Html content = new Html("<span>" + HtmlSanitizer.sanitizeText(infoText) + "</span>");
        return open(caption, content, okHandler);
    }

    /**
     * Opens the dialog.
     * 
     * @param caption the caption
     * @param content a component that is rendered as content
     */
    public static ConfirmationDialog open(String caption, Component content) {
        return open(caption, content, Handler.NOP_HANDLER);
    }

    /**
     * Opens the dialog.
     * 
     * @param caption the caption
     * @param content a component that is rendered as content
     * @param okHandler a function that is executed when the OK button was pressed
     */
    public static ConfirmationDialog open(String caption,
            Component content,
            Handler okHandler) {
        ConfirmationDialog d = new ConfirmationDialog(caption, okHandler, content);
        d.open();
        return d;
    }

}
