package org.linkki.core.ui.section.annotations;

/**
 * /** The element definition for the {@link UILabel} annotation. As annotations can't implement an
 * interface, the {@link UIAnnotationReader} is used to get instances of this interface for the
 * annotated methods of a class.
 *
 * @see UIAnnotationReader
 */
public interface UILabelDefinition extends UIElementDefinition {

    String modelAttribute();

}
