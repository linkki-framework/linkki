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

import java.lang.annotation.Annotation;
import java.util.function.Consumer;

import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.aspect.definition.ModelToUiAspectDefinition;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.section.annotations.BindTooltip;
import org.linkki.core.ui.section.annotations.BindTooltipType;

public class BindTooltipAspectDefinition extends ModelToUiAspectDefinition<String> {

    public static final String NAME = "Tooltip";

    @SuppressWarnings("null")
    private BindTooltip annotation;

    @Override
    public void initialize(Annotation tooltipAnnotation) {
        this.annotation = (BindTooltip)tooltipAnnotation;
    }

    @Override
    public Aspect<String> createAspect() {
        if (annotation.tooltipType() == BindTooltipType.STATIC) {
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