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
package org.linkki.ips.messages;

import static java.util.stream.Collectors.toList;
import static org.linkki.core.binding.validation.message.MessageListCollector.toMessageList;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.faktorips.annotation.UtilityClass;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.ObjectProperty;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.util.StreamUtil;

/**
 * This utility class is used to convert {@link org.faktorips.runtime.Message Faktor-IPS Messages} to
 * {@link Message linkki Messages}
 */
@UtilityClass
public final class MessageConverter {

    private static final Map<org.faktorips.runtime.Severity, Severity> SEVERITY_MAP;

    static {
        Map<org.faktorips.runtime.Severity, Severity> map = new HashMap<>(5);

        map.put(org.faktorips.runtime.Severity.INFO, Severity.INFO);
        map.put(org.faktorips.runtime.Severity.WARNING, Severity.WARNING);
        map.put(org.faktorips.runtime.Severity.ERROR, Severity.ERROR);
        // map null and NONE to INFO
        map.put(null, Severity.INFO);
        map.put(org.faktorips.runtime.Severity.NONE, Severity.INFO);

        SEVERITY_MAP = Collections.unmodifiableMap(map);
    }


    private MessageConverter() {
        // no instantiation for utility classes
    }


    /**
     * Converts the given {@link org.faktorips.runtime.MessageList Faktor-IPS MessageList} to a
     * {@link MessageList linkki MessageList}.
     * <p>
     * If the given {@link org.faktorips.runtime.MessageList Faktor-IPS MessageList} is {@code null}, an
     * empty {@link MessageList linkki MessageList} will be returned.
     *
     * @param ipsMessages {@link org.faktorips.runtime.MessageList Faktor-IPS MessageList} to convert
     *
     * @return the converted {@link MessageList linkki MessageList}
     *
     * @see #convert(org.faktorips.runtime.Message)
     */
    public static MessageList convert(org.faktorips.runtime.MessageList ipsMessages) {
        if (ipsMessages == null) {
            return new MessageList();
        }

        return StreamUtil.stream(ipsMessages)
                .map(MessageConverter::convert)
                .collect(toMessageList());

    }

    /**
     * Converts the given {@link org.faktorips.runtime.Message Faktor-IPS Message} to a {@link Message
     * linkki Message}.
     * <p>
     * The {@link org.faktorips.runtime.IMarker Faktor-IPS IMarkers} are wrapped in
     * {@link ValidationMarkerWrapper ValidationMarkerWrappers}
     * <p>
     * Since the text is not required in Faktor-IPS but in linkki, an empty text will be converted into
     * an empty String ("").<br>
     * A {@code null} {@link org.faktorips.runtime.Severity} or
     * {@link org.faktorips.runtime.Severity#NONE} will be converted to {@link Severity#INFO}.
     *
     * @param ipsMessage {@link org.faktorips.runtime.Message Faktor-IPS Message} to convert
     *
     * @return to converted {@link MessageList linkki Message}
     *
     * @throws NullPointerException if {@code ipsMessage} is {@code null}
     */
    public static Message convert(org.faktorips.runtime.Message ipsMessage) {
        Objects.requireNonNull(ipsMessage, "ipsMessage must not be null");

        String text = ipsMessage.getText();
        if (text == null) {
            text = StringUtils.EMPTY;
        }

        Message.Builder builder = Message.builder(text, SEVERITY_MAP.get(ipsMessage.getSeverity()))
                .code(ipsMessage.getCode());

        if (ipsMessage.hasMarkers()) {
            builder.markers(ipsMessage.getMarkers().stream()
                    .map(ValidationMarkerWrapper::new)
                    .collect(toList()));
        }

        builder.invalidObjects(ipsMessage.getInvalidObjectProperties().stream()
                .map(o -> new ObjectProperty(o.getObject(), o.getProperty()))
                .collect(toList()));

        return builder.create();
    }

}
