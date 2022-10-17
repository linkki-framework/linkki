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
 *
 */

package org.linkki.core.ui.aspects.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.LabelType;
import org.linkki.core.ui.aspects.LabelAspectDefinition;
import org.linkki.core.ui.aspects.annotation.BindLabel.BindLabelAspectDefinitionCreator;

/**
 * Tests {@link BindLabel @BindLabel}.
 */
class BindLabelTest {

    private static final String LABEL = "I am a label";

    @Test
    void testBindLabel_Static() {

        BindLabelAspectDefinitionCreator creator = new BindLabelAspectDefinitionCreator();
        var testObject = new Object() {
            @BindLabel(value = LABEL, labelType = LabelType.STATIC)
            public void testMethod() {
                // nop
            }
        };
        BindLabel bindLabelAnnotation = getBindLabelAnnotation(testObject);
        LinkkiAspectDefinition aspectDefinition = creator.create(bindLabelAnnotation);

        Aspect<String> aspect = ((LabelAspectDefinition)aspectDefinition).createAspect();

        assertThat(aspect.getName()).isEqualTo(LabelAspectDefinition.NAME);
        assertThat(aspect.getValue()).isEqualTo(LABEL);
    }

    @Test
    void testBindLabel_Dynamic() {

        BindLabelAspectDefinitionCreator creator = new BindLabelAspectDefinitionCreator();
        var testObject = new Object() {
            @BindLabel(labelType = LabelType.DYNAMIC)
            public void testMethod() {
                // nop
            }
        };
        LinkkiAspectDefinition aspectDefinition = creator.create(getBindLabelAnnotation(testObject));

        Aspect<String> aspect = ((LabelAspectDefinition)aspectDefinition).createAspect();

        assertThat(aspect.getName()).isEqualTo(LabelAspectDefinition.NAME);
        assertThat(aspect.isValuePresent()).isFalse();
    }

    private BindLabel getBindLabelAnnotation(Object object) {
        return object.getClass().getMethods()[0].getAnnotation(BindLabel.class);
    }

}
