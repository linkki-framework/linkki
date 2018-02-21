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

package org.linkki.core.binding.annotations.aspect;

import java.util.Arrays;
import java.util.List;

import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.binding.aspect.definition.CompositeAspectDefinition;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;

/**
 * Composite of all aspect binding definitions for the {@link Bind} annotation.
 */
public class BindAnnotationAspectDefinition extends CompositeAspectDefinition {

    public BindAnnotationAspectDefinition() {
        super(createAspectDefinitions());
    }

    private static List<LinkkiAspectDefinition> createAspectDefinitions() {
        BindEnabledAspectDefinition enabledTypeAspectDefinition = new BindEnabledAspectDefinition();
        return Arrays.asList(new BindAvailableValuesAspectDefinition(),
                             enabledTypeAspectDefinition,
                             new BindRequiredAspectDefinition(enabledTypeAspectDefinition),
                             new BindFieldValueAspectDefinition(),
                             new BindReadOnlyAspectDefinition(),
                             new BindLabelValueAspectDefinition(),
                             new BindButtonInvokeAspectDefinition());
    }
}
