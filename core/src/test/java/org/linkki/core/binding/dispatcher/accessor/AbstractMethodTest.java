package org.linkki.core.binding.dispatcher.accessor;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.dispatcher.accessor.PropertyAccessDescriptor;
import org.linkki.core.binding.dispatcher.accessor.WriteMethod;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractMethodTest {
    @Mock
    private PropertyAccessDescriptor descriptor;

    @Test
    public void testHasNoMethod() {
        when(descriptor.getReflectionWriteMethod()).thenReturn(Optional.empty());

        WriteMethod writeMethod = new WriteMethod(descriptor);

        assertFalse(writeMethod.hasMethod());
    }

}
