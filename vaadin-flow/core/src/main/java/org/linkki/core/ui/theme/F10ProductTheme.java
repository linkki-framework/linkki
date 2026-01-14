/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.linkki.core.ui.theme;

/**
 * Theming for Faktor Zehn products, e.g. Faktor Zehn branding colors.
 * 
 * @since 2.8.0
 */
public class F10ProductTheme {

    /**
     * Used together with {@link com.vaadin.flow.component.dependency.StyleSheet} to import the f10
     * product theme.
     * 
     * @since 2.10.0
     */
    public static final String STYLESHEET = "themes/f10-product/styles.css";

    /**
     * The name of the f10-product theme. Should be used in*
     * {@link com.vaadin.flow.theme.Theme @Theme} annotation.
     *
     * @deprecated Use {@link com.vaadin.flow.component.dependency.StyleSheet} together with
     *             {@link F10ProductTheme#STYLESHEET} instead.
     */
    @Deprecated(since = "2.10.0")
    public static final String F10_PRODUCT_THEME = "f10-product";

    private F10ProductTheme() {
        // do not instantiate
    }
}
