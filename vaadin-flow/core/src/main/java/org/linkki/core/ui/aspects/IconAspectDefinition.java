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

package org.linkki.core.ui.aspects;

import java.util.function.Consumer;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.ui.aspects.types.IconType;
import org.linkki.core.vaadin.component.HasIcon;
import org.linkki.util.Consumers;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Aspect definition to bind the icon of a component.
 */
public class IconAspectDefinition extends ModelToUiAspectDefinition<VaadinIcon> {

    public static final String NAME = "icon";

    private final IconType type;

    @CheckForNull
    private final VaadinIcon value;

    public IconAspectDefinition(IconType type, @CheckForNull VaadinIcon value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public Aspect<VaadinIcon> createAspect() {
        if (type == IconType.AUTO) {
            return value == null
                    ? Aspect.of(NAME)
                    : Aspect.of(NAME, value);
        } else if (type == IconType.STATIC) {
            return Aspect.of(NAME, value);
        } else {
            return Aspect.of(NAME);
        }
    }

    @Override
    public Consumer<VaadinIcon> createComponentValueSetter(ComponentWrapper componentWrapper) {
        Component component = (Component)componentWrapper.getComponent();
        if (component instanceof Button) {
            return icon -> {
                Button button = ((Button)component);
                button.setIcon(icon != null ? icon.create() : null);
            };
        } else if (component instanceof HasIcon) {
            return ((HasIcon)component)::setIcon;
        } else {
            return Consumers.nopConsumer();
        }
    }
}