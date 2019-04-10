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

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.validation.ValidationDisplayState;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.defaults.style.LinkkiTheme;
import org.linkki.core.ui.component.area.TabSheetArea;
import org.linkki.core.ui.component.page.Page;
import org.linkki.framework.ui.LinkkiApplicationTheme;
import org.linkki.framework.ui.component.MessageUiComponents;
import org.linkki.framework.ui.nls.NlsText;
import org.linkki.util.handler.Handler;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page.PopStateListener;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * A modal dialog with a header/title, an OK button and an optional cancel button at the bottom. To add
 * a component as dialog content use {@link #addContent(Component)}.
 * <p>
 * To create a new {@link OkCancelDialog}, consider using the {@link Builder} that can be retrieved by
 * calling {@link #builder(String)}.
 * 
 * @implNote To create a dialog with fixed dimensions, use the method {@link #setSize(String, String)}.
 *           This is useful if:
 *           <ul>
 *           <li>there are different options that may change the layout</li>
 *           <li>you do not want that the dialog dynamically increase height for validation
 *           messages</li>
 *           </ul>
 *           For more information on sizing and layout behavior see {@link #setSize(String, String)}.
 *           <p>
 *           To validate the data in the dialog or give the user warning or information messages, set a
 *           validation service via ({@link #setValidationService(ValidationService)}). The first
 *           message with the highest {@link Severity} reported during the validation via the
 *           {@link ValidationService#getValidationMessages()} is displayed at the bottom of the dialog,
 *           between its content and the OK and cancel buttons. (see
 *           {@link MessageList#getFirstMessage(Severity)})
 */
public class OkCancelDialog extends Window {

    private static final long serialVersionUID = 1L;

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

    /** The handler that handles clicks on the Cancel button. */
    private final Handler cancelHandler;

    /** Service to validate data in the dialog, called when the user clicks OK. */
    private ValidationService validationService = ValidationService.NOP_VALIDATION_SERVICE;

    /** The state which validation messages are displayed. */
    private ValidationDisplayState validationDisplayState = ValidationDisplayState.HIDE_MANDATORY_FIELD_VALIDATIONS;

    /** Called when the dialog was validated, e.g. after the OK button was clicked. */
    private Handler beforeOkHandler = Handler.NOP_HANDLER;

    /**
     * The message row that displays the first message from the message list if there is a message to
     * display.
     */
    private Optional<Component> messageRow = Optional.empty();

    private boolean okPressed = false;
    private boolean cancelPressed = false;
    private boolean mayProceed = true;

    /**
     * Creates a new dialog.
     * 
     * @param caption the dialog's caption
     * @param okHandler the handler that handle clicks on the OK button
     * @param cancelHandler the handler that is apply upon clicks on the Cancel button or upon closing
     *            the dialog
     * @param buttonOption whether to show both buttons (OK and Cancel) or only the OK button
     * @param contentComponents dialog's main area content
     */
    protected OkCancelDialog(String caption, Handler okHandler, Handler cancelHandler,
            ButtonOption buttonOption, Component... contentComponents) {
        super(caption);
        this.okHandler = requireNonNull(okHandler, "okHandler must not be null"); //$NON-NLS-1$
        this.cancelHandler = requireNonNull(cancelHandler, "cancelHandler must not be null"); //$NON-NLS-1$
        this.layout = new VerticalLayout();
        this.contentArea = new VerticalLayout();
        this.mainArea = new VerticalLayout();
        this.okButton = new Button(NlsText.getString("OkCancelDialog.OkButtonCaption")); //$NON-NLS-1$

        okButton.setClickShortcut(KeyCode.ENTER);
        okButton.setStyleName(ValoTheme.BUTTON_PRIMARY);

        initDialogWindow();
        initLayout();
        initMainArea(contentComponents);
        initButtons(buttonOption);
        initCloseListener();

        // We want to set the dialog's content, the method is overridden here
        super.setContent(layout);

        center();
    }

    /**
     * Creates a new dialog with the given caption that displays both the OK and Cancel button and uses
     * a handler that does nothing when the OK button is clicked.
     * 
     * @param caption the dialog's caption
     * 
     * @deprecated due to the introduction of the more flexible {@link Builder}. Use
     *             {@link #builder(String)} to create a builder instead.
     */
    @Deprecated
    public OkCancelDialog(String caption) {
        this(caption, Handler.NOP_HANDLER, Handler.NOP_HANDLER, ButtonOption.OK_CANCEL);
    }

    /**
     * Creates a new dialog with the given caption that displays both the OK and Cancel button.
     * 
     * @param caption The dialog's caption
     * @param okHandler The handler that handles clicks on the OK button
     * 
     * @deprecated due to the introduction of the more flexible {@link Builder}. Use
     *             {@link #builder(String)} to create a builder instead.
     */
    @Deprecated
    public OkCancelDialog(String caption, Handler okHandler) {
        this(caption, okHandler, Handler.NOP_HANDLER, ButtonOption.OK_CANCEL);
    }

    /**
     * Creates a new dialog with the given caption.
     * 
     * @param caption The dialog's caption
     * @param okHandler The handler that handles clicks on the OK button
     * @param buttonOption Whether to show both buttons (OK and Cancel) or only the OK button
     * 
     * @deprecated due to the introduction of the more flexible {@link Builder}. Use
     *             {@link #builder(String)} to create a builder instead.
     */
    @Deprecated
    public OkCancelDialog(String caption, Handler okHandler, ButtonOption buttonOption) {
        this(caption, okHandler, Handler.NOP_HANDLER, buttonOption);
    }

    /**
     * Creates a new dialog with the given caption.
     * 
     * @param caption the dialog's caption
     * @param content dialog's main area content
     * @param okHandler the handler that handle clicks on the OK button
     * @param buttonOption whether to show both buttons (OK and Cancel) or only the OK button
     * 
     * @deprecated due to the introduction of the more flexible {@link Builder}. Use
     *             {@link #builder(String)} to create a builder instead.
     */
    @Deprecated
    public OkCancelDialog(String caption, Component content, Handler okHandler, ButtonOption buttonOption) {
        this(caption, okHandler, Handler.NOP_HANDLER, buttonOption, toArray(content));
    }

    private static Component[] toArray(@CheckForNull Component contentComponent) {
        if (contentComponent == null) {
            return new Component[] {};
        } else {
            return new Component[] { contentComponent };
        }
    }

    /**
     * Overrides {@link Window#setContent(Component)} as the OkCancelDialog does not allow replacing its
     * entire content (e.g. the OK and Cancel buttons). Instead, only the content of the main area is
     * replaced.
     * <p>
     * Note that this also removes any components that were added using {@link #addContent(Component)}.
     */
    // mainArea is null when setContent is called from the superclass constructor. For all other
    // purposes, we consider it @NonNull
    @SuppressFBWarnings(value = "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE", justification = "yeah, we know...")
    @Override
    public void setContent(@CheckForNull Component content) {
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
        setStyleName(LinkkiApplicationTheme.DIALOG_CAPTION);
        setModal(true);
        setResizable(false);
    }

    private void initLayout() {
        layout.setWidth("100%"); //$NON-NLS-1$
        layout.setMargin(true);
        layout.addComponent(contentArea);
        layout.setExpandRatio(contentArea, 1f);
    }

    private void initMainArea(Component... contentComponents) {
        mainArea.addStyleName(LinkkiApplicationTheme.DIALOG_CONTENT);
        contentArea.addStyleName("content-area"); //$NON-NLS-1$
        contentArea.addComponent(mainArea);
        contentArea.setExpandRatio(mainArea, 1f);
        for (Component contentComponent : contentComponents) {
            mainArea.addComponent(contentComponent);
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
        buttons.addStyleName(LinkkiTheme.DIALOG_BUTTON_BAR);
        buttons.setSpacing(true);
        buttons.addComponent(okButton);
        buttons.setComponentAlignment(okButton, Alignment.BOTTOM_CENTER);
        okButton.addClickListener(e -> {
            setOkPressed();
            beforeOkHandler.andThen(() -> {
                if (mayProceed) {
                    ok();
                    close();
                }
            }).apply();

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
     * example 60% of browser window. Specifies the width and height of the dialog and sets all internal
     * layout components to full size.
     * <p>
     * If you specify the height and add multiple components using {@link #addContent(Component)} all
     * added components will have the same expand ratio by default (which is 0). This causes all
     * components to be assigned equal space. Use {@link #addContent(Component, float)} to assign a
     * specific expand ratios to a component. For example, if you want to have all components to use
     * only as much space as they need and the last component to consume all excess space, add the last
     * component using <code>addContent(component, 1)</code> and all other components without expand
     * ratio (using <code>addContent(component)</code>).
     * <p>
     * When calculating the correct height always consider that there might be validation messages below
     * your content. If the dialog's height is too small the components may overlap or be cropped.
     * <p>
     * The dialog will never create scroll bars. If you want scroll bars, add a single panel as root
     * content, and configure it to use scroll bars. The header, the button(s) and the validation
     * messages will then always be visible.
     * <p>
     * Note: If you have multiple nested layout components (like {@link TabSheetArea tab sheet areas},
     * {@link Page pages} or vaadin layouts you have to make sure that every component is set to full
     * size (AbstractComponent{@link #setSizeFull()}.
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
     * {@link #getValidationDisplayState()}. The filtered messages are returned. If needed, a message
     * from the list is displayed and the OK button is disabled.
     * 
     * @implSpec A previously displayed message is removed if the message list does not contain any
     *           messages. If the message list contains a message, the first message with the highest
     *           errorLevel is displayed. If the message list contains an error message the OK button is
     *           disabled.
     */
    public MessageList validate() {
        MessageList messages = validationDisplayState.filter(getValidationService().getValidationMessages());
        messageRow.ifPresent(contentArea::removeComponent);
        messages.getSeverity()
                .flatMap(messages::getFirstMessage)
                .ifPresent(this::addMessageRow);
        mayProceed = !messages.containsErrorMsg();
        okButton.setEnabled(mayProceed);
        return messages;
    }

    private void addMessageRow(Message message) {
        Component messageLabel = MessageUiComponents.createMessageComponent(message);
        messageRow = Optional.of(messageLabel);
        contentArea.addComponent(messageLabel);
    }


    /** Returns the validation service that validates data in the dialog. */

    public ValidationService getValidationService() {
        return validationService;
    }

    /** Sets the validation service that validates data in the dialog. */
    public void setValidationService(ValidationService validationService) {
        this.validationService = requireNonNull(validationService, "validationService must not be null"); //$NON-NLS-1$
    }

    /**
     * Sets a handler that will be called after the OK button is pressed but before the
     * {@code okHandler} (given to this dialog's constructor). This handler may change this dialog's
     * (validation) state so that the {@code okHandler} may not be called afterwards.
     */
    public void setBeforeOkHandler(Handler beforeOkHandler) {
        this.beforeOkHandler = beforeOkHandler;
    }

    /**
     * Opens this dialog in the current window.
     */
    public OkCancelDialog open() {
        UI current = UI.getCurrent();
        if (current != null) {
            current.addWindow(this);
            initURIChangeListener();
        }
        return this;
    }

    /**
     * Add {@link PopStateListener} to the dialog. By default, the dialog is closed upon uri change by
     * calling {@link #close()}.
     */
    protected void initURIChangeListener() {
        UI current = UI.getCurrent();
        current.getPage().addPopStateListener(e -> close());
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
     * Called when the user clicks the Cancel button or closes the window. Delegates to the dialog's
     * CancelHandler.
     */
    protected void cancel() {
        cancelHandler.apply();
    }

    /**
     * Options to choose the buttons.
     */
    public static enum ButtonOption {
        OK_ONLY,
        OK_CANCEL
    }


    /**
     * Creates a {@link Builder} for a {@link OkCancelDialog} with the required parameters for a
     * {@link OkCancelDialog}.
     *
     * @param caption caption the dialog's caption
     *
     * @return a new {@link Builder}
     *
     * @see Builder
     */
    public static Builder builder(String caption) {
        return new Builder(caption);
    }

    /**
     * A builder for the {@link OkCancelDialog} class. This builder to simply the creation of a
     * {@link OkCancelDialog}.
     * <p>
     * To use the builder simply create an instance by calling the
     * {@link OkCancelDialog#builder(String)}. Afterwards add needed information to the builder for
     * example call {@link #okHandler(Handler okHandler)} to provide an {@link Handler} for clicking on
     * the OK button. When the builder has every information that is needed to create a proper
     * {@link OkCancelDialog} call {@link #build()}.
     */
    @SuppressWarnings("hiding")
    public static class Builder {

        private String caption;
        private Component[] contentComponents = new Component[] {};
        private Handler okHandler = Handler.NOP_HANDLER;
        private Handler cancelHandler = Handler.NOP_HANDLER;
        private ButtonOption buttonOption = ButtonOption.OK_CANCEL;

        @CheckForNull
        private String width;
        @CheckForNull
        private String height;

        /**
         * Creates a new builder that is able to create a proper {@link Message} with all needed
         * information.
         * 
         * @param caption the dialog's caption
         */
        Builder(String caption) {
            this.caption = requireNonNull(caption, "caption must not be null");
        }

        /**
         * Sets content of main area that should be provided to the new {@link OkCancelDialog}.
         * 
         * @param contentComponents components that should be added to the main area of the resulting
         *            {@link OkCancelDialog}
         * @return this builder instance to directly add further properties
         */
        public Builder content(Component... contentComponents) {
            this.contentComponents = requireNonNull(contentComponents, "content must not be null");
            return this;
        }

        /**
         * Sets {@link Handler okHandler}, which will be invoked on click on the OK button in the
         * resulting {@link OkCancelDialog}.
         * 
         * @param okHandler the handler that handle clicks on the OK button on resulting
         *            {@link OkCancelDialog}
         * @return this builder instance to directly add further properties
         */
        public Builder okHandler(Handler okHandler) {
            this.okHandler = requireNonNull(okHandler, "okHandler must not be null");
            return this;
        }

        /**
         * Sets {@link Handler cancelHandler} which will be invoked on click on the Cancel button in the
         * resulting {@link OkCancelDialog} or when the dialog is closed.
         * 
         * @param cancelHandler the handler that handles
         * @return this builder instance to directly add further properties
         */
        public Builder cancelHandler(Handler cancelHandler) {
            this.cancelHandler = requireNonNull(cancelHandler, "cancelHandler must not be null");
            return this;
        }

        /**
         * Sets {@link ButtonOption}, which will control apprearence of OK, Cancel buttons in the new
         * {@link OkCancelDialog}.
         * 
         * @param buttonOption whether to show both buttons (OK and Cancel) or only the OK button on
         *            resulting {@link OkCancelDialog}
         * @return this builder instance to directly add further properties
         */
        public Builder buttonOption(ButtonOption buttonOption) {
            this.buttonOption = requireNonNull(buttonOption, "buttonOption must not be null");
            return this;
        }

        /**
         * Sets the size of the dialog.
         * 
         * @param width the width of the dialog including the unit (for example 700px or 65%)
         * @param height the height of the dialog including the unit (for example 700px or 65%)
         * @return this builder instance to directly add further properties
         * 
         * @see OkCancelDialog#setSize(String, String)
         */
        public Builder size(String width, String height) {
            this.width = requireNonNull(width, "width must not be null");
            this.height = requireNonNull(height, "height must not be null");
            return this;
        }

        /**
         * Creates a new {@link OkCancelDialog} with all previously given properties.
         * 
         * @return a new {@link OkCancelDialog} that has the parameters of this builder.
         */
        public OkCancelDialog build() {
            OkCancelDialog createdDialog = new OkCancelDialog(caption, okHandler, cancelHandler, buttonOption,
                    contentComponents);
            if (StringUtils.isNotBlank(width) && StringUtils.isNotBlank(height)) {
                createdDialog.setSize(width, height);
            }
            return createdDialog;
        }
    }
}
