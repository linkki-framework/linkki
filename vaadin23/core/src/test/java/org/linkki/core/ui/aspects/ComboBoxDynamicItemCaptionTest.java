package org.linkki.core.ui.aspects;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.security.SecureRandom;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.ui.aspects.annotation.BindComboBoxDynamicItemCaption;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.layout.annotation.UICssLayout;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;

class ComboBoxDynamicItemCaptionTest {

    /**
     * As {@link DataProvider#refreshAll()} only fires a change event, the caption provider is not
     * really triggered. Thus, this test only verifies the method call.
     */
    @SuppressWarnings({ "deprecation", "unchecked" })
    @Test
    void testCaptionUpdate() {
        var bindingContext = new BindingContext();
        var comboBox = (ComboBox<Object>)VaadinUiCreator.createComponent(new TestPmo(), bindingContext)
                .getChildren().findFirst().get();
        var dataProvider = (ListDataProvider<Object>)spy(comboBox.getDataProvider());
        comboBox.setDataProvider(dataProvider);

        bindingContext.modelChanged();

        verify(dataProvider).refreshAll();
    }

    @UICssLayout
    private static class TestPmo {

        private final TestObject selected;
        private final List<TestObject> availableValues;

        public TestPmo() {
            this.availableValues = List.of(new TestObject(), new TestObject());
            this.selected = availableValues.get(0);
        }

        @BindComboBoxDynamicItemCaption
        @UIComboBox(position = 0, content = AvailableValuesType.DYNAMIC)
        public TestObject getComboBox() {
            return selected;
        }

        @SuppressWarnings("unused")
        public List<TestObject> getComboBoxAvailableValues() {
            return availableValues;
        }
    }

    private static class TestObject {

        private final double value = new SecureRandom().nextDouble();

        @SuppressWarnings("unused")
        public double getValue() {
            return value;
        }
    }
}
