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
package org.linkki.core.ui.section;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import org.linkki.core.ButtonPmo;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ButtonPmoBinder;
import org.linkki.core.nls.pmo.PmoNlsService;
import org.linkki.core.ui.UiElementCreator;
import org.linkki.core.ui.components.LabelComponentWrapper;
import org.linkki.core.ui.section.annotations.SectionLayout;
import org.linkki.core.ui.section.annotations.UICheckBox;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UIDateField;
import org.linkki.core.ui.section.annotations.UIIntegerField;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.core.ui.table.ContainerPmo;
import org.linkki.core.ui.table.PmoBasedTableFactory;
import org.linkki.core.ui.table.TableSection;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * Base class for a factory to create sections based on an annotated PMO.
 * 
 * @see UISection
 * @see UITextField
 * @see UICheckBox
 * @see UIDateField
 * @see UIComboBox
 * @see UIIntegerField
 */
public class PmoBasedSectionFactory {

    /**
     * Creates a new section based on the given annotated PMO and binds the created controls via the
     * given binding context to the PMO. If the given PMO is a {@link ContainerPmo}, a table section is
     * created.
     */
    public AbstractSection createSection(Object pmo, BindingContext bindingContext) {
        SectionBuilder builder = new SectionBuilder(requireNonNull(pmo, "pmo must not be null"),
                requireNonNull(bindingContext, "bindingContext must not be null"));
        return builder.createSection();
    }

    /**
     * Creates a new base section based on the given annotated PMO and binds the created controls via
     * the given binding context to the PMO.
     */
    public BaseSection createBaseSection(Object pmo, BindingContext bindingContext) {
        return (BaseSection)createSection(pmo, bindingContext);
    }

    /**
     * Creates a new section containing a table based on the given annotated {@link ContainerPmo} and
     * binds the table via the given binding context to the PMO.
     */
    public <T> TableSection createTableSection(ContainerPmo<T> pmo, BindingContext bindingContext) {
        return (TableSection)createSection(pmo, bindingContext);
    }

    /**
     * Object holding references to PMO and binding context while creating a section for them. Intended
     * to be used only once.
     */
    /* private */ static class SectionBuilder {

        private final Object pmo;
        private final BindingContext bindingContext;

        public SectionBuilder(Object pmo, BindingContext bindingContext) {
            this.pmo = pmo;
            this.bindingContext = bindingContext;
        }

        public AbstractSection createSection() {
            UISection sectionDefinition = pmo.getClass().getAnnotation(UISection.class);

            SectionLayout layout = sectionDefinition != null ? sectionDefinition.layout() : SectionLayout.COLUMN;
            String caption = PmoNlsService.get()
                    .getSectionCaption(pmo.getClass(), sectionDefinition != null ? sectionDefinition.caption() : "");
            boolean closable = sectionDefinition != null ? sectionDefinition.closeable() : false;
            int columns = sectionDefinition != null ? sectionDefinition.columns() : 1;

            AbstractSection section = pmo instanceof ContainerPmo
                    ? createTableSection(caption, closable)
                    : createBaseSection(layout, caption, closable, columns);

            section.setId(Sections.getSectionId(pmo));
            return section;
        }

        private BaseSection createBaseSection(SectionLayout layout, String caption, boolean closeable, int columns) {
            BaseSection section = createEmptySection(layout, caption, closeable, columns);
            UiElementCreator.createUiElements(pmo,
                                              bindingContext,
                                              component -> new LabelComponentWrapper(new Label(), (Component)component))
                    .forEach(wrapper -> add(section, wrapper));
            return section;
        }

        private void add(BaseSection section, LabelComponentWrapper wrapper) {
            Label label = wrapper.getLabelComponent().get();
            Component component = wrapper.getComponent();
            section.add(component.getId(), label, component);
        }

        private BaseSection createEmptySection(SectionLayout layout, String caption, boolean closeable, int columns) {
            Optional<Button> editButton = createEditButton(Sections.getEditButtonPmo(pmo));
            switch (layout) {
                case COLUMN:
                    return new FormSection(caption,
                            closeable,
                            editButton,
                            columns);
                case HORIZONTAL:
                    return new HorizontalSection(caption,
                            closeable,
                            editButton);
                case CUSTOM:
                    return new CustomLayoutSection(pmo.getClass().getSimpleName(), caption,
                            closeable,
                            editButton);
                default:
                    throw new IllegalStateException("unknown SectionLayout#" + layout);
            }
        }

        private Optional<Button> createEditButton(Optional<ButtonPmo> buttonPmo) {
            return buttonPmo.map(b -> ButtonPmoBinder.createBoundButton(bindingContext, b));
        }

        private TableSection createTableSection(String caption, boolean closable) {
            @SuppressWarnings("deprecation")
            com.vaadin.v7.ui.Table table = new PmoBasedTableFactory((ContainerPmo<?>)pmo, bindingContext).createTable();
            Optional<Button> addItemButton = ((ContainerPmo<?>)pmo).getAddItemButtonPmo()
                    .map(b -> ButtonPmoBinder.createBoundButton(bindingContext, b));
            return new TableSection(caption, closable, addItemButton, table);
        }

    }
}
