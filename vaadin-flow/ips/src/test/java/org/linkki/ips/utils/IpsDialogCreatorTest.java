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
package org.linkki.ips.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.framework.ui.dialogs.PmoBasedDialogFactory;
import org.linkki.ips.binding.dispatcher.IpsPropertyDispatcherFactory;

class IpsDialogCreatorTest {

    @Test
    void testDialogFactory_ContainsOnlyIpsPropertyDispatcherFactory() throws Exception {
        var factory = IpsDialogCreator.create();

        var dispatcherFactoryField = PmoBasedDialogFactory.class.getDeclaredField("propertyDispatcherFactory");
        dispatcherFactoryField.setAccessible(true);
        var actualDispatcherFactory = (PropertyDispatcherFactory)dispatcherFactoryField.get(factory);

        assertThat(actualDispatcherFactory).isInstanceOf(IpsPropertyDispatcherFactory.class);
    }

    @Test
    void testDialogFactory_ContainsValidationServiceAndIpsPropertyDispatcherFactory() throws Exception {
        var mockValidationService = mock(ValidationService.class);
        var factory = IpsDialogCreator.with(mockValidationService);

        var dispatcherFactoryField = PmoBasedDialogFactory.class.getDeclaredField("propertyDispatcherFactory");
        dispatcherFactoryField.setAccessible(true);
        var actualDispatcherFactory = (PropertyDispatcherFactory)dispatcherFactoryField.get(factory);

        var validationServiceField = PmoBasedDialogFactory.class.getDeclaredField("validationService");
        validationServiceField.setAccessible(true);
        var actualValidationService = (ValidationService)validationServiceField.get(factory);

        assertThat(actualDispatcherFactory).isInstanceOf(IpsPropertyDispatcherFactory.class);
        assertThat(actualValidationService).isSameAs(mockValidationService);
    }
}
