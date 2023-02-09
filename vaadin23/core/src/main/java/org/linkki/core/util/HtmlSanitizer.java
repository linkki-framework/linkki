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

import com.vaadin.flow.component.icon.Icon;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Utility class for sanitizing HTML text.
 */
public class HtmlSanitizer {

    private HtmlSanitizer() {
        // prevent instantiation
    }

    /**
     * Sanitizes the given HTML text and removes potentially dangerous tags and attributes.
     * <p>
     * Allowed tags and attributes are defined by {@link Safelist#basicWithImages()}, which includes
     * basic styling tags as well as 'img'.
     * <p>
     * Additionally, the 'style' attribute is whitelisted for the tags 'b', 'i', 'strong', 'em', 'u'.
     * The attributes 'style', 'class' and 'id' are whitelisted for 'div'. 'img' can be used with a
     * relative or absolute image source.
     * <p>
     * The 'vaadin-icon' tag is also whitelisted together with the attributes 'id', 'class', 'width',
     * 'height', 'style' and 'icon'.
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
     * Escapes HTML characters ({@literal & < > " '}) in the given text, in order to make the text safe
     * for inclusion in HTML content.
     *
     * @param text the text to be escaped, may be {@code null}
     * @return the escaped content, or {@code null} if the input is {@code null}
     */
    public static @CheckForNull String escapeText(@CheckForNull String text) {
        if (StringUtils.isEmpty(text)) {
            return text;
        } else {
            // '&' has to be escaped first to avoid escaping the replacements which starts with '&'
            return text.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
        }
    }

    /**
     * Creates a {@link Safelist whitelist} which defines allowed HTML tags and attributes.
     */
    private static Safelist createHtmlWhitelist() {
        var whitelist = Safelist.basicWithImages();

        // whitelist additional attributes in order to style tags
        var styleAttribute = "style";
        var classAttribute = "class";
        var idAttribute = "id";
        whitelist.addAttributes("div", styleAttribute, classAttribute, idAttribute);
        whitelist.addAttributes("b", styleAttribute);
        whitelist.addAttributes("em", styleAttribute);
        whitelist.addAttributes("i", styleAttribute);
        whitelist.addAttributes("strong", styleAttribute);
        whitelist.addAttributes("u", styleAttribute);

        // whitelist Vaadin icons with common attributes
        var vaadinIconTag = new Icon().getElement().getTag();
        whitelist.addTags(vaadinIconTag);
        whitelist.addAttributes(vaadinIconTag,
                                styleAttribute, classAttribute, idAttribute, "width", "height", "icon");

        // remove all whitelisted protocols, so no protocol restrictions apply
        // required to allow relative URLs on images
        whitelist.removeProtocols("img", "src", "http", "https");

        return whitelist;
    }
}
