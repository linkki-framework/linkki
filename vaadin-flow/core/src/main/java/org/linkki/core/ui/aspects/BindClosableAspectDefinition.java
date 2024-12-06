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

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.base.StaticModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.vaadin.component.section.LinkkiSection;
import org.linkki.util.Consumers;

/**
 * Aspect definition to make a {@link LinkkiSection} closable.
 */
public class BindClosableAspectDefinition extends StaticModelToUiAspectDefinition<Boolean> {

    private static final String NAME = "closable";
    private final boolean initial;

    public BindClosableAspectDefinition(boolean initial) {
        this.initial = initial;
    }

    @Override
    public Aspect<Boolean> createAspect() {
        return Aspect.of(NAME, initial);
    }

    @Override
    public Consumer<Boolean> createComponentValueSetter(ComponentWrapper componentWrapper) {
        if (componentWrapper.getComponent() instanceof LinkkiSection component) {
            return initiallyClosed -> {
                component.setClosable(true);
                if (initiallyClosed) {
                    component.close();
                }
            };
        } else {
            return Consumers.nopConsumer();
        }
    }
}
