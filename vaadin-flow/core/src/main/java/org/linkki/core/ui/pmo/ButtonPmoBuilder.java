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
package org.linkki.core.ui.pmo;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.pmo.ButtonPmo;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.component.icon.VaadinIcon;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Builder to create a {@link ButtonPmo} with a fluent API. <em>{@link #icon(Icon)} must be called
 * before {@link #get()} as buttons are not created without an icon</em>
 */
public class ButtonPmoBuilder {

    private final Handler action;

    @CheckForNull
    private Icon icon;

    public ButtonPmoBuilder(Handler onClickAction) {
        this.action = requireNonNull(onClickAction, "onClickAction must not be null");
    }

    /**
     * Creates a new {@link ButtonPmo} for an edit button and returns it.
     * 
     * @param onClickAction the action that is applied when the button is clicked
     * 
     * @implSpec If used directly in getter methods, like in
     *           {@link ContainerPmo#getAddItemButtonPmo()}, this component's context can't be
     *           removed by {@link BindingContext#removeBindingsForPmo(Object)}. This can be avoided
     *           by always returning the same instance of {@link ButtonPmo}.
     */
    public static ButtonPmo newEditButton(Handler onClickAction) {
        return ButtonPmoBuilder.action(onClickAction).icon(VaadinIcon.PENCIL).get();
    }

    /**
     * Creates a new {@link ButtonPmo} for an add button and returns it.
     * 
     * @param onClickAction the action that is applied when the button is clicked
     * 
     * @implSpec If used directly in getter methods, like in
     *           {@link ContainerPmo#getAddItemButtonPmo()}, this component's context can't be
     *           removed by {@link BindingContext#removeBindingsForPmo(Object)}. This can be avoided
     *           by always returning the same instance of {@link ButtonPmo}.
     */
    public static ButtonPmo newAddButton(Handler onClickAction) {
        return ButtonPmoBuilder.action(onClickAction).icon(VaadinIcon.PLUS).get();
    }

    /**
     * Creates a new {@link ButtonPmo} for a delete button and returns it.
     * 
     * @param onClickAction the action that is applied when the button is clicked
     * 
     * @implSpec If used directly in getter methods, like in
     *           {@link ContainerPmo#getAddItemButtonPmo()}, this component's context can't be
     *           removed by {@link BindingContext#removeBindingsForPmo(Object)}. This can be avoided
     *           by always returning the same instance of {@link ButtonPmo}.
     */
    public static ButtonPmo newDeleteButton(Handler onClickAction) {
        return ButtonPmoBuilder.action(onClickAction).icon(VaadinIcon.TRASH).get();
    }

    public static ButtonPmoBuilder action(Handler onClickAction) {
        return new ButtonPmoBuilder(onClickAction);
    }

    public ButtonPmoBuilder icon(Icon buttonIcon) {
        this.icon = buttonIcon;
        return this;
    }

    public ButtonPmoBuilder icon(IconFactory buttonIconFactory) {
        this.icon = buttonIconFactory.create();
        return this;
    }

    Handler getAction() {
        return action;
    }

    public ButtonPmo get() {

        Icon existingIcon = requireNonNull(icon, "icon must be set before calling get()");

        return new ButtonPmo() {

            @Override
            public void onClick() {
                getAction().apply();
            }

            @Override
            public Icon getButtonIcon() {
                return existingIcon;
            }

            @Override
            public List<String> getStyleNames() {
                return new ArrayList<>();
            }

        };
    }

}
