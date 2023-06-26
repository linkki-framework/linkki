/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.util.reflection.other;


import org.linkki.util.reflection.TestInterface;

public class OtherPackageTestObject implements TestInterface {

    @Override
    public void doSomething() {
        // do nothing :)
    }

    @Override
    public String getRoDefaultMethod() {
        return "other";
    }

    public static TestInterface getInstance() {
        return new OtherPackageTestObject();
    }

    public static TestInterface getPackagePrivateInstance() {
        return new PackagePrivateInnerClass();
    }

    static class PackagePrivateInnerClass extends OtherPackageTestObject {

        @Override
        public void doSomething() {
            // do nothing :)
        }

        @Override
        public String getRoDefaultMethod() {
            return "otherPackage";
        }

    }
}
