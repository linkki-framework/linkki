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

package org.linkki.samples.playground.ts.components;

import java.util.Arrays;
import java.util.List;

import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider.ToStringCaptionProvider;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.util.handler.Handler;

@UISection
public class DynamicComponentPmo {

    private static final List<Class<?>> ALLOWED_TYPES = Arrays.asList(UITextField.class, UIComboBox.class);
    private static final List<String> ALLOWED_VALUES = Arrays.asList("foo", "baa");

    private Class<?> type = ALLOWED_TYPES.get(1);
    private String value = ALLOWED_VALUES.get(0);
    private Handler changeComponentHandler;

    public DynamicComponentPmo(Handler changeComponentHandler) {
        this.changeComponentHandler = changeComponentHandler;
    }

    @UIComboBox(position = 10, label = "Component Type", itemCaptionProvider = FieldTypeCaptionProvider.class, content = AvailableValuesType.DYNAMIC)
    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
        changeComponentHandler.apply();
    }

    public List<Class<?>> getTypeAvailableValues() {
        return ALLOWED_TYPES;
    }

    @UITextField(position = 20, label = "Dynamic Component")
    @UIComboBox(position = 20, label = "Dynamic Component", itemCaptionProvider = ToStringCaptionProvider.class, content = AvailableValuesType.DYNAMIC)
    public String getValue() {
        return value;
    }

    public List<String> getValueAvailableValues() {
        return ALLOWED_VALUES;
    }

    public Class<?> getValueComponentType() {
        return type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // TODO LIN-2343 eigenes TC für Custom ItemCaptionProvider und AvailableValuesProvider?
    public static class FieldTypeCaptionProvider implements ItemCaptionProvider<Class<?>> {
        @Override
        public String getCaption(Class<?> value) {
            return value.getSimpleName();
        }
    }
}
