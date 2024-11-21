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

import static org.linkki.core.defaults.ui.aspects.types.VisibleType.VISIBLE;
import static org.linkki.core.defaults.ui.element.ItemCaptionProvider.instantiate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider.DefaultCaptionProvider;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.IconPositionAspectDefinition;
import org.linkki.core.ui.aspects.LabelAspectDefinition;
import org.linkki.core.ui.aspects.LabelValueAspectDefinition;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.aspects.types.IconPosition;
import org.linkki.core.ui.converters.LinkkiConverterRegistry;
import org.linkki.core.ui.element.annotation.UILabel.LabelAspectDefinitionCreator;
import org.linkki.core.ui.element.annotation.UILabel.LabelComponentDefinitionCreator;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.core.uiframework.UiFramework;
import org.linkki.core.util.HtmlSanitizer;
import org.linkki.core.vaadin.component.base.LinkkiText;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

/**
 * Provides a single UI-element to display text content. Creates a
 * {@link com.vaadin.flow.component.html.Div}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBoundProperty
@LinkkiComponent(LabelComponentDefinitionCreator.class)
@LinkkiAspect(LabelAspectDefinitionCreator.class)
@LinkkiPositioned
public @interface UILabel {

    /**
     * Mandatory attribute that defines the order in which UI-Elements are displayed
     */
    @LinkkiPositioned.Position
    int position();

    /**
     * Provides a description label next to the UI element
     * <p>
     * The default value is an empty string ({@code ""}). This means that internationalization
     * strings will not be automatically used. If an internationalization string should be used, the
     * value must be set to {@code LinkkiAspectDefinition.DERIVED_BY_LINKKI}.
     */
    String label() default "";

    /**
     * Specifies if a component is shown, using values of {@link VisibleType}
     */
    VisibleType visible() default VISIBLE;

    /**
     * Specifies the {@link IconPosition position} of the icon, whether it is displayed on the left
     * or on the right side of the label.
     */
    IconPosition iconPosition() default IconPosition.LEFT;

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
     * Defines a list of CSS class names that are added to the created UI component.
     */
    String[] styleNames() default {};

    /**
     * When set to {@code true}, the label's content will be displayed as HTML, otherwise as plain
     * text. The HTML content is automatically {@link HtmlSanitizer#sanitizeText(String) sanitized}.
     * <br>
     * Note that <b>user-supplied strings have to be {@link HtmlSanitizer#escapeText(String)
     * escaped}</b> when including them in the HTML content. Otherwise, they will also be
     * interpreted as HTML.
     * <p>
     * HTML content is not compatible with some annotations that manipulate the resulting component,
     * like {@link BindIcon}.
     * 
     * @deprecated Use {@link org.linkki.core.util.HtmlContent} as return type instead.
     */
    @Deprecated(since = "2.5.0")
    boolean htmlContent() default false;

    /**
     * Specifies which {@link ItemCaptionProvider} should be used to convert the value into a
     * String.
     * <p>
     * For enum values, getName method is used if the enum class provides such a method.
     *
     * @see DefaultCaptionProvider
     */
    Class<? extends ItemCaptionProvider<?>> itemCaptionProvider() default DefaultLabelCaptionProvider.class;

    static class DefaultLabelCaptionProvider implements ItemCaptionProvider<Object> {

        @Override
        public String getCaption(Object o) {
            if (o != null) {
                try {
                    Converter<String, Object> converter = LinkkiConverterRegistry.getCurrent()
                            .findConverter(String.class,
                                           o.getClass());
                    return converter.convertToPresentation(o,
                                                           new ValueContext(new Binder<>(), UiFramework.getLocale()));
                } catch (IllegalArgumentException e) {
                    // no converter
                    return new DefaultCaptionProvider().getCaption(o);
                }
            } else {
                return StringUtils.EMPTY;
            }
        }
    }

    /**
     * Aspect definition creator for the {@link UILabel} annotation.
     */
    static class LabelAspectDefinitionCreator implements AspectDefinitionCreator<UILabel> {

        @SuppressWarnings("deprecation")
        @Override
        public LinkkiAspectDefinition create(UILabel annotation) {
            return new CompositeAspectDefinition(
                    new LabelAspectDefinition(annotation.label()),
                    new VisibleAspectDefinition(annotation.visible()),
                    new LabelValueAspectDefinition(annotation.htmlContent(),
                            instantiate(annotation::itemCaptionProvider)),
                    new IconPositionAspectDefinition(annotation.iconPosition()));
        }
    }

    static class LabelComponentDefinitionCreator implements ComponentDefinitionCreator<UILabel> {

        @Override
        public LinkkiComponentDefinition create(UILabel annotation, AnnotatedElement annotatedElement) {
            return pmo -> {
                LinkkiText label = new LinkkiText();
                for (String styleName : annotation.styleNames()) {
                    label.addClassName(styleName);
                }
                return label;
            };
        }

    }

}
