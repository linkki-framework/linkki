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

package org.linkki.core.binding.aspect;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.components.WrapperType;
import org.linkki.util.handler.Handler;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AspectUpdatersTest {

    @Mock
    @SuppressWarnings("null")
    private LinkkiAspectDefinition aspect1;
    @Mock
    @SuppressWarnings("null")
    private LinkkiAspectDefinition aspect2NotSupported;
    @Mock
    @SuppressWarnings("null")
    private LinkkiAspectDefinition aspect3;
    @Mock
    @SuppressWarnings("null")
    private Handler updater1;
    @Mock
    @SuppressWarnings("null")
    private Handler updater2;
    @Mock
    @SuppressWarnings("null")
    private Handler updater3;
    @Mock
    @SuppressWarnings("null")
    private PropertyDispatcher propertyDispatcher;
    @Mock
    @SuppressWarnings("null")
    private ComponentWrapper componentWrapper;
    private Handler modelChanged = Handler.NOP_HANDLER;
    @SuppressWarnings("null")
    private AspectUpdaters aspectUpdaters;

    @Before
    public void setUp() {
        when(aspect1.supports(WrapperType.FIELD)).thenReturn(true);
        when(aspect3.supports(WrapperType.FIELD)).thenReturn(true);
        when(aspect1.createUiUpdater(propertyDispatcher, componentWrapper)).thenReturn(updater1);
        when(aspect3.createUiUpdater(propertyDispatcher, componentWrapper)).thenReturn(updater3);

        when(componentWrapper.getType()).thenReturn(WrapperType.FIELD);

        aspectUpdaters = new AspectUpdaters(Arrays.asList(aspect1, aspect2NotSupported, aspect3), propertyDispatcher,
                componentWrapper, modelChanged);
    }

    @Test
    public void testAspectUpdaters_initializesAspectsAndCreatesUiUpdaterForSupportedTypes() {
        verify(aspect1, times(2)).supports(WrapperType.FIELD);
        verify(aspect2NotSupported, times(2)).supports(WrapperType.FIELD);
        verify(aspect3, times(2)).supports(WrapperType.FIELD);

        verify(aspect1).initModelUpdate(propertyDispatcher, componentWrapper, modelChanged);
        verify(aspect2NotSupported, never()).initModelUpdate(propertyDispatcher, componentWrapper, modelChanged);
        verify(aspect3).initModelUpdate(propertyDispatcher, componentWrapper, modelChanged);

        verify(aspect1).createUiUpdater(propertyDispatcher, componentWrapper);
        verify(aspect2NotSupported, never()).createUiUpdater(propertyDispatcher, componentWrapper);
        verify(aspect3).createUiUpdater(propertyDispatcher, componentWrapper);
    }

    @Test
    public void testUpdateUI_OnlySupportedTypes() {
        aspectUpdaters.updateUI();

        verify(updater1).apply();
        verify(updater2, never()).apply();
        verify(updater3).apply();
    }

}
