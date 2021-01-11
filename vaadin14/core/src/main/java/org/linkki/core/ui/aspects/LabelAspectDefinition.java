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
import org.linkki.core.binding.descriptor.aspect.base.StaticModelToUiAspectDefinition;
import org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.ui.creation.table.GridColumnWrapper;

/**
 * Aspect definition to bind the label of a component.
 * <p>
 * This definition only supports static labels. It may be directly described by the
 * {@link BindingDefinition} but there might be a dispatcher that overwrites the value for example with
 * a translation from a property file.
 */
public class LabelAspectDefinition extends StaticModelToUiAspectDefinition<String> {

    public static final String NAME = "label";

    private final String label;


    public LabelAspectDefinition(String label) {
        super();
        this.label = label;
    }

    @Override
    public Aspect<String> createAspect() {
        return Aspect.of(NAME, label);
    }

    @Override
    public Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
        return l -> componentWrapper.setLabel(l);
    }

    @Override
    public boolean supports(WrapperType type) {
        return WrapperType.COMPONENT.isAssignableFrom(type) || GridColumnWrapper.COLUMN_TYPE.isAssignableFrom(type);
    }

}
