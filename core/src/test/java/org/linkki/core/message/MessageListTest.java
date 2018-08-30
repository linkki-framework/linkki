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
package org.linkki.core.message;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.linkki.core.matcher.MessageMatchers.emptyMessageList;
import static org.linkki.core.matcher.MessageMatchers.hasSize;
import static org.linkki.test.matcher.Matchers.present;

import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.linkki.util.validation.ValidationMarker;

import com.vaadin.server.ErrorMessage.ErrorLevel;

public class MessageListTest {

    @SuppressWarnings("null")
    private Message msg1;
    @SuppressWarnings("null")
    private Message msg2;
    @SuppressWarnings("null")
    private Message msg3;

    @SuppressWarnings("null")
    private MessageList msgList1;
    @SuppressWarnings("null")
    private ObjectProperty invalidObjectProperty1;
    @SuppressWarnings("null")
    private ObjectProperty invalidObjectProperty2;
    @SuppressWarnings("null")
    private ObjectProperty invalidObjectProperty3;

    @Before
    public void setUp() {

        invalidObjectProperty1 = new ObjectProperty("A", "testProperty");
        invalidObjectProperty2 = new ObjectProperty("B", "anotherTestProperty");
        invalidObjectProperty3 = new ObjectProperty("C", "higherIndexProperty", 2);


        msg1 = Message.builder("Test1", ErrorLevel.INFORMATION).invalidObject(invalidObjectProperty1).create();
        msg2 = Message.builder("Test2", ErrorLevel.INFORMATION).invalidObject(invalidObjectProperty2).create();
        msg3 = Message.builder("Test3", ErrorLevel.INFORMATION).invalidObject(invalidObjectProperty3).create();

        msgList1 = new MessageList(msg1);
        msgList1.add(msg2);
        msgList1.add(msg3);
    }


    @Test
    public void testGetMessagesFor_shouldAddMessage() {

        assertThat(msgList1.getMessagesFor("A", "testProperty", -1).getMessage(0), is(msg1));
        assertThat(msgList1.getMessagesFor("B", "anotherTestProperty", -1).getMessage(0), is(msg2));
        assertThat(msgList1.getMessagesFor("B", null, -1).getMessage(0), is(msg2));
        assertThat(msgList1.getMessagesFor("C", "higherIndexProperty", 2).getMessage(0), is(msg3));
        assertThat(msgList1.getMessagesFor("C", "higherIndexProperty", -1).getMessage(0), is(msg3));
    }

    @Test
    public void testGetMessagesFor_shouldNotAddMessage() {

        assertThat(msgList1.getMessagesFor("A", "anotherTestProperty", -1).isEmpty(), is(true));
        assertThat(msgList1.getMessagesFor("B", "anotherTestProperty", 1).isEmpty(), is(true));
        assertThat(msgList1.getMessagesFor("C", "higherIndexProperty", 1).isEmpty(), is(true));
    }

    @Test
    public void testGetMessagesByMarker_withValidationMarker() {
        ValidationMarker marker = () -> false;

        MessageList messages = new MessageList(Message.builder("msg1", ErrorLevel.INFORMATION).create(),
                Message.builder("msgWithMarker", ErrorLevel.WARNING).markers(marker).create());

        MessageList messagesByMarker = messages.getMessagesByMarker(marker);
        assertThat(messagesByMarker, is(not(sameInstance(messages))));
        assertThat(messagesByMarker, hasSize(1));
        assertThat(messagesByMarker.getMessage(0), is(messages.getMessage(1)));
    }

    @Test
    public void testGetMessagesByMarker_markerNull_shouldReturnAllMessagesWithoutMarker() {

        MessageList messages = new MessageList(Message.builder("msg1", ErrorLevel.INFORMATION).create(),
                Message.builder("msgWithMarker", ErrorLevel.WARNING).markers(() -> false).create());

        MessageList messagesByMarker = messages.getMessagesByMarker((ValidationMarker)null);
        assertThat(messagesByMarker, is(not(sameInstance(messages))));
        assertThat(messagesByMarker, hasSize(1));
        assertThat(messagesByMarker.getMessage(0), is(messages.getMessage(0)));
    }

    @Test
    public void testGetMessagesByMarker_predicate() {

        MessageList messages = new MessageList(Message.builder("msgWithoutMarker", ErrorLevel.INFORMATION).create(),
                Message.builder("msgWithMatchingMarker", ErrorLevel.WARNING).markers(() -> true).create(),
                Message.builder("msgWithNonMathingMarker", ErrorLevel.ERROR).markers(() -> false).create());

        MessageList messagesByMarker = messages.getMessagesByMarker(ValidationMarker::isRequiredInformationMissing);
        assertThat(messagesByMarker, is(not(sameInstance(messages))));
        assertThat(messagesByMarker, hasSize(1));
        assertThat(messagesByMarker.getMessage(0), is(messages.getMessage(1)));
    }

    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    @Test(expected = NullPointerException.class)
    public void testGetMessagesByMarker_predicateNull_shouldThrowNullPointerException() {
        new MessageList().getMessagesByMarker((Predicate)null);
    }

