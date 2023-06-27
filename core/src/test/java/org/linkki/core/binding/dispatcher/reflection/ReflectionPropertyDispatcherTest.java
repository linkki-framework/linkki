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
package org.linkki.core.binding.dispatcher.reflection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.linkki.core.matcher.MessageMatchers.emptyMessageList;
import static org.linkki.core.matcher.MessageMatchers.hasSize;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.fallback.ExceptionPropertyDispatcher;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.pmo.ModelObject;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class ReflectionPropertyDispatcherTest {
    private static final String PRIMITIVE_BOOLEAN = "primitiveBoolean";
    private static final String OBJECT_BOOLEAN = "objectBoolean";
    private static final String XYZ = "xyz";
    private static final String ABC = "abc";
    private TestModelObject testModelObject;
    private TestPMO testPmo;

    @BeforeEach
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

    @Test
    public void testConstructor_NoSupplier() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            @SuppressWarnings("unused")
            ReflectionPropertyDispatcher reflectionPropertyDispatcher = new ReflectionPropertyDispatcher(
                    null, TestPMO.class,
                    "null", new ExceptionPropertyDispatcher("null", testModelObject, testPmo));
        });
    }

    @Test
    public void testConstructor_NoFallback() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            @SuppressWarnings("unused")
            ReflectionPropertyDispatcher reflectionPropertyDispatcher = new ReflectionPropertyDispatcher(
                    this::getTestPmo, TestPMO.class,
                    "foo", null);
        });
    }

    @Test
    public void testConstructor_NoProperty() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            @SuppressWarnings("unused")
            ReflectionPropertyDispatcher reflectionPropertyDispatcher = new ReflectionPropertyDispatcher(
                    this::getTestPmo, TestPMO.class,
                    null, new ExceptionPropertyDispatcher("null", testModelObject, testPmo));
        });
    }

    @Test
    public void testGetValueClass() {
        assertEquals(String.class, setupPmoDispatcher(ABC).getValueClass());
        assertEquals(String.class, setupPmoDispatcher(XYZ).getValueClass());
        assertEquals(Boolean.class, setupPmoDispatcher(OBJECT_BOOLEAN).getValueClass());
        assertEquals(Boolean.TYPE, setupPmoDispatcher(PRIMITIVE_BOOLEAN).getValueClass());
    }

    @Test
    public void testGetValue_FromPmo() {
        assertEquals("890", setupPmoDispatcher(XYZ).pull(Aspect.of("")));

        testPmo.setPmoProp("abc123");
        assertEquals("abc123", setupPmoDispatcher(TestPMO.PROPERTY_PMO_PROP).pull(Aspect.of("")));
    }

    @Test
    public void testGetValue_FromModelObject() {
        assertEquals("567", setupPmoDispatcher(ABC).pull(Aspect.of("")));
        testModelObject.setAbc("anotherValue");
        assertEquals("anotherValue", setupPmoDispatcher(ABC).pull(Aspect.of("")));
    }

    @Test
    public void testGetValue_ChangedModelObject() {
        TestModelObject newTestModelObject = new TestModelObject();
        testPmo.setModelObject(newTestModelObject);
        newTestModelObject.setAbc("newAbcValue");

        assertEquals("newAbcValue", setupPmoDispatcher(ABC).pull(Aspect.of("")));
    }

    @Test
    public void testGetValue_ChangedPmo() {
        TestModelObject newTestModelObject = new TestModelObject();
        testPmo = new TestPMO(newTestModelObject);
        testPmo.setPmoProp("newValue");

        assertEquals("newValue", setupPmoDispatcher(TestPMO.PROPERTY_PMO_PROP).pull(Aspect.of("")));
    }

    @Test
    public void testGetValue_ChangedPmoAndModelObject() {
        TestModelObject newTestModelObject = new TestModelObject();
        testPmo = new TestPMO(newTestModelObject);
        testPmo.setPmoProp("newValue");
        newTestModelObject.setAbc("newAbcValue");

        assertEquals("newValue", setupPmoDispatcher(TestPMO.PROPERTY_PMO_PROP).pull(Aspect.of("")));
        assertEquals("newAbcValue", setupPmoDispatcher(ABC).pull(Aspect.of("")));
    }

    @Test
    public void testGetValue_IllegalProperty() {
        ExceptionPropertyDispatcher exceptionDispatcher = new ExceptionPropertyDispatcher("doesNotExist",
                testModelObject, testPmo);
        ReflectionPropertyDispatcher modelObjectDispatcher = new ReflectionPropertyDispatcher(this::getTestModelObject,
                TestModelObject.class, "doesNotExist", exceptionDispatcher);
        ReflectionPropertyDispatcher pmoDispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                TestModelObject.class,
                "doesNotExist", modelObjectDispatcher);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            pmoDispatcher.pull(Aspect.of(""));
        });

    }

    @Test
    public void testGetValue_NullProperty() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            setupPmoDispatcher(null).pull(Aspect.of(""));
        });

    }

    @Test
    public void testSetValue_DispatchToModelObject() {
        assertEquals("567", setupPmoDispatcher(ABC).pull(Aspect.of("")));
    }

    @Test
    public void testGetMessages_Empty() {
        MessageList messageList = new MessageList();
        assertThat(setupPmoDispatcher(XYZ).getMessages(messageList), emptyMessageList());
        assertThat(setupPmoDispatcher("invalidProperty").getMessages(messageList), emptyMessageList());
    }

    @Test
    public void testGetMessages_ShouldReturnMessagesFromModelObject() {
        MessageList messageList = new MessageList();
        Message msg1 = Message.builder(ABC, Severity.ERROR).invalidObjectWithProperties(testModelObject, XYZ)
                .create();
        Message msg2 = Message.builder(ABC, Severity.ERROR).invalidObjectWithProperties(testModelObject, ABC)
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
        Message msg1 = Message.builder(ABC, Severity.ERROR).invalidObjectWithProperties(testPmo, XYZ).create();
        Message msg2 = Message.builder(ABC, Severity.ERROR).invalidObjectWithProperties(testPmo, ABC).create();
        Message msg3 = Message.builder(ABC, Severity.ERROR).invalidObjectWithProperties(testModelObject, XYZ)
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
        Message msg1 = Message.builder(ABC, Severity.ERROR).invalidObjectWithProperties(new Object(), XYZ).create();
        Message msg2 = Message.builder(ABC, Severity.ERROR).invalidObjectWithProperties(new Object(), ABC).create();
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

    @Test
    public void testPull_Static() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> testPmo,
                TestPMO.class, TestModelObject.PROPERTY_ABC,
                new ExceptionPropertyDispatcher(TestModelObject.PROPERTY_ABC));

        Assertions.assertThrows(IllegalStateException.class, () -> {
            dispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME, false));
        });
    }

    @Test
    public void testPull_Dynamic() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> testPmo,
                TestPMO.class, TestPMO.PROPERTY_XYZ,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_XYZ));
        testModelObject.setAbc("nicht bla");
        assertThat(dispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME)), is(false));

        testModelObject.setAbc("bla");
        assertThat(dispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME)), is(true));
    }

    @Test
    public void testPush_Static() {
        TestModelObject spyObject = spy(new TestModelObject());
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> spyObject,
                TestModelObject.class, TestModelObject.PROPERTY_ABC,
                new ExceptionPropertyDispatcher(TestModelObject.PROPERTY_ABC));

        dispatcher.push(Aspect.of("", "something"));
        verify(spyObject).setAbc(Mockito.eq("something"));
    }

    @Test
    public void testPush_Static_PmoWithoutReadMethod() {
        PropertyDispatcher mockedDispatcher = noActionInPushMockedDispatcher();

        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                TestPMO.class, TestPMO.INVALID_PROPERTY_MISSING_GETTER_PMO, mockedDispatcher);

        dispatcher.push(Aspect.of("", "something"));

        verify(mockedDispatcher, times(1)).push(ArgumentMatchers.<Aspect<?>> any());
    }

    @Test
    public void testPush_Static_PmoWithoutWriteMethod() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                TestPMO.class, TestPMO.PROPERTY_PMO_GETTER_ONLY, noActionInPushMockedDispatcher());


        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            dispatcher.push(Aspect.of("", "something"));
        });
    }


    @Test
    public void testPush_Dynamic() {
        TestPMO spyPmo = spy(testPmo);
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> spyPmo,
                TestPMO.class, TestPMO.PROPERTY_BUTTON_CLICK,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_BUTTON_CLICK));

        dispatcher.push(Aspect.of(""));
        verify(spyPmo).buttonClick();
    }

    @Test
    public void testPush_Dynamic_NoBoundObject() {
        TestPMO spyPmo = spy(testPmo);
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> null,
                TestPMO.class, TestPMO.PROPERTY_BUTTON_CLICK,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_BUTTON_CLICK));

        dispatcher.push(Aspect.of(""));
        verify(spyPmo, never()).buttonClick();
    }

    @Test
    public void testPush_Dynamic_NoMethod() {
        TestPMO spyPmo = spy(testPmo);
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> spyPmo,
                TestPMO.class, TestPMO.PROPERTY_BUTTON_CLICK,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_BUTTON_CLICK));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            dispatcher.push(Aspect.of("NoMethod"));
        });
    }

    @Test
    public void testIsPushable_WithReadWithoutWrite() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                TestPMO.class, TestPMO.PROPERTY_PMO_GETTER_ONLY,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_PMO_GETTER_ONLY));

        assertThat(dispatcher.isPushable(Aspect.of("", "something")), is(false));
    }

    @Test
    public void testIsPushable_WithReadAndWrite() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                TestPMO.class, TestPMO.PROPERTY_PMO_PROP,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_PMO_PROP));

        assertThat(dispatcher.isPushable(Aspect.of("", "something")), is(true));
    }

    @Test
    public void testIsPushable_WithReadAndWriteNoBoundObject() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> null,
                TestPMO.class, TestPMO.PROPERTY_PMO_PROP,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_PMO_PROP));

        assertThat(dispatcher.isPushable(Aspect.of("", "something")), is(false));
    }

    @Test
    public void testIsPushable_NoInvokeMethod() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                TestPMO.class, TestPMO.PROPERTY_PMO_GETTER_ONLY,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_PMO_GETTER_ONLY));

        assertThat(dispatcher.isPushable(Aspect.of("")), is(false));
    }

    @Test
    public void testIsPushable_InvokeMethod() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                TestPMO.class, TestPMO.PROPERTY_BUTTON_CLICK,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_BUTTON_CLICK));

        assertThat(dispatcher.isPushable(Aspect.of("")), is(true));
    }

    @Test
    public void testIsPushable_InvokeMethodNoBoundObject() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> null,
                TestPMO.class, TestPMO.PROPERTY_BUTTON_CLICK,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_BUTTON_CLICK));

        assertThat(dispatcher.isPushable(Aspect.of("")), is(false));
    }
    @Test
    public void testToString() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                TestPMO.class, TestPMO.PROPERTY_BUTTON_CLICK,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_BUTTON_CLICK));
        assertThat(dispatcher.toString(), is("ReflectionPropertyDispatcher[TestPMO#buttonClick]\n\t-> ExceptionPropertyDispatcher[#buttonClick]"));
    }

    private static PropertyDispatcher noActionInPushMockedDispatcher() {
        PropertyDispatcher mockedDispatcher = mock(PropertyDispatcher.class);
        doNothing().when(mockedDispatcher).push(ArgumentMatchers.<Aspect<?>> any());
        return mockedDispatcher;
    }

    private ReflectionPropertyDispatcher setupPmoDispatcher(String property) {
        ExceptionPropertyDispatcher exceptionDispatcher = new ExceptionPropertyDispatcher(property, testModelObject,
                testPmo);
        ReflectionPropertyDispatcher modelObjectDispatcher = new ReflectionPropertyDispatcher(this::getTestModelObject,
                TestModelObject.class, property, exceptionDispatcher);
        return new ReflectionPropertyDispatcher(this::getTestPmo, TestPMO.class, property, modelObjectDispatcher);
    }

    private ReflectionPropertyDispatcher setupModelObjectDispatcher(String property) {
        ExceptionPropertyDispatcher exceptionDispatcher = new ExceptionPropertyDispatcher(property,
                testModelObject,
                testPmo);
        ReflectionPropertyDispatcher modelObjectDispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                TestPMO.class, property, exceptionDispatcher);
        return new ReflectionPropertyDispatcher(this::getTestModelObject, TestModelObject.class, property,
                modelObjectDispatcher);
    }

    public static class TestPMO {

        public static final String PROPERTY_PMO_PROP = "pmoProp";
        public static final String PROPERTY_PMO_GETTER_ONLY = "pmoGetterOnly";
        public static final String INVALID_PROPERTY_MISSING_GETTER_PMO = "missingGetterProp";
        public static final String PROPERTY_XYZ = "xyz";
        public static final String PROPERTY_BUTTON_CLICK = "buttonClick";

        private TestModelObject modelObject;

        @CheckForNull
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

        @CheckForNull
        public String getPmoProp() {
            return pmoProp;
        }

        public void setPmoProp(String pmoProp) {
            this.pmoProp = pmoProp;
        }

        public String getPmoGetterOnly() {
            return "";
        }

        public void setMissingGetterProp() {
            // Nothing to do
        }

        public void buttonClick() {
            // Nothing to do
        }
    }

    static class TestModelObject {

        public static final String PROPERTY_ABC = "abc";

        private String xyz = "123";
        private String abc = "567";

        private boolean primitiveBoolean = false;
        @CheckForNull
        private Boolean objectBoolean = null;

        public boolean isPrimitiveBoolean() {
            return primitiveBoolean;
        }

        public void setPrimitiveBoolean(boolean b) {
            this.primitiveBoolean = b;
        }

        @CheckForNull
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

        public void setPmoSet(@SuppressWarnings("unused") String pmoSet) {
            // do nothing
        }

        public String getPmoSet() {
            return "";
        }

    }
}
