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

package org.linkki.samples.playground.bugs.lin2200;

import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection(caption = ComboBoxNewInstancePmo.CAPTION)
public class ComboBoxNewInstancePmo {

    public static final String CAPTION = "LIN-2200";

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private List<Choice> choices = IntStream.range(0, 20).mapToObj(i -> new SecureRandom().nextDouble())
            .map(Choice::new)
            .collect(Collectors.toList());
    private Choice choice = choices.get(0);

    @UILabel(position = 5, htmlContent = true)
    public String getDescription() {
        return "The getters for this combo box return new instances that do not implement equals() every time.\n" +
                "Interacting with this combo box should not cause a repeated reset of the field. This can be observed in the network monitor.";
    }

    @UIComboBox(position = 10, label = "Values", content = AvailableValuesType.DYNAMIC)
    public Choice getChoice() {
        return new Choice(choice.getValue());
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }

    public List<Choice> getChoiceAvailableValues() {
        return choices.stream().map(c -> new Choice(c.getValue())).collect(Collectors.toList());
    }

    @UIButton(position = 20, caption = "Change values")
    public void changeChoicesValues() {
        choices.forEach(c -> c.setValue(new SecureRandom().nextDouble() + COUNTER.getAndIncrement()));
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

        @Override
        public boolean equals(Object obj) {
            return false;
        }

        @Override
        public int hashCode() {
            return 42;
        }
    }
}