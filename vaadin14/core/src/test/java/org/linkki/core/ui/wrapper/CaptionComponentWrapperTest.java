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

package org.linkki.core.ui.wrapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.wrapper.WrapperType;
import org.mockito.ArgumentCaptor;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;

import edu.umd.cs.findbugs.annotations.NonNull;

public class CaptionComponentWrapperTest {

    @Test
    public void testSetId() {
        Span component = spy(new Span());
        CaptionComponentWrapper wrapper = new CaptionComponentWrapper("testID", component,
                WrapperType.FIELD);

        verify(component).setId("testID");

        wrapper.setId("anotherId");

        verify(component).setId("anotherId");
    }

    @Test
    public void testSetLabel() {
        Span component = spy(new Span());
        CaptionComponentWrapper wrapper = new CaptionComponentWrapper("testID", component,
                WrapperType.FIELD);

        wrapper.setLabel("testLabel");

        // TODO LIN-2057
        // verify(component).setText("testLabel");
    }

    @Test
    public void testSetLabel_EmptyString() {
        Span component = new Span();
        CaptionComponentWrapper wrapper = new CaptionComponentWrapper("testID", component,
                WrapperType.FIELD);

        wrapper.setLabel("");

        assertThat(component.getText(), is(""));
    }

    @Test
    public void testSetEnabled() {
        Button button = spy(new Button());
        CaptionComponentWrapper wrapper = new CaptionComponentWrapper("testID", button,
                WrapperType.FIELD);

        wrapper.setEnabled(true);

        verify(button).setEnabled(true);

        wrapper.setEnabled(false);

        verify(button).setEnabled(false);
    }

    @Test
    public void testSetVisible() {
        Button button = spy(new Button());
        CaptionComponentWrapper wrapper = new CaptionComponentWrapper("testID", button,
                WrapperType.FIELD);

        wrapper.setVisible(true);

        verify(button).setVisible(true);

        wrapper.setVisible(false);

        verify(button).setVisible(false);
    }

    // TODO LIN-2054
    // @Test
    // public void testSetTooltip_NotOnVanillaComponent() {
    // Component component = mock(Component.class);
    // CaptionComponentWrapper wrapper = new CaptionComponentWrapper("testID", component,
    // WrapperType.FIELD);
    // verify(component).setId("testID");
    // verifyNoMoreInteractions(component);
    //
    // wrapper.setTooltip("testTip");
    // }

    // TODO LIN-2054
    // @Test
    // public void testSetTooltip() {
    // AbstractComponent component = mock(AbstractComponent.class);
    // CaptionComponentWrapper wrapper = new CaptionComponentWrapper("testID", component,
    // WrapperType.FIELD);
    //
    // wrapper.setTooltip("testTip");
    // verify(component).setDescription("testTip", ContentMode.HTML);
    //
    // wrapper.setTooltip("<script>");
    // verify(component).setDescription("&lt;script&gt;", ContentMode.HTML);
    //
    // wrapper.setTooltip("<div>");
    // }

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
        TextField component = spy(new TextField());
        CaptionComponentWrapper wrapper = new CaptionComponentWrapper("testID", component,
                WrapperType.FIELD);
        ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass(String.class);

        MessageList messages = new MessageList(Message.newError("e", "testError"),
                Message.newWarning("w", "testWarning"));
        wrapper.setValidationMessages(messages);

        verify(component).setErrorMessage(errorMessageCaptor.capture());

        @NonNull
        String errorMessage = errorMessageCaptor.getValue();
        assertThat(errorMessage, containsString("testError"));
        assertThat(errorMessage, containsString("testWarning"));
    }

    @Test
    public void testSetValidationMessages_Empty() {
        TextField component = spy(new TextField());
        CaptionComponentWrapper wrapper = new CaptionComponentWrapper("testID", component,
                WrapperType.FIELD);

        MessageList messages = new MessageList();
        wrapper.setValidationMessages(messages);

        verify(component).setErrorMessage("");
    }

    @Test
    public void testGetType() {
        Span component = new Span();

        assertThat(new CaptionComponentWrapper("testID", component, WrapperType.FIELD).getType(),
                   is(WrapperType.FIELD));
        assertThat(new CaptionComponentWrapper("testID", component, WrapperType.LAYOUT).getType(),
                   is(WrapperType.LAYOUT));
    }

}
