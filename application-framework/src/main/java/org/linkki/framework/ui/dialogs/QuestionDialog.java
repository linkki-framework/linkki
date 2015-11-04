package org.linkki.framework.ui.dialogs;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A modal dialog that asks the user a question that can be confirmed with OK (and not confirmed
 * with Cancel).
 */
public class QuestionDialog extends OkCancelDialog {

    private static final long serialVersionUID = 1L;

    private final Label questionLabel = new Label();
    private final OkHandler okHandler;

    @FunctionalInterface
    public static interface OkHandler {
        void onOk();
    }

    public QuestionDialog(String caption, String question, OkHandler okHandler) {
        super(caption);
        questionLabel.setValue(question);
        this.okHandler = okHandler;
    }

    @Override
    protected void ok() {
        okHandler.onOk();
    }

    @Override
    protected Component createContent() {
        questionLabel.setWidthUndefined();
        questionLabel.setStyleName(ValoTheme.LABEL_H3);
        return questionLabel;
    }

}
