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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Test;
import org.linkki.util.validation.ValidationMarker;

import com.vaadin.server.ErrorMessage.ErrorLevel;

public class MessageTest {

    @Test
    public void testIsMandatoryFieldMessage() {
        ValidationMarker mandatoryMarker = () -> true;
        ValidationMarker notMandatoryMarker = () -> false;

        assertThat(createMessage().isMandatoryFieldMessage(), is(false));
        assertThat(createMessage(notMandatoryMarker).isMandatoryFieldMessage(), is(false));
        assertThat(createMessage(mandatoryMarker).isMandatoryFieldMessage(), is(true));
        assertThat(createMessage(notMandatoryMarker, mandatoryMarker).isMandatoryFieldMessage(), is(true));
    }

    private Message createMessage(@NonNull ValidationMarker... markers) {
        return Message.builder("", ErrorLevel.ERROR).markers(markers).create();
    }

}