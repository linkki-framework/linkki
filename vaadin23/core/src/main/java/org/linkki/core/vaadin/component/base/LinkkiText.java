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

import org.linkki.core.defaults.style.LinkkiTheme;
import org.linkki.core.vaadin.component.HasIcon;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Span;
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

    private static final long serialVersionUID = -1027646873177686722L;

    private final Span content;

    @CheckForNull
    private VaadinIcon icon;

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
        addClassName(CLASS_NAME);

        content = new Span();
        getElement().appendChild(content.getElement());

        setText(text);
        if (icon != null) {
            setIcon(icon);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @implNote in case of HTML mode it returns the content as string including all HTML tags
     */
    @Override
    public String getText() {
        String innerHTML = content.getElement().getProperty("innerHTML");
        if (innerHTML != null) {
            return innerHTML;
        } else {
            return content.getText();
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
     * 
     * @param text the text or HTML content to set
     * @param html use the text as HTML content when <code>true</code>, use the text as plain text
     *            otherwise
     */
    public void setText(String text, boolean html) {
        if (html) {
            content.setText(null);
            content.getElement().setProperty("innerHTML", text);
        } else {
            content.getElement().removeProperty("innerHTML");
            content.setText(text);
        }
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
            if (icon != null) {
                setPrefixComponent(icon.create());
                setClassName(LinkkiTheme.HAS_ICON);
            } else {
                setPrefixComponent(null);
                removeClassName(LinkkiTheme.HAS_ICON);
            }
        }
    }

}
