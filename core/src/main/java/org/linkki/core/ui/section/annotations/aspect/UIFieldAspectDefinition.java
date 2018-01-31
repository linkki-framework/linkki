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

import java.util.Arrays;
import java.util.List;

import org.linkki.core.binding.aspect.definition.CompositeAspectDefinition;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.ui.section.annotations.LinkkiBindingDefinition;
import org.linkki.core.ui.section.annotations.UIFieldDefinition;

/**
 * Aspect definition for all annotations using {@link UIFieldDefinition} as
 * {@link LinkkiBindingDefinition}.
 */
public class UIFieldAspectDefinition extends CompositeAspectDefinition {

    public UIFieldAspectDefinition() {
        super(createAspectDefinitions());
    }

    private static List<LinkkiAspectDefinition> createAspectDefinitions() {
        UIElementEnabledAspectDefinition enabledAspectDefinition = new UIElementEnabledAspectDefinition();
        UIElementRequiredAspectDefinition requiredAspectDefinition = new UIElementRequiredAspectDefinition(
                enabledAspectDefinition);
        return Arrays.asList(enabledAspectDefinition, requiredAspectDefinition);
    }
}
