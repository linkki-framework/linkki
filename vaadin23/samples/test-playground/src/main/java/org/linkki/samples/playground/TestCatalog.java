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
     * ID of the testcase
     */
    public static final String ID_KEY = "id";
    /**
     * Short description of the testcase
     */
    public static final String DESCRIPTION_KEY = "description";
    /**
     * List of items that are covered by the testcase separated by a newline
     */
    public static final String ITEMS_KEY = "items";

    private static final String BUNDLE_NAME = "org/linkki/samples/playground/testcatalog"; // $NON-NLS-1$

    private TestCatalog() {
        // do not instantiate
    }

    public static String getScenarioTitle(String scenarioId) {
        return getString(scenarioId);
    }

    public static String getCaseTitle(String testId) {
        return getString(testId);
    }

    public static String getCaseTitle(String scenarioId, String caseId) {
        return getCaseTitle(scenarioId + "." + caseId);
    }

    public static String getCaseDescription(String testId) {
        return getString(testId + "." + DESCRIPTION_KEY);
    }

    public static String getCaseItems(String testId) {
        return getString(testId + "." + ITEMS_KEY);
    }

    private static String getString(String key) {
        return NlsService.get().getString(BUNDLE_NAME, key).orElseGet(() -> '!' + key + '!');
    }

}
