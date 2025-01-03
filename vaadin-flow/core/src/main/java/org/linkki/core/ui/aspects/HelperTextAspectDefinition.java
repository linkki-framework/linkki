package org.linkki.core.ui.aspects;

import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.aspects.annotation.BindHelperText;
import org.linkki.core.vaadin.component.base.LinkkiText;

import com.vaadin.flow.component.HasHelper;
import com.vaadin.flow.component.HasTheme;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBoxVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextAreaVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import edu.umd.cs.findbugs.annotations.NonNull;

public class HelperTextAspectDefinition extends ModelToUiAspectDefinition<String> {

    private static final String NAME = "helperText";

    private final BindHelperText annotation;

    public HelperTextAspectDefinition(BindHelperText annotation) {
        this.annotation = annotation;
    }

    private void setThemeVariant(@NonNull HasTheme component) {
        if (ComboBox.class.isAssignableFrom(component.getClass())) {
            ((ComboBox<?>)component).addThemeVariants(ComboBoxVariant.LUMO_HELPER_ABOVE_FIELD);
        } else if (MultiSelectComboBox.class.isAssignableFrom(component.getClass())) {
            ((MultiSelectComboBox<?>)component).addThemeVariants(MultiSelectComboBoxVariant.LUMO_HELPER_ABOVE_FIELD);
        } else if (TextArea.class.isAssignableFrom(component.getClass())) {
            ((TextArea)component).addThemeVariants(TextAreaVariant.LUMO_HELPER_ABOVE_FIELD);
        } else if (TextField.class.isAssignableFrom(component.getClass())) {
            ((TextField)component).addThemeVariants(TextFieldVariant.LUMO_HELPER_ABOVE_FIELD);
        } else {
            component.setThemeName("helper-above-field", true);
        }
    }

    @Override
    public Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
        final var helper = extractHelper(componentWrapper);

        return getStringConsumer(helper);
    }

    @NonNull
    private HasHelper extractHelper(ComponentWrapper componentWrapper) {
        if (!(componentWrapper.getComponent() instanceof HasHelper)) {
            throw new IllegalArgumentException("Component " + componentWrapper.getComponent().getClass().getSimpleName() + //
                    " does not implement HasHelper");
        }

        return (HasHelper)componentWrapper.getComponent();
    }

    @NonNull
    private Consumer<String> getStringConsumer(HasHelper hasHelper) {
        return text -> {
            if (annotation.placeAboveElement() && hasHelper instanceof HasTheme) {
                setThemeVariant((HasTheme)hasHelper);
            }

            if (annotation.htmlContent()) {
                final var component = Optional.of(hasHelper)
                        .map(HasHelper::getHelperComponent)
                        .filter(LinkkiText.class::isInstance)
                        .map(LinkkiText.class::cast)
                        .orElseGet(() -> createHelperComponent(hasHelper));

                setIcon(component);
                setInnerHtml(text, component);
            } else {
                hasHelper.setHelperText(text);
            }
        };
    }

    private void setIcon(LinkkiText component) {
        if (annotation.showIcon()) {
            component.setIcon(annotation.icon());
            component.setIconPosition(annotation.iconPosition());
        } else {
            component.setIcon(null);
        }
    }

    private void setInnerHtml(String text, LinkkiText element) {
        element.setText(text, true);
    }

    private LinkkiText createHelperComponent(HasHelper hasHelper) {
        final var component = new LinkkiText();

        hasHelper.setHelperComponent(component);

        return component;
    }

    @Override
    public Aspect<String> createAspect() {
        return switch (annotation.helperTextType()) {
            case AUTO -> annotation.value() == null || StringUtils.isEmpty(annotation.value()) ?
                    Aspect.of(NAME) :
                    Aspect.of(NAME, annotation.value());
            case STATIC -> Aspect.of(NAME, annotation.value());
            case DYNAMIC -> Aspect.of(NAME);
        };
    }
}