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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.linkki.test.matcher.Matchers.absent;
import static org.linkki.test.matcher.Matchers.hasValue;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.TestButtonPmo;
import org.linkki.core.pmo.PresentationModelObject;

public class SectionsTest {

    @Test
    public void testGetSectionId() {
        assertThat(Sections.getSectionId(new SectionPmoWithoutId()), is(SectionPmoWithoutId.class.getSimpleName()));

        TestSectionPmo testSectionPmo = new TestSectionPmo();
        testSectionPmo.setId("foo");
        assertThat(Sections.getSectionId(testSectionPmo), is("foo"));
    }

    @Test
    public void testGetEditButtonPmo() {
        assertThat(Sections.getEditButtonPmo(new SectionPmoWithoutId()), is(absent()));
        assertThat(Sections.getEditButtonPmo(new DefaultPresentationModelObject()), is(absent()));

        TestSectionPmo testSectionPmo = new TestSectionPmo();
        assertThat(Sections.getEditButtonPmo(testSectionPmo), is(absent()));

        TestButtonPmo editButtonPmo = new TestButtonPmo();
        testSectionPmo.setEditButtonPmo(editButtonPmo);
        assertThat(Sections.getEditButtonPmo(testSectionPmo), hasValue(editButtonPmo));
    }

    private static class SectionPmoWithoutId {
        // empty
    }

    private static class DefaultPresentationModelObject implements PresentationModelObject {
        // empty
    }

}
