/*
 * Copyright Faktor Zehn AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.linkki.core.ui.section.annotations.aspect;

import java.lang.annotation.Annotation;

import org.linkki.core.binding.aspect.definition.ButtonInvokeAspectDefinition;
import org.linkki.core.binding.aspect.definition.CaptionAspectDefinition;
import org.linkki.core.binding.aspect.definition.CompositeAspectDefinition;
import org.linkki.core.ui.section.annotations.CaptionType;
import org.linkki.core.ui.section.annotations.UIButton;

/**
 * Aspect definition for {@link UIButton} annotation.
 */
public class UIButtonAspectDefinition extends CompositeAspectDefinition {

    public UIButtonAspectDefinition() {
        super(new UIElementEnabledAspectDefinition(),
                new UIElementVisibleAspectDefinition(),
                new ButtonCaptionAspectDefinition(),
                new ButtonInvokeAspectDefinition());
    }

    public static class ButtonCaptionAspectDefinition extends CaptionAspectDefinition {

        @SuppressWarnings("null")
        private UIButton buttonAnnotation;

        @Override
        public void initialize(Annotation annotation) {
            this.buttonAnnotation = (UIButton)annotation;
        }

        @Override
        protected String getStaticCaption() {
            return buttonAnnotation.caption();
        }

        @Override
        public CaptionType getCaptionType() {
            return buttonAnnotation.captionType();
        }

    }
}
