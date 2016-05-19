package org.linkki.framework.ui.dialogs;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;
import org.linkki.core.ui.application.ApplicationStyles;
import org.linkki.framework.ui.component.MessageRow;
import org.linkki.util.handler.Handler;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/** A modal dialog with OK and Cancel buttons. */
public class OkCancelDialog extends Window {

    private static final long serialVersionUID = 1L;

    private boolean okPressed = false;
    private boolean cancelPressed = false;

    /**
     * The overall layout of this window. This is the content of the dialog window that contains all
     * other UI component.
     */
    private final VerticalLayout layout;

    /** The main area that contains any content that is added by subclasses etc. */
    private final VerticalLayout mainArea;

    /** The OK button that is displayed in the dialog. */
    private final Button okButton;

    /** The handler that handles clicks on the OK button. */
    private final Handler okHandler;

    /** The message list that is displayed in this dialog. */
    private MessageList messageList = new MessageList();

    /**
     * The message row that displays the first message from the message list if there is a message
     * to display.
     */
    private Optional<MessageRow> messageRow = Optional.empty();

    /**
     * Creates a new dialog with the given caption that displays both the OK and Cancel button and
     * uses a handler that does nothing when the OK button is clicked.
     * 
     * @param caption the dialog's caption
     */
    public OkCancelDialog(String caption) {
        this(caption, Handler.NOP_HANDLER, ButtonOption.OK_CANCEL);
    }

    /**
     * Creates a new dialog with the given caption that displays both the OK and Cancel button.
     * 
     * @param caption the dialog's caption
     * @param okHandler okHandler the handler that handles clicks on the OK button
     */
    public OkCancelDialog(String caption, @Nonnull Handler okHandler) {
        this(caption, okHandler, ButtonOption.OK_CANCEL);
    }

    /**
     * Creates a new dialog with the given caption.
     * 
     * @param caption the dialog's caption
     * @param okHandler the handler that handles clicks on the OK button
     * @param buttonOption whether to show both buttons (OK and Cancel) or only the OK button
     */
    public OkCancelDialog(@Nonnull String caption, @Nonnull Handler okHandler, @Nonnull ButtonOption buttonOption) {
        this(caption, null, okHandler, buttonOption);
    }

    /**
     * Creates a new dialog with the given caption.
     * 
     * @param caption the dialog's caption
     * @param okHandler the handler that handle clicks on the OK button
     * @param buttonOption whether to show both buttons (OK and Cancel) or only the OK button
     */
    public OkCancelDialog(@Nonnull String caption, Component content, @Nonnull Handler okHandler,
            @Nonnull ButtonOption buttonOption) {
        super(caption);
        this.okHandler = requireNonNull(okHandler);
        this.layout = new VerticalLayout();
        this.mainArea = new VerticalLayout();
        this.okButton = new Button("OK");
        okButton.setClickShortcut(KeyCode.ENTER);
        okButton.setStyleName(ValoTheme.BUTTON_PRIMARY);

        initDialogWindow();
        initLayout();
        initMainArea(content);
        initButtons(buttonOption);
        initCloseListener();

        setContent(layout);
        center();
    }

    /**
     * If a fixed sized is set to the dialog window, the two inner vertical layouts gets 100% width
     * to give the space to their components. If the dialog window width is set to 100%, the inner
     * layouts get undefined width so that the actual width is defined by the content of the inner
     * layout (main area).
     */
    @Override
    public void setWidth(float width, Unit unit) {
        super.setWidth(width, unit);

        // Superclass constructor calls this method so we have to handle uninitialized fields...
        if (layout == null || mainArea == null) {
            return;
        }
        if (width == 100f && unit == Unit.PERCENTAGE) {
            layout.setWidthUndefined();
            mainArea.setWidthUndefined();
        } else {
            layout.setWidth(100f, Unit.PERCENTAGE);
            mainArea.setWidth(100f, Unit.PERCENTAGE);
        }
    }

    private void initDialogWindow() {
        setStyleName(ApplicationStyles.DIALOG_CAPTION);
        setModal(true);
        setResizable(false);
    }

    private void initLayout() {
        layout.setWidthUndefined();
        layout.setMargin(true);
    }

    private void initMainArea(Component c) {
        mainArea.setWidthUndefined();
        mainArea.setMargin(false);
        mainArea.setStyleName(ApplicationStyles.DIALOG_CONTENT);
        layout.addComponent(mainArea);

        if (c != null) {
            mainArea.addComponent(c);
        }
    }

    private void initButtons(ButtonOption buttonOption) {
        HorizontalLayout buttons = createButtons(buttonOption);
        layout.addComponent(buttons);
        layout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
    }

    private void initCloseListener() {
        addCloseListener(e -> {
            if (!isOkPressed() && !isCancelPressed()) {
                // close event was triggered by user clicking on window close icon, not a button
                cancel();
            }
        });
    }

    private HorizontalLayout createButtons(ButtonOption buttonOption) {
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidthUndefined();
        buttons.setSpacing(true);
        buttons.addComponent(okButton);
        buttons.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
        okButton.addClickListener(e -> {
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
     * Displays a message from given message list and disables the OK button if needed.
     * <p>
     * A previously displayed message is removed. If the message list contains a message, the first
     * message with the highest severity is displayed. If the message list contains an error message
     * the OK button is disabled.
     */
    public void setMessageList(@Nonnull MessageList messageList) {
        this.messageList = requireNonNull(messageList);
        messageRow.ifPresent(layout::removeComponent);
        getMessageToDisplay().ifPresent(m -> {
            MessageRow newRow = new MessageRow(m);
            newRow.setWidth("100%");
            messageRow = Optional.of(newRow);
            layout.addComponentAsFirst(newRow);
        });
        update();
    }

    /** Returns the message list displayed in the dialog. */
    public MessageList getMessages() {
        return messageList;
    }

    private Optional<Message> getMessageToDisplay() {
        return Optional.ofNullable(messageList.getFirstMessage(messageList.getSeverity()));
    }

    /**
     * Updates the state of the dialog. Currently only the enabled state of the OK button is
     * updated.
     * 
     * @see #isOkEnabled()
     */
    public void update() {
        okButton.setEnabled(isOkEnabled());
    }

    /** Adds the given component to be displayed in the dialog. */
    public void addContent(Component c) {
        mainArea.addComponent(c);
    }

    /**
     * Returns whether or not the OK button is enabled. The OK button is enabled when
     * {@link #getMessages()} does not contain an error message.
     */
    public boolean isOkEnabled() {
        return !messageList.containsErrorMsg();
    }

    /**
     * Returns {@code true} if the OK button was pressed.
     */
    public boolean isOkPressed() {
        return okPressed;
    }

    private void setOkPressed(boolean okPressed) {
        this.okPressed = okPressed;
    }

    /**
     * Returns {@code true} if the cancel button was pressed.
     */
    public boolean isCancelPressed() {
        return cancelPressed;
    }

    private void setCancelPressed(boolean cancelPressed) {
        this.cancelPressed = cancelPressed;
    }

    /** Called when the user clicks the OK button. Delegates to the dialog's OkHandler. */
    protected void ok() {
        okHandler.apply();
    }

    /**
     * Called when the user clicks the cancel button or closes the window. Default implementation
     * does nothing.
     */
    protected void cancel() {
        // nothing to do as explained in the Java Doc.
    }

    /**
     * Options to choose the buttons.
     */
    public static enum ButtonOption {
        OK_ONLY,
        OK_CANCEL
    }

}
