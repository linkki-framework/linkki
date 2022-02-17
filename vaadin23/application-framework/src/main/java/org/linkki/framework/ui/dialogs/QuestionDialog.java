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
package org.linkki.framework.ui.dialogs;

import org.linkki.util.HtmlSanitizer;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;

/**
 * A modal dialog that asks the user a question that can be confirmed with OK (and not confirmed with
 * Cancel).
 */
public class QuestionDialog extends OkCancelDialog {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new dialog.
     * 
     * @param caption the caption
     * @param content a component containing the question to ask
     * @param okHandler a function that is executed when the OK button was pressed
     */
    public QuestionDialog(String caption, Component content, Handler okHandler) {
        super(caption, okHandler, Handler.NOP_HANDLER, ButtonOption.OK_CANCEL, content);
    }

    /**
     * Opens the dialog.
     * 
     * @param caption the caption
     * @param question the question to ask the user
     * @param okHandler a function that is executed when the OK button was pressed
     */
    public static QuestionDialog open(String caption, String question, Handler okHandler) {
        Html content = new Html("<span>" + HtmlSanitizer.sanitize(question) + "</span>");
        return open(caption, content, okHandler);
    }

    /**
     * Opens the dialog.
     * 
     * @param caption he caption
     * @param content a component containing the question to ask
     * @param okHandler a function that is executed when the OK button was pressed
     */
    public static QuestionDialog open(String caption,
            Component content,
            Handler okHandler) {
        QuestionDialog d = new QuestionDialog(caption, content, okHandler);
        d.open();
        return d;
    }

}
