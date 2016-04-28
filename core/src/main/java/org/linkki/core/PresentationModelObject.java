package org.linkki.core;

import java.util.Optional;

/**
 * Common interface for presentation model objects.
 */
public interface PresentationModelObject {

    /**
     * Returns the {@code ButtonPmo} for the button that edits the PMO if the PMO allows editing.
     * The default implementation returns {@code Optional.empty()} indicating that the PMO does not
     * allow editing.
     * 
     * @return the {@code ButtonPmo} for the button that edits the PMO if the PMO allows editing
     */
    default Optional<ButtonPmo> getEditButtonPmo() {
        return Optional.empty();
    }

}
