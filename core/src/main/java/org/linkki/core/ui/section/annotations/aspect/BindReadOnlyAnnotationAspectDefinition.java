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

package org.linkki.core.ui.section.annotations.aspect;

import java.lang.annotation.Annotation;

import org.apache.commons.lang3.BooleanUtils;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.section.annotations.BindReadOnly;
import org.linkki.core.ui.section.annotations.BindReadOnly.ReadOnlyType;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.AbstractField;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Aspect definition for read-only state.
 */
public class BindReadOnlyAnnotationAspectDefinition implements LinkkiAspectDefinition {

    public static final String NAME = "readOnly";

    @SuppressFBWarnings("NP_NONNULL_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
    @SuppressWarnings("null")
    private ReadOnlyType value;

    @Override
    public void initialize(Annotation bindReadOnlyAnnotation) {
        setReadOnlyType(((BindReadOnly)bindReadOnlyAnnotation).value());
    }

    public void setReadOnlyType(ReadOnlyType value) {
        this.value = value;
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        AbstractField<?> component = (AbstractField<?>)componentWrapper.getComponent();

        switch (value) {
            case ALWAYS:
                return () -> component.setReadOnly(true);
            case DYNAMIC:
                Aspect<Boolean> aspect = Aspect.of(NAME);
                return () -> {
                    component.setReadOnly(component.isReadOnly()
                            || BooleanUtils.toBoolean(propertyDispatcher.pull(aspect)));
                };
            default:
                return Handler.NOP_HANDLER;
        }
    }
}
