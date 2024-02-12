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

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.aspects.annotation.BindVisible.BindVisibleAspectDefinitionCreator;
import org.linkki.core.ui.layout.annotation.UISection;

/**
 * This aspect annotation sets the visibility of a component to {@link VisibleType#DYNAMIC},
 * determining its visibility by calling the <b>is[PropertyName]Visible()</b> method. When
 * annotating an entire PMO the method is <b>isVisible()</b>.
 * <p>
 * Apart from an initial update upon creation, updates of child bindings are skipped if a PMO is
 * invisible.
 *
 * <p>
 * <b>Note</b>: To ensure stability during the initial update phase:
 * <ul>
 * <li>Instantiate PMOs with a temporary model object, then switch to the actual model object and
 * invoke {@link BindingContext#uiUpdated()}.</li>
 * <li>Implement null and exception checks in getter methods without direct model binding.</li>
 * </ul>
 *
 * <p>
 * If used on a {@link ContainerPmo}, the visibility of the surrounding {@link UISection} is set
 * instead of the table's visibility.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
@LinkkiAspect(BindVisibleAspectDefinitionCreator.class)
public @interface BindVisible {

    class BindVisibleAspectDefinitionCreator implements AspectDefinitionCreator<BindVisible> {

        @Override
        public LinkkiAspectDefinition create(BindVisible annotation) {
            return new VisibleAspectDefinition(VisibleType.DYNAMIC);
        }
    }
}
