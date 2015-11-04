/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.util.cdi;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.test.cdi.TestBeanManager;
import org.linkki.util.cdi.BeanInstantiator;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BeanInstantiatorTest {
    private TestBeanManager testBeanManager = new TestBeanManager();
    private BeanInstantiator beanInstantiator = new BeanInstantiator(testBeanManager);

    @Test
    public void testExactlyOneInstance() {
        setUpBeans("A");
        assertEquals("A", beanInstantiator.getInstance(String.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testNoInstance() {
        setUpBeans();
        beanInstantiator.getInstance(String.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testMultipleInstances() {
        setUpBeans("A", "B", "C");
        beanInstantiator.getInstance(String.class);
    }

    private void setUpBeans(String... args) {
        Arrays.asList(args).forEach(arg -> testBeanManager.addInstance(arg));
    }
}
