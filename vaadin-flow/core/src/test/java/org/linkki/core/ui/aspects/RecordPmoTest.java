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

package org.linkki.core.ui.aspects;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.modelobject.ModelObjects.ModelObjectAnnotationException;
import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.test.KaribuUtils.Grids;
import org.linkki.core.vaadin.component.base.LinkkiText;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;

@ExtendWith(KaribuUIExtension.class)
class RecordPmoTest {

    @Test
    void testPmoWithoutModelObject() {
        var pmo = new PmoWithoutModelObject("This is a text field");

        var component = VaadinUiCreator.createComponent(pmo, new BindingContext());
        assertThat(_get(component, TextField.class, ss -> ss.withId("textField")).getValue())
                .isEqualTo("This is a text field");
    }

    @Test
    void testPmoWithModelObject() {
        var pmo = new PmoWithModelObject(new TestModelObject("This is a text from model object"));

        var bindingContext = new BindingContext();
        assertThatExceptionOfType(ModelObjectAnnotationException.class)
                .isThrownBy(() -> VaadinUiCreator.createComponent(pmo, bindingContext))
                .withMessage("Presentation model object " + pmo.getClass().getName()
                        + " has multiple members (modelObject, modelObject) that are annotated with @ModelObject");
    }

    @Test
    void testPmoWithModelObjectOnMethod() {
        var pmo = new PmoWithModelObjectOnMethod(new TestModelObject("123"), "This is a text field");

        var component = VaadinUiCreator.createComponent(pmo, new BindingContext());
        assertThat(_get(component, TextField.class, ss -> ss.withId("text")).getValue())
                .isEqualTo("123");
        assertThat(_get(component, TextField.class, ss -> ss.withId("text")).isReadOnly())
                .as("UI component must be read-only because records are immutable").isTrue();
        assertThat(_get(component, TextField.class, ss -> ss.withId("textField")).getValue())
                .isEqualTo("This is a text field");
        assertThat(_get(component, LinkkiText.class, ss -> ss.withId("label")).getText())
                .isEqualTo("label");
    }

    @Test
    void testTablePmoWithRecordRowPmo() {
        var tablePmo = new TablePmo(List.of(new TestModelObject("text 1"), new TestModelObject("text 2")));

        var component = VaadinUiCreator.createComponent(tablePmo, new BindingContext());
        Grid<Object> grid = Grids.get(component);
        assertThat(Grids.getTextContentsInColumn(grid, "col1"))
                .isEqualTo(List.of("Constant Value", "Constant Value"));

        assertThat(Grids.getTextContentsInColumn(grid, "col2"))
                .isEqualTo(List.of("text 1", "text 2"));
    }

    @UISection
    record PmoWithoutModelObject(@UITextField(position = 0) String textField) {

    }

    @UISection
    record PmoWithModelObjectOnMethod(TestModelObject modelObject, @UITextField(position = 0) String textField) {

        @Override
        @ModelObject
        public TestModelObject modelObject() {
            return modelObject;
        }

        @UITextField(position = 1, modelAttribute = "text")
        public void text() {
            // model binding
        }

        @UILabel(position = 10)
        public String getLabel() {
            return "label";
        }

    }

    @UISection
    record PmoWithModelObject(@ModelObject TestModelObject modelObject) {
        @UITextField(position = 0, modelAttribute = "text")
        public void text() {
            // model binding
        }
    }

    @UISection
    public static class TablePmo extends SimpleTablePmo<TestModelObject, TablePmo.RecordRowPmo> {

        public TablePmo(List<? extends TestModelObject> modelObjects) {
            super(modelObjects);
        }

        @Override
        protected RecordRowPmo createRow(TestModelObject modelObject) {
            return new RecordRowPmo("Constant Value", modelObject.text());
        }

        record RecordRowPmo(
                @UILabel(position = 10, label = "Col 1") String col1,
                @UILabel(position = 30, label = "Col 2") String col2) {
        }
    }

    record TestModelObject(String text) {

    }

}