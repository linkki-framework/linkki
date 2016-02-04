/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;
import org.linkki.core.PresentationModelObject;
import org.linkki.core.ui.components.SubsetChooser;

import com.google.gwt.thirdparty.guava.common.collect.Sets;

public class UISubsetChooserTest {

    @UISection
    protected static class TestPmo implements PresentationModelObject {

        private final Set<Integer> foo = Sets.newLinkedHashSet();

        @UISubsetChooser(position = 1, content = AvailableValuesType.DYNAMIC)
        public Set<Integer> getFoo() {
            return foo;
        }

        public void setFoo(Set<Integer> selectedFoos) {
            foo.clear();
            foo.addAll(selectedFoos);
        }

        public Set<Integer> getFooAvailableValues() {
            LinkedHashSet<Integer> someValues = Sets.newLinkedHashSet();
            someValues.add(1);
            someValues.add(2);
            someValues.add(3);
            return someValues;
        }

        @Override
        public Object getModelObject() {
            return this;
        }

    }

    /**
     * Returns a {@code SubsetChooser} that is bound to a {@link TestPmo} using the IPM data binder.
     * The {@code SubsetChooser} is part of a mostly mocked UI so that a rudimentary Vaadin
     * environment is in place.
     * 
     * @return a {@code SubsetChooser} that is bound to a {@link TestPmo}
     */
    private SubsetChooser createSubsetChooser() {
        TestPmo pmo = new TestPmo();
        return (SubsetChooser)TestUi.componentBoundTo(pmo);
    }

    @Test
    public void testDefaultItemCaptionProvider() {
        SubsetChooser subsetChooser = createSubsetChooser();
        assertThat(subsetChooser.getItemCaption(null), is(""));
        assertThat(subsetChooser.getItemCaption(1), is("1"));
    }

}
