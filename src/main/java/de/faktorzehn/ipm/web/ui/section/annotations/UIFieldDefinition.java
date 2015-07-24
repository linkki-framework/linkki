package de.faktorzehn.ipm.web.ui.section.annotations;

import com.vaadin.ui.Component;

/**
 * A common interface for all UI-Field annotations. As annotations can't implement an interface, the
 * <tt>UIFieldExtractor</tt> is used to get an instance of this interface for a given annotation
 * instance.
 *
 * @see UIAnnotationReader
 */
public interface UIFieldDefinition {

    Component newComponent();

    int position();

    String label();

    boolean noLabel();

    EnabledType enabled();

    RequiredType required();

    VisibleType visible();

    AvailableValuesType availableValues();

    String modelAttribute();

}
