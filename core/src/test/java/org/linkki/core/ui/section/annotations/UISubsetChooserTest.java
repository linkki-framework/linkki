/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

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

import org.junit.Test;
import org.linkki.core.binding.TestEnum;
import org.linkki.core.ui.components.SubsetChooser;

import com.google.gwt.thirdparty.guava.common.collect.Sets;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;

public class UISubsetChooserTest {

    @UISection
    protected static class TestPmo {

        private final Set<TestEnum> foo = Sets.newLinkedHashSet();

        @UISubsetChooser(position = 1, leftColumnCaption = "Left Column Caption", rightColumnCaption = "Right Column Caption")
        public Set<TestEnum> getFoo() {
            return foo;
        }

        public void setFoo(Set<TestEnum> selectedFoos) {
            foo.clear();
            foo.addAll(selectedFoos);
        }

        public Set<TestEnum> getFooAvailableValues() {
            LinkedHashSet<TestEnum> someValues = Sets.newLinkedHashSet();
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
     * Returns a {@code SubsetChooser} that is bound to a {@link TestPmo} using the IPM data binder.
     * The {@code SubsetChooser} is part of a mostly mocked UI so that a rudimentary Vaadin
     * environment is in place.
     * 
     * @return a {@code SubsetChooser} that is bound to a {@link TestPmo}
     */
    private SubsetChooser createSubsetChooser(Object pmo) {
        try {
            SubsetChooser subsetChooser = (SubsetChooser)TestUi.componentBoundTo(pmo);
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

        @SuppressWarnings("unchecked")
        Set<TestEnum> selectedValuesResultEmpty = (Set<TestEnum>)subsetChooser.getValue();
        assertThat(selectedValuesResultEmpty, empty());
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
