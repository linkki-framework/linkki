/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import java.util.Set;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.themes.ValoTheme;

import de.faktorzehn.ipm.utils.LazyInitializingMap;
import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.binding.BindingContext;
import de.faktorzehn.ipm.web.binding.FieldBinding;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyBehaviorProvider;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcherFactory;
import de.faktorzehn.ipm.web.ui.application.ApplicationStyles;
import de.faktorzehn.ipm.web.ui.section.annotations.FieldDescriptor;
import de.faktorzehn.ipm.web.ui.section.annotations.UIAnnotationReader;

/**
 * A factory to create a table based on a {@link ContainerPmo}.
 *
 * @author ortmann
 */
public class PmoBasedTableFactory<T extends PresentationModelObject> {

    private static final String REMOVE_COLUMN = "remove";

    private static final PropertyDispatcherFactory DISPATCHER_FACTORY = new PropertyDispatcherFactory();

    private ContainerPmo<T> containerPmo;
    private UIAnnotationReader annotationReader;
    private BindingContext bindingContext;
    private PropertyBehaviorProvider propertyBehaviorProvider;

    /**
     * Creates a new factory.
     * 
     * @param containerPmo The container providing the contents and column definitions.
     * @param bindingContext The binding context to which the cell bindings are added.
     * @param propertyBehaviorProvider The property behavior provider ...
     */
    public PmoBasedTableFactory(ContainerPmo<T> containerPmo, BindingContext bindingContext,
            PropertyBehaviorProvider propertyBehaviorProvider) {
        this.containerPmo = containerPmo;
        this.annotationReader = new UIAnnotationReader(containerPmo.getItemPmoClass());
        this.bindingContext = bindingContext;
        this.propertyBehaviorProvider = propertyBehaviorProvider;
    }

    /**
     * Create a new table based on the container PMO.
     */
    public PmoBasedTable<T> createTable() {
        PmoBasedTable<T> table = createTableComponent();
        createColumns(table);
        return table;
    }

    public ContainerPmo<T> getContainerPmo() {
        return containerPmo;
    }

    protected BindingContext getBindingContext() {
        return bindingContext;
    }

    protected PropertyDispatcher createPropertyDispatcher(final PresentationModelObject pmo) {
        return DISPATCHER_FACTORY.defaultDispatcherChain(pmo, propertyBehaviorProvider);
    }

    private PmoBasedTable<T> createTableComponent() {
        PmoBasedTable<T> table = new PmoBasedTable<>(containerPmo);
        table.addStyleName(ApplicationStyles.TABLE);
        table.setHeightUndefined();
        table.setPageLength(0);
        table.setSortEnabled(false);
        return table;
    }

    private void createColumns(Table table) {
        Set<FieldDescriptor> fields = annotationReader.getFields();
        boolean receiveFocusOnNew = true;
        for (FieldDescriptor field : fields) {
            createColumn(table, field, receiveFocusOnNew);
            receiveFocusOnNew = false;
        }
        if (containerPmo.isDeleteItemAvailable()) {
            table.addGeneratedColumn(REMOVE_COLUMN, new RemoveIconColumnGenerator());
            table.setColumnHeader(REMOVE_COLUMN, "Entfernen");
            table.setColumnExpandRatio(REMOVE_COLUMN, 0.0f);
        }
    }

    private void createColumn(Table table, FieldDescriptor field, boolean receiveFocusOnNew) {
        table.addGeneratedColumn(field.getPropertyName(), new FieldColumnGenerator(field, receiveFocusOnNew));
        table.setColumnHeader(field.getPropertyName(), field.getLabelText());
    }

    class FieldColumnGenerator implements ColumnGenerator {

        private static final long serialVersionUID = 1L;

        private final LazyInitializingMap<PresentationModelObject, PropertyDispatcher> dispatchers = new LazyInitializingMap<>(
                PmoBasedTableFactory.this::createPropertyDispatcher);
        private final FieldDescriptor fieldDescriptor;
        private final boolean receiveFocusOnNew;

        public FieldColumnGenerator(FieldDescriptor fieldDescriptor, boolean receiveFocusOnNew) {
            this.fieldDescriptor = fieldDescriptor;
            this.receiveFocusOnNew = receiveFocusOnNew;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public Object generateCell(Table source, Object itemId, Object columnId) {
            Component component = fieldDescriptor.newComponent();
            component.addStyleName(ApplicationStyles.BORDERLESS);
            component.addStyleName(ApplicationStyles.TABLE_CELL);
            component.setEnabled(getContainerPmo().isEditable());
            AbstractField field = (AbstractField)component;
            T pmo = (T)itemId;

            FieldBinding binding = FieldBinding.create(getBindingContext(), (String)columnId, null, field,
                                                       dispatchers.get(pmo));
            getBindingContext().add(binding);
            binding.updateFieldFromPmo();
            if (receiveFocusOnNew) {
                field.focus();
            }
            return component;
        }
    }

    class RemoveIconColumnGenerator implements ColumnGenerator {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("unchecked")
        @Override
        public Object generateCell(Table source, Object itemId, Object columnId) {
            Button button = new Button(FontAwesome.TRASH_O);
            button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
            button.addStyleName(ValoTheme.BUTTON_LARGE);
            T row = (T)itemId;
            button.addClickListener(e -> {
                getContainerPmo().deleteItem(row);
                ((PmoBasedTable<T>)source).updateFromPmo();
                getBindingContext().updateUI();
            });
            return button;
        }

    }

}
