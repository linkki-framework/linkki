package org.linkki.core.ui.aspects.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.ui.aspects.BindReadOnlyBehaviorAspectDefinition;
import org.linkki.core.ui.aspects.annotation.BindReadOnlyBehavior.BindButtonReadOnlyAspectDefinitionCreator;

/**
 * Binds the application's read-only state to a specific behaviour of the annotated
 * {@link com.vaadin.ui.Component}. The component will automatically be disabled or invisible in
 * read-only-mode. This annotation will override the component's dynamic or static aspects of
 * {@link EnabledAspectDefinition enabled} or {@link VisibleAspectDefinition visible}, respectively.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
@LinkkiAspect(BindButtonReadOnlyAspectDefinitionCreator.class)
public @interface BindReadOnlyBehavior {

    ReadOnlyBehaviorType value() default ReadOnlyBehaviorType.INVSIBLE;

    enum ReadOnlyBehaviorType {

        /**
         * The button is visible, but not enabled in read-only mode.
         */
        DISABLED,
        /**
         * The button is not visible in read-only mode.
         */
        INVSIBLE;

    }

    class BindButtonReadOnlyAspectDefinitionCreator implements AspectDefinitionCreator<BindReadOnlyBehavior> {

        @Override
        public LinkkiAspectDefinition create(BindReadOnlyBehavior annotation) {
            return new BindReadOnlyBehaviorAspectDefinition(annotation.value());
        }

    }

}