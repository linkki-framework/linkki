/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.section.annotations;

import java.util.Collections;

import org.junit.Test;

import com.vaadin.data.Buffered;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ConnectorTracker;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.binding.BindingContext;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyBehaviorProvider;
import de.faktorzehn.ipm.web.ui.section.BaseSection;
import de.faktorzehn.ipm.web.ui.section.PmoBasedSectionFactory;
import elemental.json.JsonObject;
import elemental.json.impl.JreJsonFactory;
import elemental.json.impl.JreJsonObject;

public class UiCheckBoxTest {

    public static class TestModelObjectWithPrimitiveBoolean {

        private boolean value = false;

        public boolean getBooleanValue() {
            return value;
        }

        public void setBooleanValue(boolean b) {
            this.value = b;
        }
    }

    @UISection
    public static class TestPmo implements PresentationModelObject {
        @UICheckBox(position = 1, modelAttribute = "booleanValue")
        public void primitiveBoolean() {
            // data binding
        }

        @Override
        public Object getModelObject() {
            return new TestModelObjectWithPrimitiveBoolean();
        }
    }

    public static class SectionFactory extends PmoBasedSectionFactory {

        @Override
        public PropertyBehaviorProvider getPropertyBehaviorProvider() {
            return () -> Collections.emptyList();
        }
    }

    public static class TestConnectorTracker extends ConnectorTracker {

        private static final long serialVersionUID = 1L;

        public TestConnectorTracker(UI uI) {
            super(uI);
        }

        @Override
        public JsonObject getDiffState(ClientConnector connector) {
            return new JreJsonObject(new JreJsonFactory());
        }
    }

    public static class TestUi extends UI {

        private static final long serialVersionUID = 1L;

        @Override
        protected void init(VaadinRequest request) {
            // Nothing to do
        }

        @Override
        public ConnectorTracker getConnectorTracker() {
            return new TestConnectorTracker(this);
        }
    }

    @Test(expected = Buffered.SourceException.class)
    public void testSetValueWithPrimitiveBooleanInPmo() {

        TestUi testUi = new TestUi();
        TestPmo pmo = new TestPmo();
        SectionFactory sectionFactory = new SectionFactory();
        BindingContext bindingContext = new BindingContext();
        BaseSection section = sectionFactory.createSection(pmo, bindingContext);

        testUi.setContent(section);

        bindingContext.updateUI();

        Panel panel = (Panel)section.getComponent(1);
        GridLayout gridLayout = (GridLayout)panel.getContent();
        CheckBox checkBox = (CheckBox)gridLayout.getComponent(1, 0);
        checkBox.setValue(true);
    }
}
