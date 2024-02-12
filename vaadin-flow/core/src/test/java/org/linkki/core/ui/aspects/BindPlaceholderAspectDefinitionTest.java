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

package org.linkki.core.ui.aspects;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.aspects.types.PlaceholderType;
import org.linkki.core.ui.creation.table.AbstractGridComponentWrapper;
import org.linkki.core.ui.creation.table.GridComponentCreator;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextArea;

class BindPlaceholderAspectDefinitionTest {

    private final PropertyDispatcher dispatcher = new PropertyDispatcherFactory()
            .createDispatcherChain(new TestObject(), BoundProperty.empty(),
                                   PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

    @Test
    void testPlaceholderType_Static() {
        TextArea area = new TextArea();

        callPlaceholderAspectDefinition(area, "A nice static Placeholder", PlaceholderType.STATIC);
        String placeholderResult = area.getPlaceholder();

        assertThat(placeholderResult).isEqualTo("A nice static Placeholder");
    }

    @Test
    void testPlaceholderType_Dynamic() {
        TextArea area = new TextArea();

        callPlaceholderAspectDefinition(area, "I can be changed", PlaceholderType.DYNAMIC);
        String placeholderResult = area.getPlaceholder();

        assertThat(placeholderResult).isEqualTo("I am the superior placeholder");
    }

    @Test
    void testPlaceholderType_AutoEmptyPlaceholder() {
        TextArea area = new TextArea();

        callPlaceholderAspectDefinition(area, "", PlaceholderType.AUTO);
        String placeholderResult = area.getPlaceholder();

        assertThat(placeholderResult).isEqualTo("I am the superior placeholder");
    }

    @Test
    void testPlaceholderType_AutoFilledPlaceholder() {
        TextArea area = new TextArea();

        callPlaceholderAspectDefinition(area, "I am a placeholder", PlaceholderType.AUTO);
        String suffixResult = area.getPlaceholder();

        assertThat(suffixResult).isEqualTo("I am a placeholder");
    }

    @Test
    void testHasPlaceholder() {
        TestGridPmo pmo = new TestGridPmo();

        Grid<?> grid = applyPlaceholderAspectDefinition(pmo, "Hidden placeholder");

        assertThat(grid.getElement().hasAttribute(PlaceholderAspectDefinition.HAS_PLACEHOLDER)).isTrue();
    }

    @Test
    void testHasPlaceholder_WhenNull_ThenFalse() {
        TestGridPmo pmo = new TestGridPmo();

        Grid<?> grid = applyPlaceholderAspectDefinition(pmo, null);

        assertThat(grid.getElement().hasAttribute(PlaceholderAspectDefinition.HAS_PLACEHOLDER)).isFalse();
    }

    @Test
    void testHasPlaceholder_WhenEmpty_ThenTrue() {
        TestGridPmo pmo = new TestGridPmo();

        Grid<?> grid = applyPlaceholderAspectDefinition(pmo, "");

        assertThat(grid.getElement().hasAttribute(PlaceholderAspectDefinition.HAS_PLACEHOLDER)).isTrue();
    }

    private Grid<?> applyPlaceholderAspectDefinition(ContainerPmo<?> pmo, String value) {
        AbstractGridComponentWrapper<?> componentWrapper = GridComponentCreator.createComponentWrapper(pmo,
                                                                                                       new Grid<>());
        new PlaceholderAspectDefinition(value, PlaceholderType.STATIC)
                .createUiUpdater(dispatcher, componentWrapper).apply();
        return componentWrapper.getComponent();
    }

    private void callPlaceholderAspectDefinition(TextArea area, String value, PlaceholderType placeholderType) {
        PlaceholderAspectDefinition aspectDefinition = new PlaceholderAspectDefinition(value, placeholderType);
        createUiUpdaterAndApplyIt(aspectDefinition, area);
    }

    private void createUiUpdaterAndApplyIt(PlaceholderAspectDefinition aspectDefinition, TextArea area) {
        aspectDefinition.createUiUpdater(dispatcher, new NoLabelComponentWrapper(area)).apply();
    }

    private static class TestObject {

        @SuppressWarnings("unused")
        public String getPlaceholder() {
            return "I am the superior placeholder";
        }

    }

    static class TestGridPmo implements ContainerPmo<String> {

        @Override
        public List<String> getItems() {
            return Collections.emptyList();
        }

    }

}
