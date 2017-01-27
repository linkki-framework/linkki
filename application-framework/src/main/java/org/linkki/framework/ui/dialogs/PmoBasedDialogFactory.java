/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.dialogs;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnull;

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
    public PmoBasedDialogFactory(@Nonnull ValidationService validationService) {
        this(validationService, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
    }

    /**
     * Creates a new dialog factory.
     * 
     * @param validationService A service validating the data in the dialog.
     * @param propertyBehaviorProvider A provider providing special property behavior like read-only
     *            modus if an objects can't be edited.
     */
    public PmoBasedDialogFactory(@Nonnull ValidationService validationService,
            @Nonnull PropertyBehaviorProvider propertyBehaviorProvider) {
        this.pmoBasedSectionFactory = new DefaultPmoBasedSectionFactory();
        this.validationService = requireNonNull(validationService);
        this.propertyBehaviorProvider = requireNonNull(propertyBehaviorProvider);
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
    @Nonnull
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
    @Nonnull
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
            dialog.addContent(content);
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
    @Nonnull
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
    @Nonnull
    public static OkCancelDialog open(OkCancelDialog dialog) {
        UI.getCurrent().addWindow(dialog);
        return dialog;
    }

}
