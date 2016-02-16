package org.linkki.core.binding;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.linkki.core.binding.dispatcher.ExceptionPropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.ReflectionPropertyDispatcher;
import org.linkki.core.ui.components.LinkkiComboBox;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;

public class FieldBindingIntergartionTest {

    private TestPmo pmo = new TestPmo();

    private Label label = new Label();
    private ComboBox comboField = new LinkkiComboBox();

    private FieldBinding<Object> selectBinding;

    private List<TestEnum> valueList = Arrays.asList(new TestEnum[] { TestEnum.ONE, TestEnum.THREE });

    private PropertyDispatcher propertyDispatcher;

    private BindingContext context;

    @Before
    public void setUp() {
        ExceptionPropertyDispatcher exceptionDispatcher = new ExceptionPropertyDispatcher(pmo);
        propertyDispatcher = new ReflectionPropertyDispatcher(() -> pmo, exceptionDispatcher);

        context = TestBindingContext.create();

        selectBinding = new FieldBinding<Object>(context, pmo, "enumValue", label, comboField, propertyDispatcher);
        context.add(selectBinding);

        context.add(selectBinding);
    }

    /**
     * Tests bug fixed in LIN-76, where combo field would throw a ReadOnlyException when binding was
     * initialized.
     */
    @Test
    public void testFieldBinding_readonlyField() {
        ComboBox comboField2 = new LinkkiComboBox();
        pmo.setEnumValue(TestEnum.THREE);
        @SuppressWarnings("unused")
        FieldBinding<Object> fieldBinding = new FieldBinding<Object>(context, pmo, "readonlyEnumValue", label,
                comboField2, propertyDispatcher);
    }

    @Test
    public void testBindAvailableValues_retainSelectedValue() {
        pmo.setEnumValue(null);
        pmo.setEnumValueAvailableValues(valueList);
        context.updateUI();

        comboField.setValue(TestEnum.THREE);
        assertThat(pmo.getEnumValue(), is(TestEnum.THREE));
        assertThat(comboField.getValue(), is(TestEnum.THREE));

        List<TestEnum> otherValues = Arrays.asList(new TestEnum[] { TestEnum.ONE, TestEnum.THREE });
        pmo.setEnumValueAvailableValues(otherValues);
        context.updateUI();

        assertThat(comboField.getValue(), is(TestEnum.THREE));
        assertThat(pmo.getEnumValue(), is(TestEnum.THREE));
    }

    /**
     * Tests whether the current value is retained, even if it is no longer available.
     */
    @Test
    public void testBindAvailableValues_retainSelectedValue2() {
        pmo.setEnumValue(null);
        pmo.setEnumValueAvailableValues(valueList);
        context.updateUI();

        comboField.setValue(TestEnum.THREE);
        assertThat(comboField.getValue(), is(TestEnum.THREE));
        assertThat(pmo.getEnumValue(), is(TestEnum.THREE));
        Collection<?> itemIds = comboField.getItemIds();
        assertThat(itemIds.size(), is(2));
        Iterator<?> iterator = itemIds.iterator();
        assertThat(iterator.next(), is(TestEnum.ONE));
        assertThat(iterator.next(), is(TestEnum.THREE));

        List<TestEnum> otherValues = Arrays.asList(new TestEnum[] { TestEnum.ONE });
        pmo.setEnumValueAvailableValues(otherValues);
        context.updateUI();

        assertThat(comboField.getValue(), is(TestEnum.THREE));
        assertThat(pmo.getEnumValue(), is(TestEnum.THREE));
        Collection<?> itemIds2 = comboField.getItemIds();
        assertThat(itemIds2.size(), is(1));
        Iterator<?> iterator2 = itemIds2.iterator();
        assertThat(iterator2.next(), is(TestEnum.ONE));
    }

}
