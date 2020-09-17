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

package org.linkki.core.ui.aspects;

import java.util.function.Consumer;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.ui.aspects.types.IconType;

import com.vaadin.server.Resource;
import com.vaadin.ui.Component;

/**
 * Aspect definition to bind the icon of a component.
 */
public class IconAspectDefinition extends ModelToUiAspectDefinition<Resource> {

    public static final String NAME = "icon";

    private final IconType type;

    private final Resource value;

    public IconAspectDefinition(IconType type, Resource value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public Aspect<Resource> createAspect() {
        if (type == IconType.STATIC) {
            return Aspect.of(NAME, value);
        } else {
            return Aspect.of(NAME);
        }
    }

    @Override
    public Consumer<Resource> createComponentValueSetter(ComponentWrapper componentWrapper) {
        Component component = (Component)componentWrapper.getComponent();
        return component::setIcon;
    }

}