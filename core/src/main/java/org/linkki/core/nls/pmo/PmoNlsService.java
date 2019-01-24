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

import org.eclipse.jdt.annotation.Nullable;

/**
 * Service for Native Language Support for presentation model objects.
 * <p>
 * If you want to implement your own service, {@link PmoNlsService#get()} uses CDI to load the
 * implementation, so you might want to replace the {@link DefaultPmoNlsService}.
 */
public interface PmoNlsService {
    static final String CAPTION_KEY = "caption";

    /**
     * @param pmoClass required, PMO class
     * @param property PMO property. Can be null for some lableTypes like <code>SECTION_CAPTION</code>
     * @param fallbackValue required, value that returned if there is no corresponding resources
     * @return internationalized label for corresponding <code>property</code> of <code>pmo</code>
     *         class. As a locale use <code>UiUtil.getUiLocale()</code>
     * @throws NullPointerException if {@code pmoClass} is {@code null} or if no label was found and the
     *             {@code fallbackValue} is {@code null}
     */
    String getSectionCaption(Class<?> pmoClass, @Nullable String property, String fallbackValue);

    String getLabel(Class<?> pmoClass, String propertyName, String aspectName, String fallbackValue);

    /**
     * @return the {@link PmoNlsService} implementation for the current context.
     */
    public static PmoNlsService get() {
        return new DefaultPmoNlsService();
    }

    public static String getPropertyKey(Class<?> pmoClass, String propertyName, String aspectName) {
        return pmoClass.getSimpleName() + '_' + propertyName + '_' + aspectName;
    }

    public static String getSectionCaptionKey(Class<?> pmoClass) {
        return pmoClass.getSimpleName() + '_' + CAPTION_KEY;
    }
}
