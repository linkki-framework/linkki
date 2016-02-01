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
import java.util.function.Function;

import org.linkki.core.ui.components.SubsetChooser.ToStringCaptionProvider;

/**
 * Creates a subset chooser, i.e. a multi-select component with a left and a right list.
 * <p>
 * Note that the value handled by a subset chooser needs to be a {@link Set} whereas the list of
 * available values could be any kind of {@link Collection}. When using this annotation you will
 * presumably need something like this:
 * 
 * <pre>
 * <code>
&#64;UISubsetChooser(...)
public Set&lt;T&gt; getFoo() { ... }

public void setFoo(Set&lt;T&gt; selectedFoos) { ... }

public Set&lt;T&gt; getFooAvailableValues() { ... }
 * </code>
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UISubsetChooser {

    int position();

    String label() default "";

    boolean noLabel() default false;

    AvailableValuesType content() default AvailableValuesType.STATIC;

    EnabledType enabled() default ENABLED;

    RequiredType required() default NOT_REQUIRED;

    VisibleType visible() default VISIBLE;

    Class<?> modelClass() default NoModelClassProvided.class;

    String modelAttribute() default "";

    Class<? extends Function<Object, String>> itemCaptionProvider() default ToStringCaptionProvider.class;

}
