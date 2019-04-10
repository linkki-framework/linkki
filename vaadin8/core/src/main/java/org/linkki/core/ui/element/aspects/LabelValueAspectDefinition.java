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

package org.linkki.core.ui.element.aspects;

import java.util.Objects;
import java.util.function.Consumer;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;

import com.vaadin.ui.Label;

/**
 * The value aspect for label components. The label is a read-only component, hence this aspect only
 * reads the value from model and updates the UI.
 */
public class LabelValueAspectDefinition extends ModelToUiAspectDefinition<Object> {

    public static final String NAME = LabelAspectDefinition.VALUE_ASPECT_NAME;

    @Override
    public Aspect<Object> createAspect() {
        return Aspect.of(NAME);
    }

    @Override
    public Consumer<Object> createComponentValueSetter(ComponentWrapper componentWrapper) {
        return v -> ((Label)componentWrapper.getComponent()).setValue(Objects.toString(v, ""));
    }
}
