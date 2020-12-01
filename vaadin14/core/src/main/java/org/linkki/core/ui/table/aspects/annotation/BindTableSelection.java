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

package org.linkki.core.ui.table.aspects.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.InheritedAspect;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.ui.table.aspects.TableSelectionAspectDefinition;
import org.linkki.core.ui.table.aspects.annotation.BindTableSelection.TableSelectionAspectDefinitionCreator;

/**
 * Binds the selection of a table row to the aspect
 * {@value TableSelectionAspectDefinition#SELECTION_ASPECT_NAME}. In addition, the double click invokes
 * the aspect {@value TableSelectionAspectDefinition#DOUBLE_CLICK_ASPECT_NAME}.
 */
@InheritedAspect
@LinkkiAspect(TableSelectionAspectDefinitionCreator.class)
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindTableSelection {

    class TableSelectionAspectDefinitionCreator implements AspectDefinitionCreator<Annotation> {

        @Override
        public LinkkiAspectDefinition create(Annotation annotation) {
            return new TableSelectionAspectDefinition();
        }
    }
}
