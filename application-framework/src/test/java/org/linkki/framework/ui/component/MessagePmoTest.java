/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.component;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.faktorips.runtime.Message;
import org.faktorips.runtime.ObjectProperty;
import org.junit.Test;

public class MessagePmoTest {

    private final ObjectProperty propError = new ObjectProperty(new Object(), "foo");
    private final ObjectProperty propWarning = new ObjectProperty(new Object());

    private final Message error = Message.error("error").invalidObject(propError).create();
    private final Message warning = Message.warning("warning").invalidObject(propWarning).create();
    private final Message info = Message.info("info").create();

    @Test
    public void testGetText() {
        assertThat(new MessagePmo(error).getText(), is("error"));
        assertThat(new MessagePmo(warning).getText(), is("warning"));
        assertThat(new MessagePmo(info).getText(), is("info"));
    }

    @Test
    public void testGetIcon() {
        assertThat(new MessagePmo(error).getIcon(), is(notNullValue()));
        assertThat(new MessagePmo(warning).getIcon(), is(notNullValue()));
        assertThat(new MessagePmo(info).getIcon(), is(notNullValue()));
    }

    @Test
    public void testGetStyle() {
        assertThat(new MessagePmo(error).getStyle(), endsWith("error"));
        assertThat(new MessagePmo(warning).getStyle(), endsWith("warning"));
        assertThat(new MessagePmo(info).getStyle(), endsWith("info"));
    }

    @Test
    public void testGetToolTip() {
        assertThat(new MessagePmo(error).getToolTip(), is("Object: foo"));
        assertThat(new MessagePmo(warning).getToolTip(), is("Object"));
        assertThat(new MessagePmo(info).getToolTip(), is(""));
    }

}
