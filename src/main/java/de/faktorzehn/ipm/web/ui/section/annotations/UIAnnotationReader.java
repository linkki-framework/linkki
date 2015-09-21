package de.faktorzehn.ipm.web.ui.section.annotations;

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
 * Provides a set of {@link FieldDescriptor} for all found properties via {@link #getFields()}.
 * Provides a {@link FieldDescriptor} for a single property by means of {@link #get(String)}.
 *
 * @author widmaier
 */
public class UIAnnotationReader {

    private final PositionComparator POSITION_COMPARATOR = new PositionComparator();

    private final UIFieldDefinitionRegistry fieldDefinitionRegistry = new UIFieldDefinitionRegistry();
    private final Class<?> annotatedClass;
    private final Map<String, FieldDescriptor> fieldDescriptors;
    private final Map<FieldDescriptor, TableColumnDescriptor> columnDescriptors;

    public UIAnnotationReader(Class<?> annotatedClass) {
        this.annotatedClass = annotatedClass;
        fieldDescriptors = new TreeMap<>();
        columnDescriptors = new HashMap<>();
        initDescriptorMaps();
    }

    public Set<FieldDescriptor> getFields() {
        TreeSet<FieldDescriptor> treeSet = new TreeSet<FieldDescriptor>(POSITION_COMPARATOR);
        treeSet.addAll(fieldDescriptors.values());
        return treeSet;
    }

    public FieldDescriptor get(String property) {
        return fieldDescriptors.get(property);
    }

    public TableColumnDescriptor getTableColumnDescriptor(String property) {
        Optional<FieldDescriptor> fieldDescriptor = Optional.ofNullable(fieldDescriptors.get(property));
        return fieldDescriptor.map(this::getTableColumnDescriptor).orElse(null);
    }

    public TableColumnDescriptor getTableColumnDescriptor(FieldDescriptor d) {
        return columnDescriptors.get(d);
    }

    private void initDescriptorMaps() {
        fieldDescriptors.clear();
        Method[] methods = annotatedClass.getMethods();
        for (Method method : methods) {
            if (isUiDefiningMethod(method)) {
                UIFieldDefinition uiField = getUiField(method);
                FieldDescriptor fieldDescriptor = new FieldDescriptor(uiField, getFallbackPropertyNameByMethod(method));
                fieldDescriptors.put(fieldDescriptor.getPropertyName(), fieldDescriptor);

                UITableColumn columnAnnotation = method.getAnnotation(UITableColumn.class);
                if (columnAnnotation != null) {
                    columnDescriptors.put(fieldDescriptor, new TableColumnDescriptor(annotatedClass, method,
                            columnAnnotation));
                }
            }
        }
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

    public UIFieldDefinition getUiField(Method method) {
        return annotations(method) //
                .filter(fieldDefinitionRegistry::containsAnnotation) //
                .map(fieldDefinitionRegistry::fieldDefinition) //
                .findFirst().orElse(null);
    }

    private Stream<Annotation> annotations(Method m) {
        return Arrays.stream(m.getAnnotations());
    }

    /**
     * Compares two annotated methods by their position.
     */
    class PositionComparator implements Comparator<FieldDescriptor> {

        @Override
        public int compare(FieldDescriptor fieldDef1, FieldDescriptor fieldDef2) {
            return fieldDef1.getPosition() - fieldDef2.getPosition();
        }

    }

    public boolean hasAnnotation(String property) {
        return fieldDescriptors.containsKey(property);
    }

    public boolean hasTableColumnAnnotation(FieldDescriptor d) {
        return columnDescriptors.containsKey(d);
    }
}
