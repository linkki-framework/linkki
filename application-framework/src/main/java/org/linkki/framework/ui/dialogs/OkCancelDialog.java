package org.linkki.framework.ui.dialogs;

import org.linkki.core.ui.application.ApplicationStyles;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * A modal dialog with OK and Cancel buttons.
 */
public abstract class OkCancelDialog extends Window {

    private static final long serialVersionUID = 1L;

    private boolean okPressed = false;
    private boolean cancelPressed = false;

    private final VerticalLayout contentContainer;

    /**
     * Creates a new dialog with the given caption.
     */
    public OkCancelDialog(String caption) {
        this(caption, null);
    }

    /**
     * Creates a new dialog with the given caption.
     * 
     * @param caption The caption.
     * @param params Parameters needed during creation of the dialog content.
     */
    public OkCancelDialog(String caption, DialogStartParameters params) {
        super(caption);
        setStyleName(ApplicationStyles.DIALOG_CAPTION);
        setModal(true);
        setResizable(false);

        VerticalLayout main = new VerticalLayout();
        setContent(main);
        main.setWidthUndefined();
        main.setMargin(true);

        this.contentContainer = new VerticalLayout();
        contentContainer.setWidthUndefined();
        contentContainer.setMargin(false);
        contentContainer.setStyleName(ApplicationStyles.DIALOG_CONTENT);
        main.addComponent(contentContainer);

        HorizontalLayout buttons = createButtons();
        main.addComponent(buttons);
        main.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);

        addCloseListener(e -> {
            if (!isOkPressed() && !isCancelPressed()) {
                // close event was triggered by user clicking on window close icon, not a button
                cancel();
            }
        });
        center();
    }

    /**
     * Returns <code>true</code> if OK was pressed.
     */
    public boolean isOkPressed() {
        return okPressed;
    }

    void setOkPressed(boolean okPressed) {
        this.okPressed = okPressed;
    }

    /**
     * Returns <code>true</code> if Cancel was pressed.
     */
    public boolean isCancelPressed() {
        return cancelPressed;
    }

    void setCancelPressed(boolean cancelPressed) {
        this.cancelPressed = cancelPressed;
    }

    public void initContent() {
        contentContainer.addComponent(createContent());
    }

    /**
     * Creates the content of this dialog.
     */
    protected abstract Component createContent();

    /**
     * Called when the user clicks OK. Default implementation does nothing.
     */
    protected abstract void ok();

    /**
     * Called when the user clicks Cancel or closes the window. Default implementation does nothing.
     */
    protected void cancel() {
        // nothing to do as explained in the Java Doc.
    }

    private HorizontalLayout createButtons() {
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidthUndefined();
        buttons.setSpacing(true);
        Button ok = new Button("OK");
        buttons.addComponent(ok);
        buttons.setComponentAlignment(ok, Alignment.MIDDLE_CENTER);
        ok.addClickListener(e -> {
            setOkPressed(true);
            ok();
            close();
        });

        Button cancel = new Button("Abbrechen");
        buttons.addComponent(cancel);
        buttons.setComponentAlignment(cancel, Alignment.MIDDLE_CENTER);
        cancel.addClickListener(e -> {
            setCancelPressed(true);
            cancel();
            close();
        });
        return buttons;
    }
}
