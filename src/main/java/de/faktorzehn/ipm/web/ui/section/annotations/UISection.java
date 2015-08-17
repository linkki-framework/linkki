package de.faktorzehn.ipm.web.ui.section.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UISection {

    SectionLayout layout() default SectionLayout.COLUMN;

    String caption() default "";

    boolean closeable() default false;

}
