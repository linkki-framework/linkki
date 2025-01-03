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
import org.linkki.core.ui.aspects.TooltipAspectDefinition;
import org.linkki.core.ui.aspects.types.TooltipType;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.shared.HasTooltip;
import com.vaadin.flow.component.shared.Tooltip;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Setzt einen Tooltip für die {@linkplain Component Komponente}. Die Komponente muss {@link HasTooltip} implementieren.
 * Im Gegensatz zu {@link org.linkki.core.defaults.ui.aspects.annotations.BindTooltip}, wird die neue Vaadin 23 Tooltip API verwendet, statt dem Setzen des Attributes
 * title.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
@LinkkiAspect(BindTooltip.BindTooltipAspectDefinitionCreator.class)
public @interface BindTooltip {

    int DEFAULT_DELAY = -1;

    String value() default StringUtils.EMPTY;

    /** Defines how the tooltip text should be retrieved */
    TooltipType tooltipType() default TooltipType.AUTO;

    int focusDelay() default DEFAULT_DELAY;
    int hideDelay() default DEFAULT_DELAY;
    int hoverDelay() default DEFAULT_DELAY;

    Tooltip.TooltipPosition position() default Tooltip.TooltipPosition.TOP;


    class BindTooltipAspectDefinitionCreator implements AspectDefinitionCreator<BindTooltip> {

        @Override
        public LinkkiAspectDefinition create(@NonNull BindTooltip annotation) {
            return new TooltipAspectDefinition(annotation);
        }

    }

}
