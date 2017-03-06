package org.linkki.core.ui.section.annotations;

import javax.annotation.Nullable;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.TestBindingContext;
import org.linkki.core.ui.section.AbstractSection;
import org.linkki.core.ui.section.DefaultPmoBasedSectionFactory;
import org.linkki.core.ui.section.PmoBasedSectionFactory;

import com.vaadin.server.ClientConnector;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.ConnectorTracker;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

import elemental.json.JsonObject;
import elemental.json.impl.JreJsonFactory;
import elemental.json.impl.JreJsonObject;

/** A Vaadin UI for tests. */
public class TestUi extends UI {

    /** A ConnectorTracker needed for our TestUi. */
    static class TestConnectorTracker extends ConnectorTracker {

        private static final long serialVersionUID = 1L;

        public TestConnectorTracker(UI uI) {
            super(uI);
        }

        @Override
        public JsonObject getDiffState(@Nullable ClientConnector connector) {
            return new JreJsonObject(new JreJsonFactory());
        }
    }

    private static final long serialVersionUID = 1L;

    @Override
    protected void init(@Nullable VaadinRequest request) {
        // Nothing to do
    }

    @Override
    public ConnectorTracker getConnectorTracker() {
        return new TestConnectorTracker(this);
    }

    /**
     * Returns a component that is bound to the given PMO using the IPM data binder. The component
     * is part of a TestUI so that a rudimentary Vaadin environment is in place.
     * 
     * @param pmo the PMO to which the component is bound is bound
     * @return a {@code CheckBox} that is bound to the model object
     */
    public static Component componentBoundTo(Object pmo) {
        BindingContext bindingContext = TestBindingContext.create();
        return componentBoundTo(pmo, bindingContext);
    }

    public static Component componentBoundTo(Object pmo, BindingContext bindingContext) {
        TestUi testUi = new TestUi();
        PmoBasedSectionFactory sectionFactory = new DefaultPmoBasedSectionFactory();
        AbstractSection section = sectionFactory.createSection(pmo, bindingContext);

        testUi.setContent(section);

        bindingContext.updateUI();

        Panel panel = (Panel)section.getComponent(1);
        GridLayout gridLayout = (GridLayout)panel.getContent();
        return gridLayout.getComponent(1, 0);
    }
}