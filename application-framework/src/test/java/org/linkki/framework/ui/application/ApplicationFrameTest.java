/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.framework.ui.application;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.linkki.test.matcher.Matchers.absent;
import static org.linkki.test.matcher.Matchers.hasValue;
import static org.linkki.test.matcher.Matchers.present;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationFrameTest {

    @SuppressWarnings("null")
    @Mock
    UI ui;

    @SuppressWarnings("null")
    @Mock
    Page page;

    @SuppressWarnings("null")
    @Mock
    ApplicationHeader header;

    @SuppressWarnings("null")
    @Mock
    ApplicationFooter footer;

    @SuppressWarnings("null")
    @Mock
    CDIViewProvider viewProvider;

    @SuppressWarnings("null")
    @InjectMocks
    ApplicationFrame applicationFrame;

    private void setUpApplicationFrame() {
        when(ui.getPage()).thenReturn(page);
        applicationFrame.init(ui);
    }

    @Test
    public void testGetCurrentView_empty() {
        setUpApplicationFrame();

        Optional<Component> currentView = applicationFrame.getCurrentView();

        assertThat(currentView, is(absent()));
    }

    @Test
    public void testGetCurrentView() {
        setUpApplicationFrame();
        VerticalLayout mainArea = (VerticalLayout)applicationFrame.getContent().getComponent(1);
        Component c = mock(Component.class);
        mainArea.addComponent(c);

        Optional<Component> currentView = applicationFrame.getCurrentView();

        assertThat(currentView, is(present()));
        assertThat(currentView, hasValue(c));
    }

}
