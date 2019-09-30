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

package org.linkki.core.ui.aspects;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.behavior.BehaviorDependentDispatcher;
import org.linkki.core.ui.aspects.annotation.BindReadOnly.ReadOnlyType;
import org.linkki.core.ui.wrapper.LabelComponentWrapper;
import org.mockito.Mockito;

import com.vaadin.data.HasValue;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;

import edu.umd.cs.findbugs.annotations.NonNull;

public class BindReadOnlyAspectDefinitionTest {

    
    private @NonNull BehaviorDependentDispatcher dispatcher;

    @BeforeEach
    public void setUp() {
        dispatcher = mock(BehaviorDependentDispatcher.class);
    }

    @Test
    public void testReadOnlyType_Always() {
        HasValue<?> field = createWritableComponent();

        createUiUpdaterAndApplyIt_BindReadOnlyAspect(field, ReadOnlyType.ALWAYS);

        assertThat(field.isReadOnly(), is(true));
    }


    @Test
    public void testReadOnlyType_Dynamic_writableComponent() {
        when(dispatcher.pull(Mockito.any())).thenReturn(true);
        HasValue<?> field = createWritableComponent();

        createUiUpdaterAndApplyIt_BindReadOnlyAspect(field, ReadOnlyType.DYNAMIC);

        assertThat(field.isReadOnly(), is(true));
    }

    @Test
    public void testReadOnlyType_Dynamic_NonWritableComponent() {
        when(dispatcher.pull(Mockito.any())).thenReturn(true);
        HasValue<?> field = createWritableComponent();
        field.setReadOnly(true);

        createUiUpdaterAndApplyIt_BindReadOnlyAspect(field, ReadOnlyType.DYNAMIC);

        assertThat(field.isReadOnly(), is(true));
    }


    @Test
    public void testReadOnlyType_Derived() {
        HasValue<?> field = createWritableComponent();

        assertThat(field.isReadOnly(), is(false));
        createUiUpdaterAndApplyIt_BindReadOnlyAspect(field, ReadOnlyType.DERIVED);
        assertThat(field.isReadOnly(), is(false));
        createUiUpdaterAndApplyIt_ReadOnlyAspect(field);
        assertThat(field.isReadOnly(), is(true));
        createUiUpdaterAndApplyIt_BindReadOnlyAspect(field, ReadOnlyType.DERIVED);
        assertThat(field.isReadOnly(), is(true));
    }

    private HasValue<?> createWritableComponent() {
        TextArea text = new TextArea();
        text.setReadOnly(false);
        return text;
    }

    private void createUiUpdaterAndApplyIt_ReadOnlyAspect(HasValue<?> field) {
        createUiUpdaterAndApplyIt(field, new DerivedReadOnlyAspectDefinition());
    }

    private void createUiUpdaterAndApplyIt_BindReadOnlyAspect(HasValue<?> field, ReadOnlyType value) {
        BindReadOnlyAspectDefinition aspectDefinition = new BindReadOnlyAspectDefinition(value);
        createUiUpdaterAndApplyIt(field, aspectDefinition);
    }

    private void createUiUpdaterAndApplyIt(HasValue<?> field, LinkkiAspectDefinition aspectDefinition) {
        aspectDefinition.createUiUpdater(dispatcher, new LabelComponentWrapper((Component)field)).apply();
    }

}
