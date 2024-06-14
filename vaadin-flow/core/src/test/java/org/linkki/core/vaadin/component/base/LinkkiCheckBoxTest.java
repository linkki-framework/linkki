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

package org.linkki.core.vaadin.component.base;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

@SuppressWarnings("deprecation")
class LinkkiCheckBoxTest {

    @Test
    void testSetReadOnly_WhenWritable_ThenEnabled() {
        LinkkiCheckBox checkBox = new LinkkiCheckBox();

        checkBox.setReadOnly(false);

        assertThat(checkBox.isEnabled()).isTrue();
        assertThat(isDisabledAttribute(checkBox)).isFalse();
        assertThat(checkBox.getElement().hasAttribute("readonly")).isFalse();
    }

    @Test
    void testSetReadOnly_WhenReadOnly_ThenDisabled() {
        LinkkiCheckBox checkBox = new LinkkiCheckBox();

     checkBox.setReadOnly(true);

     assertThat(checkBox.isEnabled()).isTrue();
     assertThat(isDisabledAttribute(checkBox)).isTrue();
     assertThat(checkBox.getElement().hasAttribute("readonly")).isTrue();
 }

     @Test
     void testSetReadOnly_WhenReadOnlyAndDisabled_ThenDisabled() {
         LinkkiCheckBox checkBox = new LinkkiCheckBox();

     checkBox.setEnabled(false);
     checkBox.setReadOnly(true);

     assertThat(checkBox.isEnabled()).isFalse();
     assertThat(isDisabledAttribute(checkBox)).isTrue();
     assertThat(checkBox.getElement().hasAttribute("readonly")).isTrue();
 }

     @Test
     void testSetReadOnly_WhenReadOnlyAndEnabled_ThenDisabled() {
         LinkkiCheckBox checkBox = new LinkkiCheckBox();

     checkBox.setEnabled(true);
     checkBox.setReadOnly(true);

     assertThat(checkBox.isEnabled()).isTrue();
     assertThat(isDisabledAttribute(checkBox)).isTrue();
     assertThat(checkBox.getElement().hasAttribute("readonly")).isTrue();
 }

     @Test
     void testSetReadOnly_WhenWritableAndDisabled_ThenDisabled() {
         LinkkiCheckBox checkBox = new LinkkiCheckBox();

     checkBox.setEnabled(false);
     checkBox.setReadOnly(false);

     assertThat(checkBox.isEnabled()).isFalse();
     assertThat(isDisabledAttribute(checkBox)).isTrue();
     assertThat(checkBox.getElement().hasAttribute("readonly")).isFalse();
 }

     @Test
     void testSetReadOnly_WhenWritableAndEnabled_ThenEnabled() {
         LinkkiCheckBox checkBox = new LinkkiCheckBox();

     checkBox.setEnabled(true);
     checkBox.setReadOnly(false);

     assertThat(checkBox.isEnabled()).isTrue();
     assertThat(isDisabledAttribute(checkBox)).isFalse();
     assertThat(checkBox.getElement().hasAttribute("readonly")).isFalse();
 }

     @Test
     void testSetReadOnly_DisabledReadOnlyThenEnable_ThenDisabled() {
         LinkkiCheckBox checkBox = new LinkkiCheckBox();

     checkBox.setEnabled(false);
     checkBox.setReadOnly(true);
     checkBox.setEnabled(true);

     assertThat(checkBox.isEnabled()).isTrue();
     assertThat(isDisabledAttribute(checkBox)).isTrue();
     assertThat(checkBox.getElement().hasAttribute("readonly")).isTrue();
 }

     @Test
     void testSetReadOnly_EnabledWritableThenDisable_ThenDisabled() {
         LinkkiCheckBox checkBox = new LinkkiCheckBox();

     checkBox.setEnabled(true);
     checkBox.setReadOnly(false);
     checkBox.setEnabled(false);

     assertThat(checkBox.isEnabled()).isFalse();
     assertThat(isDisabledAttribute(checkBox)).isTrue();
     assertThat(checkBox.getElement().hasAttribute("readonly")).isFalse();
 }

     private boolean isDisabledAttribute(LinkkiCheckBox checkBox) {
         return checkBox.getElement().hasAttribute("disabled");
     }

}
