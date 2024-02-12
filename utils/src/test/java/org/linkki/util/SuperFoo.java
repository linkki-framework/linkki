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

package org.linkki.util;

/**
 * Test class for {@link BeanUtilsTest}
 */
public class SuperFoo implements IFoo {
    @SuppressWarnings("unused")
    private int superField = 3;

    public void superFoo() {
        // do nothing
    }

    public static class Foo extends SuperFoo {

        @SuppressWarnings("unused")
        private int field = 1;

        public int publicField = 2;

        public void foo() {
            // do nothing
        }

        @SuppressWarnings("unused")
        private void bar() {
            // do nothing
        }

        public int baz() {
            return 0;
        }

        public int baz(int foo) {
            return foo + 1;
        }

    }
}