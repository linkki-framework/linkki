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
import java.util.Objects;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.aspect.definition.ModelToUiAspectDefinition;
import org.linkki.core.ui.components.ComponentWrapper;

import com.vaadin.ui.Label;

/**
 * The value aspect for label components. The label is a read-only component, hence this aspect only
 * reads the value from model and updates the UI.
 */
public class LabelValueAspectDefinition extends ModelToUiAspectDefinition<String> {

    public static final String NAME = StringUtils.EMPTY;

    @Override
    public void initialize(Annotation annotation) {
        // does nothing
    }

    @Override
    public Aspect<String> createAspect() {
        return Aspect.of(NAME);
    }

    @Override
    public Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
        return v -> ((Label)componentWrapper.getComponent()).setValue(Objects.toString(v, ""));
    }
}
