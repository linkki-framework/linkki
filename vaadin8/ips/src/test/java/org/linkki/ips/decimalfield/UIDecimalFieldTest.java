/*******************************************************************************
 * Copyright (c) Faktor Zehn GmbH - www.faktorzehn.de
 * 
 * All Rights Reserved - Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.ips.decimalfield;

import static java.util.Objects.requireNonNull;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.lang.annotation.Annotation;

import org.junit.Test;
import org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition;

import edu.umd.cs.findbugs.annotations.OverrideMustInvoke;

public class UIDecimalFieldTest {


    @UIDecimalField(position = 0, label = "")
    @OverrideMustInvoke
    public void annotatedMethod() {
        // Nothing to do
    }

    private <T extends Annotation> T annotation(Class<T> annotationClass) {
        try {
            T annotation = getClass().getMethod("annotatedMethod", new Class<?>[] {}).getAnnotation(annotationClass);
            return requireNonNull(annotation, () -> "Missing annotation @" + annotationClass.getSimpleName());
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testIsLinkkiBindingDefinition() {
        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(UIDecimalField.class)), is(true));
    }


    @Test
    public void testFrom() {
        assertThat(BindingDefinition.from(annotation(UIDecimalField.class)),
                   is(instanceOf(DecimalFieldBindingDefinition.class)));
    }

}
