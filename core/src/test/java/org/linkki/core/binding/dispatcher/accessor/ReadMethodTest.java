package org.linkki.core.binding.dispatcher.accessor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReadMethodTest {
    @Mock
    @SuppressWarnings("null")
    PropertyAccessDescriptor descriptor;

    @Test
    @SuppressWarnings("unused")
    // warning suppressed as object is created to test the constructor, not to use it
    public void testConstructor() {
        when(descriptor.getReflectionReadMethod()).thenReturn(null);
        new ReadMethod(descriptor);
    }

    @Test
    public void testReadValue() {
        TestObject testObject = new TestObject();
        PropertyAccessDescriptor propertyAccessDescriptor = new PropertyAccessDescriptor(testObject.getClass(),
                TestObject.READ_ONLY_LONG_PROPERTY);

        ReadMethod readMethod = propertyAccessDescriptor.createReadMethod();
        assertEquals(42, ((Long)readMethod.readValue(testObject)).longValue());
    }

}
