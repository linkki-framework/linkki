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
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.ui.aspects.CaptionAspectDefinition;
import org.linkki.core.ui.aspects.annotation.BindCaption.BindCaptionAspectDefinitionCreator;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.component.HasText;

/**
 * This aspect sets a user defined caption text using {@link HasText#setText(String)}.
 * <p>
 * If used on a {@link ContainerPmo}, the caption of the surrounding {@link UISection} is set instead of
 * the table's caption.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
@LinkkiAspect(BindCaptionAspectDefinitionCreator.class)
public @interface BindCaption {

    /** The displayed text for {@link CaptionType#STATIC} */
    String value() default StringUtils.EMPTY;


    /** Defines how the caption text should be retrieved */
    CaptionType captionType() default CaptionType.AUTO;

    class BindCaptionAspectDefinitionCreator implements AspectDefinitionCreator<BindCaption> {

        @Override
        public LinkkiAspectDefinition create(BindCaption annotation) {
            return new CaptionAspectDefinition(annotation.captionType(), annotation.value());
        }

    }

}
