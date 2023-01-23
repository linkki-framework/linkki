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

package org.linkki.core.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Utility class for sanitizing HTML text.
 */
public class HtmlSanitizer {

    private HtmlSanitizer() {
        // prevent instantiation
    }

    /**
     * Escapes all {@literal <} and {@literal >} symbols that do not belong to an allowed HTML tag.
     * <p>
     * Allowed tags and attributes are defined by {@link Safelist#basicWithImages()}, which includes
     * basic styling tags as well as {@code img} Tags. Additionally, common attributes of styling tags
     * as 'style' or 'class' are whitelisted.
     * 
     * @param htmlText the HTML text to be sanitized, may be {@code null}
     * @return the sanitized content, or {@code null} if the input is {@code null}
     */
    public static @CheckForNull String sanitizeText(@CheckForNull String htmlText) {
        if (StringUtils.isEmpty(htmlText)) {
            return htmlText;
        } else {
            return Jsoup.clean(htmlText, createHtmlWhitelist());
        }
    }

    /**
     * Creates a {@link Safelist whitelist} which defines allowed HTML tags and attributes.
     */
    private static Safelist createHtmlWhitelist() {
        var whitelist = Safelist.basicWithImages();

        // whitelisting additional attributes in order to style tags
        var styleAttribute = "style";
        whitelist.addAttributes("div", styleAttribute, "class", "id");
        whitelist.addAttributes("b", styleAttribute);
        whitelist.addAttributes("em", styleAttribute);
        whitelist.addAttributes("i", styleAttribute);
        whitelist.addAttributes("strong", styleAttribute);
        whitelist.addAttributes("u", styleAttribute);

        // required for using relative URLs on images
        whitelist.removeProtocols("img", "src", "http", "https");

        return whitelist;
    }
}
