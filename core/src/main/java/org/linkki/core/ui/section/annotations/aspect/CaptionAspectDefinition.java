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

package org.linkki.core.ui.section.annotations.aspect;

import java.util.function.Consumer;

import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.aspect.definition.ModelToUiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyNamingConvention;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.section.annotations.CaptionType;

import com.vaadin.ui.Component;

/**
 * Aspect definition for caption binding. Assumes that the {@link ComponentWrapper} wraps a
 * {@link Component} by default.
 * <p>
 * In most cases, the annotation in which this aspect definition is referenced is needed to
 * determine the {@link #getStaticCaption() static caption} and the {@link #getCaptionType() caption
 * type}. This annotation can be stored in {@link #initialize(java.lang.annotation.Annotation)}.
 */
public abstract class CaptionAspectDefinition extends ModelToUiAspectDefinition<String> {

    public static final String NAME = PropertyNamingConvention.CAPTION_PROPERTY_SUFFIX;

    @Override
    public Aspect<String> createAspect() {
        if (getCaptionType() == CaptionType.DYNAMIC) {
            return Aspect.newDynamic(NAME);
        } else if (getCaptionType() == CaptionType.STATIC) {
            return Aspect.ofStatic(NAME, getStaticCaption());
        } else {
            return Aspect.ofStatic(NAME, null);
        }
    }

    protected abstract String getStaticCaption();

    @Override
    public Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
        return caption -> ((Component)componentWrapper.getComponent()).setCaption(caption);
    }

    /**
     * Type of the caption binding. Usually has to be retrieved from an annotation.
     */
    public abstract CaptionType getCaptionType();
}
