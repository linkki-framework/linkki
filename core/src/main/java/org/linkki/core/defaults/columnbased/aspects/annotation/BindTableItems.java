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

package org.linkki.core.defaults.columnbased.aspects.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.InheritedAspect;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.defaults.columnbased.aspects.ColumnBasedComponentItemsAspectDefinition;
import org.linkki.core.defaults.columnbased.aspects.annotation.BindTableItems.TableItemsAspectDefinitionCreator;

/**
 * Binds the items that should be shown in the table.
 */
@InheritedAspect
@LinkkiAspect(TableItemsAspectDefinitionCreator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindTableItems {

    public class TableItemsAspectDefinitionCreator implements AspectDefinitionCreator<BindTableItems> {

        @Override
        public LinkkiAspectDefinition create(BindTableItems annotation) {
            return new ColumnBasedComponentItemsAspectDefinition<>();
        }
    }
}
