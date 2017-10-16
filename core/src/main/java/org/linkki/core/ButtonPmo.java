/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.core;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;
import org.linkki.util.handler.Handler;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.themes.ValoTheme;

/** A presentation model object for a button with only displays an icon (no text). */
@FunctionalInterface
public interface ButtonPmo {

    /** Executes the button click action. */
    void onClick();

    /** Returns the icon to display for the button. */
    default Resource getButtonIcon() {
        return FontAwesome.PENCIL;
    }

    /** Returns the style names for the button. */
    default Collection<String> getStyleNames() {
        // return a modifiable list, so the user can add custom styles
        return new ArrayList<>(Arrays.asList(ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_ICON_ONLY));
    }

    /**
     * Returns <code>true</code> if the button is enabled, otherwise <code>false</code>.
     */
    default boolean isEnabled() {
        return true;
    }

    /**
     * Returns <code>true</code> if the button is visible, otherwise <code>false</code>.
     */
    default boolean isVisible() {
        return true;
    }

    public static ButtonPmo newEditButton(Handler onClickAction) {
        return onClickAction::apply;
    }

    public static ButtonPmo newAddButton(Handler onClickAction) {
        return Builder.action(onClickAction).icon(FontAwesome.PLUS).get();
    }

    public static ButtonPmo newDeleteButton(Handler onClickAction) {
        return Builder.action(onClickAction).icon(FontAwesome.TRASH_O).get();
    }

    /**
     * Builder to create a {@link ButtonPmo} with a fluent API. <em>{@link #icon(Resource)} must be
     * called before {@link #get()} as buttons are not created without an icon</em>
     */
    public static class Builder {

        private Handler action;

        @Nullable
        private Resource icon;

        public Builder(Handler onClickAction) {
            this.action = requireNonNull(onClickAction, "onClickAction must not be null");
        }

        public static Builder action(Handler onClickAction) {
            return new Builder(onClickAction);
        }

        public Builder icon(Resource buttonIcon) {
            this.icon = buttonIcon;
            return this;
        }

        public ButtonPmo get() {
            Resource existingIcon = Validate.notNull(icon, "icon must be set before calling get()");

            return new ButtonPmo() {

                @Override
                public void onClick() {
                    action.apply();
                }

                @Override
                public Resource getButtonIcon() {
                    return existingIcon;
                }
            };
        }

    }

}
