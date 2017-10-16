/*
 * Copyright Faktor Zehn AG.
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
