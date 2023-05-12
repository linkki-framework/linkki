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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtil {

    private static final Logger LOGGER = Logger.getLogger(ScreenshotUtil.class.getName());

    private ScreenshotUtil() {
        // prevent instantiation
    }

    public static void takeScreenshot(WebDriver driver, String description) {
        try {
            LOGGER.info("Taking screenshot of " + driver.getCurrentUrl());
            BufferedImage screenshotImage = ImageIO
                    .read(new ByteArrayInputStream(((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES)));
            final File errorScreenshotFile = getErrorScreenshotFile(description);
            LOGGER.info("Writing screenshot to " + errorScreenshotFile.getAbsolutePath());
            ImageIO.write(screenshotImage, "png", errorScreenshotFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to take a screenshot of the test failure", e);
        }
    }

    private static File getErrorScreenshotFile(String description) {
        File outputFolder = new File("target", "error-screenshots");
        outputFolder.mkdir();
        return new File(outputFolder.getPath(), description + ".png");
    }

}
