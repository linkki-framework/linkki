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

package org.linkki.core.ui.table.aspects;

import org.linkki.core.ui.aspects.types.TextAlignment;
import org.linkki.core.ui.table.column.aspects.StaticColumnAspectDefinition;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid.Column;

public class ColumnTextAlignAspectDefinition extends StaticColumnAspectDefinition<TextAlignment> {

    public static final String NAME = "textAlignment";

    public ColumnTextAlignAspectDefinition(TextAlignment textAlignment) {
        super(NAME, textAlignment, (cw, a) -> setTextAlign(cw.getComponent(), a));
    }

    private static void setTextAlign(Column<?> column, TextAlignment textAlignment) {
        if (textAlignment != TextAlignment.DEFAULT) {
            column.setTextAlign(getColumnTextAlign(textAlignment));
        }
    }

    private static ColumnTextAlign getColumnTextAlign(TextAlignment alignment) {
        switch (alignment) {
            case START:
                return ColumnTextAlign.START;
            case CENTER:
                return ColumnTextAlign.CENTER;
            case END:
                return ColumnTextAlign.END;
            default:
                throw new IllegalArgumentException("Invalid text alignment: " + alignment.name());
        }
    }

}
