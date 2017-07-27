package org.linkki.core.ui.section.annotations;

/**
 * A common interface for all UI-Field annotations. As annotations can't implement an interface, the
 * {@link UIAnnotationReader} is used to get instances of this interface for the annotated methods
 * of a class.
 *
 * @see UIAnnotationReader
 */
public interface UIFieldDefinition extends UIElementDefinition {

    /**
     * Name of the model object that is to be bound if multiple model objects are included for model
     * binding
     */
    String modelObject();

    /**
     * The name of a property in the class of the bound {@link ModelObject} to use model binding
     */
    String modelAttribute();

}
