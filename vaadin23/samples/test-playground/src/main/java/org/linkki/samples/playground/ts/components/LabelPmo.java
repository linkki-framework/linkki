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
package org.linkki.samples.playground.ts.components;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.defaults.columnbased.pmo.TableFooterPmo;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.aspects.annotation.BindSuffix;
import org.linkki.core.ui.aspects.types.TextAlignment;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.nested.annotation.UINestedComponent;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.framework.ui.LinkkiApplicationTheme;
import org.linkki.samples.playground.ips.model.Marker;

import com.vaadin.flow.component.icon.VaadinIcon;

@UISection(layout = SectionLayout.VERTICAL)
public class LabelPmo {

    @UILabel(position = 10, label = "Label with HTML Content", htmlContent = true)
    @BindIcon(value = VaadinIcon.ACCORDION_MENU)
    @BindSuffix("%")
    public String getHtmlContentLabel() {
        return "<i style=\"color: red;\">HTML</i> <b>Content</b>";
    }

    @UILabel(position = 20, label = "Label without HTML Content")
    public String getNotHtmlContentLabel() {
        return "<b>NOT</b> HTML Content";
    }

    @UILabel(position = 50, label = "Label with a BigDecimal as Value")
    @BindIcon(value = VaadinIcon.ACCORDION_MENU)
    @BindSuffix("%")
    public BigDecimal getBigDecimalLabel() {
        return BigDecimal.valueOf(12345.6789);
    }

    @UILabel(position = 60, label = "Label with a LocalDate as Value")
    public LocalDate getLocalDateLabel() {
        return LocalDate.of(4321, 5, 6);
    }

    @UILabel(position = 70, label = "Label with a LocalDateTime as Value")
    public LocalDateTime getLocalDateTimeLabel() {
        return LocalDateTime.of(3456, 1, 2, 7, 8, 9);
    }

    @UILabel(position = 80, label = "Label with a enum as Value")
    public RoundingMode getEnumLabel() {
        return RoundingMode.DOWN;
    }

    @UILabel(position = 90, label = "Label with a enum with getName() as Value")
    public Marker getNamedEnumLabel() {
        return Marker.REQUIRED_INFORMATION_MISSING;
    }

    @UILabel(position = 95, label = "Label with a custom style", styleNames = { "style1",
            LinkkiApplicationTheme.TEXT_RIGHT_ALIGNED })
    public String getStyledLabel() {
        return "I am a custom styled GREEN and RIGHT ALIGNED label";
    }

    @UINestedComponent(position = 100, label = "UILabel in table")
    public LabelTablePmo getLabelTable() {
        return new LabelTablePmo();
    }

    @UISection
    public static class LabelTablePmo implements ContainerPmo<LabelRowPmo> {

        @Override
        public List<LabelRowPmo> getItems() {
            return Arrays.asList(new LabelRowPmo());
        }

        @Override
        public int getPageLength() {
            return 0;
        }

        @Override
        public Optional<TableFooterPmo> getFooterPmo() {
            return Optional.of(new TableFooterPmo() {

                @Override
                public String getFooterText(String column) {
                    return "Footer for " + column;
                }
            });
        }

    }

    public static class LabelRowPmo {

        @UITableColumn(width = 200)
        @UILabel(position = 10, label = "Label with HTML Content", htmlContent = true)
        public String getHtmlContentLabel() {
            return "<i style=\"color: red;\">HTML</i> <b>Content</b>";
        }

        @UITableColumn(width = 200)
        @UILabel(position = 20, label = "Long label that should break")
        public String getLongLabel() {
            return "a long label with a loooooooooooong word";
        }

        @UITableColumn(flexGrow = 1, textAlign = TextAlignment.RIGHT)
        @UILabel(position = 30, label = "Right aligned label")
        public String getRightAlignedLabel() {
            return "This label should be right aligned";
        }

        @UITableColumn(flexGrow = 0)
        @UILabel(position = 40, label = "Icon")
        @BindIcon(value = VaadinIcon.ARROW_CIRCLE_LEFT)
        public String getIconOnly() {
            return "";
        }

    }
}
