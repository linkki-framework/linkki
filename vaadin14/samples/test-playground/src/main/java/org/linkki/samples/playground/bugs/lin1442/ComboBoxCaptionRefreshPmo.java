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

package org.linkki.samples.playground.bugs.lin1442;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection(caption = "LIN-1442")
public class ComboBoxCaptionRefreshPmo {

    private static AtomicInteger COUNTER = new AtomicInteger(0);

    public static final String PROPERTY_CHOICE = "choice";
    public static final String PROPERTY_CHANGE_CHOICES_VALUES = "changeChoicesValues";

    private List<Choice> choices = IntStream.range(0, 20).mapToObj(i -> Math.random()).map(Choice::new)
            .collect(Collectors.toList());
    private Choice choice = choices.get(0);

    @UILabel(position = 5, htmlContent = true)
    public String getDescription() {
        return "Change the available values. <br/>1. bug: the value of the combo box does not update<br/>2. bug: the values in the list do not update until they are selected";
    }

    @UIComboBox(position = 10, label = "choices", content = AvailableValuesType.DYNAMIC)
    public Choice getChoice() {
        return choice;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }

    public List<Choice> getChoiceAvailableValues() {
        return choices;
    }

    @UIButton(position = 20, caption = "change choices values")
    public void changeChoicesValues() {
        choices.forEach(c -> c.setValue(Math.random() + COUNTER.getAndIncrement()));
        choice = choices.get(0);
    }

    public static class Choice {

        private Double value;

        public Choice(Double value) {
            this.value = value;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

        public String getName() {
            return String.valueOf(value);
        }
    }
}