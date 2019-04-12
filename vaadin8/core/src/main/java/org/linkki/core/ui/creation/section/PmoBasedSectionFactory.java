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
package org.linkki.core.ui.creation.section;

import static java.util.Objects.requireNonNull;
import static org.linkki.util.Optionals.either;

import java.util.Optional;
import java.util.function.Function;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.nls.PmoNlsService;
import org.linkki.core.ui.creation.table.PmoBasedTableFactory;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.uicreation.ComponentAnnotationReader;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.core.uicreation.layout.LayoutAnnotationReader;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;
import org.linkki.core.vaadin.component.section.AbstractSection;
import org.linkki.core.vaadin.component.section.BaseSection;
import org.linkki.core.vaadin.component.section.TableSection;

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
        return createAndBindSection(pmo, bindingContext);
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
     * Creates a new section based on the given annotated PMO and binds the created controls via the
     * given binding context to the PMO. If the given PMO is a {@link ContainerPmo}, a table section is
     * created.
     */
    public static AbstractSection createAndBindSection(Object pmo, BindingContext bindingContext) {
        requireNonNull(pmo, "pmo must not be null");
        requireNonNull(bindingContext, "bindingContext must not be null");
        Function<Class<?>, Optional<LinkkiComponentDefinition>> componentDefinitionFinder = c -> {
            return ContainerPmo.class.isAssignableFrom(c)
                    ? Optional.of(TableSectionDefinition::createTableSection)
                    : either(ComponentAnnotationReader.findComponentDefinition(c))
                            .or(() -> Optional.of(SectionComponentDefiniton.DEFAULT));
        };
        Function<Class<?>, Optional<LinkkiLayoutDefinition>> layoutDefinitionFinder = c -> {
            return ContainerPmo.class.isAssignableFrom(c)
                    ? Optional.of(TableSectionDefinition::createTable)
                    : either(LayoutAnnotationReader.findLayoutDefinition(c))
                            .or(() -> Optional.of(SectionLayoutDefinition.DEFAULT));
        };
        ComponentWrapper componentWrapper = UiCreator
                .createComponent(pmo, bindingContext, componentDefinitionFinder, layoutDefinitionFinder);
        return (AbstractSection)componentWrapper.getComponent();
    }

    // private to contain this hack here
    private static final class TableSectionDefinition {

        private TableSectionDefinition() {
            // do not instantiate
        }

        /* Static implementation of {@link LinkkiComponentDefinition} */
        public static TableSection createTableSection(Object pmo) {
            UISection sectionDefinition = pmo.getClass().getAnnotation(UISection.class);
            String nlsCaption = PmoNlsService.get().getSectionCaption(pmo.getClass(), sectionDefinition != null
                    ? sectionDefinition.caption()
                    : "");
            return new TableSection(nlsCaption, sectionDefinition != null ? sectionDefinition.closeable() : false);
        }

        /* Static implementation of {@link LinkkiLayoutDefinition} */
        public static void createTable(Object parentComponent, Object pmo, BindingContext bindingContext) {
            TableSection tableSection = (TableSection)parentComponent;
            ((ContainerPmo<?>)pmo).getAddItemButtonPmo()
                    .map(b -> ButtonPmoBinder.createBoundButton(bindingContext, b))
                    .ifPresent(tableSection::addHeaderButton);
            @SuppressWarnings("deprecation")
            com.vaadin.v7.ui.Table table = new PmoBasedTableFactory((ContainerPmo<?>)pmo, bindingContext)
                    .createTable();
            tableSection.setTable(table);

        }
    }
}
