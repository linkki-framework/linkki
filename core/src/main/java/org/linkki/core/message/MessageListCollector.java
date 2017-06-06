/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.message;

import java.util.function.BinaryOperator;
import java.util.stream.Collector;

/**
 * {@link Collector} collecting {@link Message Messages} into a {@link MessageList}.
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
