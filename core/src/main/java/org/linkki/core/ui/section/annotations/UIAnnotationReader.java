package org.linkki.core.ui.section.annotations;

import static java.util.Objects.requireNonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.ui.section.annotations.adapters.LabelBindingDefinition;
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

    private final Class<?> annotatedClass;
    private final Map<Integer, ElementDescriptors> descriptors;
    private final Map<ElementDescriptors, TableColumnDescriptor> columnDescriptors;

    public UIAnnotationReader(Class<?> annotatedClass) {
        this.annotatedClass = requireNonNull(annotatedClass, "annotatedClass must not be null");
        descriptors = new HashMap<>();
        columnDescriptors = new HashMap<>();
        initDescriptorMaps();
    }

    public Set<ElementDescriptors> getUiElements() {
        TreeSet<ElementDescriptors> treeSet = new TreeSet<>(Comparator.comparing(ElementDescriptors::getPosition));
        treeSet.addAll(descriptors.values());
        return treeSet;
    }

    public TableColumnDescriptor getTableColumnDescriptor(ElementDescriptors d) {
        return columnDescriptors.get(d);
    }

    @SuppressWarnings("null")
    private void initDescriptorMaps() {
        descriptors.clear();
        Method[] methods = annotatedClass.getMethods();
        for (Method method : methods) {

            readUiAnnotations(method).forEach(a -> createAndAddDescriptor(a, method));
        }
    }

    private Stream<Annotation> readUiAnnotations(Method m) {
        return Arrays.stream(m.getAnnotations())
                .filter(UIElementDefinition::isLinkkiBindingDefinition);
    }

    private void createAndAddDescriptor(Annotation annotation, Method method) {
        UIElementDefinition uiElement = UIElementDefinition.from(annotation);
        UIToolTipDefinition toolTipDefinition = getUIToolTipDefinition(method);
        ElementDescriptors elementDescriptors = addDescriptor(uiElement, toolTipDefinition, method, annotation);

        UITableColumn columnAnnotation = method.getAnnotation(UITableColumn.class);
        if (columnAnnotation != null) {
            columnDescriptors.put(elementDescriptors,
                                  new TableColumnDescriptor(annotatedClass, method, columnAnnotation));
        }

    }

    private UIToolTipDefinition getUIToolTipDefinition(Method method) {
        UIToolTip toolTip = method.getAnnotation(UIToolTip.class);
        return new UIToolTipAdapter(toolTip);
    }

    private ElementDescriptors addDescriptor(UIElementDefinition uiElement,
            UIToolTipDefinition toolTipDefinition,
            Method method,
            Annotation annotation) {
        ElementDescriptor descriptor;

        if (uiElement instanceof LabelBindingDefinition) {
            descriptor = new LabelDescriptor((UIFieldDefinition)uiElement, toolTipDefinition,
                    getPmoPropertyName(method), annotatedClass);
        } else if (uiElement instanceof UIFieldDefinition) {
            descriptor = new FieldDescriptor((UIFieldDefinition)uiElement, toolTipDefinition,
                    getPmoPropertyName(method), annotatedClass);
        } else if (uiElement instanceof UIButtonDefinition) {
            descriptor = new ButtonDescriptor((UIButtonDefinition)uiElement, toolTipDefinition, method.getName(),
                    annotatedClass);
        } else {
            throw new IllegalStateException(
                    "Unknown UIElementDefinition of type " + uiElement + " on method " + method);
        }

        ElementDescriptors elementDescriptors = descriptors.computeIfAbsent(uiElement.position(),
                                                                            ElementDescriptors::new);
        elementDescriptors.addDescriptor(annotation, descriptor, annotatedClass);

        return elementDescriptors;
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


    /**
     * Currently for testing purposes only!
     *
     * @return the descriptor for the given property.
     *
     * @throws NoSuchElementException if no descriptor with the given property can be found
     */
    public ElementDescriptors findDescriptors(String propertyName) {
        return getUiElements().stream()
                .filter(el -> el.getPmoPropertyName().equals(propertyName))
                .findFirst()
                .get();
    }

    public boolean hasTableColumnAnnotation(ElementDescriptors d) {
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
     *
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
     *
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

}
