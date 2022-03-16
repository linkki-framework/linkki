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

import static java.util.stream.Collectors.joining;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.base.StaticModelToUiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.renderer.LitRenderer;

/**
 * Aspect definition that calls a method named {@code Function<?,String> get<PropertyName>ItemStyle()}
 * in the pmo. The function is called for every item in the combobox drop down list and the returned
 * string is set as class name for this item.
 * <p>
 * The aspect definition extends {@link StaticModelToUiAspectDefinition} because it either uses a static
 * list of style classes or it calls the returned function for every item. It is not supported to get
 * different functions for every update. This is important to not set a new renderer with every ui
 * update.
 */
public class BindComboBoxItemStyleAspectDefinition extends StaticModelToUiAspectDefinition<Function<Object, String>> {


    public static final String NAME = "itemStyle";

    static final String LABEL = "label";
    static final String STYLE = "style";

    private final List<String> staticStyles;

    /**
     * Creates a new aspect definition to modify the style names of combo box popup items.
     */
    public BindComboBoxItemStyleAspectDefinition(String... staticStyles) {
        this.staticStyles = Arrays.asList(staticStyles);
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        return super.createUiUpdater(propertyDispatcher, componentWrapper);
    }

    @Override
    public Aspect<Function<Object, String>> createAspect() {
        return staticStyles.isEmpty() ? Aspect.of(NAME)
                : Aspect.of(NAME, i -> staticStyles.stream().collect(joining(" ")));
    }

    @Override
    public Consumer<Function<Object, String>> createComponentValueSetter(ComponentWrapper componentWrapper) {
        @SuppressWarnings("unchecked")
        var comboBox = (ComboBox<Object>)componentWrapper.getComponent();
        return f -> comboBox
                .setRenderer(LitRenderer.of("<div class=\"${item." + STYLE + "}\">${item." + LABEL + "}</div>")
                        .withProperty(STYLE, f::apply)
                        .withProperty(LABEL, comboBox.getItemLabelGenerator()::apply));
    }

}