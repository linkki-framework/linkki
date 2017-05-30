package org.linkki.core.ui.section.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a method returning a domain model object for a presentation model. The annotated method
 * must not return {@code null}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ModelObject {

    public static final String DEFAULT_NAME = "modelObject";

    /**
     * @return the name used to reference the domain model object. If there is only one such object
     *         in the presentation model, the default name "modelObject" can be used by omitting
     *         this attribute.
     */
    String name() default DEFAULT_NAME;

}
