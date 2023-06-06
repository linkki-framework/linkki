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

import static java.util.function.Predicate.not;
import static org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition.DERIVED_BY_LINKKI;
import static org.linkki.core.defaults.ui.aspects.types.EnabledType.ENABLED;
import static org.linkki.core.defaults.ui.aspects.types.RequiredType.NOT_REQUIRED;
import static org.linkki.core.defaults.ui.aspects.types.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.base.StaticModelToUiAspectDefinition;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider.DefaultCaptionProvider;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.AvailableValuesAspectDefinition;
import org.linkki.core.ui.aspects.BindComboBoxItemStyleAspectDefinition;
import org.linkki.core.ui.aspects.DerivedReadOnlyAspectDefinition;
import org.linkki.core.ui.aspects.LabelAspectDefinition;
import org.linkki.core.ui.aspects.RequiredAspectDefinition;
import org.linkki.core.ui.aspects.ValueAspectDefinition;
import org.linkki.core.ui.aspects.types.TextAlignment;
import org.linkki.core.ui.element.annotation.UIComboBox.ComboBoxAspectCreator;
import org.linkki.core.ui.element.annotation.UIComboBox.ComboBoxComponentDefinitionCreator;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.core.vaadin.component.ComponentFactory;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

/**
 * Creates a ComboBox with the specified parameters.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBoundProperty
@LinkkiComponent(ComboBoxComponentDefinitionCreator.class)
@LinkkiAspect(ComboBoxAspectCreator.class)
@LinkkiPositioned
public @interface UIComboBox {

    /**
     * Mandatory attribute that defines the order in which UI-Elements are displayed
     */
    @LinkkiPositioned.Position
    int position();

    /**
     * Provides a description label next to the UI element
     */
    String label() default DERIVED_BY_LINKKI;

    /**
     * Specifies the source of the available values, the content of the combo box.
     *
     * @see AvailableValuesType
     */
    AvailableValuesType content() default AvailableValuesType.ENUM_VALUES_INCL_NULL;

    /**
     * Defines if an UI-Component is editable, using values of {@link EnabledType}
     */
    EnabledType enabled() default ENABLED;

    /**
     * Marks mandatory fields visually
     */
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
    @LinkkiBoundProperty.ModelObjectProperty
    String modelObject() default ModelObject.DEFAULT_NAME;

    /**
     * The name of a property in the class of the bound {@link ModelObject} to use model binding
     */
    @LinkkiBoundProperty.ModelAttribute
    String modelAttribute() default "";

    /**
     * Specifies which {@link ItemCaptionProvider} should be used to convert {@link #content()} into
     * String captions.
     * <p>
     * Default value assumes that the value class has a method "getName" and uses this method for the
     * String representation.
     */
    Class<? extends ItemCaptionProvider<?>> itemCaptionProvider() default DefaultCaptionProvider.class;

    /**
     * The alignment of the text. Use {@link TextAlignment#RIGHT} for numeric value.
     * <p>
     * Text alignment is set for both the selected value and the values in the drop down list. When used
     * in combination with {@link BindComboBoxItemStyleAspectDefinition} the style for the drop down
     * list is overwritten by the bind annotation. Therefore it may be necessary to set the style class
     * "text-left", "text-center" or "text-right" manually.
     */
    TextAlignment textAlign() default TextAlignment.DEFAULT;

    /**
     * Aspect definition creator for the {@link UIComboBox} annotation.
     */
    class ComboBoxAspectCreator implements AspectDefinitionCreator<UIComboBox> {

        @Override
        public LinkkiAspectDefinition create(UIComboBox annotation) {
            var availableValuesAspectDefinition = new ComboBoxAvailableValuesAspectDefinition(
                    annotation.content(), ComboBox::setItems, annotation);

            EnabledAspectDefinition enabledAspectDefinition = new EnabledAspectDefinition(annotation.enabled());
            RequiredAspectDefinition requiredAspectDefinition = new RequiredAspectDefinition(
                    annotation.required(),
                    enabledAspectDefinition);

            return new CompositeAspectDefinition(new LabelAspectDefinition(annotation.label()),
                    enabledAspectDefinition,
                    requiredAspectDefinition,
                    availableValuesAspectDefinition,
                    new VisibleAspectDefinition(annotation.visible()),
                    new ComboBoxValueAspectDefinition(),
                    new DerivedReadOnlyAspectDefinition(),
                    new TextAlignAspectDefinition(annotation.textAlign()));
        }

        private static final class ComboBoxAvailableValuesAspectDefinition
                extends AvailableValuesAspectDefinition<ComboBox<Object>> {
            private final UIComboBox annotation;

            private ComboBoxAvailableValuesAspectDefinition(AvailableValuesType availableValuesType,
                    BiConsumer<ComboBox<Object>, List<Object>> dataProviderSetter,
                    UIComboBox annotation) {
                super(availableValuesType, dataProviderSetter);
                this.annotation = annotation;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void handleNullItems(ComponentWrapper componentWrapper, List<?> items) {
                boolean dynamicItemsEmpty = annotation.content() == AvailableValuesType.DYNAMIC && items.isEmpty();
                boolean hasNullItem = items.removeIf(Objects::isNull);
                ((ComboBox<Object>)componentWrapper.getComponent())
                        .setClearButtonVisible(hasNullItem || dynamicItemsEmpty);
            }
        }

        private static final class ComboBoxValueAspectDefinition extends ValueAspectDefinition {

            @Override
            protected Converter<?, ?> getConverter(Type presentationType, Type modelType) {
                return new ComboBoxValueConverter(super.getConverter(presentationType, modelType));
            }

            /**
             * Do not set any warning message as it should be expected that only valid values can be
             * selected.
             */
            @Override
            protected MessageList getInvalidInputMessage(Object value) {
                return new MessageList();
            }
        }

        private static final class ComboBoxValueConverter implements Converter<Object, Object> {

            private static final long serialVersionUID = -7386708403118050830L;

            private final Converter<Object, Object> wrappedConverter;

            @SuppressWarnings("unchecked")
            public ComboBoxValueConverter(Converter<?, ?> wrappedConverter) {
                this.wrappedConverter = (Converter<Object, Object>)wrappedConverter;
            }

            @SuppressWarnings("unchecked")
            @Override
            public Result<Object> convertToModel(Object value, ValueContext context) {
                if (value == null && !((ComboBox<Object>)context.getComponent().get()).isClearButtonVisible()) {
                    return Result.error("Null is not an available value");
                } else {
                    return wrappedConverter.convertToModel(value, context);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            public Object convertToPresentation(Object value, ValueContext context) {
                return context.getComponent()
                        .map(component -> ((ComboBox<Object>)component).getItemLabelGenerator().apply(value))
                        .filter(not(String::isEmpty))
                        .map(s -> wrappedConverter.convertToPresentation(value, context))
                        .orElse(null);
            }
        }

        private static final class TextAlignAspectDefinition extends StaticModelToUiAspectDefinition<TextAlignment> {

            public static final String NAME = "textAlignment";
            private final TextAlignment textAlignment;

            public TextAlignAspectDefinition(TextAlignment textAlignment) {
                this.textAlignment = textAlignment;
            }

            @Override
            public Aspect<TextAlignment> createAspect() {
                return Aspect.of(NAME, textAlignment);
            }

            @Override
            public Consumer<TextAlignment> createComponentValueSetter(ComponentWrapper componentWrapper) {
                return ta -> setTextAlign(componentWrapper, ta);
            }

            private void setTextAlign(ComponentWrapper componentWrapper, TextAlignment alignment) {
                if (alignment != TextAlignment.DEFAULT) {
                    String style = getStyle(alignment);
                    new BindComboBoxItemStyleAspectDefinition(getStyle(alignment))
                            .createComponentValueSetter(componentWrapper)
                            .accept($ -> style);
                    ComboBox<?> comboBox = (ComboBox<?>)componentWrapper.getComponent();
                    comboBox.addThemeVariants(getVariant(alignment));
                }
            }

            private ComboBoxVariant getVariant(TextAlignment alignment) {
                switch (alignment) {
                    // Vaadin names the variants LEFT/CENTER/RIGHT but uses css value start/center/end
                    case LEFT:
                        return ComboBoxVariant.LUMO_ALIGN_LEFT;
                    case CENTER:
                        return ComboBoxVariant.LUMO_ALIGN_CENTER;
                    case RIGHT:
                        return ComboBoxVariant.LUMO_ALIGN_RIGHT;
                    default:
                        throw new IllegalArgumentException("Invalid text alignment: " + alignment.name());
                }
            }

            private String getStyle(TextAlignment alignment) {
                switch (alignment) {
                    case LEFT:
                        return "text-left";
                    case CENTER:
                        return "text-center";
                    case RIGHT:
                        return "text-right";
                    default:
                        throw new IllegalArgumentException("Invalid text alignment: " + alignment.name());
                }
            }
        }
    }

    class ComboBoxComponentDefinitionCreator implements ComponentDefinitionCreator<UIComboBox> {

        @Override
        public LinkkiComponentDefinition create(UIComboBox annotation, AnnotatedElement annotatedElement) {
            return pmo -> {
                ComboBox<?> comboBox = ComponentFactory.newComboBox();
                comboBox.setItemLabelGenerator(ItemCaptionProvider
                        .instantiate(annotation::itemCaptionProvider)::getUnsafeCaption);
                comboBox.setWidth(annotation.width());
                return comboBox;
            };
        }
    }
}
