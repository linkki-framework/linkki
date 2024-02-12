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

package org.linkki.core.ui.aspects.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.ui.aspects.BindComboBoxItemStyleAspectDefinition;
import org.linkki.core.ui.aspects.ComboBoxDynamicItemCaptionAspectDefinition;
import org.linkki.core.ui.aspects.annotation.BindComboBoxDynamicItemCaption.ComboBoxDynamicItemCaptionAspectDefinitionCreator;

/**
 * Aspect for @{@link org.linkki.core.ui.element.annotation.UIComboBox} whose caption can change for
 * the same item.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
@LinkkiAspect(ComboBoxDynamicItemCaptionAspectDefinitionCreator.class)
public @interface BindComboBoxDynamicItemCaption {

    /**
     * Internal aspect definition creator for {@link BindComboBoxDynamicItemCaption} to create the
     * {@link BindComboBoxItemStyleAspectDefinition}.
     */
    class ComboBoxDynamicItemCaptionAspectDefinitionCreator
            implements AspectDefinitionCreator<BindComboBoxDynamicItemCaption> {

        @Override
        public LinkkiAspectDefinition create(BindComboBoxDynamicItemCaption annotation) {
            return new ComboBoxDynamicItemCaptionAspectDefinition();
        }

    }

}
