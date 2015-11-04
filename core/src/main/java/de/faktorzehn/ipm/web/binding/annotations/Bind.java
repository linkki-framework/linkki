package de.faktorzehn.ipm.web.binding.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ortmann
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Bind {

    String valueProperty() default "";

    String enabledProperty() default "";

    String requiredProperty() default "";

    String lovProperty() default "";

}
