package org.linkki.core.ui.section.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * Reads UIField annotations, e.g. {@link UITextField}, {@link UIComboBox}, etc. from a given
 * object's class.
 * <p>
 * Provides a set of {@link ElementDescriptor} for all found properties via {@link #getUiElements()}.
 * Provides a {@link ElementDescriptor} for a single property by means of {@link #get(String)}.
 *
 * @author widmaier
 */
public class UIAnnotationReader {

    private final PositionComparator POSITION_COMPARATOR = new PositionComparator();

    private final UIElementDefinitionRegistry fieldDefinitionRegistry = new UIElementDefinitionRegistry();
    private final Class<?> annotatedClass;
    private final Map<String, ElementDescriptor> descriptors;
    private final Map<ElementDescriptor, TableColumnDescriptor> columnDescriptors;

    public UIAnnotationReader(Class<?> annotatedClass) {
        this.annotatedClass = annotatedClass;
        descriptors = new TreeMap<>();
        columnDescriptors = new HashMap<>();
        initDescriptorMaps();
    }

    public Set<ElementDescriptor> getUiElements() {
        TreeSet<ElementDescriptor> treeSet = new TreeSet<ElementDescriptor>(POSITION_COMPARATOR);
        treeSet.addAll(descriptors.values());
        return treeSet;
    }

    public ElementDescriptor get(String property) {
        return descriptors.get(property);
    }

    public TableColumnDescriptor getTableColumnDescriptor(String property) {
        Optional<ElementDescriptor> fieldDescriptor = Optional.ofNullable(descriptors.get(property));
        return fieldDescriptor.map(this::getTableColumnDescriptor).orElse(null);
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
                    columnDescriptors.put(descriptor, new TableColumnDescriptor(annotatedClass, method,
                            columnAnnotation));
                }
            }
        }
    }

    private ElementDescriptor addDescriptor(UIElementDefinition uiElement, Method method) {
        if (uiElement instanceof UIFieldDefinition) {
            FieldDescriptor fieldDescriptor = new FieldDescriptor((UIFieldDefinition)uiElement,
                    getFallbackPropertyNameByMethod(method));
            descriptors.put(fieldDescriptor.getPropertyName(), fieldDescriptor);
            return fieldDescriptor;
        }
        if (uiElement instanceof UIButtonDefinition) {
            ButtonDescriptor buttonDescriptor = new ButtonDescriptor((UIButtonDefinition)uiElement, method.getName());
            descriptors.put(method.getName(), buttonDescriptor);
            return buttonDescriptor;
        }
        throw new IllegalStateException("Unknown UIElementDefinition of type " + uiElement + " on method " + method);
    }

    private String getFallbackPropertyNameByMethod(Method method) {
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
        return annotations(method) //
                .filter(fieldDefinitionRegistry::containsAnnotation) //
                .map(fieldDefinitionRegistry::elementDefinition) //
                .findFirst().orElse(null);
    }

    private Stream<Annotation> annotations(Method m) {
        return Arrays.stream(m.getAnnotations());
    }

    /**
     * Compares two annotated methods by their position.
     */
    class PositionComparator implements Comparator<ElementDescriptor> {

        @Override
        public int compare(ElementDescriptor fieldDef1, ElementDescriptor fieldDef2) {
            return fieldDef1.getPosition() - fieldDef2.getPosition();
        }

    }

    public boolean hasAnnotation(String property) {
        return descriptors.containsKey(property);
    }

    public boolean hasTableColumnAnnotation(ElementDescriptor d) {
        return columnDescriptors.containsKey(d);
    }
}
