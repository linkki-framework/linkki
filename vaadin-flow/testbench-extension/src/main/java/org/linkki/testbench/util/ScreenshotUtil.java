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
package org.linkki.testbench.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtil {
    
    private static final String ERROR_MESSAGE = "Failed to take a screenshot of the test failure";

    private static final Logger LOGGER = Logger.getLogger(ScreenshotUtil.class.getName());

    private ScreenshotUtil() {
        // prevent instantiation
    }

    public static void takeScreenshot(WebDriver driver, String description) {
        try {
            LOGGER.info("Taking screenshot of " + driver.getCurrentUrl());
            var screenshotImage = ImageIO
                    .read(new ByteArrayInputStream(((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES)));
            var errorScreenshotFile = getErrorScreenshotFile(description);
            LOGGER.info("Writing screenshot to " + errorScreenshotFile.getAbsolutePath());
            ImageIO.write(screenshotImage, "png", errorScreenshotFile);
        } catch (IOException e) {
            throw new RuntimeException(ERROR_MESSAGE, e);
        }
    }

    private static File getErrorScreenshotFile(String description) throws IOException {
        var outputFolder = new File("target", "error-screenshots");
        outputFolder.mkdir();
        var fileName = getFileName(outputFolder, description);
        return new File(outputFolder.getPath(), fileName);
    }

    private static String getFileName(File outputFolder, String description) {
        var existingFiles = outputFolder.list((dir, name) -> name.startsWith(description));
        if (existingFiles == null) {
            // throw an exception to stop before overwriting existing files
            throw new RuntimeException(ERROR_MESSAGE + ": Unable to read existing screenshots");
        }
        // ensure no screenshots are overwritten
        if (existingFiles.length == 0) {
            return description + ".png";
        } else {
            return description + "_" + existingFiles.length + 1 + ".png";
        }
    }
}
