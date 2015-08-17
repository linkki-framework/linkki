package de.faktorzehn.ipm.web.binding.dispatcher.accessor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PropertyAccessDescriptorTest {

    private TestObject testObject;

    @Before
    public void setUp() {
        testObject = new TestObject();
    }

    @Test
    public void testConstructor_nonExistentProperty() {
        PropertyAccessDescriptor accessDescriptor = new PropertyAccessDescriptor(testObject.getClass(), "doesNotExist");
        assertFalse(accessDescriptor.createReadMethod().canRead());
        assertFalse(accessDescriptor.createWriteMethod().canWrite());
    }

    @Test
    public void testCreateWriteMethod_writeOnlyProperty() {
        PropertyAccessDescriptor accessDescriptor = new PropertyAccessDescriptor(testObject.getClass(),
                TestObject.WRITE_ONLY_INT_PROPERTY);

        WriteMethod writeMethod = accessDescriptor.createWriteMethod();
        assertNotNull(writeMethod);
        assertTrue(writeMethod.canWrite());
        ReadMethod readMethod = accessDescriptor.createReadMethod();
        assertNotNull(readMethod);
        assertFalse(readMethod.canRead());
    }

    @Test
    public void testCreateReadMethod_readOnlyProperty() {
        PropertyAccessDescriptor accessDescriptor = new PropertyAccessDescriptor(testObject.getClass(),
                TestObject.READ_ONLY_LONG_PROPERTY);

        WriteMethod writeMethod = accessDescriptor.createWriteMethod();
        assertNotNull(writeMethod);
        assertFalse(writeMethod.canWrite());
        ReadMethod readMethod = accessDescriptor.createReadMethod();
        assertNotNull(readMethod);
        assertTrue(readMethod.canRead());
    }

}
