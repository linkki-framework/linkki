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
package org.linkki.core.binding.dispatcher.reflection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.linkki.core.matcher.MessageMatchers.emptyMessageList;
import static org.linkki.core.matcher.MessageMatchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

class ReflectionPropertyDispatcherTest {
    private static final String PRIMITIVE_BOOLEAN = "primitiveBoolean";
    private static final String OBJECT_BOOLEAN = "objectBoolean";
    private static final String DUMMY = "dummy";
    private TestModelObject testModelObject;
    private TestPMO testPmo;

    @BeforeEach
    void setUp() {
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
    void testConstructor_NoSupplier() {
        assertThrows(NullPointerException.class, () -> {
            @SuppressWarnings("unused")
            ReflectionPropertyDispatcher reflectionPropertyDispatcher = new ReflectionPropertyDispatcher(
                    null, TestPMO.class,
                    "null", new ExceptionPropertyDispatcher("null", testModelObject, testPmo));
        });
    }

    @Test
    void testConstructor_NoFallback() {
        assertThrows(NullPointerException.class, () -> {
            @SuppressWarnings("unused")
            ReflectionPropertyDispatcher reflectionPropertyDispatcher = new ReflectionPropertyDispatcher(
                    this::getTestPmo, TestPMO.class,
                    "foo", null);
        });
    }

    @Test
    void testConstructor_NoProperty() {
        assertThrows(NullPointerException.class, () -> {
            @SuppressWarnings("unused")
            ReflectionPropertyDispatcher reflectionPropertyDispatcher = new ReflectionPropertyDispatcher(
                    this::getTestPmo, TestPMO.class,
                    null, new ExceptionPropertyDispatcher("null", testModelObject, testPmo));
        });
    }

    @Test
    void testGetValueClass() {
        assertEquals(String.class, setupPmoDispatcher(TestModelObject.MODEL_PROPERTY).getValueClass());
        assertEquals(String.class, setupPmoDispatcher(TestPMO.PROPERTY_XYZ).getValueClass());
        assertEquals(Boolean.class, setupPmoDispatcher(OBJECT_BOOLEAN).getValueClass());
        assertEquals(Boolean.TYPE, setupPmoDispatcher(PRIMITIVE_BOOLEAN).getValueClass());
    }

    @Test
    void testGetValue_FromPmo() {
        assertEquals("890", setupPmoDispatcher(TestPMO.PROPERTY_XYZ).pull(Aspect.of("")));

        testPmo.setPmoProp("abc123");
        assertEquals("abc123", setupPmoDispatcher(TestPMO.PROPERTY_PMO_PROP).pull(Aspect.of("")));
    }

    @Test
    void testGetValue_FromModelObject() {
        assertEquals("567", setupPmoDispatcher(TestModelObject.MODEL_PROPERTY).pull(Aspect.of("")));
        testModelObject.setModelProperty("anotherValue");
        assertEquals("anotherValue", setupPmoDispatcher(TestModelObject.MODEL_PROPERTY).pull(Aspect.of("")));
    }

    @Test
    void testGetValue_ChangedModelObject() {
        TestModelObject newTestModelObject = new TestModelObject();
        testPmo.setModelObject(newTestModelObject);
        newTestModelObject.setModelProperty("newModelObjectPropertyValue");

        assertEquals("newModelObjectPropertyValue",
                     setupPmoDispatcher(TestModelObject.MODEL_PROPERTY).pull(Aspect.of("")));
    }

    @Test
    void testGetValue_ChangedPmo() {
        TestModelObject newTestModelObject = new TestModelObject();
        testPmo = new TestPMO(newTestModelObject);
        testPmo.setPmoProp("newValue");

        assertEquals("newValue", setupPmoDispatcher(TestPMO.PROPERTY_PMO_PROP).pull(Aspect.of("")));
    }

    @Test
    void testGetValue_ChangedPmoAndModelObject() {
        TestModelObject newTestModelObject = new TestModelObject();
        testPmo = new TestPMO(newTestModelObject);
        testPmo.setPmoProp("newValue");
        newTestModelObject.setModelProperty("newModelPropertyValue");

        assertEquals("newValue", setupPmoDispatcher(TestPMO.PROPERTY_PMO_PROP).pull(Aspect.of("")));
        assertEquals("newModelPropertyValue", setupPmoDispatcher(TestModelObject.MODEL_PROPERTY).pull(Aspect.of("")));
    }

    @Test
    void testGetValue_IllegalProperty() {
        ExceptionPropertyDispatcher exceptionDispatcher = new ExceptionPropertyDispatcher("doesNotExist",
                testModelObject, testPmo);
        ReflectionPropertyDispatcher modelObjectDispatcher = new ReflectionPropertyDispatcher(this::getTestModelObject,
                TestModelObject.class, "doesNotExist", exceptionDispatcher);
        ReflectionPropertyDispatcher pmoDispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                TestModelObject.class,
                "doesNotExist", modelObjectDispatcher);

        assertThrows(IllegalStateException.class, () -> {
            pmoDispatcher.pull(Aspect.of(""));
        });

    }

