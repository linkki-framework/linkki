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

import org.eclipse.jdt.annotation.NonNull;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.aspect.definition.ModelToUiAspectDefinition;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.section.annotations.BindTooltip;
import org.linkki.core.ui.section.annotations.BindTooltip.TooltipType;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class BindTooltipAspectDefinition extends ModelToUiAspectDefinition<@NonNull String> {

    public static final String NAME = "tooltip";

    @SuppressFBWarnings("NP_NONNULL_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
    @SuppressWarnings("null")
    private BindTooltip annotation;

    @Override
    public void initialize(Annotation tooltipAnnotation) {
        this.annotation = (BindTooltip)tooltipAnnotation;
    }

    @Override
    public Aspect<@NonNull String> createAspect() {
        if (annotation.tooltipType() == TooltipType.STATIC) {
            return Aspect.of(NAME, annotation.value());
        } else {
            return Aspect.of(NAME);
        }
    }

    @Override
    public Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
        return componentWrapper::setTooltip;
    }

}