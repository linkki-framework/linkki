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

package org.linkki.samples.playground.allelements;

import java.util.Arrays;
import java.util.List;

import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider.ToStringCaptionProvider;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.util.handler.Handler;

@UISection(caption = "Multiple Component Annotations")
public class MultipleComponentAnnotationsPmo {

    public static final String PROPERTY_TYPE = "type";
    public static final String PROPERTY_VALUE = "value";

    private static final List<Class<?>> ALLOWED_TYPES = Arrays.asList(UITextField.class, UIComboBox.class);
    private static final List<String> ALLOWED_VALUES = Arrays.asList("foo", "bar");

    private Class<?> type = UIComboBox.class;
    private String value = "foo";
    private Handler modelChangedHandler;

    public MultipleComponentAnnotationsPmo(Handler modelChangedHandler) {
        this.modelChangedHandler = modelChangedHandler;
    }

    @UIComboBox(position = 10, label = "field type", itemCaptionProvider = FieldTypeCaptionProvider.class, content = AvailableValuesType.DYNAMIC)
    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
        modelChangedHandler.apply();
    }

    public List<Class<?>> getTypeAvailableValues() {
        return ALLOWED_TYPES;
    }


    @UITextField(position = 20, label = "dynamic field")
    @UIComboBox(position = 20, label = "dynamic field", itemCaptionProvider = ToStringCaptionProvider.class, content = AvailableValuesType.DYNAMIC)
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

    public static class FieldTypeCaptionProvider implements ItemCaptionProvider<Class<?>> {

        @Override
        public String getCaption(Class<?> value) {
            return value.getSimpleName();
        }

    }
}
