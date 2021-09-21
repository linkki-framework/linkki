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
import org.linkki.framework.ui.LinkkiApplicationTheme;
import org.linkki.framework.ui.component.MessageUiComponents;
import org.linkki.framework.ui.nls.NlsText;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;

import edu.umd.cs.findbugs.annotations.CheckForNull;

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
@CssImport(value = "./styles/ok-cancel-dialog.css", include = "@vaadin/vaadin-lumo-styles/all-imports")
@CssImport(value = "./styles/dialog-overlay.css", themeFor = "vaadin-dialog-overlay")
public class OkCancelDialog extends Composite<Dialog> implements HasSize, BeforeLeaveObserver {

    public static final String CLASS_NAME_CONTENT_AREA = "content-area";
    public static final String CLASS_NAME_DIALOG_LAYOUT = "linkki-dialog-layout";
    public static final String OK_BUTTON_ID = "okButton";
    public static final String CANCEL_BUTTON_ID = "cancelButton";

    private static final long serialVersionUID = 1L;

    /**
     * The overall layout of this dialog. This is the content of the dialog that contains all components
     * including the header.
     */
    private final VerticalLayout layout;

    /**
     * The area that contains any added content as well as the component displaying the validation
     * message.
     */
    private final VerticalLayout contentArea;

    /** The area that contains all buttons */
    private final HorizontalLayout buttonArea;

    /** The OK button that is displayed in the dialog */
    private final Button okButton;

    /** The handler that handles clicks on the OK button */
    private final Handler okHandler;

    /** The cancel button that is displayed in the dialog */
    @CheckForNull
    private final Button cancelButton;

    /** The handler that handles clicks on the Cancel button */
    private final Handler cancelHandler;

    /** Service to validate data in the dialog, called when the user clicks OK */
    private ValidationService validationService = ValidationService.NOP_VALIDATION_SERVICE;

    /** The state which validation messages are displayed. */
    private ValidationDisplayState validationDisplayState = ValidationDisplayState.HIDE_MANDATORY_FIELD_VALIDATIONS;

    /** Called when the dialog was validated, e.g. after the OK button was clicked */
    private Handler beforeOkHandler = Handler.NOP_HANDLER;

    /**
     * The message row that displays the first message from the message list if there is a message to
     * display
     */
    private Optional<Component> messageRow = Optional.empty();

    private boolean okPressed = false;
    private boolean cancelPressed = false;
    private boolean mayProceed = true;

    private H3 title;

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

        this.okHandler = requireNonNull(okHandler, "okHandler must not be null"); //$NON-NLS-1$
        this.cancelHandler = requireNonNull(cancelHandler, "cancelHandler must not be null"); //$NON-NLS-1$

        this.layout = new VerticalLayout();
        this.contentArea = new VerticalLayout();
        this.buttonArea = new HorizontalLayout();

        this.okButton = new Button(NlsText.getString("OkCancelDialog.OkButtonCaption")); //$NON-NLS-1$
        this.cancelButton = buttonOption == ButtonOption.OK_CANCEL
                ? new Button(NlsText.getString("OkCancelDialog.CancelButtonCaption")) //$NON-NLS-1$
                : null;

        getContent().setModal(true);
        getContent().setResizable(false);
        getContent().setDraggable(true);
        getContent().setMaxWidth("100%");
        getContent().setMaxHeight("100%");

        layout.setSizeFull();
        layout.setMargin(false);
        layout.setPadding(false);
        layout.addClassName(CLASS_NAME_DIALOG_LAYOUT);
        getContent().add(layout);

        title = new H3(caption);
        title.addClassName(LinkkiApplicationTheme.DIALOG_CAPTION);
        layout.setHorizontalComponentAlignment(Alignment.START, title);
        layout.add(title);

        contentArea.setPadding(false);
        contentArea.setSpacing(false);
        contentArea.setSizeFull();
        layout.add(contentArea);
        layout.setFlexGrow(1, contentArea);

        buttonArea.setPadding(false);
        buttonArea.setSpacing(true);
        buttonArea.addClassName(LinkkiApplicationTheme.DIALOG_BUTTON_BAR);
        buttonArea.setAlignItems(Alignment.CENTER);
        buttonArea.setJustifyContentMode(JustifyContentMode.END);
        buttonArea.getElement().getStyle().set("flexWrap", "wrap");
        layout.add(buttonArea);
        layout.setHorizontalComponentAlignment(Alignment.END, buttonArea);
        layout.setFlexGrow(0, buttonArea);

        initContentArea(contentComponents);
        initButtons();
        initCloseListener();

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

    private void initContentArea(Component... contentComponents) {
        contentArea.addClassName(CLASS_NAME_CONTENT_AREA); // $NON-NLS-1$
        contentArea.setWidthFull();
        for (Component contentComponent : contentComponents) {
            contentArea.add(contentComponent);
        }
    }

