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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;

import com.vaadin.flow.component.textfield.TextArea;

public class BindVisibleAspectDefinitionTest {

    private final PropertyDispatcher dispatcher = new PropertyDispatcherFactory()
            .createDispatcherChain(new TestObject(), BoundProperty.empty(),
                                   PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

    @Test
    public void testVisibleType_Visible() {
        TextArea area = createVisibleComponent();
        area.setVisible(false);

        createUiUpdaterAndApplyIt_BindVisibleAspect(area, VisibleType.VISIBLE);

        assertThat(area.isVisible(), is(true));
    }

    @Test
    public void testVisibleType_Invisible() {
        TextArea area = createVisibleComponent();
        area.setVisible(true);

        createUiUpdaterAndApplyIt_BindVisibleAspect(area, VisibleType.INVISIBLE);

        assertThat(area.isVisible(), is(false));
    }

    @Test
    public void testVisibleType_Dynamic() {
        TextArea area = createVisibleComponent();
        area.setVisible(false);
        assertThat(area.isVisible(), is(false));

        createUiUpdaterAndApplyIt_BindVisibleAspect(area, VisibleType.DYNAMIC);

        // TestObject#isVisible() -> true
        assertThat(area.isVisible(), is(true));
    }

    private TextArea createVisibleComponent() {
        TextArea text = new TextArea();
        text.setVisible(true);
        return text;
    }

    private void createUiUpdaterAndApplyIt_BindVisibleAspect(TextArea area, VisibleType value) {
        VisibleAspectDefinition aspectDefinition = new VisibleAspectDefinition(value);
        createUiUpdaterAndApplyIt(area, aspectDefinition);
    }

    private void createUiUpdaterAndApplyIt(TextArea area, LinkkiAspectDefinition aspectDefinition) {
        aspectDefinition.createUiUpdater(dispatcher, new NoLabelComponentWrapper(area)).apply();
    }

    private static class TestObject {

        @SuppressWarnings("unused")
        public boolean isVisible() {
            return true;
        }

    }

}
