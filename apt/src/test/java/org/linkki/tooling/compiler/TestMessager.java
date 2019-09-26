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


package org.linkki.tooling.compiler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

public class TestMessager implements Messager {

    private final boolean verbose;
    private final List<String> logs;

    public TestMessager(boolean verbose) {
        this.verbose = verbose;
        this.logs = new ArrayList<>();
    }

    @Override
    public void printMessage(Kind kind, CharSequence msg) {
        String log = "(kind=" + kind + ", msg=" + msg + ')';
        logs.add(log);
        print(log);
    }

    @Override
    public void printMessage(Kind kind, CharSequence msg, Element e) {
        String log = "(kind=" + kind + ", msg=" + msg + ", element=" + e + ')';
        logs.add(log);
        print(log);
    }

    @Override
    public void printMessage(Kind kind, CharSequence msg, Element e, AnnotationMirror a) {
        String log = "(kind=" + kind + ", msg=" + msg + ", element=" + e + ", annotation=" + a + ')';
        logs.add(log);
        print(log);
    }

    @Override
    public void printMessage(Kind kind, CharSequence msg, Element e, AnnotationMirror a, AnnotationValue v) {
        String log = "(kind=" + kind + ", msg=" + msg + ", element=" + e + ", annotation=" + a + ", value=" + v + ')';
        logs.add(log);
        print(log);
    }

    private void print(String log) {
        if (verbose) {
            System.out.println(log);
        }
    }

    public List<String> getLogs() {
        return logs;
    }
}
