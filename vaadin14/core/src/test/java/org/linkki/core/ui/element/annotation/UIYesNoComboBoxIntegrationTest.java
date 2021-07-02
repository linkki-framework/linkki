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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.ui.element.annotation.UIYesNoComboBoxIntegrationTest.YesNoComboBoxTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.provider.Query;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class UIYesNoComboBoxIntegrationTest
        extends ComponentAnnotationIntegrationTest<ComboBox<Boolean>, YesNoComboBoxTestPmo> {

    public UIYesNoComboBoxIntegrationTest() {
        super(YesNoComboBoxTestModelObject::new, YesNoComboBoxTestPmo::new);
    }

    @Test
    public void testNullSelection() {
        assertThat(getAllowedValues(getStaticComponent()), contains(true, false));

        ComboBox<Boolean> comboBox = getDynamicComponent();
        assertThat(getAllowedValues(comboBox), contains(null, true, false));

        // TODO LIN-2051
        // TestUiUtil.setUserOriginatedValue(comboBox, (Boolean)null);
        // assertThat(getDefaultModelObject().value, is(nullValue()));

        // TestUiUtil.setUserOriginatedValue(comboBox, false);

        getDefaultModelObject().value = true;
        modelChanged();
        assertThat(comboBox.getValue(), is(true));
    }

    @Test
    public void testStaticAvailableValues() {
        assertThat(getAllowedValues(getDynamicComponent()), contains(null, true, false));
        assertThat(getAllowedValues(getStaticComponent()), contains(true, false));
    }

    @Test
    public void testCaptionProvider() {
        assertThat(getDynamicComponent().getItemLabelGenerator().apply(true), is("true"));
        assertThat(getDynamicComponent().getItemLabelGenerator().apply(null),
                   is(NullCaptionTestCaptionProvider.NULL_CAPTION));
        assertThat(getStaticComponent().getItemLabelGenerator().apply(null), is(""));
    }

    @Test
    public void testCaptionProvider_NoDefaultConstructor() {
        NoDefaultConstructorCaptionProviderPmo pmo = new NoDefaultConstructorCaptionProviderPmo();

        BindingContext bindingContext = new BindingContext();
        Function<Object, NoLabelComponentWrapper> wrapperCreator = c -> new NoLabelComponentWrapper((Component)c);

        assertThrows(LinkkiBindingException.class, () -> UiCreator.createUiElements(pmo, bindingContext,
                                                                                    wrapperCreator)
                .count());
    }

    @Test
    public void testValue() {
        ComboBox<Boolean> comboBox = getDynamicComponent();

        assertThat(comboBox.getValue(), is(nullValue()));

        getDefaultModelObject().setValue(true);
        modelChanged();
        assertThat(comboBox.getValue(), is(true));

        // TODO LIN-2051
        // TestUiUtil.setUserOriginatedValue(comboBox, false);
        // assertThat(getDefaultModelObject().getValue(), is(false));
    }


    @Test
    public void testNullInputIfRequired() {
        ComboBox<Boolean> comboBox = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(comboBox.isRequiredIndicatorVisible(), is(true));

        // TODO LIN-2051
        // TestUiUtil.setUserOriginatedValue(comboBox, true);
        // assertThat(getDefaultModelObject().getValue(), is(true));
        //
        // TestUiUtil.setUserOriginatedValue(comboBox, (Boolean)null);
        // assertThat(getDefaultModelObject().getValue(), is(nullValue()));
    }

    @Test
    public void testDerivedLabel() {
        // TODO LIN-2051
        // assertThat(TestUiUtil.getLabelOfComponentAt(getDefaultSection(), 2), is("Foo"));
    }

    @Override
    protected YesNoComboBoxTestModelObject getDefaultModelObject() {
        return (YesNoComboBoxTestModelObject)super.getDefaultModelObject();
    }

    private static List<Boolean> getAllowedValues(ComboBox<Boolean> comboBox) {
        return comboBox.getDataProvider().fetch(new Query<>())
                .collect(Collectors.toList());
    }

    @UISection
    protected static class YesNoComboBoxTestPmo extends AnnotationTestPmo {

        public YesNoComboBoxTestPmo(Object modelObject) {
            super(modelObject);
        }

        @Override
        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        @UIYesNoComboBox(position = 1, label = "", enabled = EnabledType.DYNAMIC, required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, itemCaptionProvider = NullCaptionTestCaptionProvider.class)
        public void value() {
            // model binding
        }

        @Override
        @BindTooltip(TEST_TOOLTIP)
        @UIYesNoComboBox(position = 2, label = TEST_LABEL, enabled = EnabledType.DISABLED, required = RequiredType.REQUIRED, visible = VisibleType.INVISIBLE)
        public void staticValue() {
            // model binding
        }

        @UIYesNoComboBox(position = 3)
        public boolean getFoo() {
            return true;
        }
    }


    protected static class YesNoComboBoxTestModelObject {
        @CheckForNull
        private Boolean value;

        @CheckForNull
        public Boolean getValue() {
            return value;
        }

        public void setValue(Boolean value) {
            this.value = value;

        }

        public boolean getStaticValue() {
            Boolean b = getValue();
            return b == null ? false : b;
        }
    }

    protected static class NullCaptionTestCaptionProvider implements ItemCaptionProvider<Boolean> {

        static final String NULL_CAPTION = "<no selection>";

        public NullCaptionTestCaptionProvider() {
            super();
        }

        @Override
        public String getCaption(Boolean value) {
            return value != null ? value.toString() : getNullCaption();
        }

        @Override
        public String getNullCaption() {
            return NULL_CAPTION;
        }

    }

    protected static class NoDefaultConstructorCaptionProviderPmo {

        @UIYesNoComboBox(position = 3, itemCaptionProvider = PrivateItemCaptionProvider.class)
        public boolean getFoo() {
            return false;
        }

        private static class PrivateItemCaptionProvider implements ItemCaptionProvider<Boolean> {

            private PrivateItemCaptionProvider() {
                // hide default constructor
            }

            @Override
            public String getCaption(Boolean value) {
                return String.format("Value: %s", value);
            }

        }
    }
}
