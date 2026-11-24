package org.linkki.core.ui.aspects.annotation;

import com.vaadin.flow.component.combobox.ComboBoxBase;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.util.handler.Handler;

/**
 * Sets the width of the popup of a {@link ComboBoxBase} to a custom value.
 * This is useful for combo boxes with long values that would otherwise be truncated.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@LinkkiAspect(BindPopupWidth.BindPopupWidthAspectDefinitionCreator.class)
public @interface BindPopupWidth {

  String popupWidth() default "20rem";

  class BindPopupWidthAspectDefinitionCreator implements AspectDefinitionCreator<BindPopupWidth> {

    @Override
    public LinkkiAspectDefinition create(BindPopupWidth annotation) {
      return new PopupWidthAspectDefinition(annotation.popupWidth());
    }
  }

  class PopupWidthAspectDefinition implements LinkkiAspectDefinition {

    private final String popupWidth;

    public PopupWidthAspectDefinition(String popupWidth) {
      this.popupWidth = popupWidth;
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
      if (componentWrapper.getComponent() instanceof ComboBoxBase<?, ?, ?> comboBox) {
        return () -> setLazyLoadingAndStyles(comboBox, popupWidth);
      }
      throw new UnsupportedOperationException("component for custom popup width is not a ComboBoxBase");
    }

    private void setLazyLoadingAndStyles(ComboBoxBase<?, ?, ?> comboBox, String width) {
      comboBox.getStyle().set(comboBox instanceof MultiSelectComboBox
          ? "--vaadin-multi-select-combo-box-overlay-width"
          : "--vaadin-combo-box-overlay-width", width);
    }
  }
}
