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

package de.faktorzehn.commons.linkki;

import org.linkki.framework.ui.LinkkiApplicationTheme;

public class F10Styles {

    public static final String LABEL_APPLICATION_ENVIRONMENT = "f10-application-environment";

    /**
     * @deprecated Use {@link LinkkiApplicationTheme#GRID_FOOTER_SUM}
     */
    @Deprecated(since = "1.5.0")
    public static final String TABLE_FOOTER_DECIMAL_SUM = LinkkiApplicationTheme.GRID_FOOTER_SUM;

    private F10Styles() {
        // do not instantiate
    }
}
