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
package org.linkki.doc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.linkki.core.binding.aspect.LinkkiAspect;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.doc.BindTooltipAspectDefinition.TooltipType;

// tag::BindTooltip[]
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
// end::BindTooltip[]
// tag::BindTooltipLinkkiAspect[]
@LinkkiAspect(BindTooltip.AspectCreator.class)
// tag::BindTooltip[]
public @interface BindTooltip {
    // end::BindTooltipLinkkiAspect[]

    TooltipType tooltipType() default TooltipType.STATIC;

    String value() default StringUtils.EMPTY;
    // end::BindTooltip[]

    // tag::BindTooltipLinkkiAspectCreator[]
    class AspectCreator implements LinkkiAspect.Creator<@NonNull BindTooltip> {

        @Override
        public LinkkiAspectDefinition create(BindTooltip annotation) {
            return new BindTooltipAspectDefinition(annotation.tooltipType(), annotation.value());
        }

    }
    // end::BindTooltipLinkkiAspectCreator[]
    // tag::BindTooltip[]

}
// end::BindTooltip[]
