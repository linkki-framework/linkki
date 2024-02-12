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

package org.linkki.core.util;

import static org.apache.commons.lang3.StringUtils.containsNone;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jsoup.safety.Safelist;

import com.vaadin.flow.component.icon.Icon;

/**
 * A utility class for building sanitized HTML content. This class allows you to construct HTML
 * content with various tags and attributes while ensuring that the generated HTML is safe and free
 * from potential security vulnerabilities.
 */
public final class HtmlContentBuilder {

    private final StringBuilder contentBuilder = new StringBuilder();

    /**
     * Appends plain text to the HTML content being built. Any HTML content contained in the given
     * text is escaped.
     *
     * @param text the text to be appended
     * @return this {@code HtmlContentBuilder} instance for method chaining
     */
    public HtmlContentBuilder text(String text) {
        contentBuilder.append(HtmlSanitizer.escapeText(text));
        return this;
    }

    /**
     * Appends an HTML tag without any attributes or content. The tag is escaped.
     * 
     * @param tag the HTML tag to be appended, e.g. 'b', 'i', 'strong', 'em', etc.
     * @return this {@code HtmlContentBuilder} instance for method chaining
     * 
     */
    public HtmlContentBuilder tag(String tag) {
        return appendHtml(tag, Collections.emptyMap(), "");
    }

    /**
     * Appends an HTML tag with specified attributes and no content. The tag and its attributes are
     * sanitized to ensure they adhere to the HTML whitelist. The tag is escaped.
     *
     * @param tag the HTML tag to be appended, e.g. 'b', 'i', 'strong', 'em', etc.
     * @param attributes a map of tag attributes, e.g. Map.of("style","textcolor:red;")
     * @return this {@code HtmlContentBuilder} instance for method chaining
     */
    public HtmlContentBuilder tag(String tag, Map<String, String> attributes) {
        return tag(tag, attributes, "");
    }

    /**
     * Appends an HTML tag with specified content. The tag, its attributes, and the content are
     * sanitized to ensure they adhere to the HTML whitelist. The tag is escaped.
     *
     * @param tag the HTML tag to be appended, e.g., 'b', 'i', 'strong', 'em', etc.
     * @param content the content to be included within the tag
     * @return this {@code HtmlContentBuilder} instance for method chaining
     */
    public HtmlContentBuilder tag(String tag, String content) {
        return tag(tag, Collections.emptyMap(), content);
    }

    /**
     * Appends an HTML tag with specified attributes and HTML content. The tag, its attributes, and
     * the content are sanitized to ensure they adhere to the HTML whitelist. The given tag is only
     * appended if it is contained in the {@link Safelist HTML whitelist}.
     *
     * @param tag the HTML tag to be appended, e.g. 'b', 'i', 'strong', 'em', etc.
     * @param attributes a map of tag attributes, e.g. Map.of("style","textcolor:red;")
     * @param content the HTML content to be included within the tag
     * @return this {@code HtmlContentBuilder} instance for method chaining
     */
    public HtmlContentBuilder tag(String tag, Map<String, String> attributes, HtmlContent content) {
        return appendHtml(tag, attributes, content);
    }

    /**
     * Appends an HTML tag with specified attributes and plain text content. The tag, its
     * attributes, and the content are sanitized to ensure they adhere to the HTML whitelist. The
     * given tag is only appended if it is contained in the {@link Safelist HTML whitelist}.
     *
     * @param tag the HTML tag to be appended, e.g. 'b', 'i', 'strong', 'em', etc.
     * @param attributes a map of tag attributes, e.g. Map.of("style","textcolor:red;")
     * @param content the plain text content to be included within the tag
     * @return this {@code HtmlContentBuilder} instance for method chaining
     */
    public HtmlContentBuilder tag(String tag, Map<String, String> attributes, String content) {
        return appendHtml(tag, attributes, content);
    }

    /**
     * Appends an HTML tag with specified style and content. The tag, its style, and the content are
     * sanitized to ensure they adhere to the HTML whitelist.
     *
     * @param tag the HTML tag to be appended, e.g. 'b', 'i', 'strong', 'em', etc.
     * @param style the CSS style to be applied to the tag, e.g. "color: red; font-size: 14px;"
     * @param content the HTML content to be included within the tag
     * @return this {@code HtmlContentBuilder} instance for method chaining
     */
    public HtmlContentBuilder styledTag(String tag, String style, String content) {
        return tag(tag, Map.of("style", style), content);
    }

    /**
     * Appends a &lt;br&gt; tag to the HTML content being built.
     *
     * @return this {@code HtmlContentBuilder} instance for method chaining
     */
    public HtmlContentBuilder br() {
        return tag("br");
    }

    /**
     * Appends an HTML icon element represented by a Vaadin {@code Icon} component to the HTML
     * content being built.
     *
     * @param icon the Vaadin {@code Icon} component representing the icon
     * @return this {@code HtmlContentBuilder} instance for method chaining
     */
    public HtmlContentBuilder icon(Icon icon) {
        return appendHtml(icon.getElement().getOuterHTML());
    }

    /**
     * @return a {@code HtmlContent} object representing the sanitized HTML content
     */
    public HtmlContent build() {
        var sanitizedContent = HtmlSanitizer.sanitizeText(contentBuilder.toString());
        return new HtmlContent(sanitizedContent);
    }

    /**
     * Appends potentially harmful text content to the HTML content being built. The text is
     * sanitized to ensure it does not contain any harmful HTML or JavaScript code.
     *
     * @param html the HTML text content to be appended
     * @return this {@code HtmlContentBuilder} instance for method chaining
     */
    HtmlContentBuilder appendHtml(String html) {
        contentBuilder.append(html);
        return this;
    }

    private HtmlContentBuilder appendHtml(String tag, Map<String, String> attributes, Object content) {
        var html = buildHtml(tag, attributes, getHtmlString(content));
        contentBuilder.append(html);
        return this;
    }

    private String getHtmlString(Object content) {
        if (content instanceof String string) {
            return HtmlSanitizer.escapeText(string);
        } else {
            return content.toString();
        }
    }

    private String buildHtml(String tag, Map<String, String> attributes, String innerHtml) {
        var escapedTag = HtmlSanitizer.escapeText(tag);
        var builder = new StringBuilder();

        builder.append("<");
        builder.append(escapedTag);
        attributes.entrySet()
                .stream()
                .map(entry -> " %s=\"%s\"".formatted(entry.getKey(), entry.getValue()))
                .filter(attribute -> containsNone(attribute, '<', '>'))
                .forEach(builder::append);

        builder.append(">");
        if (allowsContent(escapedTag)) {
            builder.append(innerHtml);
            builder.append("</");
            builder.append(escapedTag);
            builder.append(">");
        }
        return builder.toString();
    }

    private boolean allowsContent(String tag) {
        return !List.of("img", "br").contains(tag);
    }
}
