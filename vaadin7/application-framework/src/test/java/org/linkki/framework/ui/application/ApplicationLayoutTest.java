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
package org.linkki.framework.ui.application;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.framework.state.ApplicationConfig;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vaadin.navigator.Navigator.EmptyView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

@ExtendWith(MockitoExtension.class)
public class ApplicationLayoutTest {

    @Mock
    private UI ui;


    @Mock
    private Page page;


    @Mock
    private ApplicationHeader header;


    @Mock
    private ApplicationFooter footer;


    @Mock
    private ApplicationConfig config;


    @Mock
    private ViewProvider viewProvider;


    private ApplicationLayout applicationLayout;

    private void setUpApplicationLayout() {
        applicationLayout = new ApplicationLayout(header, footer);
    }

    @Test
    public void testGetCurrentView_empty() {
        setUpApplicationLayout();

        Component currentView = applicationLayout.getCurrentView();

        assertThat(currentView, is(instanceOf(EmptyView.class)));
    }

    @Test
    public void testGetCurrentView() {
        setUpApplicationLayout();
        View view = mock(View.class, withSettings().extraInterfaces(Component.class));
        applicationLayout.showView(view);

        Component currentView = applicationLayout.getCurrentView();

        assertThat(currentView, is(view));
    }

    @Test
    public void testShowView() {
        setUpApplicationLayout();
        assertThat(applicationLayout.getComponentCount(), is(3));
        assertThat(applicationLayout.getComponent(1), is(instanceOf(EmptyView.class)));

        View view = mock(View.class, withSettings().extraInterfaces(Component.class));
        applicationLayout.showView(view);

        assertThat(applicationLayout.getComponentCount(), is(3));
        assertThat(applicationLayout.getComponent(1), is(view));

        View view2 = mock(View.class, withSettings().extraInterfaces(Component.class));
        applicationLayout.showView(view2);

        assertThat(applicationLayout.getComponentCount(), is(3));
        assertThat(applicationLayout.getComponent(1), is(view2));
    }

    @Test
    public void testShowView_NoFooter() {
        applicationLayout = new ApplicationLayout(header, null);

        assertThat(applicationLayout.getComponentCount(), is(2));
        assertThat(applicationLayout.getComponent(1), is(instanceOf(EmptyView.class)));

        View view = mock(View.class, withSettings().extraInterfaces(Component.class));
        applicationLayout.showView(view);

        assertThat(applicationLayout.getComponentCount(), is(2));
        assertThat(applicationLayout.getComponent(1), is(view));

        View view2 = mock(View.class, withSettings().extraInterfaces(Component.class));
        applicationLayout.showView(view2);

        assertThat(applicationLayout.getComponentCount(), is(2));
        assertThat(applicationLayout.getComponent(1), is(view2));
    }

}
