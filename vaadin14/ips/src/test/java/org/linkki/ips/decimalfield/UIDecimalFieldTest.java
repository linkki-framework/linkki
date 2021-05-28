/*******************************************************************************
 * Copyright (c) Faktor Zehn GmbH - www.faktorzehn.de
 * 
 * All Rights Reserved - Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.ips.decimalfield;

import static java.util.Objects.requireNonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.is;

import java.lang.annotation.Annotation;

import org.junit.jupiter.api.Test;

import edu.umd.cs.findbugs.annotations.OverrideMustInvoke;

@SuppressWarnings({ "deprecation", "javadoc" })
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

    /**
     * {@link org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition} is not used
     * anymore
     */
    @Test
    public void testIsLinkkiBindingDefinition() {
        assertThat(org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition
                .isLinkkiBindingDefinition(annotation(UIDecimalField.class)), is(false));
    }

    @Test
    public void testFrom() {
        Class<? extends Annotation> annotationClass = requireNonNull(annotation(UIDecimalField.class),
                                                                     "annotation must not be null").annotationType();
        org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition[] bindingDefinitions = annotationClass
                .getAnnotationsByType(org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition.class);
        assertThat(bindingDefinitions, is(emptyArray()));
    }

}
