package de.faktorzehn.ipm.web.ui.section.annotations;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UICheckBoxAdpater;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIComboBoxAdpater;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIDateFieldAdpater;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIDecimalFieldAdapter;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIDoubleFieldAdpater;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIIntegerFieldAdpater;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UITextAreaAdpater;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UITextFieldAdpater;

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
        return getUiField(method) != null;
    }

    public UIFieldDefinition getUiField(Method method) {
        UIFieldDefinition uiField = null;
        if (method.getAnnotation(UITextField.class) != null) {
            uiField = new UITextFieldAdpater(method.getAnnotation(UITextField.class));
        } else if (method.getAnnotation(UIComboBox.class) != null) {
            uiField = new UIComboBoxAdpater(method.getAnnotation(UIComboBox.class));
        } else if (method.getAnnotation(UICheckBox.class) != null) {
            uiField = new UICheckBoxAdpater(method.getAnnotation(UICheckBox.class));
        } else if (method.getAnnotation(UIIntegerField.class) != null) {
            uiField = new UIIntegerFieldAdpater(method.getAnnotation(UIIntegerField.class));
        } else if (method.getAnnotation(UIDateField.class) != null) {
            uiField = new UIDateFieldAdpater(method.getAnnotation(UIDateField.class));
        } else if (method.getAnnotation(UITextArea.class) != null) {
            uiField = new UITextAreaAdpater(method.getAnnotation(UITextArea.class));
        } else if (method.getAnnotation(UIDecimalField.class) != null) {
            uiField = new UIDecimalFieldAdapter(method.getAnnotation(UIDecimalField.class));
        } else if (method.getAnnotation(UIDoubleField.class) != null) {
            uiField = new UIDoubleFieldAdpater(method.getAnnotation(UIDoubleField.class));
        } else if (method.getAnnotation(UIDecimalField.class) != null) {
            uiField = new UIDecimalFieldAdapter(method.getAnnotation(UIDecimalField.class));
        }
        return uiField;
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
