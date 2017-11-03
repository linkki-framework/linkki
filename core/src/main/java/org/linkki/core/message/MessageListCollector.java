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

import java.util.function.BinaryOperator;
import java.util.stream.Collector;

/**
 * {@link Collector} collecting {@link Message NlsText} into a {@link MessageList}.
 */
public final class MessageListCollector {

    private MessageListCollector() {
        // no instantiation for utility classes
    }

    /**
     * Returns a {@code Collector} that accumulates the input messages into a new
     * {@code MessageList}.
     * 
     * @return a {@code Collector} which collects all the input messages into a {@code MessageList},
     *         in encounter order
     */
    public static Collector<Message, ?, MessageList> toMessageList() {
        return Collector.of(MessageList::new, MessageList::add, addAll());
    }


    private static BinaryOperator<MessageList> addAll() {
        return (list, listToAdd) -> {
            list.add(listToAdd);
            return list;
        };
    }

}
