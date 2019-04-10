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

package org.linkki.core.binding.validation.message;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.linkki.core.binding.validation.message.MessageListCollector.toMessageList;
import static org.linkki.core.matcher.MessageMatchers.emptyMessageList;
import static org.linkki.core.matcher.MessageMatchers.hasErrorMessage;
import static org.linkki.core.matcher.MessageMatchers.hasInfoMessage;
import static org.linkki.core.matcher.MessageMatchers.hasSize;

import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

public class MessageListCollectorTest {

    @Test
    public void testToMessageList() {

        MessageList emptyMessageList = Stream.<Message> empty().collect(toMessageList());
        assertThat(emptyMessageList, is(emptyMessageList()));

        MessageList singleMessageList = Stream.of(Message.newError("A", "bla"))
                .collect(MessageListCollector.toMessageList());
        assertThat(singleMessageList, hasSize(1));

        MessageList sortedMessageList = Stream.of(Message.newInfo("A", "bla"), Message.newError("B", "blubb"))
                .collect(MessageListCollector.toMessageList());
        assertThat(sortedMessageList, hasSize(2));
        assertThat(sortedMessageList, hasInfoMessage("A"));
        assertThat(sortedMessageList, hasErrorMessage("B"));
        List<String> codes = sortedMessageList.stream().map(Message::getCode).collect(toList());
        assertThat(codes, contains("A", "B"));
    }

    @Test
    public void testToMessageList_Parallel() {

        MessageList sortedMessageList = Stream.of(Message.newInfo("A", "bla"), Message.newError("B", "blubb"))
                .parallel()
                .collect(MessageListCollector.toMessageList());
        assertThat(sortedMessageList, hasSize(2));
        assertThat(sortedMessageList, hasInfoMessage("A"));
        assertThat(sortedMessageList, hasErrorMessage("B"));
        List<String> codes = sortedMessageList.stream().map(Message::getCode).collect(toList());
        assertThat(codes, contains("A", "B"));
    }

}
