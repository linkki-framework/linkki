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

package org.linkki.tooling.apt.validator;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import javax.annotation.processing.Processor;

import org.junit.jupiter.api.Test;
import org.linkki.tooling.apt.processor.LinkkiAnnotationProcessor;

class UITableColumnValidatorTest extends BaseAnnotationProcessorTest {

    @Test
    void noErrors() {
        compile(asList(getSourceFile("Person.java"),
                       getSourceFile("tableColumn/SortableRowPmo.java")));

        verifyNoErrors();
    }

    @Test
    void sortableMethodReturnsVoid() {
        compile(asList(getSourceFile("Person.java"),
                       getSourceFile("tableColumn/ModelObjectRowPmo.java")));

        List<String> logs = getLogs();
        assertThat(logs, containsError());
        assertThat(logs, hasMessage(Messages.getString(UITableColumnValidator.SORTABLE_METHOD_RETURNS_VOID)));
    }

    @Test
    void sortableReturnTypeNotComparable() {
        compile(asList(getSourceFile("Person.java"),
                       getSourceFile("tableColumn/NonComparableRowPmo.java")));

        List<String> logs = getLogs();
        assertThat(logs, containsError());
        assertThat(logs, hasMessage(Messages.getString(UITableColumnValidator.SORTABLE_RETURN_TYPE_NOT_COMPARABLE)));
    }

    @Override
    protected Processor createProcessor() {
        return new LinkkiAnnotationProcessor();
    }

}
