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

    private static final Logger logger = Logger.getLogger(ScreenshotUtil.class.getName());

    public static void takeScreenshot(WebDriver driver, String description) {
        try {
            BufferedImage screenshotImage = ImageIO
                    .read(new ByteArrayInputStream(((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES)));
            final File errorScreenshotFile = getErrorScreenshotFile(description);
            ImageIO.write(screenshotImage, "png", errorScreenshotFile);
            logger.info("Error screenshot written to: " + errorScreenshotFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to take a screenshot of the test failure", e);
        }
    }

    private static File getErrorScreenshotFile(String description) {
        File outputFolder = new File("target", "uitest");
        outputFolder.mkdir();
        return new File(outputFolder.getPath(), description + ".png");
    }

}
