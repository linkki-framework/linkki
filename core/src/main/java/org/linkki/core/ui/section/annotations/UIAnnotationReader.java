package org.linkki.core.ui.section.annotations;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.ui.section.annotations.adapters.UIToolTipAdapter;
import org.linkki.util.BeanUtils;

/**
 * Reads UIField annotations, e.g. {@link UITextField}, {@link UIComboBox}, etc. from a given
 * object's class.
 * <p>
 * Provides a set of {@link ElementDescriptor} for all found properties via {@link #getUiElements()}
 *
 * @author widmaier
 */
public class UIAnnotationReader {

    private static final PositionComparator POSITION_COMPARATOR = new PositionComparator();

    private final Class<?> annotatedClass;
    private final Set<ElementDescriptor> descriptors;
    private final Map<ElementDescriptor, TableColumnDescriptor> columnDescriptors;

    public UIAnnotationReader(Class<?> annotatedClass) {
        this.annotatedClass = requireNonNull(annotatedClass, "annotatedClass must not be null");
        descriptors = new HashSet<>();
        columnDescriptors = new HashMap<>();
        initDescriptorMaps();
    }

    public Set<ElementDescriptor> getUiElements() {
        TreeSet<ElementDescriptor> treeSet = new TreeSet<ElementDescriptor>(POSITION_COMPARATOR);
        treeSet.addAll(descriptors);
        return treeSet;
    }

    public TableColumnDescriptor getTableColumnDescriptor(ElementDescriptor d) {
        return columnDescriptors.get(d);
    }

    @SuppressWarnings("null")
    private void initDescriptorMaps() {
        descriptors.clear();
        Method[] methods = annotatedClass.getMethods();
        for (Method method : methods) {
            if (isUiDefiningMethod(method)) {
                UIElementDefinition uiElement = getUiElement(method);
                UIToolTipDefinition toolTipDefinition = getUIToolTipDefinition(method);
                ElementDescriptor descriptor = addDescriptor(uiElement, toolTipDefinition, method);

                UITableColumn columnAnnotation = method.getAnnotation(UITableColumn.class);
                if (columnAnnotation != null) {
                    columnDescriptors.put(descriptor,
                                          new TableColumnDescriptor(annotatedClass, method, columnAnnotation));
                }
            }
        }
    }

    private UIToolTipDefinition getUIToolTipDefinition(Method method) {
        UIToolTip toolTip = method.getAnnotation(UIToolTip.class);
        return new UIToolTipAdapter(toolTip);
    }

    private ElementDescriptor addDescriptor(UIElementDefinition uiElement,
            UIToolTipDefinition toolTipDefinition,
            Method method) {
        ElementDescriptor descriptor;
        if (uiElement instanceof UIFieldDefinition) {
            descriptor = new FieldDescriptor((UIFieldDefinition)uiElement, toolTipDefinition,
                    getPmoPropertyName(method));
        } else if (uiElement instanceof UIButtonDefinition) {
            descriptor = new ButtonDescriptor((UIButtonDefinition)uiElement, toolTipDefinition, method.getName());
        } else if (uiElement instanceof UILabelDefinition) {
            descriptor = new LabelDescriptor((UILabelDefinition)uiElement, toolTipDefinition,
                    getPmoPropertyName(method));
        } else {
            throw new IllegalStateException(
                    "Unknown UIElementDefinition of type " + uiElement + " on method " + method);
        }

        descriptors.add(descriptor);
        return descriptor;
    }

    private String getPmoPropertyName(Method method) {
        if (method.getName().startsWith("is")) {
            return StringUtils.uncapitalize(method.getName().substring(2));
        } else if (method.getName().startsWith("get")) {
            return StringUtils.uncapitalize(method.getName().substring(3));
        } else {
            return method.getName();
        }
    }

    private boolean isUiDefiningMethod(Method method) {
        return annotations(method).anyMatch(UIElementDefinition::isLinkkiBindingDefinition);
    }

    public UIElementDefinition getUiElement(Method method) {
        return annotations(method)
                .filter(UIElementDefinition::isLinkkiBindingDefinition)
                .map(UIElementDefinition::from)
                .findFirst().orElse(null);
    }

    private Stream<Annotation> annotations(Method m) {
        return Arrays.stream(m.getAnnotations());
    }

