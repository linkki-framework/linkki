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
package org.linkki.framework.ui.component;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.linkki.core.message.Message;
import org.linkki.core.message.ObjectProperty;

import com.vaadin.server.ErrorMessage.ErrorLevel;
import com.vaadin.server.FontAwesome;

@RunWith(Parameterized.class)
public class MessagePmoTest {


    @Parameterized.Parameter(value = 0)
    public ErrorLevel errorLevel;

    @Parameterized.Parameter(value = 1)
    public ObjectProperty objectProperty;

    @Parameterized.Parameter(value = 2)
    public FontAwesome icon;

    @Parameterized.Parameter(value = 3)
    public String tooltip;


    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][] {
                { ErrorLevel.ERROR, new ObjectProperty(new Object(), "foo"), FontAwesome.EXCLAMATION_CIRCLE,
                        "Object: foo" },
                { ErrorLevel.WARNING, new ObjectProperty(new Object()), FontAwesome.EXCLAMATION_TRIANGLE, "Object" },
                { ErrorLevel.INFORMATION, null, FontAwesome.INFO_CIRCLE, "" }
        };
    }


    @Test
    public void testMessagePmo() {
        Message.Builder messageBuilder = Message.builder("text", errorLevel);
        if (objectProperty != null) {
            messageBuilder.invalidObject(objectProperty);
        }

        MessagePmo message = new MessagePmo(messageBuilder.create());

        assertThat(message.getIcon(), is(icon));
        assertThat(message.getStyle(), endsWith(errorLevel.name().toLowerCase()));
        assertThat(message.getTooltip(), is(tooltip));
    }

}
