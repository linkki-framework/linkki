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

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.defaults.columnbased.ColumnBasedComponentWrapper;
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
        switch (captionType) {
            case AUTO:
                return StringUtils.isEmpty(staticCaption)
                        ? Aspect.of(NAME)
                        : Aspect.of(NAME, staticCaption);
            case DYNAMIC:
                return Aspect.of(NAME);
            case STATIC:
                return Aspect.of(NAME, staticCaption);
            case NONE:
                return Aspect.of(NAME, null);
            default:
                throw new IllegalArgumentException("CaptionType " + captionType + " is not supported.");
        }
    }

    @Override
    public Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
        return caption -> ((Component)componentWrapper.getComponent()).setCaption(caption);
    }

    @Override
    public boolean supports(WrapperType type) {
        // TODO refactor in LIN-1892
        return super.supports(type) && !ColumnBasedComponentWrapper.COLUMN_BASED_TYPE.isAssignableFrom(type);
    }
}
