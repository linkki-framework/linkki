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
import org.linkki.core.binding.TestEnum;

import com.google.gwt.thirdparty.guava.common.collect.Sets;
import com.vaadin.ui.ComboBox;

public class UIComboBoxTest {

    @UISection
    protected static class TestPmo implements PresentationModelObject {

        @UIComboBox(position = 1)
        public TestEnum getFoo() {
            return TestEnum.ONE;
        }

        public Set<TestEnum> getFooAvailableValues() {
            LinkedHashSet<TestEnum> someValues = Sets.newLinkedHashSet();
            someValues.add(TestEnum.ONE);
            someValues.add(TestEnum.TWO);
            someValues.add(TestEnum.THREE);
            return someValues;
        }

        @Override
        public Object getModelObject() {
            return this;
        }
    }

    private ComboBox createComboBox() {
        TestPmo pmo = new TestPmo();
        ComboBox comboBox = (ComboBox)TestUi.componentBoundTo(pmo);
        return comboBox;
    }

    @Test
    public void testNullSelectionIsNotAllowed() {
        ComboBox comboBox = createComboBox();
        assertThat(comboBox.isNullSelectionAllowed(), is(false));
    }
}
