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
package org.linkki.samples.playground;

import org.linkki.core.nls.NlsService;

/**
 * Service that loads informations from a test catalog.
 * <p>
 * This service reuses the {@link NlsService} to load the information from a structured properties file.
 */
public class TestCatalog {

    /**
     * ID of the test
     */
    public static final String ID_KEY = "id";
    /**
     * Title of the test
     */
    public static final String TITLE_KEY = "title";
    /**
     * Short description of the test
     */
    public static final String DESCRIPTION_KEY = "description";
    /**
     * List of aspects that are covered by the test separated by a newline
     */
    public static final String ASPECTS_KEY = "aspects";

    private static final String BUNDLE_NAME = "org/linkki/samples/playground/testcatalogue"; //$NON-NLS-1$

    private TestCatalog() {
        // do not instantiate
    }

    public static String getId(String testId) {
        return getString(testId + "." + ID_KEY);
    }

    public static String getTitle(String testId) {
        return getString(testId + "." + TITLE_KEY);
    }

    public static String getDescription(String testId) {
        return getString(testId + "." + DESCRIPTION_KEY);
    }

    public static String getAspects(String testId) {
        return getString(testId + "." + ASPECTS_KEY);
    }

    private static String getString(String key) {
        return NlsService.get().getString(BUNDLE_NAME, key).orElseGet(() -> '!' + key + '!');
    }

}
