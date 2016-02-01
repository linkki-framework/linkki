package org.linkki.core.ui.section.annotations;

import static org.linkki.core.ui.section.annotations.EnabledType.ENABLED;
import static org.linkki.core.ui.section.annotations.RequiredType.NOT_REQUIRED;
import static org.linkki.core.ui.section.annotations.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Set;

/**
 * Creates a twin column select (a multi-select component with a left and a right list) a.k.a. a
 * "subset chooser".
 * <p>
 * Note that the values handled by a twin column select needs to be a {@link Set} whereas the list
 * of available values could be any kind of {@link Collection}. When using this annotation you will
 * presumably need something like this:
 * 
 * <pre>
 * <code>
&#64;UITwinColSelect(...)
public Set&lt;T&gt; getFoo() { ... }

public void setFoo(Set&lt;T&gt; selectedFoos) { ... }

public Set&lt;T&gt; getFooAvailableValues() { ... }
 * </code>
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UITwinColSelect {

    int position();

    String label() default "";

    boolean noLabel() default false;

    AvailableValuesType content() default AvailableValuesType.STATIC;

    EnabledType enabled() default ENABLED;

    RequiredType required() default NOT_REQUIRED;

    VisibleType visible() default VISIBLE;

    Class<?> modelClass() default NoModelClassProvided.class;

    String modelAttribute() default "";

}
