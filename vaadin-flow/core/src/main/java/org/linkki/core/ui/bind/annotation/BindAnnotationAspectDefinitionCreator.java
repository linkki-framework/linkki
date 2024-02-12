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

package org.linkki.core.ui.bind.annotation;

import static org.linkki.core.binding.descriptor.aspect.base.ApplicableAspectDefinition.ifApplicable;
import static org.linkki.core.binding.descriptor.aspect.base.ApplicableTypeAspectDefinition.ifComponentTypeIs;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.ui.aspects.BindLabelValueAspectDefinition;
import org.linkki.core.ui.aspects.ButtonInvokeAspectDefinition;
import org.linkki.core.ui.aspects.DerivedReadOnlyAspectDefinition;
import org.linkki.core.ui.aspects.GenericAvailableValuesAspectDefinition;
import org.linkki.core.ui.aspects.RequiredAspectDefinition;
import org.linkki.core.ui.aspects.ValueAspectDefinition;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.data.provider.HasListDataView;
import com.vaadin.flow.data.selection.MultiSelect;

public class BindAnnotationAspectDefinitionCreator implements AspectDefinitionCreator<Bind> {

    @Override
    public LinkkiAspectDefinition create(Bind annotation) {
        EnabledAspectDefinition enabledTypeAspectDefinition = new EnabledAspectDefinition(annotation.enabled());
        return new CompositeAspectDefinition(
                ifComponentTypeIs(HasListDataView.class,
                                  new GenericAvailableValuesAspectDefinition(annotation.availableValues())),
                enabledTypeAspectDefinition,
                new RequiredAspectDefinition(annotation.required(), enabledTypeAspectDefinition),
                new VisibleAspectDefinition(annotation.visible()),
                ifApplicable(w -> w.getComponent() instanceof HasValue && !(w.getComponent() instanceof MultiSelect),
                             new ValueAspectDefinition()),
                ifComponentTypeIs(AbstractField.class, new DerivedReadOnlyAspectDefinition()),
                ifComponentTypeIs(NativeLabel.class, new BindLabelValueAspectDefinition()),
                ifComponentTypeIs(H2.class, new BindLabelValueAspectDefinition()),
                ifComponentTypeIs(Button.class, new ButtonInvokeAspectDefinition()));
    }

}