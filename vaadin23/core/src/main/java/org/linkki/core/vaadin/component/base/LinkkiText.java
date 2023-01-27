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

package org.linkki.core.vaadin.component.base;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import org.linkki.core.ui.aspects.types.IconPosition;
import org.linkki.core.util.HtmlSanitizer;
import org.linkki.core.vaadin.component.HasIcon;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.HasPrefixAndSuffix;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * A text component that can have an additional {@link VaadinIcon} and a label. It also supports a HTML
 * mode.
 */
@Tag("linkki-text")
@JsModule("./src/linkki-text.ts")
public class LinkkiText extends Component implements HasIcon, HasPrefixAndSuffix, HasText {

    public static final String CLASS_NAME = "linkki-text";

    protected static final String ICON_CLASS_NAME = "linkki-text-icon";

    private static final long serialVersionUID = -1027646873177686722L;

    private static final String INNER_HTML_PROPERTY = "innerHTML";

    private final HasText content;

    @CheckForNull
    private VaadinIcon icon;

    @CheckForNull
    private IconPosition iconPosition = IconPosition.LEFT;

    /**
     * Creates an empty LinkkiText with no text and no icons.
     */
    public LinkkiText() {
        this("", null);
    }

    /**
     * Creates a new LinkkiText component with a plain text and an icon as prefix component.
     */
    public LinkkiText(String text, @CheckForNull VaadinIcon icon) {
        this(new Span(), text, icon);
    }

    protected LinkkiText(HasText content, String text, @CheckForNull VaadinIcon icon) {
        addClassName(CLASS_NAME);

        this.content = content;
        getElement().appendChild(content.getElement());

        setText(text);
        if (icon != null) {
            setIcon(icon);
        }
    }

    protected HasText getContent() {
        return content;
    }

    /**
     * {@inheritDoc}
     *
     * @implNote in case of HTML mode it returns the content as string including all HTML tags
     */
    @Override
    public String getText() {
        String innerHTML = getContent().getElement().getProperty(INNER_HTML_PROPERTY);
        if (innerHTML != null) {
            return innerHTML;
        } else {
            return getContent().getText();
        }
    }

    /**
     * Sets the given text as content of this component.
     * <p>
     * To set HTML content use {@link #setText(String, boolean)} instead.
     *
     * @param text the text to set
     */
    @Override
    public void setText(String text) {
        setText(text, false);
    }

    /**
     * Sets the given text as content of this component.
     * <p>
     * HTML text will be sanitized by using {@link HtmlSanitizer#sanitizeText(String)} for security
     * reasons.<br>
     * Note that <b>user-supplied strings have to be {@link HtmlSanitizer#escapeText(String)
     * escaped}</b> when including them in the HTML content. Otherwise, they will also be interpreted as
     * HTML.
     *
     * @param text the text or HTML content to set
     * @param html use the text as HTML content when <code>true</code>, use the text as plain text
     *            otherwise
     */
    public void setText(String text, boolean html) {
        if (html) {
            var sanitizedText = HtmlSanitizer.sanitizeText(text);
            getContent().setText(null);
            getContent().getElement().setProperty(INNER_HTML_PROPERTY, sanitizedText);
        } else {
            getContent().getElement().removeProperty(INNER_HTML_PROPERTY);
            getContent().setText(text);
        }
    }

    /**
     * Getter for the currently set {@link IconPosition position} of the icon. The default is
     * {@link IconPosition#LEFT}.
     */
    @CheckForNull
    public IconPosition getIconPosition() {
        return iconPosition;
    }

    /**
     * Sets the {@link IconPosition icon position} to be used.
     *
     * @param position The position which defines whether the icon should be displayed on the left or on
     *            the right side of the text
     */
    public void setIconPosition(@CheckForNull IconPosition position) {
        iconPosition = position;
    }

    @CheckForNull
    @Override
    public VaadinIcon getIcon() {
        return this.icon;
    }

    @Override
    public void setIcon(@Nullable VaadinIcon icon) {
        if (!Objects.equals(this.icon, icon)) {
            this.icon = icon;
            setIconOnComponent(icon);
        }
    }

    protected void setIconOnComponent(@CheckForNull VaadinIcon icon) {
        Consumer<Icon> iconConsumer = IconPosition.RIGHT == iconPosition ? this::setSuffixComponent
                : this::setPrefixComponent;
        setIconOnComponent(icon, iconConsumer);
    }

    protected void setIconOnComponent(@CheckForNull VaadinIcon icon, Consumer<Icon> iconConsumer) {
        Optional.ofNullable(icon)
                .ifPresentOrElse(vIcon -> {
                    Icon theIcon = vIcon.create();
                    theIcon.setClassName(ICON_CLASS_NAME);
                    iconConsumer.accept(theIcon);
                }, () -> iconConsumer.accept(null));
    }
}
