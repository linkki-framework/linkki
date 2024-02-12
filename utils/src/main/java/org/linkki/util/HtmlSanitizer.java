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

package org.linkki.util;

import org.apache.commons.lang3.StringUtils;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Utility class for sanitizing HTML text.
 *
 * @deprecated Use {@code org.linkki.core.util.HtmlSanitizer} instead
 */
@Deprecated(since = "2.3.1")
public class HtmlSanitizer {

    private static final String ALLOWED_TAGS = "(p|div|span|br|b|strong|i|em|u)";

    private HtmlSanitizer() {
        // prevent instantiation
    }

    /**
     * Escapes all {@literal <} and {@literal >} symbols that do not belong to an allowed HTML tag.
     * Allowed tags are p, div, span, br, b, strong, i, em and u.
     * 
     * @param text the input text, may be {@code null}
     * @return the sanitized text, or {@code null} if the input is {@code null}
     * 
     * @deprecated Use {@code org.linkki.core.util.HtmlSanitizer} instead
     */
    @Deprecated(since = "2.3.1")
    public static @CheckForNull String sanitize(@CheckForNull String text) {
        if (StringUtils.isEmpty(text)) {
            return text;
        } else {
            return text.replaceAll("<(?!\\/?" + ALLOWED_TAGS + ">)", "&lt;")
                    .replaceAll("(?<!<\\/?" + ALLOWED_TAGS + ")>", "&gt;");
        }
    }

}
