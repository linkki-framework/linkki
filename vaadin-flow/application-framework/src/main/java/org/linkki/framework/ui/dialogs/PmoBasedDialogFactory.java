/*
 * Copyright Faktor Zehn GmbH.
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

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehavior;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.pmo.PresentationModelObject;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.framework.ui.dialogs.OkCancelDialog.ButtonOption;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;

/**
 * A factory to create dialogs with a content based on a {@link PresentationModelObject}. At the
 * moment only the {@link OkCancelDialog} is supported.
 */
public class PmoBasedDialogFactory {

    private final ValidationService validationService;
    private final PropertyBehaviorProvider propertyBehaviorProvider;
    private final PropertyDispatcherFactory propertyDispatcherFactory;

    /**
     * Creates a new dialog factory with no validations and special property behavior.
     * 
     * @see ValidationService#NOP_VALIDATION_SERVICE
     * @see PropertyBehaviorProvider#NO_BEHAVIOR_PROVIDER
     */
    public PmoBasedDialogFactory() {
        this(ValidationService.NOP_VALIDATION_SERVICE);
    }

    /**
     * Creates a new dialog factory with no special property behavior.
     * 
     * @param validationService a service validating the data in the dialog
     */
    public PmoBasedDialogFactory(ValidationService validationService) {
        this(validationService, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
    }

    /**
     * Creates a new dialog factory with no validation service.
     * 
     * @param propertyBehavior configures some {@link PropertyBehavior} like
     *            {@link PropertyBehavior#readOnly()} to change the over all behavior of the dialog
     * 
     * @see PropertyBehavior
     */
    public PmoBasedDialogFactory(PropertyBehavior... propertyBehavior) {
        this(ValidationService.NOP_VALIDATION_SERVICE, PropertyBehaviorProvider.with(propertyBehavior));
    }

    /**
     * Creates a new dialog factory with the default PropertyDispatcherFactory.
     *
     * @param validationService a service validating the data in the dialog
     * @param propertyBehaviorProvider a provider providing special property behavior like read-only
     *            modus if an object can't be edited
     */
    public PmoBasedDialogFactory(ValidationService validationService,
            PropertyBehaviorProvider propertyBehaviorProvider) {
        this(validationService, propertyBehaviorProvider, new PropertyDispatcherFactory());
    }

    /**
     * Creates a new dialog factory.
     * 
     * @param validationService a service validating the data in the dialog
     * @param propertyBehaviorProvider a provider providing special property behavior like read-only
     *            modus if an objects can't be edited
     * @param propertyDispatcherFactory a factory that handles the communication between properties
     *            and their behaviour
     */
    public PmoBasedDialogFactory(ValidationService validationService,
            PropertyBehaviorProvider propertyBehaviorProvider, PropertyDispatcherFactory propertyDispatcherFactory) {
        this.validationService = requireNonNull(validationService, "validationService must not be null");
        this.propertyBehaviorProvider = requireNonNull(propertyBehaviorProvider,
                                                       "propertyBehaviorProvider must not be null");
        this.propertyDispatcherFactory = requireNonNull(propertyDispatcherFactory,
                                                        "propertyDispatcherFactory must not be null");
    }

    /**
     * Creates a new dialog with only Ok button.
     *
     * @param title the dialog title
     * @param pmos the presentation model objects providing the data and the layout information
     * @return A dialog with the content defined by the given PMO.
     */
    public OkCancelDialog newOkDialog(String title, Object... pmos) {
        return newOkCancelDialog(title, Handler.NOP_HANDLER, Handler.NOP_HANDLER, ButtonOption.OK_ONLY, pmos);
    }

    /**
     * Creates a new dialog with ok and cancel button.
     * 
     * @param title the dialog title
     * @param okHandler the called when OK is clicked
     * @param pmos the presentation model objects providing the data and the layout information
     * @return A dialog with the content defined by the given PMO.
     */
    public OkCancelDialog newOkCancelDialog(String title, Handler okHandler, Object... pmos) {
        return newOkCancelDialog(title, okHandler, Handler.NOP_HANDLER, ButtonOption.OK_CANCEL, pmos);
    }

    /**
     * Creates a new dialog with ok and cancel button.
     * 
     * @param title the dialog title
     * @param okHandler the called when OK is clicked
     * @param cancelHandler The handler that handles clicks on the CANCEL button
     * @param pmos the presentation model objects providing the data and the layout information
     * @return A dialog with the content defined by the given PMO.
     */
    public OkCancelDialog newOkCancelDialog(String title, Handler okHandler, Handler cancelHandler, Object... pmos) {
        return newOkCancelDialog(title, okHandler, cancelHandler, ButtonOption.OK_CANCEL, pmos);
    }

    OkCancelDialog newOkCancelDialog(String title,
            Handler okHandler,
            Handler cancelHandler,
            ButtonOption buttonOption,
            Object... pmos) {

        OkCancelDialog dialog = OkCancelDialog.builder(title)
                .okHandler(okHandler)
                .cancelHandler(cancelHandler)
                .buttonOption(buttonOption)
                .build();

        DialogBindingManager bindingManager = new DialogBindingManager(dialog, validationService,
                propertyBehaviorProvider, propertyDispatcherFactory);

        BindingContext bindingContext = bindingManager.getContext(dialog.getClass());

        for (Object pmo : pmos) {
            Component content = VaadinUiCreator.createComponent(pmo, bindingContext);
            float expRatio = 0f;
            if (pmo == pmos[pmos.length - 1]) {
                expRatio = 1f;
            }
            dialog.addContent(content, expRatio);
        }
        bindingContext.modelChanged();

        return dialog;
    }

    /**
     * Creates a new dialog with only Ok button and opens it.
     *
     * @param title the dialog title
     * @param pmos the presentation model objects providing the data and the layout information
     * @return A dialog with the content defined by the given PMO.
     */
    public OkCancelDialog openOkDialog(String title, Object... pmos) {
        OkCancelDialog dialog = newOkDialog(title, pmos);
        dialog.open();
        return dialog;
    }

    /**
     * Creates a new {@link OkCancelDialog} based on multiple pmos with okHandler and opens the
     * dialog.
     * 
     * @param title the dialog title
     * @param okHandler the called when OK is clicked
     * @param pmos the presentation model objects providing the data and the layout information
     * @return A dialog with the content defined by the given PMO.
     */
    public OkCancelDialog openOkCancelDialog(String title, Handler okHandler, Object... pmos) {
        OkCancelDialog dialog = newOkCancelDialog(title, okHandler, pmos);
        dialog.open();
        return dialog;
    }

    /**
     * Creates a new {@link OkCancelDialog} based on multiple pmos with okHandler and cancelHandler
     * and opens the dialog.
     * 
     * @param title the dialog title
     * @param okHandler the called when OK is clicked
     * @param cancelHandler the called when Cancel is clicked
     * @param pmos the presentation model objects providing the data and the layout information
     * @return A dialog with the content defined by the given PMO .
     */
    public OkCancelDialog openOkCancelDialog(String title, Handler okHandler, Handler cancelHandler, Object... pmos) {
        OkCancelDialog dialog = newOkCancelDialog(title, okHandler, cancelHandler, pmos);
        dialog.open();
        return dialog;
    }

}
