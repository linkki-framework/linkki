package org.linkki.tooling.apt.test.aspectCreationExceptionHandling;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiAspect(FailingAspectDefinition.Creator.class)
public @interface FailingAspect {

}
