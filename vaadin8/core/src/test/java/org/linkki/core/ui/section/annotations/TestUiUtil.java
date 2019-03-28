/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.core.ui.section.annotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.section.AbstractSection;
import org.linkki.core.ui.section.FormSection;
import org.linkki.core.ui.section.PmoBasedSectionFactory;
import org.linkki.util.StreamUtil;

import com.vaadin.data.HasItems;
import com.vaadin.data.provider.Query;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractMultiSelect;
import com.vaadin.ui.AbstractSingleSelect;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;

public final class TestUiUtil {

    /**
     * Creates the first component defined in a given PMO.
     * <p>
     * Note that this method only returns the first control Ui so the label is not accessible.
     * 
     * @param pmo the PMO to which the component is bound is bound
     * @return a {@code Component} that is bound to the model object
     */
    public static Component createFirstComponentOf(Object pmo) {
        BindingContext bindingContext = new BindingContext();
        return createFirstComponentOf(pmo, bindingContext);
    }

    
    public static Component createFirstComponentOf(Object pmo, BindingContext bindingContext) {
        return createSectionWith(pmo, bindingContext).getComponent(1, 0);
    }

    /**
     * Creates the section defined in the given pmo.
     * <p>
     * Note that this method only returns the {@link Layout} inside the {@link Panel} of the section.
     * 
     * @param pmo the PMO to which the component is bound is bound
     * @return a {@code Component} that is bound to the model object
     */
    public static GridLayout createSectionWith(Object pmo) {
        return createSectionWith(pmo, new BindingContext());
    }

    
    public static GridLayout createSectionWith(Object pmo, BindingContext bindingContext) {
        PmoBasedSectionFactory sectionFactory = new PmoBasedSectionFactory();
        AbstractSection section = sectionFactory.createSection(pmo, bindingContext);

        bindingContext.modelChanged();

        return getContentGrid((FormSection)section);
    }

    public static GridLayout getContentGrid(FormSection section) {
        try {
            Method getContentGrid = FormSection.class.getDeclaredMethod("getContentGrid");
            getContentGrid.setAccessible(true);
            return (GridLayout)getContentGrid.invoke(section);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getComponentAt(GridLayout layout, int row) {
        return (T)layout.getComponent(1, row);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getComponentById(GridLayout layout, String id) {
        return (T)StreamUtil.stream(layout).filter(c -> Objects.equals(c.getId(), id)).findFirst()
                .orElseThrow(() -> new IllegalStateException("No component with id " + id));
    }

    public static String getLabelOfComponentAt(GridLayout layout, int row) {
        Component component = layout.getComponent(0, row);
        if (!(component instanceof Label)) {
            throw new IllegalArgumentException("No label found on the left side of the component");
        } else {
            return ((Label)component).getValue();
        }
    }

    public static <T> void setUserOriginatedValue(AbstractField<T> field, T value) {
        try {
            Method setValue = AbstractField.class.getDeclaredMethod("setValue", Object.class, boolean.class);
            setValue.setAccessible(true);
            setValue.invoke(field, value, true);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException e) {
            throw new IllegalStateException("Could not set user originated value '" + value + "'", e);
        } catch (InvocationTargetException e) {
            throw (RuntimeException)e.getCause();
        }
    }

    public static <T> void setUserOriginatedValue(AbstractSingleSelect<T> field, T value) {
        try {
            Method setSelectedItem = AbstractSingleSelect.class.getDeclaredMethod("setSelectedItem", Object.class,
                                                                                  boolean.class);
            setSelectedItem.setAccessible(true);
            setSelectedItem.invoke(field, value, true);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void setUserOriginatedValue(AbstractMultiSelect<T> field, Set<T> value) {
        try {
            Set<T> copy = value.stream().map(Objects::requireNonNull)
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            Method updateSelection = AbstractMultiSelect.class.getDeclaredMethod("updateSelection", Set.class,
                                                                                 Set.class, boolean.class);
            updateSelection.setAccessible(true);
            updateSelection.invoke(field, copy, new LinkedHashSet<>(field.getSelectedItems()), true);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> getData(HasItems<T> hasDataProvider) {
        return hasDataProvider.getDataProvider().fetch(new Query<>()).collect(Collectors.toList());
    }
}
