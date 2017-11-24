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

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import javax.annotation.Nullable;

import org.linkki.core.binding.validation.ValidationDisplayState;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.message.Message;
import org.linkki.core.message.MessageList;
import org.linkki.core.ui.application.ApplicationStyles;
import org.linkki.core.ui.area.TabSheetArea;
import org.linkki.core.ui.page.Page;
import org.linkki.framework.ui.LinkkiStyles;
import org.linkki.framework.ui.component.MessageRow;
import org.linkki.framework.ui.nls.NlsText;
import org.linkki.util.handler.Handler;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A modal dialog with a header/title, an OK button and an optional cancel button at the bottom. To
 * add a component as dialog content use {@link #addContent(Component)}.
 * <p>
 * To create a dialog with fixed dimensions, use the method {@link #setSize(String, String)}. This
 * is useful if:
 * <ul>
 * <li>there are different options that may change the layout</li>
 * <li>you do not want that the dialog dynamically increase height for validation messages</li>
 * </ul>
 * For more information on sizing and layout behavior see {@link #setSize(String, String)}.
 * <p>
 * To validate the data in the dialog or give the user warning or information messages, set a
 * validation service via ({@link #setValidationService(ValidationService)}). The first message with
 * the highest {@link com.vaadin.server.ErrorMessage.ErrorLevel ErrorLevel} reported during the
 * validation via the {@link ValidationService#getValidationMessages()} is displayed at the bottom
 * of the dialog, between its content and the OK and cancel buttons. (see
 * {@link MessageList#getFirstMessage(ErrorMessage.ErrorLevel)})
 */
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
    public OkCancelDialog(String caption, Handler okHandler) {
        this(caption, okHandler, ButtonOption.OK_CANCEL);
    }

    /**
     * Creates a new dialog with the given caption.
     * 
     * @param caption the dialog's caption
     * @param okHandler the handler that handles clicks on the OK button
     * @param buttonOption whether to show both buttons (OK and Cancel) or only the OK button
     */
    public OkCancelDialog(String caption, Handler okHandler, ButtonOption buttonOption) {
        this(caption, null, okHandler, buttonOption);
    }

    /**
     * Creates a new dialog with the given caption.
     * 
     * @param caption the dialog's caption
     * @param okHandler the handler that handle clicks on the OK button
     * @param buttonOption whether to show both buttons (OK and Cancel) or only the OK button
     */
    public OkCancelDialog(String caption, @Nullable Component content, Handler okHandler,
            ButtonOption buttonOption) {
        super(caption);
        this.okHandler = requireNonNull(okHandler, "okHandler must not be null"); //$NON-NLS-1$
        this.layout = new VerticalLayout();
        this.contentArea = new VerticalLayout();
        this.mainArea = new VerticalLayout();
        this.okButton = new Button(NlsText.getString("OkCancelDialog.OkButtonCaption")); //$NON-NLS-1$
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
     * Overrides {@link Window#setContent(Component)} as the OkCancelDialog does not allow replacing
     * its entire content (e.g. the OK and Cancel buttons). Instead, only the content of the main
     * area is replaced.
     * <p>
     * Note that this also removes any components that were added using
     * {@link #addContent(Component)}.
     */
    // mainArea is null when setContent is called from the superclass constructor. For all other
    // purposes, we consider it @Nonnull
    @SuppressWarnings({ "null", "unused" })
    @Override
    public void setContent(@Nullable Component content) {
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
        setStyleName(LinkkiStyles.DIALOG_CAPTION);
        setModal(true);
        setResizable(false);
    }

    private void initLayout() {
        layout.setWidth("100%"); //$NON-NLS-1$
        layout.setMargin(true);
        layout.addComponent(contentArea);
        layout.setExpandRatio(contentArea, 1f);
    }

    private void initMainArea(@Nullable Component c) {
        mainArea.addStyleName(LinkkiStyles.DIALOG_CONTENT);
        contentArea.addStyleName("content-area"); //$NON-NLS-1$
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
            Button cancel = new Button(NlsText.getString("OkCancelDialog.CancelButtonCaption")); //$NON-NLS-1$
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
     * Use this method to create a dialog with fixed size. The size may be absolute or relative for
     * example 60% of browser window. Specifies the width and height of the dialog and sets all
     * internal layout components to full size.
     * <p>
     * If you specify the height and add multiple components using {@link #addContent(Component)}
     * all added components will have the same expand ratio by default (which is 0). This causes all
     * components to be assigned equal space. Use {@link #addContent(Component, float)} to assign a
     * specific expand ratios to a component. For example, if you want to have all components to use
     * only as much space as they need and the last component to consume all excess space, add the
     * last component using <code>addContent(component, 1)</code> and all other components without
     * expand ratio (using <code>addContent(component)</code>).
     * <p>
     * When calculating the correct height always consider that there might be validation messages
     * below your content. If the dialog's height is too small the components may overlap or be
     * cropped.
     * <p>
     * The dialog will never create scroll bars. If you want scroll bars, add a single panel as root
     * content, and configure it to use scroll bars. The header, the button(s) and the validation
     * messages will then always be visible.
     * <p>
     * Note: If you have multiple nested layout components (like {@link TabSheetArea tab sheet
     * areas}, {@link Page pages} or vaadin layouts you have to make sure that every component is
     * set to full size (AbstractComponent{@link #setSizeFull()}.
     * <p>
     * If you need a dialog with dynamic height you must not call this method.
     * 
     * @see AbstractOrderedLayout#setExpandRatio(Component, float)
     * 
     * @param width the width of the dialog including the unit (for example 700px or 65%)
     * @param height the height of the dialog including the unit (for example 700px or 65%)
     */
    public void setSize(String width, String height) {
        setHeight(height);
        setWidth(width);
        layout.setSizeFull();
        contentArea.setSizeFull();
        mainArea.setSizeFull();
    }

    /**
     * Retrieves the message list from the validation service and filters it according to its
     * {@link #getValidationDisplayState()}. The filtered messages are returned. If needed, a
     * message from the list is displayed and the OK button is disabled.
     * <p>
     * A previously displayed message is removed if the message list does not contain any messages.
     * If the message list contains a message, the first message with the highest errorLevel is
     * displayed. If the message list contains an error message the OK button is disabled.
     */
    public MessageList validate() {
        messages = validationDisplayState.filter(validationService.getValidationMessages());
        messageRow.ifPresent(contentArea::removeComponent);
        getMessageToDisplay().ifPresent(m -> {
            MessageRow newRow = new MessageRow(m);
            newRow.setWidth("100%"); //$NON-NLS-1$
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

    public ValidationService getValidationService() {
        return validationService;
    }

    /** Sets the validation service that validates data in the dialog. */
    public void setValidationService(ValidationService validationService) {
        this.validationService = requireNonNull(validationService, "validationService must not be null"); //$NON-NLS-1$
    }

    private Optional<Message> getMessageToDisplay() {
        return messages.getErrorLevel()
                .flatMap(messages::getFirstMessage);
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

    /**
     * Adds the given component to be displayed in the dialog. If you have fixed the height of the
     * dialog using {@link #setSize(String, String)} you might want to use
     * {@link #addContent(Component, float)} to assign specific expand ratios to components.
     */
    public void addContent(Component c) {
        mainArea.addComponent(c);
    }

    /**
     * Adds the given component to be displayed in the dialog with the given expand ratio. For more
     * explanation about expand ratio please read
     * {@link AbstractOrderedLayout#setExpandRatio(Component, float)}
     * 
     * @see AbstractOrderedLayout#setExpandRatio(Component, float)
     * 
     */
    public void addContent(Component c, float expandRatio) {
        mainArea.addComponent(c);
        mainArea.setExpandRatio(c, expandRatio);
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
