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

package org.linkki.core.ui.element.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.core.vaadin.component.section.LinkkiSection;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout.FormItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.data.binder.HasItems;
import com.vaadin.flow.data.renderer.Renderer;

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
        return UiCreator
                .createUiElements(pmo, bindingContext,
                                  c -> new NoLabelComponentWrapper((Component)c, WrapperType.FIELD))
                .findFirst()
                .get().getComponent();
    }

    /**
     * Creates the section defined in the given pmo.
     * 
     * @param pmo the PMO to which the component is bound is bound
     * @return a {@code Component} that is bound to the model object
     */
    public static FlexLayout createSectionWith(Object pmo) {
        return createSectionWith(pmo, new BindingContext());
    }

    public static FlexLayout createSectionWith(Object pmo, BindingContext bindingContext) {
        PmoBasedSectionFactory sectionFactory = new PmoBasedSectionFactory();
        LinkkiSection section = sectionFactory.createSection(pmo, bindingContext);

        bindingContext.modelChanged();

        return section.getContentWrapper();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getComponentById(FlexLayout layout, String id) {
        return (T)layout.getChildren()
                .map(c -> (FormItem)c)
                .map(fi -> fi.getChildren().findFirst().get())
                .filter(c -> c.getId().isPresent())
                .filter(c -> Objects.equals(c.getId().get(), id))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No component with id " + id));
    }

    public static String getLabelOfComponentAt(FlexLayout layout, int row) {
        List<Component> children = layout.getChildren().collect(Collectors.toList());
        FormItem formItem = (FormItem)children.get(row);
        return ((Label)formItem.getChildren().collect(Collectors.toList()).get(1)).getText();
    }

    @SuppressWarnings("unused")
    public static <T> void setUserOriginatedValue(AbstractField<?, T> field, T value) {
        // TODO LIN-2051
    }

    public static <T> void setUserOriginatedValue(AbstractSinglePropertyField<?, T> field, T value) {
        try {
            Field fieldSupportField = AbstractField.class.getDeclaredField("fieldSupport");
            fieldSupportField.setAccessible(true);
            AbstractFieldSupport<?, ?> fieldSupport = (AbstractFieldSupport<?, ?>)fieldSupportField.get(field);

            Method valueSetter = AbstractFieldSupport.class.getDeclaredMethod("setValue", Object.class, boolean.class,
                                                                              boolean.class);
            valueSetter.setAccessible(true);
            valueSetter.invoke(fieldSupport, value, false, true);

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
                | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Failed to set user-originated value", e);
        }
    }

    @SuppressWarnings("unused")
    public static <T> void setUserOriginatedValues(AbstractField<?, T> field, Set<T> value) {
        // TODO LIN-2051
    }

    @SuppressWarnings("unused")
    public static <T> List<T> getData(HasItems<T> hasDataProvider) {
        // TODO LIN-2051
        return Collections.emptyList();
    }

    public static Component getComponentAtIndex(int index, Component component) {
        List<Component> children = component.getChildren().collect(Collectors.toList());
        Component componentAtIndex = children.get(index);
        if (componentAtIndex instanceof FormItem) {
            FormItem formItem = (FormItem)componentAtIndex;
            return formItem.getChildren().collect(Collectors.toList()).get(0);
        } else {
            return componentAtIndex;
        }
    }

    public static List<String> getColumnHeaders(Grid<?> grid) {
        Method getHeaderRendererMethod;
        Field templateField;
        try {
            getHeaderRendererMethod = Column.class.getSuperclass().getDeclaredMethod("getHeaderRenderer");
            getHeaderRendererMethod.setAccessible(true);
            templateField = Renderer.class.getDeclaredField("template");
            templateField.setAccessible(true);
        } catch (NoSuchMethodException | NoSuchFieldException | SecurityException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }

        return grid.getColumns().stream().map(c -> getColumnHeader(c, getHeaderRendererMethod, templateField))
                .collect(Collectors.toList());
    }

    private static String getColumnHeader(Column<?> column, Method getHeaderRendererMethod, Field templateField) {
        try {
            Renderer<?> renderer = (Renderer<?>)getHeaderRendererMethod.invoke(column);
            return (String)templateField.get(renderer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
