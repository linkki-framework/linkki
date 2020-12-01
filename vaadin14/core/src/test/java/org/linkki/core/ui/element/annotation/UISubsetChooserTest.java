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
package org.linkki.core.ui.element.annotation;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.bind.TestEnum;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.ui.TwinColSelect;

import edu.umd.cs.findbugs.annotations.NonNull;

public class UISubsetChooserTest {

    @UISection
    protected static class TestPmo {

        private final Set<TestEnum> foo = new LinkedHashSet<>();

        @UISubsetChooser(position = 1, label = "", leftColumnCaption = "Left Column Caption", rightColumnCaption = "Right Column Caption")
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

        @UISubsetChooser(position = 1, label = "", width = "50%")
        @Override
        public Set<TestEnum> getFoo() {
            return super.getFoo();
        }

    }

    /**
     * Returns a {@code TwinColSelect} that is bound to a {@link AnnotationTestPmo} using the IPM data
     * binder. The {@code TwinColSelect} is part of a mostly mocked UI so that a rudimentary Vaadin
     * environment is in place.
     * 
     * @return a {@code TwinColSelect} that is bound to a {@link AnnotationTestPmo}
     */
    @SuppressWarnings("unchecked")
    private TwinColSelect<TestEnum> createSubsetChooser(Object pmo) {
        return (TwinColSelect<TestEnum>)TestUiUtil.createFirstComponentOf(pmo);
    }


    @Test
    public void testDefaultItemCaptionProvider() {
        TwinColSelect<TestEnum> subsetChooser = createSubsetChooser(new TestPmo());
        assertThat(subsetChooser.getItemCaptionGenerator().apply(null), is(""));
        assertThat(subsetChooser.getItemCaptionGenerator().apply(TestEnum.ONE), is("Oans"));
    }

    @Test
    public void testNullSelectionIsAllowed() {
        TwinColSelect<TestEnum> subsetChooser = createSubsetChooser(new TestPmo());
        assertThat(subsetChooser.getSelectedItems(), is(empty()));
    }

    @Test
    public void testDefaultWidthOneHundredPercent() {
        TwinColSelect<TestEnum> subsetChooser = createSubsetChooser(new TestPmo());
        assertThat(subsetChooser.getWidth(), is(100.0F));
    }

    @Test
    public void testCustomWidth() {
        TwinColSelect<TestEnum> subsetChooser = createSubsetChooser(new TestPmoWithCustomWidth());
        assertThat(subsetChooser.getWidth(), is(50.0F));
    }

    @Test
    public void testAddObjectToRightLineAndRemoveItAgain() {
        TwinColSelect<TestEnum> subsetChooser = createSubsetChooser(new TestPmo());

        subsetChooser.select(TestEnum.ONE);

        Set<TestEnum> selectedValuesResult = subsetChooser.getValue();
        assertThat(selectedValuesResult, containsInAnyOrder(TestEnum.ONE));

        subsetChooser.deselectAll();


        @NonNull
        Set<TestEnum> selectedValuesResultEmpty = subsetChooser.getValue();
        assertThat(selectedValuesResultEmpty, is(empty()));
    }

    @Test
    public void testLeftCaptionAnnotation() {
        TwinColSelect<TestEnum> subsetChooser = createSubsetChooser(new TestPmo());
        assertThat(subsetChooser.getLeftColumnCaption(), is("Left Column Caption"));
    }

    @Test
    public void testRightCaptionAnnotation() {
        TwinColSelect<TestEnum> subsetChooser = createSubsetChooser(new TestPmo());
        assertThat(subsetChooser.getRightColumnCaption(), is("Right Column Caption"));
    }
}
