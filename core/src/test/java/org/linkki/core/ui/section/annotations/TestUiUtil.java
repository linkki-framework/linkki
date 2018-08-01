/*
 * Copyright Faktor Zehn AG.
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

package org.linkki.core.ui.section.annotations;

import java.util.Objects;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.TestBindingContext;
import org.linkki.core.ui.section.AbstractSection;
import org.linkki.core.ui.section.PmoBasedSectionFactory;
import org.linkki.util.StreamUtil;

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
        BindingContext bindingContext = TestBindingContext.create();
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
        return createSectionWith(pmo, TestBindingContext.create());
    }

    public static GridLayout createSectionWith(Object pmo, BindingContext bindingContext) {
        PmoBasedSectionFactory sectionFactory = new PmoBasedSectionFactory();
        AbstractSection section = sectionFactory.createSection(pmo, bindingContext);

        bindingContext.modelChanged();

        Panel panel = (Panel)section.getComponent(1);
        return (GridLayout)panel.getContent();
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
}
