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
package org.linkki.core.ui.section.annotations;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Test;
import org.linkki.core.binding.TestEnum;
import org.linkki.core.ui.components.SubsetChooser;

import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;

public class UISubsetChooserTest {

    @UISection
    protected static class TestPmo {

        private final Set<TestEnum> foo = new LinkedHashSet<>();

        @UISubsetChooser(position = 1, leftColumnCaption = "Left Column Caption", rightColumnCaption = "Right Column Caption")
        public Set<TestEnum> getFoo() {
            return foo;
        }

        public void setFoo(Set<TestEnum> selectedFoos) {
            foo.clear();
            foo.addAll(selectedFoos);
        }

        public Set<TestEnum> getFooAvailableValues() {
            LinkedHashSet<TestEnum> someValues = new LinkedHashSet<>();
            someValues.add(TestEnum.ONE);
            someValues.add(TestEnum.TWO);
            someValues.add(TestEnum.THREE);
            return someValues;
        }

        @ModelObject
        public TestPmo getModelObject() {
            return this;
        }

    }

    @UISection
    protected static class TestPmoWithCustomWidth extends TestPmo {

        @UISubsetChooser(position = 1, width = "50%")
        @Override
        public Set<TestEnum> getFoo() {
            return super.getFoo();
        }

    }

    /**
     * Returns a {@code SubsetChooser} that is bound to a {@link AnnotationTestPmo} using the IPM data
     * binder. The {@code SubsetChooser} is part of a mostly mocked UI so that a rudimentary Vaadin
     * environment is in place.
     * 
     * @return a {@code SubsetChooser} that is bound to a {@link AnnotationTestPmo}
     */
    private SubsetChooser createSubsetChooser(Object pmo) {
        try {
            SubsetChooser subsetChooser = (SubsetChooser)TestUiUtil.createFirstComponentOf(pmo);
            // initializes the internal itemIdMapper which is needed to
            // fake selection from UI using AbstractSelect#changeVariables(...)
            subsetChooser.paintContent(mock(PaintTarget.class));
            return subsetChooser;
        } catch (PaintException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDefaultItemCaptionProvider() {
        SubsetChooser subsetChooser = createSubsetChooser(new TestPmo());
        assertThat(subsetChooser.getItemCaption(null), is(""));
        assertThat(subsetChooser.getItemCaption(TestEnum.ONE), is("ONE"));
    }

    @Test
    public void testNullSelectionIsAllowed() {
        SubsetChooser subsetChooser = createSubsetChooser(new TestPmo());
        assertThat(subsetChooser.isNullSelectionAllowed(), is(true));
    }

    @Test
    public void testDefaultWidthOneHundredPercent() {
        SubsetChooser subsetChooser = createSubsetChooser(new TestPmo());
        assertThat(subsetChooser.getWidth(), is(100.0F));
    }

    @Test
    public void testCustomWidth() {
        SubsetChooser subsetChooser = createSubsetChooser(new TestPmoWithCustomWidth());
        assertThat(subsetChooser.getWidth(), is(50.0F));
    }

    @Test
    public void testAddObjectToRightLineAndRemoveItAgain() {
        SubsetChooser subsetChooser = createSubsetChooser(new TestPmo());

        Map<String, Object> variables = new HashMap<>();
        variables.put("selected", new String[] { "1" });
        subsetChooser.changeVariables(null, variables);

        @SuppressWarnings("unchecked")
        Set<TestEnum> selectedValuesResult = (Set<TestEnum>)subsetChooser.getValue();
        assertThat(selectedValuesResult, containsInAnyOrder(TestEnum.ONE));

        variables.put("selected", new String[] {});
        subsetChooser.changeVariables(null, variables);

        @SuppressWarnings({ "unchecked", "null" })
        @NonNull
        Set<TestEnum> selectedValuesResultEmpty = (Set<TestEnum>)subsetChooser.getValue();
        assertThat(selectedValuesResultEmpty, is(empty()));
    }

    @Test
    public void testLeftCaptionAnnotation() {
        SubsetChooser subsetChooser = createSubsetChooser(new TestPmo());
        assertThat(subsetChooser.getLeftColumnCaption(), is("Left Column Caption"));
    }

    @Test
    public void testRightCaptionAnnotation() {
        SubsetChooser subsetChooser = createSubsetChooser(new TestPmo());
        assertThat(subsetChooser.getRightColumnCaption(), is("Right Column Caption"));
    }
}
