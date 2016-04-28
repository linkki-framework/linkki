/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations.adapters;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.ui.section.annotations.AvailableValuesType;

public class BindAnnotationAdapterTest {

    @Bind(pmoProperty = "")
    public Bind defaultAnnotation() {
        try {
            return getClass().getMethod("defaultAnnotation", new Class<?>[] {}).getAnnotation(Bind.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @Bind(pmoProperty = "", availableValues = AvailableValuesType.DYNAMIC)
    public Bind customAnnotation() {
        try {
            return getClass().getMethod("customAnnotation", new Class<?>[] {}).getAnnotation(Bind.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAvailableValues_DefaultValueIsUsed() {
        BindAnnotationAdapter adapter = new BindAnnotationAdapter(defaultAnnotation());
        assertThat(adapter.availableValues(), is(AvailableValuesType.NO_VALUES));
    }

    @Test
    public void testAvailableValues_CustomValuesIsRead() {
        BindAnnotationAdapter adapter = new BindAnnotationAdapter(customAnnotation());
        assertThat(adapter.availableValues(), is(AvailableValuesType.DYNAMIC));
    }

}
