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
package org.linkki.core.ui.section.annotations.adapters;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.ui.section.annotations.ToolTipType;
import org.linkki.core.ui.section.annotations.UIToolTip;
import org.linkki.core.ui.section.annotations.UIToolTipDefinition;

/**
 * The adapter to provide access to an {@link UIToolTip} annotation through the definition
 * interface.
 */
public class UIToolTipAdapter implements UIToolTipDefinition {

    @Nullable
    private final UIToolTip toolTipAnnotation;

    /**
     * @param annotation the annotation or <code>null</code> if no {@link UIToolTip} is present
     */
    public UIToolTipAdapter(@Nullable UIToolTip annotation) {
        this.toolTipAnnotation = annotation;
    }

    @Override
    @CheckForNull
    public ToolTipType toolTipType() {
        return toolTipAnnotation != null ? toolTipAnnotation.toolTipType() : null;
    }

    @Override
    public String text() {
        return toolTipAnnotation != null ? toolTipAnnotation.text() : StringUtils.EMPTY;
    }
}
