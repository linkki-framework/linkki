package org.linkki.core.binding;

import java.util.Arrays;
import java.util.Objects;

import org.linkki.core.PresentationModelObject;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.binding.annotations.BindContext;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.section.PmoBasedSectionFactory;
import org.linkki.core.ui.util.UiUtil;

import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

public class BindingUtils {

    private BindingUtils() {
    }

    public static final boolean isPmo(Object objectToTest) {
        Objects.requireNonNull(objectToTest);
        return isPmo(objectToTest.getClass());
    }

    public static final boolean isPmo(Class<?> classToTest) {
        Objects.requireNonNull(classToTest);
        return PresentationModelObject.class.isAssignableFrom(classToTest);
    }

    /**
     * Creates the bindings for fields in the UI-Element to the properties of PMO.
     */
    public static void bind(BindingManager bindingManager, Object uiElement, PresentationModelObject pmo) {
        BindingContext ctx = getBindingContext(bindingManager, uiElement);
        bindFields(ctx, uiElement, pmo);
        ctx.updateUI();
    }

    private static BindingContext getBindingContext(BindingManager bindingManager, Object uiElement) {
        BindContext annotation = uiElement.getClass().getAnnotation(BindContext.class);
        if (annotation == null) {
            return null;
        }
        return bindingManager.getExistingContextOrStartNewOne(annotation.contextClass());
    }

    private static void bindFields(BindingContext ctx, Object uiElement, PresentationModelObject pmo) {
        java.lang.reflect.Field[] fields = uiElement.getClass().getDeclaredFields();
        for (java.lang.reflect.Field field : fields) {
            bindField(field, ctx, uiElement, pmo);
        }
    }

    /**
     * If the Java field is of type {@link Field} and annotation with {@link Bind}, it is bound to
     * the binding context, otherwise ignored.
     */
    private static void bindField(java.lang.reflect.Field javaField,
            BindingContext bindingContext,
            Object uiElement,
            PresentationModelObject pmo) {

        bindField(javaField, bindingContext, uiElement, null, pmo);
    }

    /**
     * If the Java field is of type {@link Field} and annotation with {@link Bind}, it is bound to
     * the binding context, otherwise ignored.
     */
    private static void bindField(java.lang.reflect.Field javaField,
            BindingContext bindingContext,
            Object uiElement,
            Label label,
            PresentationModelObject pmo) {

        if (!Field.class.isAssignableFrom(javaField.getType())) {
            return;
        }
        Field<?> vaadinField;
        try {
            javaField.setAccessible(true);
            vaadinField = (Field<?>)javaField.get(uiElement);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(
                    "Error getting Vaadin field from the Java field, " + javaField.getName() + " in " + uiElement, e);
        } finally {
            javaField.setAccessible(false);
        }
        Bind bindAnnotation = javaField.getAnnotation(Bind.class);
        if (bindAnnotation == null) {
            return;
        }
        FieldBinding.create(bindingContext, pmo, bindAnnotation.valueProperty(), label, vaadinField,
                            getDispatcher(pmo));
    }

    /*
     * FIXME Quick and dirty Fix. Sections mit alter @Bind Annotation müssen entfernt werden.
     * Stattdessen erhalten die zugehörigen PMOs die neuen @UITextField Annotations. see FIPM-58
     */
    private static PropertyDispatcher getDispatcher(PresentationModelObject pmo) {
        return PmoBasedSectionFactory.createDefaultDispatcher(pmo);
    }

    public static final void fillSelectWithItems(AbstractSelect select, Object[] values) {
        UiUtil.fillSelectWithItems(select, Arrays.asList(values));
    }

}
