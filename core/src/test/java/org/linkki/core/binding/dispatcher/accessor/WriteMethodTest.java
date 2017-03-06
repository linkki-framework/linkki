package org.linkki.core.binding.dispatcher.accessor;

import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WriteMethodTest {
    @Mock
    @SuppressWarnings("null")
    PropertyAccessDescriptor descriptor;

    @Test
    @SuppressWarnings("unused")
    // warning suppressed as object is created to test the constructor, not to use it
    public void testConstructor() {
        when(descriptor.getReflectionWriteMethod()).thenReturn(null);
        new WriteMethod(descriptor);
    }

    @Test
    public void testWriteValue() {
        TestObject testObject = new TestObject();
        PropertyAccessDescriptor propertyAccessDescriptor = new PropertyAccessDescriptor(testObject.getClass(),
                TestObject.BOOLEAN_PROPERTY);

        WriteMethod writeMethod = propertyAccessDescriptor.createWriteMethod();
        writeMethod.writeValue(testObject, true);
    }

}
