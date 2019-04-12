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
package org.linkki.samples.treetable.ui;

import java.util.function.Consumer;

import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.vaadin.component.area.TabSheetArea;
import org.linkki.core.vaadin.component.page.AbstractPage;
import org.linkki.samples.treetable.dynamic.model.Bundesliga;
import org.linkki.samples.treetable.dynamic.model.League;
import org.linkki.samples.treetable.dynamic.pmo.LeagueTablePmo;
import org.linkki.samples.treetable.fixed.model.PersonRepository;
import org.linkki.samples.treetable.fixed.pmo.PersonTablePmo;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.UI;

@Theme(value = "valo")
@Widgetset("com.vaadin.v7.Vaadin7WidgetSet")
public class SampleTreeTableUI extends UI {

    private static final long serialVersionUID = 1L;
    private final PersonRepository personRepository = new PersonRepository();
    private final League league = new Bundesliga();

    @Override
    protected void init(VaadinRequest request) {

        Page.getCurrent().setTitle("linkki :: TreeTable Sample");

        PmoBasedSectionFactory pmoBasedSectionFactory = new PmoBasedSectionFactory();

        TabSheetArea tabSheetArea = new SingleSectionTabSheetArea(pmoBasedSectionFactory, area -> {
            // for a smaller, easy to debug, sample, copy the package
            // linkki-core/src/test/java/org.linkki.core.ui.table.hierarchy and add
            // area.addTab(new CodeTablePmo(), "codes");
            area.addTab(new PersonTablePmo(personRepository::getPersons), "static");
            area.addTab(new LeagueTablePmo(league), "dynamic");
        });
        tabSheetArea.createContent();

        setContent(tabSheetArea);
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
            tabPage.createContent();
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