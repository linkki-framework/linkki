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
package org.linkki.core.binding.dispatcher;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.linkki.core.matcher.MessageMatchers.emptyMessageList;
import static org.linkki.core.matcher.MessageMatchers.hasSize;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.message.Message;
import org.linkki.core.message.MessageList;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.aspect.VisibleAspectDefinition;
import org.mockito.Mockito;

import com.vaadin.server.ErrorMessage.ErrorLevel;

@SuppressWarnings("null")
public class ReflectionPropertyDispatcherTest {
    private static final String PRIMITIVE_BOOLEAN = "primitiveBoolean";
    private static final String OBJECT_BOOLEAN = "objectBoolean";
    private static final String XYZ = "xyz";
    private static final String ABC = "abc";
    private TestModelObject testModelObject;
    private TestPMO testPmo;

    @Before
    public void setUp() {
        testModelObject = new TestModelObject();
        testPmo = new TestPMO(testModelObject);
    }

    private TestPMO getTestPmo() {
        return testPmo;
    }

    private Object getTestModelObject() {
        return testPmo.getModelObject();
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_noSupplier() {
        @SuppressWarnings("unused")
        ReflectionPropertyDispatcher reflectionPropertyDispatcher = new ReflectionPropertyDispatcher(null, "null",
                new ExceptionPropertyDispatcher("null", testModelObject, testPmo));
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_noFallback() {
        @SuppressWarnings("unused")
        ReflectionPropertyDispatcher reflectionPropertyDispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                "foo", null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_noProperty() {
        @SuppressWarnings("unused")
        ReflectionPropertyDispatcher reflectionPropertyDispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                null, new ExceptionPropertyDispatcher("null", testModelObject, testPmo));
    }

    @Test
    public void testGetValueClass() {
        assertEquals(String.class, setupPmoDispatcher(ABC).getValueClass());
        assertEquals(String.class, setupPmoDispatcher(XYZ).getValueClass());
        assertEquals(Boolean.class, setupPmoDispatcher(OBJECT_BOOLEAN).getValueClass());
        assertEquals(Boolean.TYPE, setupPmoDispatcher(PRIMITIVE_BOOLEAN).getValueClass());
    }

    @Test
    public void testGetValueFromPmo() {

        assertEquals("890", setupPmoDispatcher(XYZ).pull(Aspect.of("")));
    }

    public void testGetValue_fromPmo() {
        testPmo.setPmoProp("abc123");

        assertEquals("abc123", setupPmoDispatcher(TestPMO.PROPERTY_PMO_PROP).pull(Aspect.of("")));
    }

    @Test
    public void testGetValue_fromModelObject() {
        assertEquals("567", setupPmoDispatcher(ABC).pull(Aspect.of("")));
        testModelObject.setAbc("anotherValue");
        assertEquals("anotherValue", setupPmoDispatcher(ABC).pull(Aspect.of("")));
    }

    @Test
    public void testGetValue_changedModelObject() {
        TestModelObject newTestModelObject = new TestModelObject();
        testPmo.setModelObject(newTestModelObject);
        newTestModelObject.setAbc("newAbcValue");

        assertEquals("newAbcValue", setupPmoDispatcher(ABC).pull(Aspect.of("")));
    }

    @Test
    public void testGetValue_changedPmo() {
        TestModelObject newTestModelObject = new TestModelObject();
        testPmo = new TestPMO(newTestModelObject);
        testPmo.setPmoProp("newValue");

        assertEquals("newValue", setupPmoDispatcher(TestPMO.PROPERTY_PMO_PROP).pull(Aspect.of("")));
    }

    @Test
    public void testGetValue_changedPmoAndModelObject() {
        TestModelObject newTestModelObject = new TestModelObject();
        testPmo = new TestPMO(newTestModelObject);
        testPmo.setPmoProp("newValue");
        newTestModelObject.setAbc("newAbcValue");

        assertEquals("newValue", setupPmoDispatcher(TestPMO.PROPERTY_PMO_PROP).pull(Aspect.of("")));
        assertEquals("newAbcValue", setupPmoDispatcher(ABC).pull(Aspect.of("")));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValue_illegalProperty() {
        ExceptionPropertyDispatcher exceptionDispatcher = new ExceptionPropertyDispatcher("doesNotExist",
                testModelObject, testPmo);
        ReflectionPropertyDispatcher modelObjectDispatcher = new ReflectionPropertyDispatcher(this::getTestModelObject,
                "doesNotExist", exceptionDispatcher);
        ReflectionPropertyDispatcher pmoDispatcher = new ReflectionPropertyDispatcher(this::getTestPmo, "doesNotExist",
                modelObjectDispatcher);
        pmoDispatcher.pull(Aspect.of(""));
    }

    @Test(expected = NullPointerException.class)
    public void testGetValue_nullProperty() {
        setupPmoDispatcher(null).pull(Aspect.of(""));
    }

    @Test
    public void testSetValue_dispatchToModelObject() {
        assertEquals("567", setupPmoDispatcher(ABC).pull(Aspect.of("")));
    }

    @Test
    public void testGetMessages_empty() {
        MessageList messageList = new MessageList();
        assertThat(setupPmoDispatcher(XYZ).getMessages(messageList), emptyMessageList());
        assertThat(setupPmoDispatcher("invalidProperty").getMessages(messageList), emptyMessageList());
    }

    @Test
    public void testGetMessages_ShouldReturnMessagesFromModelObject() {
        MessageList messageList = new MessageList();
        Message msg1 = Message.builder(ABC, ErrorLevel.ERROR).invalidObjectWithProperties(testModelObject, XYZ)
                .create();
        Message msg2 = Message.builder(ABC, ErrorLevel.ERROR).invalidObjectWithProperties(testModelObject, ABC)
                .create();
        messageList.add(msg1);
        messageList.add(msg2);

        assertThat(setupPmoDispatcher(XYZ).getMessages(messageList), hasSize(1));
        assertThat(setupPmoDispatcher(ABC).getMessages(messageList), hasSize(1));
        assertThat(setupPmoDispatcher("invalidProperty").getMessages(messageList), emptyMessageList());
    }

    @Test
    public void testGetMessages_ShouldReturnMessagesFromPmo() {
        MessageList messageList = new MessageList();
        Message msg1 = Message.builder(ABC, ErrorLevel.ERROR).invalidObjectWithProperties(testPmo, XYZ).create();
        Message msg2 = Message.builder(ABC, ErrorLevel.ERROR).invalidObjectWithProperties(testPmo, ABC).create();
        Message msg3 = Message.builder(ABC, ErrorLevel.ERROR).invalidObjectWithProperties(testModelObject, XYZ)
                .create();
        messageList.add(msg1);
        messageList.add(msg2);
        messageList.add(msg3);

        assertThat(setupPmoDispatcher(XYZ).getMessages(messageList), hasSize(2));
        assertThat(setupPmoDispatcher(ABC).getMessages(messageList), hasSize(1));
        assertThat(setupPmoDispatcher("invalidProperty").getMessages(messageList), emptyMessageList());
    }

    @Test
    public void testGetMessages_IgnoreIrrelevantMessages() {
        MessageList messageList = new MessageList();
        Message msg1 = Message.builder(ABC, ErrorLevel.ERROR).invalidObjectWithProperties(new Object(), XYZ).create();
        Message msg2 = Message.builder(ABC, ErrorLevel.ERROR).invalidObjectWithProperties(new Object(), ABC).create();
        messageList.add(msg1);
        messageList.add(msg2);

        assertThat(setupPmoDispatcher(XYZ).getMessages(messageList), emptyMessageList());
        assertThat(setupPmoDispatcher(ABC).getMessages(messageList), emptyMessageList());
        assertThat(setupPmoDispatcher("invalidProperty").getMessages(messageList), emptyMessageList());
    }

    @Test
    public void testModelObjectNullReturn() {
        testModelObject = null;
        testPmo = new TestPMO(null);

        assertEquals("890", setupModelObjectDispatcher("xyz").pull(Aspect.of("")));
    }

    @Test(expected = IllegalStateException.class)
    public void testPull_static() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> testPmo,
                TestModelObject.PROPERTY_ABC,
                new ExceptionPropertyDispatcher(TestModelObject.PROPERTY_ABC));
        dispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME, false));
    }

    @Test
    public void testPull_dynamic() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> testPmo, TestPMO.PROPERTY_XYZ,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_XYZ));
        testModelObject.setAbc("nicht bla");
        assertThat(dispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME)), is(false));

        testModelObject.setAbc("bla");
        assertThat(dispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME)), is(true));
    }

    @Test
    public void testPush_static() {
        TestModelObject spyObject = spy(new TestModelObject());
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> spyObject,
                TestModelObject.PROPERTY_ABC,
                new ExceptionPropertyDispatcher(TestModelObject.PROPERTY_ABC));

        dispatcher.push(Aspect.of("", "something"));
        verify(spyObject).setAbc(Mockito.eq("something"));
    }

    @Test
    public void testPush_dynamic() {
        TestPMO spyPmo = spy(testPmo);
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> spyPmo,
                TestPMO.PROPERTY_BUTTON_CLICK,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_BUTTON_CLICK));

        dispatcher.push(Aspect.of(""));
        verify(spyPmo).buttonClick();
    }

    private ReflectionPropertyDispatcher setupPmoDispatcher(String property) {
        ExceptionPropertyDispatcher exceptionDispatcher = new ExceptionPropertyDispatcher(property, testModelObject,
                testPmo);
        ReflectionPropertyDispatcher modelObjectDispatcher = new ReflectionPropertyDispatcher(this::getTestModelObject,
                property, exceptionDispatcher);
        return new ReflectionPropertyDispatcher(this::getTestPmo, property, modelObjectDispatcher);
    }

    private ReflectionPropertyDispatcher setupModelObjectDispatcher(String property) {
        ExceptionPropertyDispatcher exceptionDispatcher = new ExceptionPropertyDispatcher(property, testModelObject,
                testPmo);
        ReflectionPropertyDispatcher modelObjectDispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                property, exceptionDispatcher);
        return new ReflectionPropertyDispatcher(this::getTestModelObject, property, modelObjectDispatcher);
    }

    public static class TestPMO {

        public static final String PROPERTY_PMO_PROP = "pmoProp";
        public static final String PROPERTY_XYZ = "xyz";
        public static final String PROPERTY_BUTTON_CLICK = "buttonClick";

        private TestModelObject modelObject;

        private String pmoProp;

        public TestPMO(TestModelObject modelObject) {
            this.modelObject = modelObject;
        }

        public String getXyz() {
            return "890";
        }

        public boolean isXyzEnabled() {
            return modelObject.getAbc().equals("123");
        }

        public boolean isXyzVisible() {
            return modelObject.getAbc().equals("bla");
        }

        public boolean isXyzRequired() {
            return modelObject.getAbc().equals("zzz");
        }

        public List<String> getXyzAvailableValues() {
            if (modelObject.getAbc().equals("lov")) {
                return Arrays.asList(new String[] { "1", "2", "3" });
            } else {
                return Collections.emptyList();
            }
        }

        public String getFooBar() {
            return "";
        }

        @ModelObject
        public TestModelObject getModelObject() {
            return modelObject;
        }

        public void setModelObject(TestModelObject modelObject) {
            this.modelObject = modelObject;
        }

        public String getPmoProp() {
            return pmoProp;
        }

        public void setPmoProp(String pmoProp) {
            this.pmoProp = pmoProp;
        }

        public void buttonClick() {
            // Nothing to do
        }
    }

    public static class TestModelObject {

        public static final String PROPERTY_ABC = "abc";

        private String xyz = "123";
        private String abc = "567";

        private boolean primitiveBoolean = false;
        private Boolean objectBoolean = null;

        public boolean isPrimitiveBoolean() {
            return primitiveBoolean;
        }

        public void setPrimitiveBoolean(boolean b) {
            this.primitiveBoolean = b;
        }

        public Boolean getObjectBoolean() {
            return objectBoolean;
        }

        public void setObjectBoolean(Boolean objectBoolean) {
            this.objectBoolean = objectBoolean;
        }

        public String getXyz() {
            return xyz;
        }

        public void setXyz(String xyz) {
            this.xyz = xyz;
        }

        public String getAbc() {
            return abc;
        }

        public void setAbc(String abc) {
            this.abc = abc;
        }

        public boolean isAbcEnabled() {
            return true;
        }

        public boolean isAbcVisible() {
            return true;
        }

        public String getAbcCaption() {
            return "HodorHodor";
        }

        public boolean isAbcRequired() {
            return true;
        }

        public List<?> getAbcAvailableValues() {
            return Collections.emptyList();
        }

    }
}
