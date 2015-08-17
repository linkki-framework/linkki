package de.faktorzehn.ipm.web;


import java.util.Optional;

import org.faktorips.runtime.IModelObject;

/**
 * Common interface for presentation model objects.
 */
public interface PresentationModelObject {

    /**
     * @return the domain model object this presentation model is responsible for. Preferably
     *         {@link IModelObject} instances, as they provide validation messages. Must not return
     *         <code>null</code>.
     */
    Object getModelObject();

    /**
     * Returns the {@code EditAction} that edits the PMO if the PMO allows editing. The default
     * implementation returns {@code Optional.empty()} indicating that the PMO does not allow
     * editing.
     * 
     * @return the {@code EditAction} that edits the PMO if the PMO allows editing
     */
    default Optional<EditAction> getEditAction() {
        return Optional.empty();
    }

}
