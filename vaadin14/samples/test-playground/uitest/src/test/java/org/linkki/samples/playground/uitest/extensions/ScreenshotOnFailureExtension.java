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

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.vaadin.testbench.HasDriver;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.screenshot.ImageFileUtil;

public class ScreenshotOnFailureExtension implements TestWatcher {

    private static final Logger logger = Logger
            .getLogger(ScreenshotOnFailureRule.class.getName());

    private final HasDriver driverHolder;

    public ScreenshotOnFailureExtension(HasDriver driverHolder) {
        this.driverHolder = driverHolder;
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String description = context.getDisplayName();

        if (driverHolder.getDriver() == null) {
            return;
        }

        // Grab a screenshot when a test fails
        try {
            BufferedImage screenshotImage = ImageIO
                    .read(new ByteArrayInputStream(
                            ((TakesScreenshot)driverHolder.getDriver())
                                    .getScreenshotAs(OutputType.BYTES)));
            // Store the screenshot in the errors directory
            ImageFileUtil.createScreenshotDirectoriesIfNeeded();
            final File errorScreenshotFile = getErrorScreenshotFile(description);
            ImageIO.write(screenshotImage, "png",
                          errorScreenshotFile);
            logger.info("Error screenshot written to: " + errorScreenshotFile.getAbsolutePath());
        } catch (IOException e1) {
            throw new RuntimeException(
                    "There was a problem grabbing and writing a screen shot of a test failure.",
                    e1);
        }
    }

    protected File getErrorScreenshotFile(String description) {
        File outputFolder = new File("target", "uitest");
        outputFolder.mkdir();
        return new File(outputFolder.getPath(), description + ".png");
    }


}
