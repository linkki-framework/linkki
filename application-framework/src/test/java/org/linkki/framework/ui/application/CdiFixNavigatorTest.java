/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.application;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

@RunWith(MockitoJUnitRunner.class)
public class CdiFixNavigatorTest {

    @SuppressWarnings("null")
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private UI ui;

    @SuppressWarnings("null")
    @Mock
    private ComponentContainer container;

    @SuppressWarnings("null")
    private CdiFixNavigator navigator;

    @Test
    public void testGetViewName() {
        navigator = new CdiFixNavigator(ui, container);

        assertThat(navigator.getViewName(""), is(equalTo("")));
        assertThat(navigator.getViewName("/"), is(equalTo("")));
        assertThat(navigator.getViewName("foo"), is(equalTo("foo")));
        assertThat(navigator.getViewName("foo/bar"), is(equalTo("foo")));
        assertThat(navigator.getViewName("foo/bar?ycb=sadf&fsf"), is(equalTo("foo")));
    }

}
