/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.linkki.core.nls.pmo;

import static java.util.Objects.requireNonNull;

import org.eclipse.jdt.annotation.Nullable;
import org.linkki.core.nls.NlsService;
import org.linkki.core.ui.util.UiUtil;

/**
 * PMO NLS service using a {@link PmoBundleNameGenerator} to derive bundle names with which a
 * {@link NlsService} is queried for localized texts.
 */
public class DefaultPmoNlsService implements PmoNlsService {

    /**
     * default bundle name for PMO. We search properties file with such name in package with PMO class
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
     * Creates a {@link DefaultPmoNlsService} using the given {@link PmoBundleNameGenerator} to derive
     * the bundle names from pmo classes.
     */
    public DefaultPmoNlsService(PmoBundleNameGenerator bundleNameGenerator) {
        this.bundleNameGenerator = requireNonNull(bundleNameGenerator, "bundleNameGenerator must not be null");
    }

    @Override
    public String getSectionCaption(Class<?> pmoClass, @Nullable String property, String fallbackValue) {
        return getLabel(pmoClass, PmoNlsService.getSectionCaptionKey(pmoClass), fallbackValue);
    }

    @Override
    public String getLabel(Class<?> pmoClass, String propertyName, String aspectName, String fallbackValue) {
        return getLabel(pmoClass, PmoNlsService.getPropertyKey(pmoClass, propertyName, aspectName), fallbackValue);
    }

    private String getLabel(Class<?> pmoClass, String key, String fallbackValue) {
        return NlsService.get()
                .getString(bundleNameGenerator.getBundleName(requireNonNull(pmoClass, "pmoClass must not be null")),
                           key,
                           requireNonNull(fallbackValue, "fallbackValue must not be null"),
                           UiUtil.getUiLocale());
    }

    private static String getBundleName(Class<?> pmoClass) {
        return pmoClass.getPackage().getName() + '.' + DEFAULT_PMO_BUNDLE_NAME;
    }


}
