/*
 * Copyright Faktor Zehn GmbH.
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
package org.linkki.core.ui.aspects.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.ui.aspects.BindFormItemLabelWidthAspectDefinition;
import org.linkki.core.ui.layout.annotation.SectionLayout;

/**
 * This aspect sets a user defined label width for all form items of the annotated layout.
 *
 * Always consider the alternative of setting the sections layout to {@link SectionLayout#VERTICAL},
 * which will display the label on top of the field instead. Only use this annotation when labels
 * are actually required to be aligned left of the fields.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@LinkkiAspect(BindFormItemLabelWidth.BindFormItemLabelWidthAspectDefinitionCreator.class)
public @interface BindFormItemLabelWidth {

    /**
     * The width to be set on the form item label.
     * <p>
     * Consider using the units rem and em to define the width based on the content.
     * </p>
     */
    String value();

    class BindFormItemLabelWidthAspectDefinitionCreator implements AspectDefinitionCreator<BindFormItemLabelWidth> {
        @Override
        public LinkkiAspectDefinition create(BindFormItemLabelWidth annotation) {
            return new BindFormItemLabelWidthAspectDefinition(annotation.value());
        }
    }

}
