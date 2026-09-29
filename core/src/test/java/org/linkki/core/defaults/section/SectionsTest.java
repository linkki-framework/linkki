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

package org.linkki.core.defaults.section;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.TestButtonPmo;
import org.linkki.core.pmo.PresentationModelObject;

class SectionsTest {

    @Test
    void testGetSectionId() {
        assertThat(Sections.getSectionId(new SectionPmoWithoutId())).isEqualTo(SectionPmoWithoutId.class.getSimpleName());

        var testSectionPmo = new TestSectionPmo();
        testSectionPmo.setId("foo");
        assertThat(Sections.getSectionId(testSectionPmo)).isEqualTo("foo");
    }

    @Test
    void testGetEditButtonPmo() {
        assertThat(Sections.getEditButtonPmo(new SectionPmoWithoutId())).isEmpty();
        assertThat(Sections.getEditButtonPmo(new DefaultPresentationModelObject())).isEmpty();

        var testSectionPmo = new TestSectionPmo();
        assertThat(Sections.getEditButtonPmo(testSectionPmo)).isEmpty();

        var editButtonPmo = new TestButtonPmo();
        testSectionPmo.setEditButtonPmo(editButtonPmo);
        assertThat(Sections.getEditButtonPmo(testSectionPmo)).hasValue(editButtonPmo);
    }

    private static class SectionPmoWithoutId {
        // empty
    }

    private static class DefaultPresentationModelObject implements PresentationModelObject {
        // empty
    }

}
