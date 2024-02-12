/*
 * Copyright Faktor Zehn GmbH.
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
package org.linkki.samples.playground.bugs.lin3531;

import org.linkki.core.binding.validation.annotation.BindMessages;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.table.column.annotation.UITableColumn;

public class BindMessagesOnGridColumnsBugRow {
    @UITableColumn
    @UITextField(position = 5)
    @BindMessages
    public String getStatus() {
        return "Diese Zeile sollte zu sehen sein";
    }

    public MessageList getStatusMessages(MessageList ml) {
        return ml;
    }
}