    /**
     * Currently for testing purposes only!
     *
     * @return the descriptor for the given property.
     * @throws NoSuchElementException if no descriptor with the given property can be found
     */
    public ElementDescriptor findDescriptor(String propertyName) {
        return getUiElements().stream().filter(el -> el.getPmoPropertyName().equals(propertyName)).findFirst().get();
    }

    public boolean hasTableColumnAnnotation(ElementDescriptor d) {
        return columnDescriptors.containsKey(d);
    }

    /**
     * Reads the given presentation model object's class to find a method annotated with
     * {@link ModelObject @ModelObject} and the annotation's {@link ModelObject#name()} matching the
     * given model object name. Returns a supplier that supplies a model object by invoking that
     * method.
     *
     * @param pmo a presentation model object
     * @param modelObjectName the name of the model object as provided by a method annotated with
     *            {@link ModelObject @ModelObject}
     * @return a supplier that supplies a model object by invoking the annotated method
     *
     * @throws ModelObjectAnnotationException if no matching method is found or the method has no
     *             return value
     */
    public static Supplier<?> getModelObjectSupplier(Object pmo, String modelObjectName) {
        requireNonNull(pmo, "pmo must not be null");
        requireNonNull(modelObjectName, "modelObjectName must not be null");
        Optional<Method> modelObjectMethod = BeanUtils
                .getMethod(requireNonNull(pmo, "pmo must not be null").getClass(),
                           (m) -> m.isAnnotationPresent(ModelObject.class)
                                   && m.getAnnotation(ModelObject.class).name().equals(modelObjectName));
        if (modelObjectMethod.isPresent()) {
            Method method = modelObjectMethod.get();
            if (Void.TYPE.equals(method.getReturnType())) {
                throw new ModelObjectAnnotationException(pmo, method);
            }
            return () -> {
                try {
                    return method.invoke(pmo);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };
        }
        if (ModelObject.DEFAULT_NAME.equals(modelObjectName)) {
            throw new ModelObjectAnnotationException(pmo);
        } else {
            throw new ModelObjectAnnotationException(pmo, modelObjectName);
        }
    }

    /**
     * Tests if the presentation model object has a method annotated with
     * {@link ModelObject @ModelObject} using a given name
     * 
     * @param pmo an object used for a presentation model
     * @param modelObjectName the name of the model object
     * @return whether the object has a method annotated with {@link ModelObject @ModelObject} using
     *         the given name
     */
    public static boolean hasModelObjectAnnotatedMethod(Object pmo, @Nullable String modelObjectName) {
        return BeanUtils.getMethod(requireNonNull(pmo, "pmo must not be null").getClass(),
                                   (m) -> m.isAnnotationPresent(ModelObject.class)
                                           && m.getAnnotation(ModelObject.class).name().equals(modelObjectName))
                .isPresent();
    }

    /**
     * Thrown when trying to get a method annotated with {@link ModelObject @ModelObject} via
     * {@link UIAnnotationReader#getModelObjectSupplier(Object, String)} fails.
     */
    public static final class ModelObjectAnnotationException extends IllegalArgumentException {
        private static final long serialVersionUID = 1L;

        public ModelObjectAnnotationException(Object pmo) {
            super("Presentation model object " + pmo + " has no method annotated with @"
                    + ModelObject.class.getSimpleName());
        }

        public ModelObjectAnnotationException(Object pmo, String modelObjectName) {
            super("Presentation model object " + pmo + " has no method annotated with @"
                    + ModelObject.class.getSimpleName() + " for the model object named \"" + modelObjectName + "\"");
        }

        public ModelObjectAnnotationException(Object pmo, Method method) {
            super("Presentation model object " + pmo + "'s method " + method.getName() + " is annotated with @"
                    + ModelObject.class.getSimpleName() + " but returns void");
        }

    }

    /**
     * Compares two annotated methods by their position.
     */
    static class PositionComparator implements Comparator<ElementDescriptor>, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public int compare(@Nullable ElementDescriptor fieldDef1, @Nullable ElementDescriptor fieldDef2) {
            if (fieldDef1 == null) {
                return fieldDef2 == null ? 0 : Integer.MIN_VALUE;
            } else if (fieldDef2 == null) {
                return Integer.MAX_VALUE;
            }
            return fieldDef1.getPosition() - fieldDef2.getPosition();
        }

    }
}
