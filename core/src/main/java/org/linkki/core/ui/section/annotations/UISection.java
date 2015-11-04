package org.linkki.core.ui.section.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UISection {

    /** Layout for the section, i.e. if fields are displayed horizontally or in vertical columns. */
    SectionLayout layout() default SectionLayout.COLUMN;

    /**
     * Number of columns if the {@link SectionLayout#COLUMN} layout is used. Ignored if an other
     * layout is used.
     */
    int columns() default 1;

    /** The caption text for the section. */
    String caption() default "";

    /** Whether or not the section can be closed by the user. */
    boolean closeable() default false;

}
