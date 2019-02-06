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

package org.linkki.core.ui.components;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Test;
import org.linkki.core.message.Message;
import org.linkki.core.message.MessageList;
import org.mockito.ArgumentCaptor;

import com.vaadin.server.ErrorMessage;
import com.vaadin.server.ErrorMessage.ErrorLevel;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

public class CaptionComponentWrapperTest {

    @Test
    public void testSetId() {
        Component component = mock(Component.class);
        CaptionComponentWrapper wrapper = new CaptionComponentWrapper("testID", component,
                WrapperType.FIELD);

        verify(component).setId("testID");

        wrapper.setId("anotherId");

        verify(component).setId("anotherId");
    }

    @Test
    public void testSetLabel() {
        Component component = mock(Component.class);
        CaptionComponentWrapper wrapper = new CaptionComponentWrapper("testID", component,
                WrapperType.FIELD);

        wrapper.setLabel("testLabel");

        verify(component).setCaption("testLabel");
    }

    @Test
    public void testSetEnabled() {
        Component component = mock(Component.class);
        CaptionComponentWrapper wrapper = new CaptionComponentWrapper("testID", component,
                WrapperType.FIELD);

        wrapper.setEnabled(true);

        verify(component).setEnabled(true);

        wrapper.setEnabled(false);

        verify(component).setEnabled(false);
    }

    @Test
    public void testSetVisible() {
        Component component = mock(Component.class);
        CaptionComponentWrapper wrapper = new CaptionComponentWrapper("testID", component,
                WrapperType.FIELD);

        wrapper.setVisible(true);

        verify(component).setVisible(true);

        wrapper.setVisible(false);

        verify(component).setVisible(false);
    }

    @Test
    public void testSetTooltip_NotOnVanillaComponent() {
        Component component = mock(Component.class);
        CaptionComponentWrapper wrapper = new CaptionComponentWrapper("testID", component,
                WrapperType.FIELD);
        verify(component).setId("testID");
        verifyNoMoreInteractions(component);

        wrapper.setTooltip("testTip");
    }

    @Test
    public void testSetTooltip() {
        AbstractComponent component = mock(AbstractComponent.class);
        CaptionComponentWrapper wrapper = new CaptionComponentWrapper("testID", component,
                WrapperType.FIELD);

        wrapper.setTooltip("testTip");

        verify(component).setDescription("testTip");
    }

    @Test
    public void testGetComponent() {
        Component component = mock(Component.class);
        CaptionComponentWrapper wrapper = new CaptionComponentWrapper("testID", component,
                WrapperType.FIELD);

        assertThat(wrapper.getComponent(), is(sameInstance(component)));
    }

    @Test
    public void testSetValidationMessages_NotOnVanillaComponent() {
        Component component = mock(Component.class);
        CaptionComponentWrapper wrapper = new CaptionComponentWrapper("testID", component,
                WrapperType.FIELD);
        verify(component).setId("testID");

        MessageList messages = new MessageList(Message.newError("e", "testError"));
        verifyNoMoreInteractions(component);
        wrapper.setValidationMessages(messages);
    }

    @Test
    public void testSetValidationMessages() {
        AbstractComponent component = mock(AbstractComponent.class);
        CaptionComponentWrapper wrapper = new CaptionComponentWrapper("testID", component,
                WrapperType.FIELD);
        ArgumentCaptor<ErrorMessage> errorMessageCaptor = ArgumentCaptor.forClass(ErrorMessage.class);

        MessageList messages = new MessageList(Message.newError("e", "testError"),
                Message.newWarning("w", "testWarning"));
        wrapper.setValidationMessages(messages);

        verify(component).setComponentError(errorMessageCaptor.capture());
        @SuppressWarnings("null")
        @NonNull
        ErrorMessage errorMessage = errorMessageCaptor.getValue();
        assertThat(errorMessage.getErrorLevel(), is(ErrorLevel.ERROR));
        assertThat(errorMessage.getFormattedHtmlMessage(), containsString("testError"));
        assertThat(errorMessage.getFormattedHtmlMessage(), containsString("testWarning"));
    }

    @Test
    public void testSetValidationMessages_Empty() {
        AbstractComponent component = mock(AbstractComponent.class);
        CaptionComponentWrapper wrapper = new CaptionComponentWrapper("testID", component,
                WrapperType.FIELD);

        MessageList messages = new MessageList();
        wrapper.setValidationMessages(messages);

        verify(component).setComponentError(null);
    }

    @Test
    public void testGetType() {
        Component component = mock(Component.class);

        assertThat(new CaptionComponentWrapper("testID", component, WrapperType.FIELD).getType(),
                   is(WrapperType.FIELD));
        assertThat(new CaptionComponentWrapper("testID", component, WrapperType.LAYOUT).getType(),
                   is(WrapperType.LAYOUT));
    }

}
