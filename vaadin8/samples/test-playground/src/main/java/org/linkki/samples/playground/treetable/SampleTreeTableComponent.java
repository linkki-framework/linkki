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
package org.linkki.samples.playground.treetable;

import java.util.function.Consumer;

import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.vaadin.component.area.TabSheetArea;
import org.linkki.core.vaadin.component.page.AbstractPage;
import org.linkki.samples.playground.treetable.dynamic.Bundesliga;
import org.linkki.samples.playground.treetable.dynamic.League;
import org.linkki.samples.playground.treetable.dynamic.LeagueTablePmo;
import org.linkki.samples.playground.treetable.fixed.PersonRepository;
import org.linkki.samples.playground.treetable.fixed.PersonTablePmo;
import org.linkki.samples.playground.ui.SidebarSheetDefinition;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.ui.VerticalLayout;

public class SampleTreeTableComponent extends VerticalLayout implements SidebarSheetDefinition {

    public static final String ID = "TreeTable";

    private static final long serialVersionUID = 1L;
    private final PersonRepository personRepository = new PersonRepository();
    private final League league = new Bundesliga();

    public SampleTreeTableComponent() {

        PmoBasedSectionFactory pmoBasedSectionFactory = new PmoBasedSectionFactory();

        TabSheetArea tabSheetArea = new SingleSectionTabSheetArea(pmoBasedSectionFactory, area -> {
            // for a smaller, easy to debug, sample, copy the package
            // linkki-core/src/test/java/org.linkki.core.ui.table.hierarchy and add
            // area.addTab(new CodeTablePmo(), "codes");
            area.addTab(new PersonTablePmo(personRepository::getPersons), "static");
            area.addTab(new LeagueTablePmo(league), "dynamic");
        });
        tabSheetArea.init();

        addComponent(tabSheetArea);
    }

    @Override
    public String getSidebarSheetName() {
        return ID;
    }

    @Override
    public Resource getSidebarSheetIcon() {
        return VaadinIcons.TREE_TABLE;
    }

    @Override
    public String getSidebarSheetId() {
        return ID;
    }

    private static final class SingleSectionTabSheetArea extends TabSheetArea {
        private final PmoBasedSectionFactory pmoBasedSectionFactory;
        private static final long serialVersionUID = 1L;
        private Consumer<SingleSectionTabSheetArea> contentCreator;

        private SingleSectionTabSheetArea(PmoBasedSectionFactory pmoBasedSectionFactory,
                Consumer<SingleSectionTabSheetArea> contentCreator) {
            this.pmoBasedSectionFactory = pmoBasedSectionFactory;
            this.contentCreator = contentCreator;
        }

        @Override
        public void updateContent() {
            reloadBindings();
        }

        @Override
        public void createContent() {
            contentCreator.accept(this);
        }

        public Tab addTab(Object pmo, String caption) {
            SingleSectionPage tabPage = new SingleSectionPage(pmoBasedSectionFactory, pmo);
            tabPage.init();
            return super.addTab(tabPage, caption);
        }
    }

    private static final class SingleSectionPage extends AbstractPage {
        private static final long serialVersionUID = 1L;
        BindingManager bindingManager = new DefaultBindingManager(ValidationService.NOP_VALIDATION_SERVICE);
        private Object pmo;

        private SingleSectionPage(PmoBasedSectionFactory sectionFactory, Object pmo) {
            super(sectionFactory);
            this.pmo = pmo;
        }

        @Override
        public void createContent() {
            addSection(pmo);
        }

        @Override
        protected BindingManager getBindingManager() {
            return bindingManager;
        }
    }
}