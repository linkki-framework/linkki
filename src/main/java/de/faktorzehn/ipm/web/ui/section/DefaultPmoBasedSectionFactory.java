/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.section;

import javax.inject.Inject;

import de.faktorzehn.ipm.web.binding.dispatcher.PropertyBehaviorProvider;

/**
 * This is the default implementation of {@link PmoBasedSectionFactory}. If you do not need any
 * specialization of {@link PmoBasedSectionFactory} you are perfectly right to use this one.
 * Example:
 * 
 * <pre>
 * &#64;Inject
 * private DefaultPmoBasedSectionFactory sectionFactory;
 * 
 * <pre>
 *
 * @author dirmeier
 */
public class DefaultPmoBasedSectionFactory extends PmoBasedSectionFactory {

    @Inject
    public DefaultPmoBasedSectionFactory(PropertyBehaviorProvider propertyBehaviorProvider) {
        super(propertyBehaviorProvider);
    }

}
