/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.linkki.core.ui.element.annotation;

import static org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition.DERIVED_BY_LINKKI;
import static org.linkki.core.defaults.ui.aspects.types.CaptionType.STATIC;
import static org.linkki.core.defaults.ui.aspects.types.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator.SimpleMemberNameBoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.aspects.CaptionAspectDefinition;
import org.linkki.core.ui.aspects.LabelAspectDefinition;
import org.linkki.core.ui.aspects.LinkTargetAspectDefinition;
import org.linkki.core.ui.aspects.LinkValueAspectDefinition;
import org.linkki.core.ui.element.annotation.UILink.LinkAspectDefinitionCreator;
import org.linkki.core.ui.element.annotation.UILink.LinkComponentDefinitionCreator;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.core.vaadin.component.ComponentFactory;

/**
 * Provides a single UI-element to display a link. Creates a {@link com.vaadin.ui.Link}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBoundProperty(SimpleMemberNameBoundPropertyCreator.class)
@LinkkiComponent(LinkComponentDefinitionCreator.class)
@LinkkiAspect(LinkAspectDefinitionCreator.class)
@LinkkiPositioned
public @interface UILink {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed */
    @LinkkiPositioned.Position
    int position();

    /** Provides the link text */
    String label() default "";

    /**
     * Specifies if a component is shown, using values of {@link VisibleType}
     */
    VisibleType visible() default VISIBLE;

    /**
     * Static text displayed on the button. If the value should be determined dynamically, use
     * {@link CaptionType#DYNAMIC} instead and ignore this attribute.
     */
    String caption() default DERIVED_BY_LINKKI;

    /** Defines how the value of caption should be retrieved, using values of {@link CaptionType} */
    CaptionType captionType() default STATIC;

    /**
     * Specifies where to open the link. Might be one of {@link LinkTarget} or a specific frame.
     * <p>
     * To define a dynamic link use {@link LinkTarget#DYNAMIC} and implement a method called
     * {@code get<PropertyName>Target}
     */
    String target() default LinkTarget.SELF;

    static class LinkComponentDefinitionCreator implements ComponentDefinitionCreator<UILink> {

        @Override
        public LinkkiComponentDefinition create(UILink annotation, AnnotatedElement annotatedElement) {
            return pmo -> ComponentFactory.newLinkFullWidth(annotation.caption());
        }

    }

    /**
     * Aspect definition creator for the {@link UILink} annotation.
     */
    static class LinkAspectDefinitionCreator implements AspectDefinitionCreator<UILink> {

        @Override
        public LinkkiAspectDefinition create(UILink annotation) {
            return new CompositeAspectDefinition(
                    new LabelAspectDefinition(annotation.label()),
                    new VisibleAspectDefinition(annotation.visible()),
                    new LinkValueAspectDefinition(),
                    new CaptionAspectDefinition(annotation.captionType(), annotation.caption()),
                    new LinkTargetAspectDefinition(annotation.target(),
                            LinkTarget.DYNAMIC.equals(annotation.target())));
        }
    }

    /**
     * Common targets used for links.
     */
    final class LinkTarget {

        /**
         * The empty string indicates that the target is derived dynamically from the PMO.
         */
        public static final String DYNAMIC = "";

        /**
         * Opens the linked document in a new window or tab
         */
        public static final String BLANK = "_blank";

        /**
         * Opens the linked document in the same frame as it was clicked.
         */
        public static final String SELF = "_self";

        /**
         * Opens the linked document in the parent frame.
         */
        public static final String PARENT = "_parent";

        /**
         * Opens the linked document in the full body of the window.
         */
        public static final String TOP = "_top";

        private LinkTarget() {
            // prevents instantiation
        }
    }
}
