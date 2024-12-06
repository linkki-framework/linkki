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
package org.linkki.samples.playground.bugs.lin4104;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.ui.aspects.LabelAspectDefinition;
import org.linkki.core.ui.aspects.RequiredAspectDefinition;
import org.linkki.core.ui.element.annotation.ValueAspectDefinitionCreator;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;

@LinkkiBoundProperty(BoundPropertyCreator.SimpleMemberNameBoundPropertyCreator.class)
@LinkkiComponent(UITestCustomField.TestCustomFieldComponentDefinitionCreator.class)
@LinkkiAspect(ValueAspectDefinitionCreator.class)
@LinkkiAspect(UITestCustomField.TestCustomFieldAspectCreator.class)
@LinkkiPositioned
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UITestCustomField {

    @LinkkiPositioned.Position
    int position();

    class TestCustomFieldComponentDefinitionCreator
            implements ComponentDefinitionCreator<UITestCustomField> {
        @Override
        public LinkkiComponentDefinition create(UITestCustomField annotation,
                AnnotatedElement annotatedElement) {
            return pmo -> new TestCustomField();
        }
    }

    class TestCustomFieldAspectCreator implements AspectDefinitionCreator<UITestCustomField> {

        @Override
        public LinkkiAspectDefinition create(UITestCustomField annotation) {
            var enabledAspectDefinition = new EnabledAspectDefinition(EnabledType.ENABLED);
            return new CompositeAspectDefinition(
                    new LabelAspectDefinition("Label"),
                    enabledAspectDefinition,
                    new RequiredAspectDefinition(RequiredType.NOT_REQUIRED, enabledAspectDefinition));
        }
    }
}
