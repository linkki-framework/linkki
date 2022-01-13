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
import org.linkki.core.ui.aspects.SuffixAspectDefinition;
import org.linkki.core.ui.aspects.annotation.BindSuffix.BindSuffixAspectDefinitionCreator;
import org.linkki.core.ui.aspects.types.SuffixType;

import com.vaadin.flow.component.textfield.HasPrefixAndSuffix;

/**
 * BindSuffix can add a Suffix to UI Elements which supports the {@link HasPrefixAndSuffix} interface.
 * Common UI Elements which supports BindSuffix are UIIntegerField, UIDoubleField, UIDecimalField,
 * UITextField, UITextArea, UICustomField.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
@LinkkiAspect(BindSuffixAspectDefinitionCreator.class)
public @interface BindSuffix {

    /** The displayed text for {@link SuffixType#STATIC} */
    String value() default StringUtils.EMPTY;


    /** Defines how the suffix text should be retrieved */
    SuffixType suffixType() default SuffixType.AUTO;


    class BindSuffixAspectDefinitionCreator implements AspectDefinitionCreator<BindSuffix> {

        @Override
        public LinkkiAspectDefinition create(BindSuffix annotation) {
            return new SuffixAspectDefinition(annotation.value(), annotation.suffixType());
        }
    }
}
