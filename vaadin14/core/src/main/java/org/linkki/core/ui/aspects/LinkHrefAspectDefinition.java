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

import java.util.Optional;
import java.util.function.Consumer;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;

import com.vaadin.flow.component.html.Anchor;

/**
 * 
 * This aspect sets the HREF link target of a {@link Anchor}.
 * 
 */
public class LinkHrefAspectDefinition extends ModelToUiAspectDefinition<String> {

    @Override
    public Aspect<String> createAspect() {
        return Aspect.of(VALUE_ASPECT_NAME);
    }

    @Override
    public Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
        Anchor link = (Anchor)componentWrapper.getComponent();
        return href -> link.setHref(Optional.ofNullable(href).orElse(""));
    }
}

