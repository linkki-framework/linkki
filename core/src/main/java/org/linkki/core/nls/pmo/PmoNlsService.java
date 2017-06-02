/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.nls.pmo;

import javax.annotation.Nullable;

import org.linkki.util.cdi.BeanInstantiator;

/**
 * Service for Native Language Support for presentation model objects.
 * <p>
 * If you want to implement your own service, {@link PmoNlsService#get()} uses CDI to load the
 * implementation, so you might want to replace the {@link DefaultPmoNlsService}.
 */
public interface PmoNlsService {

    /**
     * @param pmoClass required, PMO class
     * @param property PMO property. Can be null for some lableTypes like
     *            <code>SECTION_CAPTION</code>
     * @param fallbackValue required, value that returned if there is no corresponding resources
     * @return internationalized label for corresponding <code>property</code> of <code>pmo</code>
     *         class. As a locale use <code>UiUtil.getUiLocale()</code>
     * @throws NullPointerException if {@code pmoClass} is {@code null} or the {@link PmoLabelType}
     *             requires a {@code property} and that is {@code null} or if no label was found and
     *             the {@code fallbackValue} is {@code null}
     */
    String getLabel(PmoLabelType labelType, Class<?> pmoClass, @Nullable String property, String fallbackValue);

    /**
     * @return the {@link PmoNlsService} implementation for the current context.
     */
    public static PmoNlsService get() {
        return BeanInstantiator.getCDIInstance(PmoNlsService.class, DefaultPmoNlsService::new);
    }
}
