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
package org.linkki.samples.playground.customlayout.pmo;

import java.security.SecureRandom;
import java.time.LocalDate;

import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.samples.playground.customlayout.annotation.UIHorizontalLayout;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;

// tag::hotelsearch[]
@UIHorizontalLayout
public class HotelSearchPmo {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private int noOfGuests;
    private LocalDate arrival;
    private LocalDate depature;

    @UIIntegerField(position = 10, label = "Number of Guests")
    public int getNoOfGuests() {
        return noOfGuests;
    }

    public void setNoOfGuests(int noOfGuests) {
        this.noOfGuests = noOfGuests;
    }

    // end::hotelsearch[]
    @UIDateField(position = 20, label = "Date of Arrival")
    public LocalDate getArrival() {
        return arrival;
    }

    public void setArrival(LocalDate arrival) {
        this.arrival = arrival;
    }

    @UIDateField(position = 30, label = "Date of Depature")
    public LocalDate getDepature() {
        return depature;
    }

    public void setDepature(LocalDate depature) {
        this.depature = depature;
    }

    @UIButton(position = 40, caption = "Send", icon = VaadinIcon.PAPERPLANE, showIcon = true)
    public void send() {
        int numberOfBeds = SECURE_RANDOM.nextInt() * 10;
        Notification.show(String.format("Thank you for your request!" + " We have %d bed%s available!", numberOfBeds,
                                        numberOfBeds == 1 ? "" : "s"));
    }
}
