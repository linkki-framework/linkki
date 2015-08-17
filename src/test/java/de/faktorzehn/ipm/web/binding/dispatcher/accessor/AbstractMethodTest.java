package de.faktorzehn.ipm.web.binding.dispatcher.accessor;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractMethodTest {
    @Mock
    private PropertyAccessDescriptor descriptor;

    @Test
    public void testHasNoMethod() {
        WriteMethod writeMethod = new WriteMethod(descriptor);
        assertFalse(writeMethod.hasMethod());
    }

}
