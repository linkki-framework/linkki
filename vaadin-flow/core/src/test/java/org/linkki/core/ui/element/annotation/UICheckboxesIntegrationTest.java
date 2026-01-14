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

package org.linkki.core.ui.element.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.linkki.core.ui.element.annotation.UICheckboxesIntegrationTest.CheckboxesTestPmo;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider.ToStringCaptionProvider;
import org.linkki.core.ui.bind.TestEnum;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.test.KaribuUtils;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.data.provider.Query;

;

class UICheckboxesIntegrationTest
        extends ComponentAnnotationIntegrationTest<CheckboxGroup<TestEnum>, CheckboxesTestPmo> {

    private UICheckboxesIntegrationTest() {
        super(CheckboxesTestModelObject::new, CheckboxesTestPmo::new);
    }

    @Test
    void testValue() {
        var checkboxes = getDynamicComponent();
        var modelObject = (CheckboxesTestModelObject) getDefaultModelObject();
        assertThat(checkboxes.getValue()).containsExactly(TestEnum.ONE);
        assertThat(modelObject.getValue()).containsExactly(TestEnum.ONE);

        modelObject.setValue(Set.of(TestEnum.values()));
        modelChanged();

        assertThat(checkboxes.getValue()).containsOnly(TestEnum.ONE, TestEnum.TWO, TestEnum.THREE, TestEnum.EMPTY);

        KaribuUtils.Fields.setValue(checkboxes, Set.of(TestEnum.ONE, TestEnum.TWO));

        assertThat(checkboxes.getValue()).containsOnly(TestEnum.ONE, TestEnum.TWO);
        assertThat(modelObject.getValue()).containsOnly(TestEnum.ONE, TestEnum.TWO);
    }

    @Test
    void testDynamicAvailableValues() {
        var availableValuesInPmo = getDefaultPmo().getValueAvailableValues();
        var checkboxes = getDynamicComponent();
        var allValuesInCheckboxes = checkboxes.getDataProvider().fetch(new Query<>()).collect(Collectors.toSet());

        assertThat(allValuesInCheckboxes).isEqualTo(availableValuesInPmo);
    }

    @Test
    void testRequired() {
        var checkboxes = getDynamicComponent();

        getDefaultPmo().setRequired(true);
        modelChanged();

        assertThat(checkboxes.isRequired()).isTrue();
        assertThat(checkboxes.isRequiredIndicatorVisible()).isTrue();
    }

    @Test
    void testCaptionProvider_CustomCaptionProvider() {
        var labels = getDynamicComponent().getChildren()
                .map(this::getLabel)
                .toList();
        assertThat(labels)
                .containsExactlyInAnyOrder(getDefaultPmo().getStaticValueAvailableValues().stream()
                        .map(Enum::toString).toArray(String[]::new));
    }

    @Test
    void testCaptionProvider_DefaultCaptionProvider() {
        var labels = getStaticComponent().getChildren()
                .map(this::getLabel)
                .toList();
        assertThat(labels)
                .containsExactlyInAnyOrder(getDefaultPmo().getStaticValueAvailableValues().stream()
                        .map(e -> e.getName(Locale.GERMAN)).toArray(String[]::new));
    }

    /**
     * Returns the label of an item in a {@link CheckboxGroup}. For some reason,
     * {@link CheckboxGroup#getItemLabelGenerator()} does not work correctly.
     * {@link Checkbox#getLabel()} also delivers wrong value.
     */
    private String getLabel(Object checkboxItem) {
        var checkbox = (Checkbox) checkboxItem;
        @SuppressWarnings("removal")
        var label = checkbox.getChildren()
                .filter(com.vaadin.flow.component.html.NativeLabel.class::isInstance)
                .map(com.vaadin.flow.component.html.NativeLabel.class::cast)
                .findFirst()
                .get();
        return label.getElement().getTextRecursively();
    }

    @Test
    void testValueRemainsWhenChangingAvailableValues() {
        var modelObject = (CheckboxesTestModelObject) getDefaultModelObject();
        modelObject.setValue(Set.of(TestEnum.THREE));
        modelChanged();
        assertThat(getDynamicComponent().getValue()).containsOnly(TestEnum.THREE);

        getDefaultPmo().setValueAvailableValues(Set.of(TestEnum.ONE, TestEnum.TWO, TestEnum.THREE));
        modelChanged();
        assertThat(getDynamicComponent().getValue()).containsOnly(TestEnum.THREE);
    }

    @UISection
    static class CheckboxesTestPmo extends AnnotationTestPmo {

        private Set<TestEnum> availableValues = Set.of(TestEnum.values());

        public CheckboxesTestPmo(Object modelObject) {
            super(modelObject);
        }

        @Override
        @UICheckboxes(position = 0,
                label = "",
                enabled = EnabledType.DYNAMIC,
                required = RequiredType.DYNAMIC,
                visible = VisibleType.DYNAMIC,
                itemCaptionProvider = ToStringCaptionProvider.class)
        public void value() {
            // model binding
        }

        public Set<TestEnum> getValueAvailableValues() {
            return availableValues;
        }

        public void setValueAvailableValues(Set<TestEnum> availableValues) {
            this.availableValues = availableValues;
        }

        @Override
        @UICheckboxes(position = 1,
                label = TEST_LABEL,
                enabled = EnabledType.DISABLED,
                required = RequiredType.REQUIRED,
                visible = VisibleType.INVISIBLE)
        public void staticValue() {
            // model binding
        }

        public Set<TestEnum> getStaticValueAvailableValues() {
            return availableValues;
        }

    }

    static class CheckboxesTestModelObject {
        private Set<TestEnum> value = Set.of(TestEnum.ONE);

        public Set<TestEnum> getValue() {
            return value;
        }

        public void setValue(Set<TestEnum> value) {
            this.value = value;
        }

        public Set<TestEnum> getStaticValue() {
            return getValue();
        }
    }
}
