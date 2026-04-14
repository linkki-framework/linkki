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
package org.linkki.framework.ui.component;

import static com.vaadin.flow.dom.Style.Overflow.AUTO;

import java.io.Serial;

import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.vaadin.component.section.GridSection;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * A layout to display messages. Can be used in a {@link MessagesSplitLayout}.
 * 
 * @since 2.10.0
 */
public class MessagesPanel extends GridSection {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates a {@link MessagesPanel} with default caption "Messages"
     */
    public MessagesPanel() {
        this("Messages");
    }

    public MessagesPanel(String caption) {
        super(caption, false);
        var grid = new Grid<>(Message.class, false);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS,
                              GridVariant.LUMO_COMPACT, GridVariant.LUMO_WRAP_CELL_CONTENT);
        var column = grid.addColumn(new ComponentRenderer<>(MessageUiComponents::createMessageComponent));
        column.setKey("message");
        grid.setAllRowsVisible(true);
        grid.setWidthFull();
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.getStyle().setOverflow(AUTO);

        setGrid(grid);
        addClassName(LumoUtility.Padding.MEDIUM);

        showMessages(new MessageList());
    }

    /**
     * Displays the given messages.
     */
    public void showMessages(MessageList messages) {
        var newItems = messages.stream().toList();
        var items = getGrid().getListDataView().getItems().toList();
        if (!items.equals(newItems)) {
            getGrid().setItems(newItems);
        }
        getGrid().setVisible(!messages.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Grid<Message> getGrid() {
        return (Grid<Message>)super.getGrid();
    }
}
