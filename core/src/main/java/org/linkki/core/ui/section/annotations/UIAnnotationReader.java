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
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
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

    private final UIElementDefinitionRegistry fieldDefinitionRegistry = new UIElementDefinitionRegistry();
    private final Class<?> annotatedClass;
    private final Set<ElementDescriptor> descriptors;
    private final Map<ElementDescriptor, TableColumnDescriptor> columnDescriptors;

    public UIAnnotationReader(Class<?> annotatedClass) {
        this.annotatedClass = annotatedClass;
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

    private void initDescriptorMaps() {
        descriptors.clear();
        Method[] methods = annotatedClass.getMethods();
        for (Method method : methods) {
            if (isUiDefiningMethod(method)) {
                UIElementDefinition uiElement = getUiElement(method);
                ElementDescriptor descriptor = addDescriptor(uiElement, method);

                UITableColumn columnAnnotation = method.getAnnotation(UITableColumn.class);
                if (columnAnnotation != null) {
                    columnDescriptors.put(descriptor,
                                          new TableColumnDescriptor(annotatedClass, method, columnAnnotation));
                }
            }
        }
    }

    private ElementDescriptor addDescriptor(UIElementDefinition uiElement, Method method) {
        ElementDescriptor descriptor;
        if (uiElement instanceof UIFieldDefinition) {
            descriptor = new FieldDescriptor((UIFieldDefinition)uiElement, getPmoPropertyName(method));
        } else if (uiElement instanceof UIButtonDefinition) {
            descriptor = new ButtonDescriptor((UIButtonDefinition)uiElement, method.getName());
        } else if (uiElement instanceof UILabelDefinition) {
            descriptor = new LabelDescriptor((UILabelDefinition)uiElement, getPmoPropertyName(method));
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
        return annotations(method).anyMatch(fieldDefinitionRegistry::containsAnnotation);
    }

    public UIElementDefinition getUiElement(Method method) {
     // @formatter:off
        return annotations(method)
                .filter(fieldDefinitionRegistry::containsAnnotation)
                .map(fieldDefinitionRegistry::elementDefinition)
                .findFirst().orElse(null);
     // @formatter:on
    }

    private Stream<Annotation> annotations(Method m) {
        return Arrays.stream(m.getAnnotations());
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
    @Nonnull
    public static Supplier<?> getModelObjectSupplier(@Nonnull Object pmo, @Nonnull String modelObjectName) {
        requireNonNull(pmo, "PMO must not be null");
        requireNonNull(modelObjectName, "model object name must not be null");
        Optional<Method> modelObjectMethod = BeanUtils
                .getMethod(requireNonNull(pmo).getClass(), (m) -> m.isAnnotationPresent(ModelObject.class)
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
    public static boolean hasModelObjectAnnotatedMethod(@Nonnull Object pmo, String modelObjectName) {
        return BeanUtils.getMethod(requireNonNull(pmo).getClass(),
                                   (m) -> m.isAnnotationPresent(ModelObject.class)
                                           && m.getAnnotation(ModelObject.class).name().equals(modelObjectName))
                .isPresent();
    }

    /**
     * Thrown when trying to get a method annotated with {@link ModelObject @ModelObject} via
     * {@link UIAnnotationReader#getModelObjectSupplier(Object, String)} fails.
     *
     * @author dschwering
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
        public int compare(ElementDescriptor fieldDef1, ElementDescriptor fieldDef2) {
            return fieldDef1.getPosition() - fieldDef2.getPosition();
        }

    }
}