    private void initButtons() {

        buttonArea.add(okButton);

        okButton.addClickShortcut(Key.ENTER);
        okButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        okButton.setId(OK_BUTTON_ID);
        okButton.addClickListener(e -> {
            setOkPressed();
            beforeOkHandler.andThen(() -> {
                if (mayProceed) {
                    ok();
                    getContent().close();
                }
            }).apply();

        });

        if (cancelButton != null) {
            buttonArea.add(cancelButton);
            cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
            cancelButton.setId(CANCEL_BUTTON_ID);
            cancelButton.addClickListener(e -> {
                setCancelPressed(true);
                close();
            });
        }

    }

    private void initCloseListener() {
        getContent().addDialogCloseActionListener(e -> close());
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
     * If you need a dialog with dynamic height and only want to set the width, use
     * {@link #setWidth(String)} instead.
     * 
     * @see FlexComponent#setFlexGrow(double, com.vaadin.flow.component.HasElement...)
     * 
     * @param width the width of the dialog including the unit (for example 700px or 65%)
     * @param height the height of the dialog including the unit (for example 700px or 65%)
     */
    public void setSize(String width, String height) {
        if (width != null) {
            getContent().setWidth(width);
        }
        if (height != null) {
            getContent().setHeight(height);
        }
        layout.setSizeFull();
        contentArea.setSizeFull();
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
        messageRow.ifPresent(buttonArea::remove);
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
        messageLabel.getElement().getStyle().set("width", "unset");
        buttonArea.addComponentAtIndex(0, messageLabel);
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
     * Adds the given component to be displayed in the dialog. If you have fixed the height of the
     * dialog using {@link #setSize(String, String)} you might want to use
     * {@link #addContent(Component, float)} to assign specific expand ratios to components.
     */
    public void addContent(Component c) {
        contentArea.add(c);
    }

    /**
     * Adds the given component to be displayed in the dialog with the given expand ratio. For more
     * explanation about expand ratio please read
     * {@link FlexComponent#setFlexGrow(double, com.vaadin.flow.component.HasElement...)}
     * 
     * @see FlexComponent#setFlexGrow(double, com.vaadin.flow.component.HasElement...)
     * 
     */
    public void addContent(Component c, float expandRatio) {
        contentArea.add(c);
        contentArea.setFlexGrow(expandRatio, c);
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
     * Returns the caption of the dialog.
     */
    public String getCaption() {
        return title.getText();
    }

    /**
     * Returns the caption of the OK button.
     */
    public String getOkCaption() {
        return okButton.getText();
    }

    /**
     * Sets caption of the OK button. The text is used directly, no further localization will take
     * place.
     */
    public void setOkCaption(String okCaption) {
        okButton.setText(requireNonNull(okCaption, "okCaption must not be null"));
    }

    /**
     * Returns the caption of the cancel button.
     * 
     * @throws IllegalStateException if the dialog does not have a cancel button
     */
    public String getCancelCaption() {
        if (cancelButton != null) {
            return cancelButton.getText();
        } else {
            throw new IllegalStateException("Dialog does not have a cancel button");
        }
    }

    /**
     * Sets caption of the cancel button. The text is used directly, no further localization will take
     * place.
     * 
     * @throws IllegalStateException if the dialog does not have a cancel button
     */
    public void setCancelCaption(String cancelCaption) {
        if (cancelButton != null) {
            cancelButton.setText(requireNonNull(cancelCaption, "cancelCaption must not be null"));
        } else {
            throw new IllegalStateException("Dialog does not have a cancel button");
        }
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

    public OkCancelDialog open() {
        getContent().open();
        return this;
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        // cancelHandler may not be called because it causes a stack overflow if it navigates to
        // another page
        getContent().close();
    }

    public void close() {
        cancelHandler.apply();
        getContent().close();
    }

    /**
     * Returns the layout containing the main content of the dialog.
     */
    protected VerticalLayout getContentArea() {
        return contentArea;
    }

    /**
     * Returns the layout containing the buttons.
     */
    protected HorizontalLayout getButtonArea() {
        return buttonArea;
    }

    /**
     * Options to choose the buttons.
     */
    public enum ButtonOption {
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
        @CheckForNull
        private String okCaption;
        @CheckForNull
        private String cancelCaption;

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
         * Sets caption of the OK button. The text is used directly, no further localization will take
         * place.
         * 
         * @param okCaption the localized caption to use for the OK button
         * @return this builder instance to directly add further properties
         * 
         * @see OkCancelDialog#setOkCaption(String)
         */
        public Builder okCaption(String okCaption) {
            this.okCaption = requireNonNull(okCaption, "okCaption must not be null");
            return this;
        }

        /**
         * Sets caption of the cancel button. The text is used directly, no further localization will
         * take place. This setting only takes effect if the builder is configured to use a cancel
         * button when calling {@link #build()}.
         * 
         * @param cancelCaption the localized caption to use for the cancel button
         * @return this builder instance to directly add further properties
         * 
         * @see OkCancelDialog#setCancelCaption(String)
         */
        public Builder cancelCaption(String cancelCaption) {
            this.cancelCaption = requireNonNull(cancelCaption, "cancelCaption must not be null");
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

            if (okCaption != null) {
                createdDialog.setOkCaption(okCaption);
            }
            if (cancelCaption != null && buttonOption == ButtonOption.OK_CANCEL) {
                createdDialog.setCancelCaption(cancelCaption);
            }

            return createdDialog;
        }
    }
}
