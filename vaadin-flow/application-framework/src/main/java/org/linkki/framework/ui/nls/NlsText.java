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
package org.linkki.framework.ui.nls;

import java.text.MessageFormat;

import org.linkki.core.nls.NlsService;

public class NlsText {

    /**
     * This constant is used as placeholder for values retrieved from linkki-messages.properties
     * files.
     */
    public static final String I18N = "String should be in linkki-messages.properties files!";

    private static final String BUNDLE_NAME = "org/linkki/framework/ui/nls/messages";

    private NlsText() {
        // do not instantiate
    }

    public static String getString(String key) {
        return NlsService.get().getString(BUNDLE_NAME, key).orElseGet(() -> '!' + key + '!');
    }

    public static String format(String key, Object... arguments) {
        return MessageFormat.format(getString(key), arguments);
    }

}
