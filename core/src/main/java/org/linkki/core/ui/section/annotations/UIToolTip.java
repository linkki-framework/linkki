/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.core.ui.section.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.aspect.LinkkiAspect;

/**
 * Shows a tooltip next to a UI-Element. The annotation can be added to the method the UI-Element is
 * bound.
 * <p>
 * 
 * @deprecated Since October 4th, 2018. Use {@link BindTooltip} instead. This annotation will be
 *             removed in the next release.
 */
@Deprecated
@SuppressWarnings("deprecation")
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
@LinkkiAspect(org.linkki.core.ui.section.annotations.aspect.ToolTipAspectDefinition.class)
public @interface UIToolTip {

    /** The displayed text for {@link org.linkki.core.ui.section.annotations.ToolTipType#STATIC} */
    @SuppressWarnings("javadoc")
    String text() default StringUtils.EMPTY;

    /**
     * Defines how the tooltip text should be retrieved
     * <p>
     * 
     * @deprecated Since October 4th, 2018. This annotation will be removed in the next release.
     */
    @Deprecated
    ToolTipType toolTipType() default ToolTipType.STATIC;
}
