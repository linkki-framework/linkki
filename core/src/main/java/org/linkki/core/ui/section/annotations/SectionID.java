package org.linkki.core.ui.section.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method that returns a section's id. That ID is used as id in the resulting
 * <code>&lt;div&gt;</code> element in the HTML tree, for example to identify it for UI testing.
 * <p>
 * The annotated method is called exactly once when creating the section. A section's ID will remain
 * the same while it is displayed. It is never updated dynamically.
 * <p>
 * If the PMO class has no method that is annotated with SectionID, its class name will be used as
 * an id (fallback). Note that if the class name changes, that ID also changes. This may cause UI
 * tests to break. So when a section is involved in UI testing, an ID should be supplied via a
 * method annotated with {@link SectionID @SectionID} to ensure future correctness.
 * <p>
 * If more than one method is annotated with {@link SectionID @SectionID}, the first one will be
 * used.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SectionID {
    // No properties yet.
}
