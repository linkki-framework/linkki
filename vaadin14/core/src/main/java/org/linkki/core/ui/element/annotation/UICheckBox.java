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
package org.linkki.core.ui.element.annotation;

import static org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition.DERIVED_BY_LINKKI;
import static org.linkki.core.defaults.ui.aspects.types.EnabledType.ENABLED;
import static org.linkki.core.defaults.ui.aspects.types.RequiredType.NOT_REQUIRED;
import static org.linkki.core.defaults.ui.aspects.types.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator.ModelBindingBoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.CaptionAspectDefinition;
import org.linkki.core.ui.aspects.DerivedReadOnlyAspectDefinition;
import org.linkki.core.ui.aspects.LabelAspectDefinition;
import org.linkki.core.ui.aspects.RequiredAspectDefinition;
import org.linkki.core.ui.aspects.ValueAspectDefinition;
import org.linkki.core.ui.element.annotation.UICheckBox.CheckBoxAspectCreator;
import org.linkki.core.ui.element.annotation.UICheckBox.CheckBoxComponentDefinitionCreator;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.core.vaadin.component.ComponentFactory;

import com.vaadin.flow.component.checkbox.Checkbox;

/**
 * In accordance to {@link Checkbox}, bound to a boolean property.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBoundProperty(ModelBindingBoundPropertyCreator.class)
@LinkkiComponent(CheckBoxComponentDefinitionCreator.class)
@LinkkiAspect(CheckBoxAspectCreator.class)
@LinkkiPositioned
public @interface UICheckBox {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed. */
    @LinkkiPositioned.Position
    int position();

    /**
     * Provides a label on the left side of the check box.
     * <p>
     * Normally a check box does not have a label on the left side but a {@link #caption()} which rests
     * on the right side of the box.
     */
    String label() default "";

    /**
     * Provides a caption on the right side of the check box.
     * <p>
     * Use an empty String as caption if no caption is needed.
     * <p>
     * For a label on the left, use {@link #label()} for the label text.
     * <p>
     * If the check box is used inside a {@link UITableColumn}, the {@link #label()} will be displayed
     * in the column header while the {@link #caption()} will be displayed inside the table cell
     * together with the check box.
     */
    String caption() default DERIVED_BY_LINKKI;

    /** Defines if an UI-Component is editable, using values of {@link EnabledType}. */
    EnabledType enabled() default ENABLED;

    /** Marks mandatory fields visually */
    RequiredType required() default NOT_REQUIRED;

    /**
     * Specifies if a component is shown, using values of {@link VisibleType}
     */
    VisibleType visible() default VISIBLE;

    /**
     * The name of the model object that is to be bound if multiple model objects are included for model
     * binding.
     */
    @LinkkiBoundProperty.ModelObject
    String modelObject() default ModelObject.DEFAULT_NAME;

    /**
     * The name of a property in the class of the bound {@link ModelObject} to use model binding.
     */
    @LinkkiBoundProperty.ModelAttribute
    String modelAttribute() default "";

    /**
     * Aspect definition creator for the {@link UICheckBox} annotation.
     */
    static class CheckBoxAspectCreator implements AspectDefinitionCreator<UICheckBox> {

        @Override
        public LinkkiAspectDefinition create(UICheckBox annotation) {
            EnabledAspectDefinition enabledAspectDefinition = new EnabledAspectDefinition(annotation.enabled());
            RequiredAspectDefinition requiredAspectDefinition = new RequiredAspectDefinition(
                    annotation.required(),
                    enabledAspectDefinition);

            return new CompositeAspectDefinition(new LabelAspectDefinition(annotation.label()),
                    enabledAspectDefinition,
                    requiredAspectDefinition,
                    new CaptionAspectDefinition(CaptionType.STATIC, annotation.caption()),
                    new VisibleAspectDefinition(annotation.visible()),
                    new ValueAspectDefinition(),
                    new DerivedReadOnlyAspectDefinition());
        }

    }

    static class CheckBoxComponentDefinitionCreator implements ComponentDefinitionCreator<UICheckBox> {

        @Override
        public LinkkiComponentDefinition create(UICheckBox annotation, AnnotatedElement annotatedElement) {
            return pmo -> ComponentFactory.newCheckbox();
        }

    }
}
