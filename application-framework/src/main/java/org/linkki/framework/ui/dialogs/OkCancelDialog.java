package org.linkki.framework.ui.dialogs;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;
import org.linkki.core.binding.validation.ValidationDisplayState;
import org.linkki.core.binding.validation.ValidationService;
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

    private final VerticalLayout contentArea;

    /** The main area that contains any content that is added by subclasses etc. */
    private final VerticalLayout mainArea;

    /** The OK button that is displayed in the dialog. */
    private final Button okButton;

    /** The handler that handles clicks on the OK button. */
    private final Handler okHandler;

    /** The validation messages displayed in the dialog. */
    private MessageList messages = new MessageList();

    /** Service to validate data in the dialog, called when the user clicks OK. */
    private ValidationService validationService = ValidationService.NOP_VALIDATION_SERVICE;

    /** The state which validation messages are displayed. */
    private ValidationDisplayState validationDisplayState = ValidationDisplayState.HIDE_MANDATORY_FIELD_VALIDATIONS;

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
        this.contentArea = new VerticalLayout();
        this.mainArea = new VerticalLayout();
        this.okButton = new Button("OK");
        okButton.setClickShortcut(KeyCode.ENTER);
        okButton.setStyleName(ValoTheme.BUTTON_PRIMARY);

        initDialogWindow();
        initLayout();
        initMainArea(content);
        initButtons(buttonOption);
        initCloseListener();

        // We want to set the dialog's content, the method is overridden here
        super.setContent(layout);
        center();
    }

    /**
     * Override {@link Window#setContent(Component)} as the OkCancelDialog does not allow replacing
     * its entire content (e.g. the OK and Cancel buttons). Instead, the content of the main area
     * need to be replaced.
     * <p>
     * Note that this will remove any components that were added using
     * {@link #addContent(Component)}.
     */
    @Override
    public void setContent(Component content) {
        // This method is invoked by superclass constructors. In this case the superclass'
        // implementation has to be used. Once initialization is finished, we implement a different
        // behavior (as described in the JavaDoc).
        //
        // This is bad. Thou shalt not call non-private methods in thy constructor!
        if (mainArea == null) {
            super.setContent(content);
        } else {
            mainArea.removeAllComponents();
            mainArea.addComponent(content);
        }
    }

    private void initDialogWindow() {
        setStyleName(ApplicationStyles.DIALOG_CAPTION);
        setModal(true);
        setResizable(false);
    }

    private void initLayout() {
        layout.setWidth("100%");
        layout.setMargin(true);
        layout.addComponent(contentArea);
        layout.setExpandRatio(contentArea, 1f);
    }

    private void initMainArea(Component c) {
        mainArea.addStyleName(ApplicationStyles.DIALOG_CONTENT);
        contentArea.addStyleName("content-area");
        contentArea.addComponent(mainArea);
        contentArea.setExpandRatio(mainArea, 1f);
        if (c != null) {
            mainArea.addComponent(c);
        }
    }

    private void initButtons(ButtonOption buttonOption) {
        HorizontalLayout buttons = createButtons(buttonOption);
        layout.addComponent(buttons);
        layout.setExpandRatio(buttons, 0f);
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
        buttons.addStyleName(ApplicationStyles.DIALOG_BUTTON_BAR);
        buttons.setSizeFull();
        buttons.setWidthUndefined();
        buttons.setSpacing(true);
        buttons.addComponent(okButton);
        buttons.setComponentAlignment(okButton, Alignment.BOTTOM_CENTER);
        okButton.addClickListener(e -> {
            setOkPressed();
            if (!validate().containsErrorMsg()) {
                ok();
                close();
            }
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
     * Specifies the height of the content in PIXELS.
     * <p>
     * If you specify the height you have to ensure that the content has enough space to be visible.
     * The dialog will not create scroll bars. If you have dynamic content like a table you have to
     * specify the height of the table by giving a page length or fixed height (page length must be
     * 0). The height is given in PIXELS, relative height would not work. Alternatively you should
     * consider to use {@link #setContentHeightEm(int)} to specify the height in unit EM.
     * <p>
     * If you need a dialog with dynamic height you must not call this method.
     * 
     * @param height The height of the content area in pixel.
     */
    public void setContentHeight(int height) {
        this.contentArea.setHeight(height, Unit.PIXELS);
    }

    /**
     * Specifies the height of the content in EM.
     * <p>
     * If you specify the height you have to ensure that the content has enough space to be visible.
     * The dialog will not create scroll bars. If you have dynamic content like a table you have to
     * specify the height of the table by giving a page length or fixed height (page length must be
     * 0). The height is given in EM, relative height would not work. Alternatively you should
     * consider to use {@link #setContentHeight(int)} to specify the height in unit PIXELS.
     * <p>
     * If you need a dialog with dynamic height you must not call this method.
     * 
     * @param height The height of the content area in pixel.
     */
    public void setContentHeightEm(int height) {
        this.contentArea.setHeight(height, Unit.EM);
    }

    /**
     * Retrieves the message list from the validation service and filters it according to its
     * {@link #getValidationDisplayState()}. The filtered messages are returned. If needed, a
     * message from the list is displayed and the OK button is disabled.
     * <p>
     * A previously displayed message is removed if the message list does not contain any messages.
     * If the message list contains a message, the first message with the highest severity is
     * displayed. If the message list contains an error message the OK button is disabled.
     */
    public MessageList validate() {
        messages = validationDisplayState.filter(validationService.getValidationMessages());
        messageRow.ifPresent(contentArea::removeComponent);
        getMessageToDisplay().ifPresent(m -> {
            MessageRow newRow = new MessageRow(m);
            newRow.setWidth("100%");
            messageRow = Optional.of(newRow);
            contentArea.addComponent(newRow);
            contentArea.setExpandRatio(newRow, 0f);
            contentArea.setComponentAlignment(newRow, Alignment.MIDDLE_LEFT);
        });
        update();
        return messages;
    }

    /**
     * Returns the messages from which a message is displayed in the dialog (if the list is not
     * empty).
     */
    public MessageList getMessages() {
        return messages;
    }

    /** Returns the validation service that validates data in the dialog. */
    @Nonnull
    public ValidationService getValidationService() {
        return validationService;
    }

    /** Sets the validation service that validates data in the dialog. */
    public void setValidationService(@Nonnull ValidationService validationService) {
        this.validationService = requireNonNull(validationService);
    }

    private Optional<Message> getMessageToDisplay() {
        return Optional.ofNullable(messages.getFirstMessage(messages.getSeverity()));
    }

    /**
     * Updates the state of the dialog. Currently only the enabled state of the OK button is
     * updated.
     * 
     * @see #isOkEnabled()
     */
    private void update() {
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
        return !messages.containsErrorMsg();
    }

    /**
     * Returns {@code true} if the OK button was pressed.
     */
    public boolean isOkPressed() {
        return okPressed;
    }

    /** Returns the state regarding the displayed validation messages. */
    public ValidationDisplayState getValidationDisplayState() {
        return validationDisplayState;
    }

    private void setOkPressed() {
        this.okPressed = true;
        this.validationDisplayState = ValidationDisplayState.SHOW_ALL;
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

    /**
     * Called when the user clicks the OK button and {@link #validate()} does not return any error
     * messages. Delegates to the dialog's OkHandler.
     */
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
