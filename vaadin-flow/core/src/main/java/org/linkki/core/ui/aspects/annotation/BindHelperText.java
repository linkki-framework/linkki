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
import org.linkki.core.ui.aspects.HelperTextAspectDefinition;
import org.linkki.core.ui.aspects.types.HelperTextType;
import org.linkki.core.ui.aspects.types.IconPosition;
import org.linkki.core.util.HtmlSanitizer;

import com.vaadin.flow.component.HasHelper;
import com.vaadin.flow.component.icon.VaadinIcon;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Setzt einen Helper Text an der Component. Diese muss {@link HasHelper} implementieren.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
@LinkkiAspect(BindHelperText.BindHelperTextAspectDefinition.class)
public @interface BindHelperText {

    String value() default StringUtils.EMPTY;

    /** Defines how the tooltip text should be retrieved */
    HelperTextType helperTextType() default HelperTextType.AUTO;

    /**
     * When set to {@code true}, the label's content will be displayed as HTML, otherwise as plain text.
     * The HTML content is automatically {@link HtmlSanitizer#sanitizeText(String) sanitized}. <br>
     * Note that <b>user-supplied strings have to be {@link HtmlSanitizer#escapeText(String)
     * escaped}</b> when including them in the HTML content. Otherwise, they will also be interpreted as
     * HTML.
     * <p>
     * HTML content is not compatible with some annotations that manipulate the resulting component,
     * like {@link BindIcon}.
     */
    boolean htmlContent() default false;

    boolean placeAboveElement() default false;

    boolean showIcon() default false;
    VaadinIcon icon() default VaadinIcon.INFO_CIRCLE_O;
    IconPosition iconPosition() default IconPosition.LEFT;

    class BindHelperTextAspectDefinition implements AspectDefinitionCreator<BindHelperText> {

        @Override
        public LinkkiAspectDefinition create(@NonNull BindHelperText annotation) {
            return new HelperTextAspectDefinition(annotation);
        }

    }
}
