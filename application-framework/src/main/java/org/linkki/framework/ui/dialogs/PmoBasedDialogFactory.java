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

import org.linkki.core.PresentationModelObject;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.ui.section.AbstractSection;
import org.linkki.core.ui.section.DefaultPmoBasedSectionFactory;
import org.linkki.core.ui.section.PmoBasedSectionFactory;
import org.linkki.core.ui.table.ContainerPmo;
import org.linkki.framework.ui.dialogs.OkCancelDialog.ButtonOption;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.UI;

/**
 * A factory to create dialogs with a content based on a {@link PresentationModelObject}. At the
 * moment only the {@link OkCancelDialog} is supported.
 */
public class PmoBasedDialogFactory {

    private final PmoBasedSectionFactory pmoBasedSectionFactory;
    private final ValidationService validationService;
    private final PropertyBehaviorProvider propertyBehaviorProvider;

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
     * @param validationService A service validating the data in the dialog.
     */
    public PmoBasedDialogFactory(ValidationService validationService) {
        this(validationService, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
    }

    /**
     * Creates a new dialog factory.
     * 
     * @param validationService A service validating the data in the dialog.
     * @param propertyBehaviorProvider A provider providing special property behavior like read-only
     *            modus if an objects can't be edited.
     */
    public PmoBasedDialogFactory(ValidationService validationService,
            PropertyBehaviorProvider propertyBehaviorProvider) {
        this.validationService = requireNonNull(validationService, "validationService must not be null");
        this.propertyBehaviorProvider = requireNonNull(propertyBehaviorProvider,
                                                       "propertyBehaviorProvider must not be null");
        this.pmoBasedSectionFactory = new DefaultPmoBasedSectionFactory();
    }

    /**
     * Creates a new {@link OkCancelDialog}.
     * 
     * @deprecated use {@link #newOkCancelDialog(String, Handler, Object...)} instead.
     * 
     * @param title The dialog title.
     * @param pmo The presentation model object providing the data and the layout information.
     * @param okHandler The called when OK is clicked.
     * @return A dialog with the content defined by the given PMO.
     */
    @Deprecated
    public OkCancelDialog newOkCancelDialog(String title, Object pmo, Handler okHandler) {
        return newOkCancelDialog(title, okHandler, pmo);
    }

    /**
     * Creates a new {@link OkCancelDialog}.
     * 
     * @param title The dialog title.
     * @param okHandler The called when OK is clicked.
     * @param pmos The presentation model objects providing the data and the layout information.
     * @return A dialog with the content defined by the given PMO.
     */
    public OkCancelDialog newOkCancelDialog(String title, Handler okHandler, Object... pmos) {
        OkCancelDialog dialog = new OkCancelDialog(title, okHandler, ButtonOption.OK_CANCEL);
        DialogBindingManager bindingManager = new DialogBindingManager(dialog, validationService,
                propertyBehaviorProvider);

        BindingContext bindingContext = bindingManager.startNewContext(dialog.getClass());
        for (Object pmo : pmos) {
            AbstractSection content;
            if (pmo instanceof ContainerPmo) {
                content = pmoBasedSectionFactory.createTableSection((ContainerPmo<?>)pmo, bindingContext);
            } else {
                content = pmoBasedSectionFactory.createSection(pmo, bindingContext);
            }
            float expRatio = 0f;
            if (pmo == pmos[pmos.length - 1]) {
                expRatio = 1f;
            }
            dialog.addContent(content, expRatio);
        }
        bindingContext.updateUI();

        return dialog;
    }

    /**
     * Creates a new {@link OkCancelDialog} and opens it.
     * 
     * @param title The dialog title.
     * @param pmo The presentation model object providing the data and the layout information.
     * @param okHandler The called when OK is clicked.
     * @return A dialog with the content defined by the given PMO.
     */
    public OkCancelDialog openOkCancelDialog(String title, Object pmo, Handler okHandler) {
        return open(newOkCancelDialog(title, okHandler, pmo));
    }

    /**
     * Creates a new {@link OkCancelDialog} and opens it.
     * 
     * Usage: <code>
     * PmoBasedDialogFactory.open(factory.newOkCancelDialog(...));
     * </code>
     * 
     * @param dialog the dialog that should be opened
     * @return A dialog with the content defined by the given PMO.
     */
    public static OkCancelDialog open(OkCancelDialog dialog) {
        UI.getCurrent().addWindow(dialog);
        return dialog;
    }

}
