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
import org.linkki.core.defaults.ui.aspects.types.CaptionType;

import com.vaadin.ui.Component;

/**
 * Aspect definition for caption binding. Assumes that the {@link ComponentWrapper} wraps a
 * {@link Component} by default.
 */
public class CaptionAspectDefinition extends ModelToUiAspectDefinition<String> {

    public static final String NAME = "caption";

    private final CaptionType captionType;
    private final String staticCaption;

    public CaptionAspectDefinition(CaptionType captionType, String staticCaption) {
        this.captionType = captionType;
        this.staticCaption = staticCaption;

    }

    @Override
    public Aspect<String> createAspect() {
        if (captionType == CaptionType.DYNAMIC) {
            return Aspect.of(NAME);
        } else if (captionType == CaptionType.STATIC) {
            return Aspect.of(NAME, staticCaption);
        } else {
            return Aspect.of(NAME, null);
        }
    }

    @Override
    public Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
        return caption -> ((Component)componentWrapper.getComponent()).setCaption(caption);
    }
}
