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

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.defaults.ui.aspects.types.LabelType;
import org.linkki.core.ui.creation.table.GridColumnWrapper;

/**
 * Aspect definition to bind the label of a component.
 * <p>
 * The label can be defined using a static value, or it can be dynamically bind to the model.
 */
public class LabelAspectDefinition extends ModelToUiAspectDefinition<String> {

    public static final String NAME = "label";

    private final String label;

    private final LabelType labelType;

    /**
     * Aspect definition which represents a static label.
     *
     * @param label The label text
     */
    public LabelAspectDefinition(String label) {
        this(label, LabelType.STATIC); // used for UI elements which already contain a label aspect
    }

    /**
     * Aspect definition which represents a label of the passed {@link LabelType type}.
     *
     * @param label The label text
     * @param labelType Describes how the label is set and if it is customizable
     */
    public LabelAspectDefinition(String label, LabelType labelType) {
        super();
        this.label = label;
        this.labelType = labelType;
    }

    @Override
    public Aspect<String> createAspect() {
        switch (labelType) {
            case AUTO:
                return StringUtils.isEmpty(label) ? Aspect.of(NAME) : Aspect.of(NAME, label);
            case DYNAMIC:
                return Aspect.of(NAME);
            case STATIC:
                return Aspect.of(NAME, label);
            case NONE:
                return Aspect.of(NAME, null);
            default:
                throw new IllegalArgumentException("LabelType " + labelType + " is not supported.");
        }
    }

    @Override
    public Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
        return componentWrapper::setLabel;
    }

    @Override
    public boolean supports(WrapperType type) {
        return super.supports(type) || GridColumnWrapper.COLUMN_TYPE.isAssignableFrom(type);
    }

}
