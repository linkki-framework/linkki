/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.nls.pmo;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nullable;

import org.linkki.core.nls.NlsService;
import org.linkki.core.ui.util.UiUtil;

/**
 * PMO NLS service using a {@link PmoBundleNameGenerator} to derive bundle names with which a
 * {@link NlsService} is queried for localized texts.
 */
public class DefaultPmoNlsService implements PmoNlsService {

    /**
     * default bundle name for PMO. We search properties file with such name in package with PMO
     * class
     */
    private static final String DEFAULT_PMO_BUNDLE_NAME = "linkki-messages";

    private final PmoBundleNameGenerator bundleNameGenerator;

    /**
     * Creates a {@link DefaultPmoNlsService} using the "linkki-messages" appended to the given pmo
     * classes' package name as the bundle name.
     */
    public DefaultPmoNlsService() {
        this(DefaultPmoNlsService::getBundleName);
    }

    /**
     * Creates a {@link DefaultPmoNlsService} using the given {@link PmoBundleNameGenerator} to
     * derive the bundle names from pmo classes.
     */
    public DefaultPmoNlsService(PmoBundleNameGenerator bundleNameGenerator) {
        this.bundleNameGenerator = requireNonNull(bundleNameGenerator, "bundleNameGenerator must not be null");
    }

    @Override
    public String getLabel(PmoLabelType lableType, Class<?> pmoClass, @Nullable String property, String fallbackValue) {
        return NlsService.get()
                .getString(bundleNameGenerator.getBundleName(requireNonNull(pmoClass, "pmoClass must not be null")),
                           lableType.getKey(pmoClass, property),
                           requireNonNull(fallbackValue, "fallbackValue must not be null"),
                           UiUtil.getUiLocale());
    }

    private static String getBundleName(Class<?> pmoClass) {
        return pmoClass.getPackage().getName() + '.' + DEFAULT_PMO_BUNDLE_NAME;
    }


}
