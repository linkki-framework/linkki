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

package org.linkki.samples.playground.table;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.ui.creation.VaadinUiCreator;

import com.vaadin.flow.component.Component;

public class TableWithValidationSection {

    private TableWithValidationSection() {
        throw new IllegalStateException("Utility class");
    }

    public static Component create() {
        List<TableModelObject> modelObjects = IntStream.range(1, 10)
                .mapToObj(TableModelObject::new)
                .collect(Collectors.toList());
        DefaultBindingManager bindingManager = new DefaultBindingManager(() -> validate(modelObjects));
        BindingContext bindingContext = bindingManager.getContext("table");

        return VaadinUiCreator
                .createComponent(new PlaygroundTablePmo(() -> modelObjects,
                        () -> modelObjects.add(new TableModelObject(modelObjects.size() + 1)),
                        modelObjects::remove), bindingContext);
    }

    private static MessageList validate(List<TableModelObject> modelObjects) {
        MessageList messages = modelObjects.stream()
                .filter(e -> !e.getName().endsWith(String.valueOf(e.getIndex())))
                .map(e -> Message
                        .builder("Name does not end with index " + e.getIndex(), Severity.ERROR)
                        .invalidObjectWithProperties(e, TableModelObject.PROPERTY_NAME)
                        .create())
                .collect(MessageList.collector());

        messages.add(modelObjects.stream()
                .filter(e -> e.getDate().isBefore(LocalDate.now()))
                .map(e -> Message
                        .builder("Date must be at least today.", Severity.ERROR)
                        .invalidObjectWithProperties(e, TableModelObject.PROPERTY_DATE)
                        .create())
                .collect(MessageList.collector()));

        return messages;
    }

}
