package org.linkki.framework.ui.dialogs;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnull;

import org.linkki.core.ui.application.ApplicationStyles;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/** A modal dialog with OK and Cancel buttons. */
public abstract class OkCancelDialog extends Window {

    private static final long serialVersionUID = 1L;

    private boolean okPressed = false;
    private boolean cancelPressed = false;

    private final OkHandler okHandler;
    private final VerticalLayout contentContainer;

    /**
     * Creates a new dialog with the given caption and OK and Cancel button.
     */
    public OkCancelDialog(String caption) {
        this(caption, OkHandler.NOP_HANDLER, ButtonOption.OK_CANCEL);
    }

    /**
     * Creates a new dialog with the given caption and OK and Cancel button.
     * 
     * @param caption The caption.
     * @param theOkHandler Function called when the OK button was pressed.
     */
    public OkCancelDialog(String caption, @Nonnull OkHandler theOkHandler) {
        this(caption, theOkHandler, ButtonOption.OK_CANCEL);
    }

    /**
     * Creates a new dialog with the given caption.
     * 
     * @param caption The caption.
     * @param theOkHandler Function called when the OK button was pressed.
     * @param buttonOption whether to show OK and CANCEL button or only the OK button.
     */
    public OkCancelDialog(@Nonnull String caption, @Nonnull OkHandler theOkHandler,
            @Nonnull ButtonOption buttonOption) {
        this(caption, null, theOkHandler, buttonOption);
    }

    /**
     * Creates a new dialog with the given caption.
     * 
     * @param caption The caption.
     * @param theOkHandler Function called when the OK button was pressed.
     * @param buttonOption whether to show OK and CANCEL button or only the OK button.
     */
    public OkCancelDialog(@Nonnull String caption, Component content, @Nonnull OkHandler theOkHandler,
            @Nonnull ButtonOption buttonOption) {
        super(caption);
        okHandler = requireNonNull(theOkHandler);
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
        if (content != null) {
            contentContainer.addComponent(content);
        }
        main.addComponent(contentContainer);

        HorizontalLayout buttons = createButtons(buttonOption);
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

    /** Adds the given component to the content container of the dialog. */
    public void addContent(Component c) {
        contentContainer.addComponent(c);
    }

    /**
     * Returns <code>true</code> if OK was pressed.
     */
    public boolean isOkPressed() {
        return okPressed;
    }

    private void setOkPressed(boolean okPressed) {
        this.okPressed = okPressed;
    }

    /**
     * Returns <code>true</code> if Cancel was pressed.
     */
    public boolean isCancelPressed() {
        return cancelPressed;
    }

    private void setCancelPressed(boolean cancelPressed) {
        this.cancelPressed = cancelPressed;
    }

    /**
     * Called when the user clicks OK.
     */
    protected void ok() {
        okHandler.onOk();
    }

    /**
     * Called when the user clicks Cancel or closes the window. Default implementation does nothing.
     */
    protected void cancel() {
        // nothing to do as explained in the Java Doc.
    }

    private HorizontalLayout createButtons(ButtonOption buttonOption) {
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

        if (buttonOption == ButtonOption.OK_CANCEL) {
            Button cancel = new Button("Abbrechen");
            buttons.addComponent(cancel);
            buttons.setComponentAlignment(cancel, Alignment.MIDDLE_CENTER);
            cancel.addClickListener(e -> {
                setCancelPressed(true);
                cancel();
                close();
            });
        }
        return buttons;
    }

    /**
     * Options to choose the buttons.
     */
    public static enum ButtonOption {
        OK_ONLY,
        OK_CANCEL
    }

}
