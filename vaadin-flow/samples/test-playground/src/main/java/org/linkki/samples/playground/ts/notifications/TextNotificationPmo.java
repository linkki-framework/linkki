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

package org.linkki.samples.playground.ts.notifications;

import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.framework.ui.notifications.NotificationUtil;

@UISection
public class TextNotificationPmo {

    private String title = "Something happened!";
    private String text = "You should probably take a look at it.";

    private int duration = 3000;

    @UITextField(position = 0, label = "Title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @UITextField(position = 10, label = "Text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @UIIntegerField(position = 11, label = "Duration")
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @UIButton(position = 12, caption = "Change duration")
    public void changeDurations() {
        NotificationUtil.setWarningDuration(duration);
        NotificationUtil.setInfoDuration(duration);
    }

    @UIButton(position = 20, caption = "Show info")
    public void showInfo() {
        NotificationUtil.showInfo(title, text);
    }

    @UIButton(position = 21, caption = "Show warning")
    public void showWarning() {
        NotificationUtil.showWarning(title, text);
    }

    @UIButton(position = 22, caption = "Show error")
    public void showError() {
        NotificationUtil.showError(title, text);
    }
}
