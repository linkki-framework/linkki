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

package org.linkki.core.ui.aspects.annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.defaults.ui.aspects.types.IconType;
import org.linkki.core.ui.aspects.IconAspectDefinition;
import org.linkki.core.ui.aspects.annotation.BindIcon.BindIconAspectDefinitionCreator;

import com.vaadin.flow.component.icon.VaadinIcon;

public class BindIconTest {

    @BindIcon(iconType = IconType.DYNAMIC)
    @Test
    public void testCreateAspect_Dynamic() {
        BindIconAspectDefinitionCreator bindIconAspectDefinitionCreator = new BindIconAspectDefinitionCreator();
        IconAspectDefinition linkkiAspectDefinition = bindIconAspectDefinitionCreator.create(getCurrentAnnotation());

        Aspect<VaadinIcon> aspect = linkkiAspectDefinition.createAspect();

        assertThat(aspect.isValuePresent(), is(false));
        assertThat(aspect.getName(), is(IconAspectDefinition.NAME));
    }

    @BindIcon
    @Test
    public void testCreateAspect_AutoDynamic() {
        BindIconAspectDefinitionCreator bindIconAspectDefinitionCreator = new BindIconAspectDefinitionCreator();
        IconAspectDefinition linkkiAspectDefinition = bindIconAspectDefinitionCreator.create(getCurrentAnnotation());

        Aspect<VaadinIcon> aspect = linkkiAspectDefinition.createAspect();

        assertThat(aspect.isValuePresent(), is(false));
        assertThat(aspect.getName(), is(IconAspectDefinition.NAME));
    }

    @BindIcon(VaadinIcon.ABACUS)
    @Test
    public void testCreateAspect_AutoStaticIcon() {
        BindIconAspectDefinitionCreator bindIconAspectDefinitionCreator = new BindIconAspectDefinitionCreator();
        IconAspectDefinition linkkiAspectDefinition = bindIconAspectDefinitionCreator.create(getCurrentAnnotation());

        Aspect<VaadinIcon> aspect = linkkiAspectDefinition.createAspect();

        assertThat(aspect.getValue(), is(VaadinIcon.ABACUS));
        assertThat(aspect.getName(), is(IconAspectDefinition.NAME));
    }

    @BindIcon(iconType = IconType.STATIC)
    @Test
    public void testCreateAspect_NativeButton() {
        BindIconAspectDefinitionCreator bindIconAspectDefinitionCreator = new BindIconAspectDefinitionCreator();
        IconAspectDefinition linkkiAspectDefinition = bindIconAspectDefinitionCreator.create(getCurrentAnnotation());

        Aspect<VaadinIcon> aspect = linkkiAspectDefinition.createAspect();

        assertThat(aspect.getValue(), is(VaadinIcon.NATIVE_BUTTON));
        assertThat(aspect.getName(), is(IconAspectDefinition.NAME));
    }

    @BindIcon(value = VaadinIcon.ABSOLUTE_POSITION, iconType = IconType.STATIC)
    @Test
    public void testCreateAspect_Static() {
        BindIconAspectDefinitionCreator bindIconAspectDefinitionCreator = new BindIconAspectDefinitionCreator();
        IconAspectDefinition linkkiAspectDefinition = bindIconAspectDefinitionCreator.create(getCurrentAnnotation());

        Aspect<VaadinIcon> aspect = linkkiAspectDefinition.createAspect();

        assertThat(aspect.getValue(), is(VaadinIcon.ABSOLUTE_POSITION));
        assertThat(aspect.getName(), is(IconAspectDefinition.NAME));
    }

    private BindIcon getCurrentAnnotation() {
        try {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            return this.getClass().getMethod(methodName).getAnnotation(BindIcon.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

}
