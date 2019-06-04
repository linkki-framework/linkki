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

package org.linkki.core.vaadin.component.page;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractPageTest {

    @Mock
    private PmoBasedSectionFactory sectionFactory;

    @Mock
    private BindingManager bindingManager;

    @Mock
    private BindingContext bindingContext1;
    @Mock
    private BindingContext bindingContext2;

    private AbstractPage abstractPage;

    @Before
    public void setUp() {
        abstractPage = new AbstractPage(sectionFactory) {

            private static final long serialVersionUID = 1L;

            @Override
            public void createContent() {
                // no content
            }

            @Override
            protected BindingManager getBindingManager() {
                return bindingManager;
            }
        };
    }

    @Test
    public void testGetBindingContext() {
        when(bindingManager.getContext(abstractPage.getClass())).thenReturn(bindingContext1, bindingContext2);
        abstractPage.init();

        verify(bindingManager).getContext(abstractPage.getClass());

        abstractPage.getBindingContext();

        verifyNoMoreInteractions(bindingManager);
    }

}
