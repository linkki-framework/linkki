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
package org.linkki.samples.playground.ts.components.board;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.ui.creation.table.GridComponentCreator;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UILink;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UIFormSection;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.router.Route;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import org.linkki.core.vaadin.component.board.BoardComponent;
import org.linkki.core.vaadin.component.board.BoardLayout;
import org.linkki.samples.playground.ts.components.NavigationButtonPmo;
import org.linkki.samples.playground.ui.PlaygroundAppLayout;

@SuppressWarnings("deprecation")
@Route(value = SampleBoardView.NAME, layout = PlaygroundAppLayout.class)

// tag::board_layout[]
public class SampleBoardView extends BoardLayout {
// end::board_layout[]

    public static final String NAME = "board";

    @Serial
    private static final long serialVersionUID = 1L;


    // tag::board_layout[]
    public SampleBoardView() {
        super(new BoardComponent("Zuletzt bearbeitet", createTable()),
                BoardComponent.withPmo("Policensuche", new TestBoardPmo()),
                BoardComponent.withPmo("Postkorb", new TestTablePmo(), BoardComponent.BoardComponentVariant.LARGE));
    }

    private static Grid<RecentlyEditedRowPmo> createTable() {
        Grid<RecentlyEditedRowPmo> createGrid = GridComponentCreator.createGrid(new RecentlyEditedTable(),
                                                                                new BindingContext());
        createGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        return createGrid;
    }
    // end::board_layout[]

    public static Object getNavigationPmo() {
        return new NavigationButtonPmo(SampleBoardView.NAME);
    }

    public static class RecentlyEditedTable implements ContainerPmo<RecentlyEditedRowPmo> {

        @Override
        public List<RecentlyEditedRowPmo> getItems() {
            return Arrays.asList(new RecentlyEditedRowPmo("123123213", "lobster", "won"),
                                 new RecentlyEditedRowPmo("233423413", "lobster", "won"));
        }

        @Override
        public int getPageLength() {
            return 5;
        }
    }

    public static class RecentlyEditedRowPmo {

        private String policennummer;
        private String lob;
        private String process;

        public RecentlyEditedRowPmo(String policennummer, String lob, String process) {
            super();
            this.policennummer = policennummer;
            this.lob = lob;
            this.process = process;
        }

        @UILink(position = 0, label = "Policennummer", captionType = CaptionType.DYNAMIC)
        public String getPolicennummer() {
            return "";
        }

        public String getPolicennummerCaption() {
            return policennummer;
        }

        @UILabel(position = 1, label = "LoB")
        public String getLob() {
            return lob;
        }

        @UILabel(position = 2, label = "Process")
        public String getProcess() {
            return process;
        }
    }

    @UIFormSection
    public static class TestBoardPmo {

        @CheckForNull
        private String policennummer;
        @CheckForNull
        private String fin;
        @CheckForNull
        private String akz;

        @CheckForNull
        @UITextField(position = 0)
        public String getPolicennummer() {
            return policennummer;
        }

        public void setPolicennummer(String policennummer) {
            this.policennummer = policennummer;
        }

        @CheckForNull
        @UITextField(position = 1)
        public String getFin() {
            return fin;
        }

        public void setFin(String fin) {
            this.fin = fin;
        }

        @UITextField(position = 2)
        public String getKennzeichen() {
            return akz;
        }

        public void setKennzeichen(String akz) {
            this.akz = akz;
        }

        @UIButton(position = 3, caption = "Search")
        public void button() {
            // does nothing
        }
    }

    @UISection
    public static class TestTablePmo implements ContainerPmo<TestBoardPmo> {

        @Override
        public List<TestBoardPmo> getItems() {
            return Arrays.asList(new TestBoardPmo(), new TestBoardPmo(), new TestBoardPmo());
        }

        @Override
        public int getPageLength() {
            return 0;
        }

    }
// tag::board_layout[]
}
// end::board_layout[]

