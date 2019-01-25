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

package org.linkki.core.ui.section.annotations.aspect;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;

import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.aspect.definition.ModelToUiAspectDefinition;
import org.linkki.core.ui.components.ComponentWrapper;

/**
 * @deprecated Since October 4th, 2018. {@link BindTooltipAspectDefinition} is used instead. This aspect
 *             definition will be removed in the next release.
 */
@Deprecated
@SuppressWarnings("deprecation")
public class ToolTipAspectDefinition extends ModelToUiAspectDefinition<String> {

    public static final String NAME = "toolTip";

    @SuppressWarnings("null")
    private org.linkki.core.ui.section.annotations.UIToolTip annotation;


    @Override
    public void initialize(Annotation toolTipAnnotation) {
        this.annotation = (org.linkki.core.ui.section.annotations.UIToolTip)toolTipAnnotation;
    }

    @Override
    public Aspect<String> createAspect() {
        if (annotation.toolTipType() == org.linkki.core.ui.section.annotations.ToolTipType.STATIC) {
            return Aspect.of(NAME, annotation.text());
        } else {
            return Aspect.of(NAME);
        }
    }

    @Override
    public Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
        return componentWrapper::setTooltip;
    }
}