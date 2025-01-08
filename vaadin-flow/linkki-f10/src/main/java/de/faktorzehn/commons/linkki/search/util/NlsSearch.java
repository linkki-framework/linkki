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

package de.faktorzehn.commons.linkki.search.util;

import java.text.MessageFormat;

import org.linkki.core.nls.NlsService;

public class NlsSearch {
    /**
     * This constant is used as placeholder for values retrieved from linkki-messages.properties
     * files.
     */
    public static final String I18N = "String should be in linkki-messages.properties files!"; // $NON-NLS-1$
                                                                                               // //
                                                                                               // $NON-NLS-2$

    private static final String BUNDLE_NAME = "de.faktorzehn.commons.linkki.search.pmo.nls.messages"; // $NON-NLS-1$

    private NlsSearch() {
        // do not instantiate
    }

    /**
     * Retrieve a String for the given key
     * 
     * @param key required, the key for the desired String
     * @return String for the given key
     * 
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public static String getString(String key) {
        return NlsService.get().getString(BUNDLE_NAME, key).orElseGet(() -> '!' + key + '!');
    }

    /**
     * Retrieve a pattern String for the given key and uses
     * {@link MessageFormat#format(String, Object...)} to format the given arguments.
     * 
     * @param key required, the key for the desired String
     * @param strings Strings to format
     * @return Formated string for the given key
     * 
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public static String format(String key, String... strings) {
        return MessageFormat.format(getString(key), (Object[])strings);
    }

}
