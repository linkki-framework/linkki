/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

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

    @Mock
    UI ui;

    @Mock
    Page page;

    @Mock
    ApplicationHeader header;

    @Mock
    ApplicationFooter footer;

    @Mock
    CDIViewProvider viewProvider;

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
