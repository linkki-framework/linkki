package org.linkki.core.binding.dispatcher.accessor;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class PropertyAccessDescriptorTest {

    @SuppressWarnings("null")
    private TestObject testObject;

    @Before
    public void setUp() {
        testObject = new TestObject();
    }

    @Test
    public void testConstructor_nonExistentProperty() {
        PropertyAccessDescriptor accessDescriptor = new PropertyAccessDescriptor(testObject.getClass(), "doesNotExist");
        assertThat(accessDescriptor.createReadMethod().canRead(), is(false));
        assertThat(accessDescriptor.createWriteMethod().canWrite(), is(false));
    }

    @Test
    public void testCreateReadMethod_readOnlyProperty() {
        PropertyAccessDescriptor accessDescriptor = new PropertyAccessDescriptor(testObject.getClass(),
                TestObject.READ_ONLY_LONG_PROPERTY);

        WriteMethod writeMethod = accessDescriptor.createWriteMethod();
        assertThat(writeMethod, is(notNullValue()));
        assertThat(writeMethod.canWrite(), is(false));
        ReadMethod readMethod = accessDescriptor.createReadMethod();
        assertThat(readMethod, is(notNullValue()));
        assertThat(readMethod.canRead(), is(true));
    }

    @Test
    public void testCreateReadMethod_defaultMethodProperty() {

        TestInterface testLambda = () -> System.out.println();
        PropertyAccessDescriptor accessDescriptor = new PropertyAccessDescriptor(testLambda.getClass(),
                TestInterface.RO_DEFAULT_BOOLEAN_PROPERTY);

        WriteMethod writeMethod = accessDescriptor.createWriteMethod();
        assertThat(writeMethod, is(notNullValue()));
        assertThat(writeMethod.canWrite(), is(false));
        ReadMethod readMethod = accessDescriptor.createReadMethod();
        assertThat(readMethod, is(notNullValue()));
        assertThat(readMethod.canRead(), is(true));
        assertThat(readMethod.readValue(testLambda), is(TestInterface.DEFAULT_PROPERTY_VALUE));
    }

}
