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

import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.defaults.ui.aspects.types.IconType;
import org.linkki.core.ui.aspects.IconAspectDefinition;
import org.linkki.core.ui.aspects.annotation.BindIcon.BindIconAspectDefinitionCreator;

import com.vaadin.flow.component.icon.VaadinIcon;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Shows an icon for a UI component. The annotation can be added to the method bound to a UI field .
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
@LinkkiAspect(BindIconAspectDefinitionCreator.class)
public @interface BindIcon {

    /**
     * The displayed icon for {@link IconType#STATIC}.
     * 
     * @implNote To support the convenient way to just use @BindIcon and directly get a dynamic icon
     *           binding without specifying the {@link #iconType()} explicitly, we need a default
     *           value. The icon {@link VaadinIcon#NATIVE_BUTTON} seems to be a quite useless icon.
     *           However if it is necessary to use exactly this icon it is necessary to specify
     *           {@link #iconType()} as {@link IconType#STATIC} explicitly.
     */
    VaadinIcon value() default VaadinIcon.NATIVE_BUTTON;

    /** Defines how the icon should be retrieved */
    IconType iconType() default IconType.AUTO;

    class BindIconAspectDefinitionCreator implements AspectDefinitionCreator<BindIcon> {

        @Override
        public IconAspectDefinition create(BindIcon bindIcon) {
            return new IconAspectDefinition(bindIcon.iconType(), getValueConsiderDefault(bindIcon));
        }

        @CheckForNull
        private VaadinIcon getValueConsiderDefault(BindIcon bindIcon) {
            return bindIcon.iconType() == IconType.AUTO && VaadinIcon.NATIVE_BUTTON.equals(bindIcon.value())
                    ? null
                    : bindIcon.value();
        }

    }

}