    @Test
    void testGetValue_NullProperty() {
        assertThrows(NullPointerException.class, () -> {
            setupPmoDispatcher(null).pull(Aspect.of(""));
        });

    }

    @Test
    void testSetValue_DispatchToModelObject() {
        assertEquals("567",
                     setupPmoDispatcher(TestModelObject.MODEL_PROPERTY).pull(Aspect.of("")));
    }

    @Test
    void testGetMessages_Empty() {
        MessageList messageList = new MessageList();
        assertThat(setupPmoDispatcher(TestPMO.PROPERTY_XYZ).getMessages(messageList), emptyMessageList());
        assertThat(setupPmoDispatcher("invalidProperty").getMessages(messageList), emptyMessageList());
    }

    @Test
    void testGetMessages_ShouldReturnMessagesFromModelObject() {
        MessageList messageList = new MessageList();
        Message msg1 = Message.builder(DUMMY, Severity.ERROR)
                .invalidObjectWithProperties(testModelObject, TestModelObject.PROPERTY_XYZ)
                .create();
        Message msg2 = Message.builder(DUMMY, Severity.ERROR)
                .invalidObjectWithProperties(testModelObject, DUMMY)
                .create();
        messageList.add(msg1);
        messageList.add(msg2);

        assertThat(setupPmoDispatcher(TestModelObject.PROPERTY_XYZ).getMessages(messageList), hasSize(1));
        assertThat(setupPmoDispatcher(DUMMY).getMessages(messageList), hasSize(1));
        assertThat(setupPmoDispatcher("invalidProperty").getMessages(messageList), emptyMessageList());
    }

    @Test
    void testGetMessages_ShouldReturnMessagesFromPmo() {
        MessageList messageList = new MessageList();
        Message msg1 = Message.builder(DUMMY, Severity.ERROR).invalidObjectWithProperties(testPmo, TestPMO.PROPERTY_XYZ)
                .create();
        Message msg2 = Message.builder(DUMMY, Severity.ERROR).invalidObjectWithProperties(testPmo, DUMMY).create();
        Message msg3 = Message.builder(DUMMY, Severity.ERROR)
                .invalidObjectWithProperties(testModelObject, TestModelObject.PROPERTY_XYZ)
                .create();
        messageList.add(msg1);
        messageList.add(msg2);
        messageList.add(msg3);

        assertThat(setupPmoDispatcher(TestPMO.PROPERTY_XYZ).getMessages(messageList), hasSize(2));
        assertThat(setupPmoDispatcher(DUMMY).getMessages(messageList), hasSize(1));
        assertThat(setupPmoDispatcher("invalidProperty").getMessages(messageList), emptyMessageList());
    }

    @Test
    void testGetMessages_IgnoreIrrelevantMessages() {
        MessageList messageList = new MessageList();
        Message msg1 = Message.builder(DUMMY, Severity.ERROR)
                .invalidObjectWithProperties(new Object(), TestPMO.PROPERTY_XYZ).create();
        Message msg2 = Message.builder(DUMMY, Severity.ERROR).invalidObjectWithProperties(new Object(), DUMMY).create();
        messageList.add(msg1);
        messageList.add(msg2);

        assertThat(setupPmoDispatcher(TestPMO.PROPERTY_XYZ).getMessages(messageList), emptyMessageList());
        assertThat(setupPmoDispatcher(DUMMY).getMessages(messageList), emptyMessageList());
        assertThat(setupPmoDispatcher("invalidProperty").getMessages(messageList), emptyMessageList());
    }

    @Test
    void testPull_ModelObjectNull() {
        testModelObject = null;
        testPmo = new TestPMO(null);

        assertNull(setupModelObjectDispatcher("xyz").pull(Aspect.of("")));
    }

