/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.linkki.core.ui.element.annotation;

import java.util.List;
import java.util.Objects;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.core.vaadin.component.section.LinkkiSection;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout.FormItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.data.provider.HasListDataView;
import com.vaadin.flow.data.provider.ListDataView;

public final class TestUiUtil {

    /**
     * Creates the first component defined in a given PMO.
     * <p>
     * Note that this method only returns the first control Ui so the label is not accessible.
     *
     * @param pmo the PMO to which the component is bound
     * @return a {@code Component} that is bound to the model object
     */
    public static <T extends Component> T createFirstComponentOf(Object pmo) {
        BindingContext bindingContext = new BindingContext();
        return createFirstComponentOf(pmo, bindingContext);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Component> T createFirstComponentOf(Object pmo, BindingContext bindingContext) {
        return (T)UiCreator
                .<T, NoLabelComponentWrapper> createUiElements(pmo, bindingContext,
                                                               c -> new NoLabelComponentWrapper(c, WrapperType.FIELD))
                .findFirst()
                .get().getComponent();
    }

    /**
     * Creates the section defined in the given pmo.
     *
     * @param pmo the PMO to which the component is bound
     * @return a {@code Component} that is bound to the model object
     */
    public static Div createSectionWith(Object pmo) {
        return createSectionWith(pmo, new BindingContext());
    }

    public static Div createSectionWith(Object pmo, BindingContext bindingContext) {
        PmoBasedSectionFactory sectionFactory = new PmoBasedSectionFactory();
        LinkkiSection section = sectionFactory.createSection(pmo, bindingContext);

        bindingContext.modelChanged();

        return section.getContentWrapper();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getComponentById(Div layout, String id) {
        return (T)layout.getChildren()
                .map(c -> (FormItem)c)
                .map(fi -> fi.getChildren().findFirst().get())
                .filter(c -> c.getId().isPresent())
                .filter(c -> Objects.equals(c.getId().get(), id))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No component with id " + id));
    }

    public static String getLabelOfComponentAt(Div layout, int row) {
        List<Component> children = layout.getChildren().toList();
        FormItem formItem = (FormItem)children.get(row);
        return ((NativeLabel)formItem.getChildren().toList().get(1)).getText();
    }

    public static <T> List<T> getData(HasListDataView<T, ? extends ListDataView<T, ?>> hasDataProvider) {
        return hasDataProvider.getListDataView().getItems().toList();
    }

    public static Component getComponentAtIndex(int index, Component component) {
        List<Component> children = component.getChildren().toList();
        Component componentAtIndex = children.get(index);
        if (componentAtIndex instanceof FormItem formItem) {
            return formItem.getChildren().toList().get(0);
        } else {
            return componentAtIndex;
        }
    }

    public static List<String> getColumnHeaders(Grid<?> grid) {
        HeaderRow header = grid.getHeaderRows().get(0);
        return header.getCells().stream().map(HeaderRow.HeaderCell::getText).toList();
    }

}
