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

import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.BindingDefinition;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.VisibleType;

public class BindAnnotationAdapter implements BindingDefinition {

    private final Bind bindAnnotation;

    public BindAnnotationAdapter(Bind annotation) {
        super();
        this.bindAnnotation = annotation;
    }

    @Override
    public EnabledType enabled() {
        return bindAnnotation.enabled();
    }

    @Override
    public VisibleType visible() {
        return bindAnnotation.visible();
    }

    @Override
    public RequiredType required() {
        return bindAnnotation.required();
    }

    @Override
    public AvailableValuesType availableValues() {
        return bindAnnotation.availableValues();
    }

    public String getPmoPropertyName() {
        return bindAnnotation.pmoProperty();
    }

}
