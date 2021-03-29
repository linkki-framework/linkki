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

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.ui.aspects.BindReadOnlyBehaviorAspectDefinition;
import org.linkki.core.ui.aspects.annotation.BindReadOnlyBehavior.BindButtonReadOnlyAspectDefinitionCreator;
import org.linkki.core.ui.aspects.types.ReadOnlyBehaviorType;

/**
 * Binds the application's read-only state to a specific behaviour of the annotated
 * {@link com.vaadin.ui.Component}. The component will automatically change its visible and enabled
 * state when it is set to read-only-mode. This annotation will override the component's dynamic or
 * static aspects of {@link EnabledAspectDefinition enabled} or {@link VisibleAspectDefinition visible},
 * respectively.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
@LinkkiAspect(BindButtonReadOnlyAspectDefinitionCreator.class)
public @interface BindReadOnlyBehavior {

    ReadOnlyBehaviorType value() default ReadOnlyBehaviorType.INVSIBLE;

    class BindButtonReadOnlyAspectDefinitionCreator implements AspectDefinitionCreator<BindReadOnlyBehavior> {

        @Override
        public LinkkiAspectDefinition create(BindReadOnlyBehavior annotation) {
            return new BindReadOnlyBehaviorAspectDefinition(annotation.value());
        }

    }

}