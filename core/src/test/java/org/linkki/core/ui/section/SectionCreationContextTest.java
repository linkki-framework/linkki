/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.ui.section.annotations.SectionID;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;

@SuppressWarnings("null")
public class SectionCreationContextTest {

    private BindingContext bindingContext = new BindingContext("testBindingContext",
            PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER, Handler.NOP_HANDLER);

    @Test
    public void testSetSectionId() {
        BaseSection section = createContext(new SCCPmoWithID()).createSection();
        assertEquals("test-ID", section.getId());
    }

    private SectionCreationContext createContext(Object pmo) {
        return new SectionCreationContext(pmo, bindingContext);
    }

    @Test
    public void testSetSectionDefaultId() {
        BaseSection section = createContext(new SCCPmoWithoutID()).createSection();
        assertEquals("SCCPmoWithoutID", section.getId());
    }

    @Test
    public void testSetComponentId() {
        BaseSection section = createContext(new SCCPmoWithID()).createSection();
        assertEquals(2, section.getComponentCount());
        Component textField = ((GridLayout)((Panel)section.getComponent(1)).getContent()).getComponent(1, 0);
        assertEquals("testProperty", textField.getId());
    }

    @UISection
    public static class SCCPmoWithID {

        @SectionID
        public String getId() {
            return "test-ID";
        }

        @UITextField(position = 0)
        public String getTestProperty() {
            return "some_value";
        }
    }

    @UISection
    private static class SCCPmoWithoutID {
        // no content required
    }
}
