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

package org.linkki.core.ui.aspects.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.defaults.ui.aspects.types.LabelType;
import org.linkki.core.ui.aspects.LabelAspectDefinition;
import org.linkki.core.ui.aspects.annotation.BindLabel.BindLabelAspectDefinitionCreator;

/**
 * This aspect sets a user defined label text.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
@LinkkiAspect(BindLabelAspectDefinitionCreator.class)
public @interface BindLabel {

    /** The displayed text for {@link LabelType}. */
    String value() default StringUtils.EMPTY;


    /** Defines how the label text should be retrieved. */
    LabelType labelType() default LabelType.AUTO;

    class BindLabelAspectDefinitionCreator implements AspectDefinitionCreator<BindLabel> {

        @Override
        public LinkkiAspectDefinition create(BindLabel annotation) {
            return new LabelAspectDefinition(annotation.value(), annotation.labelType());
        }

    }
}
