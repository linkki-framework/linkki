package de.faktorzehn.ipm.web;


/**
 * Action that is executed when the user clicks on the edit button in a section.
 */
@FunctionalInterface
public interface EditAction {

    /**
     * Executes the action.
     */
    public void exec();

}
