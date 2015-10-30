package de.faktorzehn.ipm.web.binding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.binding.annotations.Bind;
import de.faktorzehn.ipm.web.binding.annotations.BindContext;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;
import de.faktorzehn.ipm.web.ui.section.PmoBasedSectionFactory;
import de.faktorzehn.ipm.web.ui.section.annotations.NoModelClassProvided;
import de.faktorzehn.ipm.web.ui.util.UiUtil;

public class BindingUtils {

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

    public static final String getTextForSelectItem(Object value) {
        return getName(value) + " [" + getId(value) + "]";
    }

    /**
     * Gets the model object from the PMO. If a model class is provided the PMO's method
     * <tt>getModelObject(Class modelClass)</tt> is invoked, otherwise <tt>getModelObject()</tt> is
     * invoked.
     */
    public static final Object getModelObjectFromPmo(PresentationModelObject pmo, Class<?> modelClass) {
        Method method = getMethodToGetTheModelObject(pmo, modelClass);
        return invokeMethodToGetTheModelObject(pmo, method, modelClass);
    }

    private static final Method getMethodToGetTheModelObject(PresentationModelObject pmo, Class<?> modelClass) {
        Method method;
        try {
            if (modelClass == null || modelClass.equals(NoModelClassProvided.class)) {
                method = pmo.getClass().getMethod("getModelObject");
            } else {
                method = pmo.getClass().getMethod("getModelObject", Class.class);
            }
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(
                    "PMO " + pmo.getClass() + " hasn't got a method to acess the model object.");
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        }
        return method;
    }

    private static final Object invokeMethodToGetTheModelObject(PresentationModelObject pmo,
            Method method,
            Class<?> modelClass) {
        try {
            if (modelClass == null || modelClass.equals(NoModelClassProvided.class)) {
                return method.invoke(pmo);
            } else {
                return method.invoke(pmo, modelClass);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalStateException("Error getting model object from PMO " + pmo.getClass(), e);
        }
    }

    private static final String getId(Object value) {
        return getPropertyValue(value, "getId");
    }

    private static final String getName(Object value) {
        return getPropertyValue(value, "getName");
    }

    private static final String getPropertyValue(Object value, String methodName) {
        Method method;
        try {
            method = value.getClass().getDeclaredMethod(methodName);
            return (String)method.invoke(value);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new IllegalStateException("Can't get value from method " + methodName + ", value was " + value);
        }
    }

    private BindingUtils() {
    }

}
