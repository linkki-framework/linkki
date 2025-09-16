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

package org.linkki.core.ui.aspects;

import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.aspects.types.HelperTextType;

import com.vaadin.flow.component.HasHelper;

public class HelperTextAspectDefinition extends ModelToUiAspectDefinition<String> {

    public static final String NAME = "helperText";

    private final String value;
    private final HelperTextType type;

    public HelperTextAspectDefinition(String value, HelperTextType helperTextType) {
        this.value = value;
        this.type = helperTextType;
    }

    @Override
    public Aspect<String> createAspect() {
        return switch (type) {
            case AUTO -> StringUtils.isEmpty(value) ? Aspect.of(NAME)
                    : Aspect.of(NAME, value);
            case STATIC -> Aspect.of(NAME, value);
            case DYNAMIC -> Aspect.of(NAME);
        };
    }

    @Override
    protected Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
        var helper = (HasHelper)componentWrapper.getComponent();

        return helper::setHelperText;
    }

}
