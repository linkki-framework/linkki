/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.dialogs;

import javax.annotation.Nonnull;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A dialog to present an information to the user, who has to confirm it with OK.
 */
public class ConfirmationDialog extends OkCancelDialog {

    private static final long serialVersionUID = 1L;

    private final Component content;

    public ConfirmationDialog(@Nonnull String caption, @Nonnull Component content, @Nonnull OkHandler okHandler) {
        super(caption, okHandler, ButtonOption.OK_ONLY);
        this.content = content;
        initContent();
    }

    @Override
    protected Component createContent() {
        return content;
    }

    /**
     * Opens the dialog.
     * 
     * @param caption The caption.
     * @param infoText The information text for the user.
     */
    public static void open(@Nonnull String caption, @Nonnull String infoText) {
        open(caption, infoText, OkHandler.NOP_HANDLER);
    }

    /**
     * Opens the dialog.
     * 
     * @param caption The caption.
     * @param infoText The information text for the user.
     * @param okHandler A function that is executed when the OK button was pressed.
     */
    public static void open(@Nonnull String caption, @Nonnull String infoText, @Nonnull OkHandler okHandler) {
        Label infoLabel = new Label();
        infoLabel.setValue(infoText);
        infoLabel.setStyleName(ValoTheme.LABEL_H3);
        infoLabel.setContentMode(ContentMode.HTML);
        ConfirmationDialog d = new ConfirmationDialog(caption, infoLabel, okHandler);
        UI.getCurrent().addWindow(d);
    }

    /**
     * Opens the dialog.
     * 
     * @param caption The caption.
     * @param content A component that is rendered as content
     */
    public static void open(@Nonnull String caption, @Nonnull Component content) {
        open(caption, content, OkHandler.NOP_HANDLER);
    }

    /**
     * Opens the dialog.
     * 
     * @param caption The caption.
     * @param content A component that is rendered as content
     * @param okHandler A function that is executed when the OK button was pressed.
     */
    public static void open(@Nonnull String caption, @Nonnull Component content, @Nonnull OkHandler okHandler) {
        ConfirmationDialog d = new ConfirmationDialog(caption, content, okHandler);
        UI.getCurrent().addWindow(d);
    }

}
