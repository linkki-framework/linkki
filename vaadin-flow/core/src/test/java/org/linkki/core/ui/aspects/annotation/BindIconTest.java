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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.defaults.ui.aspects.types.IconType;
import org.linkki.core.ui.aspects.IconAspectDefinition;
import org.linkki.core.ui.aspects.annotation.BindIcon.BindIconAspectDefinitionCreator;

import com.vaadin.flow.component.icon.VaadinIcon;

class BindIconTest {

    @Test
    void testCreateAspect_Dynamic() {
        BindIconAspectDefinitionCreator aspectDefinitionCreator = new BindIconAspectDefinitionCreator();
        var testObject = new Object() {
            @BindIcon(iconType = IconType.DYNAMIC)
            public void testMethod() {
                // nop
            }
        };
        IconAspectDefinition aspectDefinition = aspectDefinitionCreator.create(getBindIconAnnotation(testObject));

        Aspect<VaadinIcon> aspect = aspectDefinition.createAspect();

        assertThat(aspect.isValuePresent(), is(false));
        assertThat(aspect.getName(), is(IconAspectDefinition.NAME));
    }

    @Test
    void testCreateAspect_AutoDynamic() {
        BindIconAspectDefinitionCreator bindIconAspectDefinitionCreator = new BindIconAspectDefinitionCreator();
        var testObject = new Object() {
            @BindIcon
            public void testMethod() {
                // nop
            }
        };
        IconAspectDefinition aspectDefinition = bindIconAspectDefinitionCreator
                .create(getBindIconAnnotation(testObject));

        Aspect<VaadinIcon> aspect = aspectDefinition.createAspect();

        assertThat(aspect.isValuePresent(), is(false));
        assertThat(aspect.getName(), is(IconAspectDefinition.NAME));
    }

    @Test
    void testCreateAspect_AutoStaticIcon() {
        BindIconAspectDefinitionCreator bindIconAspectDefinitionCreator = new BindIconAspectDefinitionCreator();
        var testObject = new Object() {
            @BindIcon(VaadinIcon.ABACUS)
            public void testMethod() {
                // nop
            }
        };
        IconAspectDefinition aspectDefinition = bindIconAspectDefinitionCreator
                .create(getBindIconAnnotation(testObject));

        Aspect<VaadinIcon> aspect = aspectDefinition.createAspect();

        assertThat(aspect.getValue(), is(VaadinIcon.ABACUS));
        assertThat(aspect.getName(), is(IconAspectDefinition.NAME));
    }

    @Test
    void testCreateAspect_NativeButton() {
        BindIconAspectDefinitionCreator bindIconAspectDefinitionCreator = new BindIconAspectDefinitionCreator();
        var testObject = new Object() {
            @BindIcon(iconType = IconType.STATIC)
            public void testMethod() {
                // nop
            }
        };
        IconAspectDefinition aspectDefinition = bindIconAspectDefinitionCreator
                .create(getBindIconAnnotation(testObject));

        Aspect<VaadinIcon> aspect = aspectDefinition.createAspect();

        assertThat(aspect.getValue(), is(VaadinIcon.NATIVE_BUTTON));
        assertThat(aspect.getName(), is(IconAspectDefinition.NAME));
    }

    @Test
    void testCreateAspect_Static() {
        BindIconAspectDefinitionCreator bindIconAspectDefinitionCreator = new BindIconAspectDefinitionCreator();
        var testObject = new Object() {
            @BindIcon(value = VaadinIcon.ABSOLUTE_POSITION, iconType = IconType.STATIC)
            public void testMethod() {
                // nop
            }
        };
        IconAspectDefinition aspectDefinition = bindIconAspectDefinitionCreator
                .create(getBindIconAnnotation(testObject));

        Aspect<VaadinIcon> aspect = aspectDefinition.createAspect();

        assertThat(aspect.getValue(), is(VaadinIcon.ABSOLUTE_POSITION));
        assertThat(aspect.getName(), is(IconAspectDefinition.NAME));
    }

    private BindIcon getBindIconAnnotation(Object object) {
        return object.getClass().getMethods()[0].getAnnotation(BindIcon.class);
    }
}