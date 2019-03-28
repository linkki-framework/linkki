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

package org.linkki.core.binding.annotations.aspect;

import static org.linkki.core.binding.aspect.definition.ApplicableAspectDefinition.ifApplicable;
import static org.linkki.core.binding.aspect.definition.ApplicableTypeAspectDefinition.ifComponentTypeIs;

import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.binding.aspect.AspectDefinitionCreator;
import org.linkki.core.binding.aspect.definition.CompositeAspectDefinition;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.ui.section.annotations.aspect.ButtonInvokeAspectDefinition;
import org.linkki.core.ui.section.annotations.aspect.DerivedReadOnlyAspectDefinition;
import org.linkki.core.ui.section.annotations.aspect.EnabledAspectDefinition;
import org.linkki.core.ui.section.annotations.aspect.HasItemsAvailableValuesAspectDefinition;
import org.linkki.core.ui.section.annotations.aspect.LabelValueAspectDefinition;
import org.linkki.core.ui.section.annotations.aspect.RequiredAspectDefinition;
import org.linkki.core.ui.section.annotations.aspect.ValueAspectDefinition;
import org.linkki.core.ui.section.annotations.aspect.VisibleAspectDefinition;

import com.vaadin.data.HasItems;
import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.MultiSelect;

public class BindAnnotationAspectDefinitionCreator implements AspectDefinitionCreator<Bind> {

    @Override
    public LinkkiAspectDefinition create(Bind annotation) {
        EnabledAspectDefinition enabledTypeAspectDefinition = new EnabledAspectDefinition(annotation.enabled());
        return new CompositeAspectDefinition(
                ifComponentTypeIs(HasItems.class,
                                  new HasItemsAvailableValuesAspectDefinition(annotation.availableValues())),
                enabledTypeAspectDefinition,
                new RequiredAspectDefinition(annotation.required(), enabledTypeAspectDefinition),
                new VisibleAspectDefinition(annotation.visible()),
                ifApplicable(w -> w.getComponent() instanceof HasValue && !(w.getComponent() instanceof MultiSelect),
                             new ValueAspectDefinition()),
                ifComponentTypeIs(AbstractField.class, new DerivedReadOnlyAspectDefinition()),
                ifComponentTypeIs(Label.class, new LabelValueAspectDefinition()),
                ifComponentTypeIs(Button.class, new ButtonInvokeAspectDefinition()));
    }

}