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

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.vaadin.flow.component.HasTheme;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.base.StaticModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.util.Consumers;

/**
 * This aspect sets a vaadin defined variant names using {@link HasTheme#addThemeName(String)}.
 */
public class BindVariantNamesAspectDefinition extends StaticModelToUiAspectDefinition<List<String>> {

    public static final String NAME = "variantNames";

    private final List<String> staticVariantNames;

    public BindVariantNamesAspectDefinition(String... variantNames) {
        this.staticVariantNames = Arrays.asList(variantNames);
    }

    @Override
    public Aspect<List<String>> createAspect() {
        return Aspect.of(NAME, staticVariantNames);
    }

    @Override
    public Consumer<List<String>> createComponentValueSetter(ComponentWrapper componentWrapper) {
        if (componentWrapper.getComponent() instanceof HasTheme) {
            HasTheme component = (HasTheme)componentWrapper.getComponent();
            return variantNames -> variantNames.forEach(component::addThemeName);
        } else {
            return Consumers.nopConsumer();
        }
    }
}
