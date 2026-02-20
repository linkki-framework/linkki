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

package org.linkki.core.ui.element.annotation;

import static org.linkki.core.defaults.ui.element.ItemCaptionProvider.instantiate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.LabelAspectDefinition;
import org.linkki.core.ui.aspects.LabelValueAspectDefinition;
import org.linkki.core.ui.element.annotation.UIBadge.BadgeComponentDefinitionCreator;
import org.linkki.core.ui.element.annotation.UIBadge.TextValueAspectDefinitionCreator;
import org.linkki.core.ui.element.annotation.UILabel.DefaultLabelCaptionProvider;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.core.vaadin.component.BadgeSeverity;
import org.linkki.core.vaadin.component.ComponentFactory;

/**
 * A badge displaying information that is visually highlighted. The annotated method should return
 * the text to be displayed in the badge.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiComponent(BadgeComponentDefinitionCreator.class)
@LinkkiAspect(TextValueAspectDefinitionCreator.class)
@LinkkiBoundProperty
@LinkkiPositioned
public @interface UIBadge {

    /**
     * Mandatory attribute that defines the order in which UI-Elements are displayed.
     */
    @LinkkiPositioned.Position
    int position();

    /**
     * Provides a label on the left side of the badge.
     */
    String label() default "";

    /**
     * Severity of the badge.
     */
    BadgeSeverity severity() default BadgeSeverity.NONE;

    /**
     * Variants for the badge that define the styling.
     *
     * @see BadgeVariant
     */
    String[] variants() default { BadgeVariant.PILL };

    /**
     * Name of the model object that is to be bound if multiple model objects are included for model
     * binding
     */
    @LinkkiBoundProperty.ModelObjectProperty
    String modelObject() default ModelObject.DEFAULT_NAME;

    /**
     * The name of a property in the class of the bound {@link ModelObject} to use model binding
     */
    @LinkkiBoundProperty.ModelAttribute
    String modelAttribute() default "";

    /**
     * Caption provider that is used to convert the return value to string.
     */
    Class<? extends ItemCaptionProvider<?>> itemCaptionProvider() default DefaultLabelCaptionProvider.class;

    class BadgeComponentDefinitionCreator implements ComponentDefinitionCreator<UIBadge> {
        @Override
        public LinkkiComponentDefinition create(UIBadge annotation, AnnotatedElement annotatedElement) {
            return pmo -> ComponentFactory.newBadge(annotation.severity(), annotation.variants());
        }
    }

    class TextValueAspectDefinitionCreator implements AspectDefinitionCreator<UIBadge> {
        @Override
        public LinkkiAspectDefinition create(UIBadge annotation) {
            return new CompositeAspectDefinition(
                    new LabelValueAspectDefinition(instantiate(annotation::itemCaptionProvider)),
                    new LabelAspectDefinition(annotation.label()));
        }
    }
}
