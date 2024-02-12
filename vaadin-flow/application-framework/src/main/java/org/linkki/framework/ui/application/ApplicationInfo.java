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

package org.linkki.framework.ui.application;

import org.linkki.framework.ui.dialogs.ApplicationInfoDialog;

/**
 * Basic information about the application.
 */
public interface ApplicationInfo {
    /**
     * The application name, displayed for example in the {@link ApplicationFooter} and/or the
     * {@link ApplicationInfoDialog}.
     */
    String getApplicationName();

    /**
     * The application version, displayed for example in the {@link ApplicationFooter} and/or
     * {@link ApplicationInfoDialog}.
     */
    String getApplicationVersion();

    /**
     * The application description, displayed for example in the {@link ApplicationInfoDialog}.
     */
    String getApplicationDescription();

    /**
     * The copyright statement displayed for example in the {@link ApplicationFooter} and/or the
     * {@link ApplicationInfoDialog}.
     */
    String getCopyright();
}
