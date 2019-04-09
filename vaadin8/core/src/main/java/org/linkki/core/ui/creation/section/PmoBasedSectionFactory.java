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
import org.linkki.core.ui.component.section.AbstractSection;
import org.linkki.core.ui.component.section.BaseSection;
import org.linkki.core.ui.component.section.TableSection;
import org.linkki.core.ui.creation.table.PmoBasedTableFactory;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.uicreation.ComponentAnnotationReader;
import org.linkki.core.uicreation.UiElementCreator;
import org.linkki.core.uicreation.layout.LayoutAnnotationReader;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;

import com.vaadin.ui.Button;

import edu.umd.cs.findbugs.annotations.CheckForNull;

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
    public <P> AbstractSection createSection(P pmo, BindingContext bindingContext) {
        SectionBuilder<P> builder = new SectionBuilder<>(requireNonNull(pmo, "pmo must not be null"),
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
    /* private */ static class SectionBuilder<P> {

        private final P pmo;
        private final BindingContext bindingContext;

        public SectionBuilder(P pmo, BindingContext bindingContext) {
            this.pmo = pmo;
            this.bindingContext = bindingContext;
        }

        public AbstractSection createSection() {
            UISection sectionDefinition = pmo.getClass().getAnnotation(UISection.class);

            Function<Class<? super P>, Optional<LinkkiComponentDefinition>> componentDefinitionFinder = c -> {
                return ContainerPmo.class.isAssignableFrom(c)
                        ? Optional.of(new TableSectionComponentDefinition(sectionDefinition))
                        : either(ComponentAnnotationReader.findComponentDefinition(pmo.getClass()))
                                .or(() -> Optional.of(SectionComponentDefiniton.DEFAULT));
            };
            Function<Class<? super P>, Optional<LinkkiLayoutDefinition>> layoutDefinitionFinder = c -> {
                return ContainerPmo.class.isAssignableFrom(c)
                        ? Optional.of(LinkkiLayoutDefinition.EMPTY)
                        : either(LayoutAnnotationReader.findLayoutDefinition(pmo.getClass()))
                                .or(() -> Optional.of(SectionLayoutDefinition.DEFAULT));
            };
            ComponentWrapper componentWrapper = UiElementCreator
                    .createComponent(pmo, bindingContext, componentDefinitionFinder,
                                     layoutDefinitionFinder);
            return (AbstractSection)componentWrapper.getComponent();
        }

        private final class TableSectionComponentDefinition implements LinkkiComponentDefinition {
            @CheckForNull
            private final UISection sectionDefinition;

            private TableSectionComponentDefinition(UISection sectionDefinition) {
                this.sectionDefinition = sectionDefinition;
            }

            @Override
            public Object createComponent(@SuppressWarnings("hiding") Object pmo) {
                String caption = PmoNlsService.get()
                        .getSectionCaption(pmo.getClass(),
                                           sectionDefinition != null ? sectionDefinition.caption() : "");
                boolean closable = sectionDefinition != null ? sectionDefinition.closeable() : false;
                return createTableSection(caption, closable);
            }

            private TableSection createTableSection(String caption, boolean closable) {
                @SuppressWarnings("deprecation")
                com.vaadin.v7.ui.Table table = new PmoBasedTableFactory((ContainerPmo<?>)pmo, bindingContext)
                        .createTable();
                Optional<Button> addItemButton = ((ContainerPmo<?>)pmo).getAddItemButtonPmo()
                        .map(b -> ButtonPmoBinder.createBoundButton(bindingContext, b));
                return new TableSection(caption, closable, addItemButton, table);
            }
        }

    }
}
