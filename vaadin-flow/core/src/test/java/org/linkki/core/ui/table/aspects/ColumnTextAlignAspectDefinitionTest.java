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

package org.linkki.core.ui.table.aspects;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.linkki.core.ui.aspects.types.TextAlignment;
import org.linkki.core.ui.creation.table.GridColumnWrapper;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid.Column;

public class ColumnTextAlignAspectDefinitionTest {

    @Test
    public void testCreateComponentValueSetter_Default() {
        ColumnTextAlignAspectDefinition aspectDefinition = new ColumnTextAlignAspectDefinition(TextAlignment.DEFAULT);
        Column<?> column = mock(Column.class);
        Consumer<TextAlignment> valueSetter = aspectDefinition
                .createComponentValueSetter(new GridColumnWrapper(column));

        valueSetter.accept(TextAlignment.DEFAULT);

        // DEFAULT should not set anything
        verifyNoInteractions(column);
    }

    @ParameterizedTest
    @MethodSource("alignments")
    public void testCreateComponentValueSetter_Start(TextAlignment value, ColumnTextAlign expected) {
        ColumnTextAlignAspectDefinition aspectDefinition = new ColumnTextAlignAspectDefinition(value);
        Column<?> column = mock(Column.class);
        Consumer<TextAlignment> valueSetter = aspectDefinition
                .createComponentValueSetter(new GridColumnWrapper(column));

        valueSetter.accept(value);

        verify(column).setTextAlign(expected);
        verifyNoMoreInteractions(column);
    }

    private static Stream<Arguments> alignments() {
        return Stream.of(
                         Arguments.of(TextAlignment.LEFT, ColumnTextAlign.START),
                         Arguments.of(TextAlignment.CENTER, ColumnTextAlign.CENTER),
                         Arguments.of(TextAlignment.RIGHT, ColumnTextAlign.END));
    }

}
