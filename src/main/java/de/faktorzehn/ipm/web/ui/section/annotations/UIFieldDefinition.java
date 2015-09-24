package de.faktorzehn.ipm.web.ui.section.annotations;

/**
 * A common interface for all UI-Field annotations. As annotations can't implement an interface, the
 * {@link UIAnnotationReader} is used to get instances of this interface for the annotated methods
 * of a class.
 *
 * @see UIAnnotationReader
 */
public interface UIFieldDefinition extends UIElementDefinition {

    RequiredType required();

    AvailableValuesType availableValues();

    String modelAttribute();

}
