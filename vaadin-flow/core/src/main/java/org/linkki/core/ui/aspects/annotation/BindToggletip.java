/*******************************************************
 * Copyright (c) Faktor Zehn GmbH - www.faktorzehn.de
 *
 * All Rights Reserved - Alle Rechte vorbehalten.
 *******************************************************/

package org.linkki.core.ui.aspects.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.ui.aspects.ToggletipAspectDefinition;
import org.linkki.core.ui.aspects.types.ToggletipType;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.shared.HasTooltip;
import com.vaadin.flow.component.shared.Tooltip;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Setzt einen Toggletip für die {@linkplain Component Komponente}. Die Komponente muss {@link HasTooltip} implementieren.
 * Im Gegensatz zu {@link BindTooltip}, wird die neue Vaadin 23 Tooltip API verwendet, statt dem Setzen des Attributes
 * title.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
@LinkkiAspect(BindToggletip.BindToggletipAspectDefinitionCreator.class)
public @interface BindToggletip {

    String value() default StringUtils.EMPTY;

    /** Defines how the tooltip text should be retrieved */
    ToggletipType toggletipType() default ToggletipType.AUTO;

    ToogletipPosition toggletipPosition() default ToogletipPosition.SUFFIX;

    Tooltip.TooltipPosition position() default Tooltip.TooltipPosition.TOP;

    VaadinIcon icon() default VaadinIcon.QUESTION_CIRCLE_O;

    enum ToogletipPosition {
        PREFIX,
        SUFFIX
    }

    class BindToggletipAspectDefinitionCreator implements AspectDefinitionCreator<BindToggletip> {

        @Override
        public LinkkiAspectDefinition create(@NonNull BindToggletip annotation) {
            return new ToggletipAspectDefinition(annotation);
        }

    }

}
