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
import java.util.List;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition.BindingDefinitionBoundPropertyCreator;
import org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.nls.NlsText;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.AvailableValuesAspectDefinition;
import org.linkki.core.ui.element.annotation.UIYesNoComboBox.YesNoComboBoxAvailableValuesAspectCreator;
import org.linkki.core.ui.element.bindingdefinitions.YesNoComboBoxBindingDefinition;
import org.linkki.core.uicreation.BindingDefinitionComponentDefinition;
import org.linkki.core.uicreation.LinkkiPositioned;

import com.vaadin.ui.ComboBox;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * A combo box for boolean or Boolean values.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBindingDefinition(YesNoComboBoxBindingDefinition.class)
@LinkkiBoundProperty(BindingDefinitionBoundPropertyCreator.class)
@LinkkiComponent(BindingDefinitionComponentDefinition.Creator.class)
@LinkkiAspect(YesNoComboBoxAvailableValuesAspectCreator.class)
@LinkkiAspect(FieldAspectDefinitionCreator.class)
@LinkkiAspect(ValueAspectDefinitionCreator.class)
@LinkkiPositioned
public @interface UIYesNoComboBox {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed */
    @LinkkiPositioned.Position
    int position();

    /** Provides a description label next to the UI element */
    String label() default DERIVED_BY_LINKKI;

    /** Defines if an UI-Component is editable, using values of {@link EnabledType} */
    EnabledType enabled() default ENABLED;

    /** Marks mandatory fields visually */
    RequiredType required() default NOT_REQUIRED;

    /**
     * Specifies if a component is shown, using values of {@link VisibleType}
     */
    VisibleType visible() default VISIBLE;

    /**
     * Specifies the width of the field. Use CSS units like em, px or %.
     * <p>
     * For example: "25em" or "100%".
     */
    String width() default "-1px";

    /**
     * Name of the model object that is to be bound if multiple model objects are included for model
     * binding
     */
    String modelObject() default ModelObject.DEFAULT_NAME;

    /**
     * The name of a property in the class of the bound {@link ModelObject} to use model binding
     */
    String modelAttribute() default "";

    /**
     * Specifies which {@link ItemCaptionProvider} should be used to convert boolean values into String
     * captions.
     * <p>
     * Default value prints the boolean values in the system locale (for example "yes"/"no" in English
     * or "ja"/nein" in German).
     */
    Class<? extends ItemCaptionProvider<?>> itemCaptionProvider() default BooleanCaptionProvider.class;

    public class BooleanCaptionProvider implements ItemCaptionProvider<Object> {

        @Override
        @NonNull
        public String getCaption(Object o) {
            return (o instanceof Boolean) ? booleanToCaption((Boolean)o) : ""; //$NON-NLS-1$
        }

        private String booleanToCaption(Boolean bool) {
            return bool ? NlsText.getString("BooleanCaptionProvider.True") //$NON-NLS-1$
                    : NlsText.getString("BooleanCaptionProvider.False"); //$NON-NLS-1$
        }
    }

    class YesNoComboBoxAvailableValuesAspectCreator implements AspectDefinitionCreator<UIYesNoComboBox> {

        @Override
        public LinkkiAspectDefinition create(UIYesNoComboBox annotation) {
            return new AvailableValuesAspectDefinition<ComboBox<Object>>(AvailableValuesType.ENUM_VALUES_INCL_NULL,
                    ComboBox<Object>::setDataProvider) {

                @Override
                @SuppressWarnings("unchecked")
                protected void handleNullItems(ComponentWrapper componentWrapper, List<?> items) {
                    boolean hasNullItem = items.removeIf(i -> i == null);
                    ((ComboBox<Object>)componentWrapper.getComponent()).setEmptySelectionAllowed(hasNullItem);
                }

            };
        }

    }

}