    @Test
    void testPull_Static_IllegalStateException() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> testPmo,
                TestPMO.class, TestModelObject.MODEL_PROPERTY,
                new ExceptionPropertyDispatcher(TestModelObject.MODEL_PROPERTY));

        var exception = assertThrows(IllegalStateException.class,
                                     () -> dispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME, false)));
        assertThat(exception.getMessage(),
                   containsString("Static aspect Aspect: 'visible', Value: 'false' should not be handled by ReflectionPropertyDispatcher."));
    }

    @Test
    void testPull_Dynamic() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> testPmo,
                TestPMO.class, TestPMO.PROPERTY_XYZ,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_XYZ));

        testModelObject.setModelProperty("not " + TestModelObject.XYZ_VISIBLE);
        assertThat(dispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME)), is(false));

        testModelObject.setModelProperty(TestModelObject.XYZ_VISIBLE);
        assertThat(dispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME)), is(true));
    }

    @Test
    void testPull_Dynamic_NoMatchingMethod() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> testPmo,
                TestPMO.class, TestPMO.PROPERTY_XYZ,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_XYZ, testPmo));

        var exception = assertThrows(IllegalStateException.class,
                                     () -> dispatcher.pull(Aspect.of("notExistingAspect")));
        assertThat(exception.getMessage(), containsString("Cannot find method \"is/getXyzNotExistingAspect\""));
        assertThat(exception.getMessage(), containsString(TestPMO.class.getName()));
    }

    @Test
    void testPush_Static() {
        TestModelObject spyObject = spy(new TestModelObject());
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> spyObject,
                TestModelObject.class, TestModelObject.MODEL_PROPERTY,
                new ExceptionPropertyDispatcher(TestModelObject.MODEL_PROPERTY));

        dispatcher.push(Aspect.of("", "something"));

        verify(spyObject).setModelProperty(Mockito.eq("something"));
    }

    @Test
    void testPush_Static_PmoWithoutReadMethod() {
        PropertyDispatcher mockedDispatcher = noActionInPushMockedDispatcher();

        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                TestPMO.class, TestPMO.INVALID_PROPERTY_MISSING_GETTER_PMO, mockedDispatcher);

        dispatcher.push(Aspect.of("", "something"));

        verify(mockedDispatcher, times(1)).push(ArgumentMatchers.<Aspect<?>> any());
    }

    @Test
    void testPush_Static_PmoWithoutWriteMethod() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                TestPMO.class, TestPMO.PROPERTY_PMO_GETTER_ONLY, noActionInPushMockedDispatcher());

        var exception = assertThrows(IllegalArgumentException.class,
                                     () -> dispatcher.push(Aspect.of("", "something")));
        assertThat(exception.getMessage(), containsString("Cannot find method \"set\" in any of the classes"));
        assertThat(exception.getMessage(), containsString(TestPMO.class.getName()));
    }

    @Test
    void testPush_Dynamic() {
        TestPMO spyPmo = spy(testPmo);
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> spyPmo,
                TestPMO.class, TestPMO.PROPERTY_BUTTON_CLICK,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_BUTTON_CLICK));

        dispatcher.push(Aspect.of(""));

        verify(spyPmo).buttonClick();
    }

    @Test
    void testPush_Dynamic_NoBoundObject() {
        TestPMO spyPmo = spy(testPmo);
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> null,
                TestPMO.class, TestPMO.PROPERTY_BUTTON_CLICK,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_BUTTON_CLICK));

        dispatcher.push(Aspect.of(""));
        verify(spyPmo, never()).buttonClick();
    }

    @Test
    void testPush_Dynamic_NoMethod() {
        TestPMO spyPmo = spy(testPmo);
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> spyPmo,
                TestPMO.class, TestPMO.PROPERTY_BUTTON_CLICK,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_BUTTON_CLICK));

        assertThrows(IllegalArgumentException.class, () -> {
            dispatcher.push(Aspect.of("NoMethod"));
        });
    }

    @Test
    void testIsPushable_WithReadWithoutWrite() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                TestPMO.class, TestPMO.PROPERTY_PMO_GETTER_ONLY,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_PMO_GETTER_ONLY));

        assertThat(dispatcher.isPushable(Aspect.of("", null)), is(false));
    }

    @Test
    void testIsPushable_WithReadAndWrite() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                TestPMO.class, TestPMO.PROPERTY_PMO_PROP,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_PMO_PROP));

        assertThat(dispatcher.isPushable(Aspect.of("", null)), is(true));
    }

    @Test
    void testIsPushable_WithReadAndWriteNoBoundObject() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> null,
                TestPMO.class, TestPMO.PROPERTY_PMO_PROP,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_PMO_PROP));

        assertThat(dispatcher.isPushable(Aspect.of("", null)), is(false));
    }

    @Test
    void testIsPushable_NoInvokeMethod() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                TestPMO.class, TestPMO.PROPERTY_PMO_GETTER_ONLY,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_PMO_GETTER_ONLY));

        assertThat(dispatcher.isPushable(Aspect.of("", null)), is(false));
    }

    @Test
    void testIsPushable_InvokeMethod() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                TestPMO.class, TestPMO.PROPERTY_BUTTON_CLICK,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_BUTTON_CLICK));

        assertThat(dispatcher.isPushable(Aspect.of("")), is(true));
    }

    @Test
    void testIsPushable_InvokeMethodNoBoundObject() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> null,
                TestPMO.class, TestPMO.PROPERTY_BUTTON_CLICK,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_BUTTON_CLICK));

        assertThat(dispatcher.isPushable(Aspect.of("")), is(false));
    }

    @Test
    void testIsPushable_VoidMethodWithModelObject() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                TestPMO.class, TestModelObject.MODEL_PROPERTY,
                new ReflectionPropertyDispatcher(this::getTestModelObject, TestModelObject.class,
                        TestModelObject.MODEL_PROPERTY,
                        new ExceptionPropertyDispatcher(TestPMO.PROPERTY_BUTTON_CLICK)));

        assertThat(dispatcher.isPushable(Aspect.of("", null)), is(true));
    }

    @Test
    void testToString() {
        ReflectionPropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                TestPMO.class, TestPMO.PROPERTY_BUTTON_CLICK,
                new ExceptionPropertyDispatcher(TestPMO.PROPERTY_BUTTON_CLICK));
        assertThat(dispatcher.toString(),
                   is("ReflectionPropertyDispatcher[TestPMO#buttonClick]\n\t-> ExceptionPropertyDispatcher[#buttonClick]"));
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
            return modelObject.getModelProperty().equals(TestModelObject.XYZ_ENABLED);
        }

        public boolean isXyzVisible() {
            return modelObject.getModelProperty().equals(TestModelObject.XYZ_VISIBLE);
        }

        public boolean isXyzRequired() {
            return modelObject.getModelProperty().equals(TestModelObject.XYZ_REQUIRED);
        }

        public List<String> getXyzAvailableValues() {
            if (modelObject.getModelProperty().equals(TestModelObject.XYZ_AVAILABLE_VALUES)) {
                return Arrays.asList("1", "2", "3");
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

        public void setPmoProp(@CheckForNull String pmoProp) {
            this.pmoProp = pmoProp;
        }

        public String getPmoGetterOnly() {
            return "";
        }

        public void setMissingGetterProp() {
            // Nothing to do
        }

        public void modelProperty() {

        }

        public void buttonClick() {
            // Nothing to do
        }
    }

    static class TestModelObject {

        public static final String MODEL_PROPERTY = "modelProperty";
        public static final String XYZ_VISIBLE = "visible";
        public static final String XYZ_ENABLED = "enabled";
        public static final String XYZ_REQUIRED = "required";
        public static final String XYZ_AVAILABLE_VALUES = "availableValues";
        public static final String PROPERTY_XYZ = "xyz";

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

        public void setObjectBoolean(@CheckForNull Boolean objectBoolean) {
            this.objectBoolean = objectBoolean;
        }

        public String getXyz() {
            return xyz;
        }

        public void setXyz(String xyz) {
            this.xyz = xyz;
        }

        public String getModelProperty() {
            return abc;
        }

        public void setModelProperty(String abc) {
            this.abc = abc;
        }

        public boolean isModelPropertyEnabled() {
            return true;
        }

        public boolean isModelPropertyVisible() {
            return true;
        }

        public String getModelPropertyCaption() {
            return "HodorHodor";
        }

        public boolean isModelPropertyRequired() {
            return true;
        }

        public List<?> getModelPropertyAvailableValues() {
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
