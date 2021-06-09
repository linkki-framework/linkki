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

package org.linkki.samples.playground.table.uitablecolumn;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.creation.table.GridComponentCreator;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.nested.annotation.UINestedComponent;
import org.linkki.core.ui.table.column.annotation.UITableColumn;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class UITableColumnTableSection {

    public static final String TITLE = "Columns With UITableColumn Annotation";
    public static final String DESCRIPTION = "To test width and flexGrow in @UITableColumn";
    public static final List<String> ASPECTS = Arrays.asList("Column 1 should have a width of 50px",
                                                             "Column 2 should have 3 times more excess width than column 4 and 5 (base width is somehow 100px if undefined)",
                                                             "Column 3 and column 4 should have the same width",
                                                             "Column 5 should have base width of 200px and the same amount of excess width as column 4 and 5");

    public static Component create() {
        BindingContext bindingContext = new BindingContext(UITableColumnTableSection.class.getSimpleName());
        VerticalLayout content = new VerticalLayout();
        content.add(new H3(UITableColumnTableSection.TITLE));
        content.add(new Span(UITableColumnTableSection.DESCRIPTION));
        UnorderedList aspects = new UnorderedList();
        ASPECTS.stream().map(s -> new ListItem(s)).forEach(aspects::add);
        content.add(aspects);
        content.add(GridComponentCreator.createGrid(new UITableColumnTablePmo(), bindingContext));
        return content;
    }

    @UISection(caption = UITableColumnTableSection.TITLE)
    public static class UITableColumnPmo {

        @UILabel(position = 10, label = "Description")
        public String getDescription() {
            return UITableColumnTableSection.DESCRIPTION;
        }

        @UINestedComponent(position = 20)
        public UITableColumnTablePmo getTable() {
            return new UITableColumnTablePmo();
        }
    }

    public static class UITableColumnTablePmo implements ContainerPmo<UITableColumnRowPmo> {
        @Override
        public List<UITableColumnRowPmo> getItems() {
            return Collections.singletonList(new UITableColumnRowPmo());
        }

        @Override
        public int getPageLength() {
            return 1;
        }
    }

    public static class UITableColumnRowPmo {

        @UITableColumn(width = 50)
        @UILabel(position = 10, label = "1")
        public String getColumnWithWidth() {
            return "50px";
        }

        @UITableColumn(flexGrow = 3)
        @UILabel(position = 20, label = "2")
        public String getColumnWithFlexGrow() {
            return "flex grow 3";
        }

        @UILabel(position = 30, label = "3")
        public String getColumnWithNoTableColumnAnnotation1() {
            return "No UITableColumn";
        }

        @UILabel(position = 40, label = "4")
        public String getColumnWithNoTableColumnAnnotation2() {
            return "No UITableColumn";
        }

        @UITableColumn(width = 200, flexGrow = 1)
        @UILabel(position = 50, label = "5")
        public String getColumnWithWidthAndFlexGrow() {
            return "200px & flex grow 1";
        }
    }
}
