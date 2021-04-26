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

package org.linkki.samples.playground.uitest.extensions;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class DomUtil {

    public static void saveDom(WebDriver driver, String description) {
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        String dom = (String)executor.executeScript("return document.documentElement.outerHTML");

        writeFile(getDomFile(description), dom);
    }

    private static File getDomFile(String description) {
        File outputFolder = new File("target", "uitest");
        outputFolder.mkdir();
        return new File(outputFolder.getPath(), description + ".html");
    }

    private static void writeFile(File output, String content) {
        try {
            FileUtils.write(output, content, Charset.forName("UTF-8"));
        } catch (IOException exception) {
            throw new RuntimeException("Failed to write HTML file", exception);
        }
    }
}
