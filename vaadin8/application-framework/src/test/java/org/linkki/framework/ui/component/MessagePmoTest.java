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
package org.linkki.framework.ui.component;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.ObjectProperty;
import org.linkki.core.binding.validation.message.Severity;

import com.vaadin.icons.VaadinIcons;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class MessagePmoTest {

    public static Object[][] data() {
        return new Object[][] {
                { Severity.ERROR, new ObjectProperty(new Object(), "foo"),
                        VaadinIcons.EXCLAMATION_CIRCLE, "Object: foo", "linkki-message-error" },
                { Severity.WARNING, new ObjectProperty(new Object()),
                        VaadinIcons.WARNING, "Object", "linkki-message-warning" },
                { Severity.INFO, null,
                        VaadinIcons.INFO_CIRCLE, "", "linkki-message-info" }
        };
    }

    @SuppressWarnings("deprecation")
    @ParameterizedTest
    @MethodSource("data")
    public void testMessagePmo(Severity severity,
            @CheckForNull ObjectProperty objectProperty,
            VaadinIcons icon,
            String tooltip,
            String stylename) {

        Message.Builder messageBuilder = Message.builder("text", severity);
        if (objectProperty != null) {
            messageBuilder.invalidObject(objectProperty);
        }

        MessagePmo message = new MessagePmo(messageBuilder.create());

        assertThat(message.getIcon(), is(icon));
        assertThat(message.getTooltip(), is(tooltip));
        assertThat(message.getStyle(), is(stylename));
    }
}
