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
import org.linkki.core.ui.aspects.annotation.BindReadOnly.ReadOnlyType;

import com.vaadin.flow.component.HasValue;

/**
 * Aspect definition for read-only state.
 */
public class BindReadOnlyAspectDefinition extends ModelToUiAspectDefinition<Boolean> {

    public static final String NAME = "readOnly";

    private final ReadOnlyType value;

    public BindReadOnlyAspectDefinition(ReadOnlyType value) {
        this.value = value;
    }

    @Override
    public Aspect<Boolean> createAspect() {
        switch (value) {
            case ALWAYS:
                return Aspect.of(NAME, true);
            case DYNAMIC:
                return Aspect.of(NAME);
            case DERIVED:
                // field is set read-only by DerivedReadOnlyAspectDefinition, so || evaluates to true
                return Aspect.of(NAME, false);
            default:
                throw new IllegalStateException("Unknown read-only type: " + value);
        }
    }

    @Override
    public Consumer<Boolean> createComponentValueSetter(ComponentWrapper componentWrapper) {
        HasValue<?, ?> field = (HasValue<?, ?>)componentWrapper.getComponent();
        return readOnly -> field.setReadOnly(field.isReadOnly() || readOnly);
    }

}