    @Test
    public void testGetMessageWithHighestErrorLevel() {

        MessageList messages = new MessageList(Message.builder("info", ErrorLevel.INFORMATION).create(),
                Message.builder("error", ErrorLevel.ERROR).create(),
                Message.builder("warning", ErrorLevel.WARNING).create());

        Optional<Message> message = messages.getMessageWithHighestErrorLevel();
        assertThat(message, is(present()));
        assertThat(message.get().getErrorLevel(), is(ErrorLevel.ERROR));

    }

    @Test
    public void testGetMessageWithHighestErrorLevel_null() {

        MessageList messages = new MessageList();

        Optional<Message> message = messages.getMessageWithHighestErrorLevel();
        assertThat(message, is(not(present())));

    }

    @SuppressWarnings({ "unused", "null" })
    @Test(expected = NullPointerException.class)
    public void testNewMessageList_null_shouldThrowNullPointerException() {
        new MessageList((Message[])null);
    }

    @Test
    public void testNewMessageList_empty() {
        assertThat(new MessageList(), is(emptyMessageList()));
    }

    @Test
    public void testNewMessageList() {
        Message m1 = Message.newError("error", "error");
        Message m2 = Message.newWarning("warning", "warning");
        assertThat(new MessageList(m1, m2), hasItems(m1, m2));
    }

    @Test
    public void testSortByErrorLevel() {
        Message e1 = Message.newError("e1", "E1");
        Message e2 = Message.newError("e2", "E2");
        Message e3 = Message.newError("e3", "E3");
        Message w1 = Message.newWarning("w1", "W1");
        Message w2 = Message.newWarning("w2", "W2");
        Message i1 = Message.newInfo("i1", "I1");
        Message i2 = Message.newInfo("i2", "I2");
        MessageList unsortedMessageList = new MessageList(i2, e1, w1, e3, i1, e2, w2);
        MessageList sortedMessageList = new MessageList(e1, e3, e2, w1, w2, i2, i1);

        MessageList actualMessageList = unsortedMessageList.sortByErrorLevel();

        assertThat(actualMessageList, is(equalTo(sortedMessageList)));
    }

    @Test
    public void testGetText_emptyList_shouldReturnEmptyString() {
        assertThat(new MessageList().getText(), is(StringUtils.EMPTY));
    }

    @Test
    public void testGetMessagesFor_noObjects_shouldReturnEmptyMessageList() {
        MessageList messages = new MessageList(Message.newError("code", "msg"),
                Message.newWarning("code", "msg"))
                        .getMessagesFor(new Object());

        assertThat(messages, emptyMessageList());
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessagesFor_objectNull_shouldThrowNullPointerException() {
        @SuppressWarnings("null")
        MessageList messages = new MessageList(Message.newError("code", "msg"),
                Message.newWarning("code", "msg"))
                        .getMessagesFor(null);

        assertThat(messages, emptyMessageList());
    }

    @Test
    public void testGetMessagesFor_nullProperty_shouldFindMessagesWithEmptyProperty() {
        Object o = new Object();
        Message m = Message.builder("1", ErrorLevel.ERROR).code("1").invalidObject(new ObjectProperty(o)).create();
        MessageList messages = new MessageList(m);

        assertThat(messages.getMessagesFor(o), hasSize(1));
        assertThat(messages.getMessagesFor(o).getMessage(0), is(m));
        assertThat(messages.getMessagesFor(o, null), hasSize(1));
        assertThat(messages.getMessagesFor(o, null).getMessage(0), is(m));
        assertThat(messages.getMessagesFor(o, ""), hasSize(1));
        assertThat(messages.getMessagesFor(o, "").getMessage(0), is(m));
    }

    @Test
    public void testGetMessagesFor_EmptyProperty_shouldFindMessagesWithEmptyProperty() {
        Object o = new Object();
        Message m = Message.builder("1", ErrorLevel.ERROR).code("1").invalidObject(new ObjectProperty(o, "")).create();
        MessageList messages = new MessageList(m);

        assertThat(messages.getMessagesFor(o), hasSize(1));
        assertThat(messages.getMessagesFor(o).getMessage(0), is(m));
        assertThat(messages.getMessagesFor(o, null), hasSize(1));
        assertThat(messages.getMessagesFor(o, null).getMessage(0), is(m));
        assertThat(messages.getMessagesFor(o, ""), hasSize(1));
        assertThat(messages.getMessagesFor(o, "").getMessage(0), is(m));
    }

}
