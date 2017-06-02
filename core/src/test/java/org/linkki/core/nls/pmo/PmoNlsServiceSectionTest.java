/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.nls.pmo;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.linkki.core.binding.Binding;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ButtonBinding;
import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.FieldBinding;
import org.linkki.core.binding.TestBindingContext;
import org.linkki.core.nls.pmo.sample.SamplePmo;
import org.linkki.core.ui.section.BaseSection;
import org.linkki.core.ui.section.DefaultPmoBasedSectionFactory;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class PmoNlsServiceSectionTest {
    private static final String LABEL = "label";
    @SuppressWarnings("null")
    private FieldBinding<?> textFieldBinding;
    @SuppressWarnings("null")
    private FieldBinding<?> textFieldBinding2;

    @SuppressWarnings("null")
    private ButtonBinding buttonBinding;
    @SuppressWarnings("null")
    private ButtonBinding buttonBinding2;
    @SuppressWarnings("null")
    Label sectionHeader;

    @Before
    public void setUp() {
        BindingContext context = TestBindingContext.create();
        BaseSection section = new DefaultPmoBasedSectionFactory().createBaseSection(new SamplePmo(), context);
        HorizontalLayout header = (HorizontalLayout)section.getComponent(0);
        sectionHeader = (Label)header.getComponent(0);
        Collection<ElementBinding> bindings = context.getElementBindings();
        for (Binding binding : bindings) {
            if (binding instanceof FieldBinding) {
                FieldBinding<?> fieldBinding = (FieldBinding<?>)binding;
                String property = fieldBinding.getPropertyDispatcher().getProperty();
                switch (property) {
                    case "textField":
                        textFieldBinding = fieldBinding;
                        break;
                    case "cbField":
                        textFieldBinding2 = fieldBinding;
                        break;

                }
            } else if (binding instanceof ButtonBinding) {
                ButtonBinding fieldBinding = (ButtonBinding)binding;
                String property = fieldBinding.getPropertyDispatcher().getProperty();
                switch (property) {
                    case "myButton":
                        buttonBinding = (ButtonBinding)binding;
                        break;
                    case "myButton2":
                        buttonBinding2 = (ButtonBinding)binding;
                        break;
                }

            }
        }


    }

    @Test
    public void testSectionCaption() {
        assertThat(sectionHeader.getValue(), is("Other sample caption"));
    }

    @Test
    public void testLabels()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

        /// label overrides in linkki-messages.properties
        assertThat(hackLabel(textFieldBinding).get().getValue(), is("Text Field Label"));
        // label from PMO. No overriding
        assertThat(hackLabel(textFieldBinding2).get().getValue(), is("CbField"));
        // button label. Overrides in linkki-messages.properties
        assertThat(hackLabel(buttonBinding).get().getValue(), is("Button label override"));
        // button label. No overriding
        assertThat(hackLabel(buttonBinding2).get().getValue(), is(""));
        // Check button captions
        assertThat(buttonBinding.getCaption(), is("Button caption override"));

        assertThat(buttonBinding2.getCaption(), is("Button2 Caption"));


    }


    @SuppressWarnings({ "unchecked", "null" })
    private Optional<Label> hackLabel(Object o)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = o.getClass();
        Field label = clazz.getDeclaredField(LABEL);
        if (label != null) {
            label.setAccessible(true);
            return (Optional<Label>)label.get(o);
        }
        return Optional.empty();

    }
}
