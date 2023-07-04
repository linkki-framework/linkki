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

import java.util.Arrays;

import org.linkki.core.ui.element.annotation.UILabel;

import com.vaadin.flow.component.icon.Icon;

/**
 * This class is used to create HTML content that is safe to display. Methods annotated with
 * {@link UILabel} can return an instance of this class to display HTML content. Instances of this class
 * can be created with an {@link HtmlContentBuilder} using the {@link #builder()} method.
 * 
 * @since 2.5.0
 */
public final class HtmlContent {

    private final String content;

    /**
     * @implSpec package-private constructor to prevent outside instantiation
     */
    HtmlContent(String content) {
        this.content = content;
    }

    /**
     * @return a new instance of {@link HtmlContentBuilder}
     */
    public static HtmlContentBuilder builder() {
        return new HtmlContentBuilder();
    }

    /**
     * Sanitizes the given HTML text and removes potentially dangerous tags and attributes. Application
     * supplied strings should be sanitized before they are displayed in the UI. <b>This method should
     * not be used to display content which was entered by the user.</b>
     * <p>
     * Example: sanitizeText("{@literal <b>Hasso</b>}") will be displayed as "<b>Hasso</b>" in bold
     * letters, since {@literal <b>} is a whitelisted tag.
     */
    public static HtmlContent sanitizeText(String htmlString) {
        return builder().appendHtml(htmlString).build();
    }

    /**
     * Creates an HTML content element with a specified HTML tag, inline CSS style, and content.
     *
     * @param tag the HTML tag for the element, e.g., "div", "span", "p", etc.
     * @param style the inline CSS style to apply to the element, e.g., "color: red; font-size: 14px;"
     * @param content the content to be enclosed within the HTML element
     * @return an instance of HtmlContent representing the HTML element with the specified tag, style,
     *         and content
     */
    public static HtmlContent styledTag(String tag, String style, String content) {
        return builder().styledTag(tag, style, content).build();
    }

    /**
     * Creates an HTML content element with an {@link Icon}
     *
     * @param icon the {@link Icon} to be displayed within the HTML element
     * 
     * @return an instance of {@link HtmlContent} representing the HTML element with the specified icon
     */
    public static HtmlContent icon(Icon icon) {
        return builder().icon(icon).build();
    }

    /**
     * Escapes the given HTML text, meaning the input of this method will not be interpreted as HTML.
     * <p>
     * Use this method to display user-content which was entered via the UI. Example:
     * text("{@literal <b>Hasso</b>}") will be displayed as {@literal <b>Hasso</b>}.
     */
    public static HtmlContent text(String text) {
        return builder().text(text).build();
    }

    /**
     * Creates an HTML content element for displaying multiple lines of text within a {@literal <div>}
     * container.
     *
     * This method takes a variable number of strings as input and generates an HTML {@literal <div>}
     * element containing the provided lines of text. Each input string represents a line of text, and
     * the method will insert a line break ({@literal <br>
     * }) between each line to format them properly within the {@literal <div>} container.
     *
     * @param lines the lines of text to be displayed within the {@literal <div>} element
     * @return an {@link HtmlContent} object representing the HTML {@literal <div>} element with the
     *         specified text lines
     */
    public static HtmlContent multilineText(String... lines) {
        var builder = builder();
        Arrays.stream(lines).forEach(s -> {
            builder.text(s);
            builder.br();
        });
        return builder.build();
    }

    /**
     * Returns the HTML content as a string. This is done in #toString() because the default
     * {@link org.linkki.core.defaults.ui.element.ItemCaptionProvider} will pick this method to display
     * the content.
     */
    @Override
    public String toString() {
        return content;
    }

}
