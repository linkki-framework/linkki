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

package org.linkki.core.binding.aspect.definition;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.TestComponentWrapper;
import org.linkki.core.ui.TestUiComponent;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.util.handler.Handler;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ApplicableTypeAspectDefinitionTest {

    @SuppressWarnings("null")
    @Mock
    private PropertyDispatcher propertyDispatcher;

    @SuppressWarnings("null")
    @Mock
    private LinkkiAspectDefinition aspectDefinition;

    @SuppressWarnings("null")
    @Mock
    private Handler modelUpdated;

    private ComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());

    @Test
    public final void testCreateUiUpdater() {
        ApplicableTypeAspectDefinition aspectDef = ApplicableTypeAspectDefinition.ifComponentTypeIs(TestUiComponent.class,
                                                                                               aspectDefinition);

        when(aspectDefinition.createUiUpdater(propertyDispatcher, componentWrapper)).thenReturn(modelUpdated);
        Handler uiUpdater = aspectDef.createUiUpdater(propertyDispatcher, componentWrapper);

        verify(aspectDefinition).createUiUpdater(propertyDispatcher, componentWrapper);
        assertThat(uiUpdater, is(modelUpdated));
    }

    @Test
    public final void testCreateUiUpdater_NotApplicable() {
        ApplicableTypeAspectDefinition aspectDef = ApplicableTypeAspectDefinition.ifComponentTypeIs(String.class,
                                                                                               aspectDefinition);

        aspectDef.createUiUpdater(propertyDispatcher, componentWrapper);

        verify(aspectDefinition, never()).createUiUpdater(any(PropertyDispatcher.class), any(ComponentWrapper.class));
    }

    @Test
    public final void testInitModelUpdate() {
        ApplicableTypeAspectDefinition aspectDef = ApplicableTypeAspectDefinition.ifComponentTypeIs(TestUiComponent.class,
                                                                                               aspectDefinition);

        aspectDef.initModelUpdate(propertyDispatcher, componentWrapper, modelUpdated);

        verify(aspectDefinition).initModelUpdate(propertyDispatcher, componentWrapper, modelUpdated);
    }

    @Test
    public final void testInitModelUpdate_NotApplicable() {
        ApplicableTypeAspectDefinition aspectDef = ApplicableTypeAspectDefinition.ifComponentTypeIs(List.class,
                                                                                               aspectDefinition);

        aspectDef.initModelUpdate(propertyDispatcher, componentWrapper, modelUpdated);

        verify(aspectDefinition, never()).initModelUpdate(any(PropertyDispatcher.class), any(ComponentWrapper.class),
                                                          any(Handler.class));
    }
}
