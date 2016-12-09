package org.linkki.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.themes.ValoTheme;

/** A presentation model object for a button. */
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

    public static ButtonPmo newEditButton(Runnable onClickAction) {
        return onClickAction::run;
    }

    public static ButtonPmo newAddButton(Runnable onClickAction) {
        return Builder.action(onClickAction).icon(FontAwesome.PLUS).get();
    }

    public static ButtonPmo newDeleteButton(Runnable onClickAction) {
        return Builder.action(onClickAction).icon(FontAwesome.TRASH_O).get();
    }

    public static class Builder {

        private Runnable action;
        private Resource icon;

        public Builder(Runnable onClickAction) {
            this.action = onClickAction;
        }

        public static Builder action(Runnable onClickAction) {
            return new Builder(onClickAction);
        }

        public Builder icon(Resource buttonIcon) {
            this.icon = buttonIcon;
            return this;
        }

        public ButtonPmo get() {
            return new ButtonPmo() {

                @Override
                public void onClick() {
                    action.run();
                }

                @Override
                public Resource getButtonIcon() {
                    return icon;
                }
            };
        }

    }

}
