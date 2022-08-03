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

package org.linkki.samples.playground.bugs.lin1486;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider.ToStringCaptionProvider;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.util.handler.Handler;

@UISection(caption = ComboBoxVanishingValuePmo.CAPTION)
public class ComboBoxVanishingValuePmo {

    public static final String CAPTION = "LIN-1486";
    public static final String PROPERTY_CHOICE = "choice";
    public static final String PROPERTY_CHANGE_CHOICES = "changeChoices";
    public static final String PROPERTY_UPDATE_BINDING_CONTEXT = "updateBindingContext";

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final Handler updateBindingContext;
    private List<Double> choices = IntStream.range(0, 20)
            .mapToObj(i -> SECURE_RANDOM.nextDouble())
            .collect(Collectors.toList());
    private Double choice = choices.get(0);

    public ComboBoxVanishingValuePmo(Handler updateBindingContext) {
        this.updateBindingContext = updateBindingContext;
    }

    @UILabel(position = 5)
    public String getDescription() {
        return "Set new objects as choices then update the new binding context. "
                + "The value in the combo box would disappear if the bug still exists.";
    }

    @UIComboBox(position = 10, label = "choices", content = AvailableValuesType.DYNAMIC,
            itemCaptionProvider = ToStringCaptionProvider.class)
    public Double getChoice() {
        return choice;
    }

    public void setChoice(Double choice) {
        this.choice = choice;
    }

    public List<Double> getChoiceAvailableValues() {
        return choices;
    }

    @UIButton(position = 20, caption = "set new objects as choices")
    public void changeChoices() {
        // must be new objects, else the bug will not appear
        choices = IntStream.range(0, 20)
                .mapToObj(i -> SECURE_RANDOM.nextDouble())
                .collect(Collectors.toList());
        choice = choices.get(0);
    }

    @UIButton(position = 30, caption = "update binding context")
    public void updateBindingContext() {
        updateBindingContext.apply();
    }
}