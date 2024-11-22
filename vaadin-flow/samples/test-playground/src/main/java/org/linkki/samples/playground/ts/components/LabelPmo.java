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
package org.linkki.samples.playground.ts.components;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.defaults.columnbased.pmo.TableFooterPmo;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.aspects.annotation.BindStyleNames;
import org.linkki.core.ui.aspects.annotation.BindSuffix;
import org.linkki.core.ui.aspects.types.IconPosition;
import org.linkki.core.ui.aspects.types.TextAlignment;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UIMultiSelect;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.nested.annotation.UINestedComponent;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.ui.theme.LinkkiTheme;
import org.linkki.core.util.HtmlContent;
import org.linkki.samples.playground.ips.model.Marker;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;

@UISection(layout = SectionLayout.VERTICAL)
public class LabelPmo {

    private static final String CUSTOM_STYLE = "default-style";
    private Set<String> styles = Set.of();

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

    @UILabel(position = 91, label = "UILabel with a CompletableFuture as Value (complete after 5 seconds)")
    public CompletableFuture<String> getLabelWithFuture() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "I am loaded asynchronously";
        });
    }

    @UILabel(position = 92,
            label = "UILabel with a CompletableFuture as Value (complete after 5 seconds) that throws Exception")
    public CompletableFuture<String> getLabelWithFutureWithException() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException("Error retrieving value");
        });
    }

    @BindStyleNames
    @UILabel(position = 93, label = "Right-aligned label with custom style")
    public String getRightAlignedLabel() {
        return "I am a RIGHT ALIGNED label with a custom style";
    }

    public Set<String> getRightAlignedLabelStyleNames() {
        return Set.of(CUSTOM_STYLE, LumoUtility.TextAlignment.RIGHT);
    }

    @BindStyleNames
    @BindIcon(VaadinIcon.CIRCLE)
    @UILabel(position = 95, label = "Label with a dynamic style", htmlContent = true)
    public String getStyledLabelAndIcon() {
        var icon = LumoIcon.ARROW_DOWN.create();
        return "Select my style in the 'Styles' multi selection box below " + icon.getElement().getOuterHTML();
    }

    @UIMultiSelect(position = 96, label = "Styles")
    public Set<String> getStyledLabelAndIconStyleNames() {
        return styles;
    }

    public void setStyledLabelAndIconStyleNames(Set<String> styles) {
        this.styles = styles;
    }

    public List<String> getStyledLabelAndIconStyleNamesAvailableValues() {
        return List.of(LinkkiTheme.Text.ICON_SUCCESS,
                       LinkkiTheme.Text.ICON_WARNING,
                       LinkkiTheme.Text.ICON_ERROR,
                       LinkkiTheme.Text.ICON_INFO,
                       LumoUtility.TextColor.SUCCESS,
                       LumoUtility.TextColor.WARNING,
                       LumoUtility.TextColor.ERROR,
                       LinkkiTheme.Text.TEXT_INFO);
    }

    @UILabel(position = 100, label = "Label with an icon on the left")
    @BindIcon(value = VaadinIcon.ABACUS)
    public String getIconLeftLabel() {
        return "Icon on the left";
    }

    @UILabel(position = 105, label = "Label with an icon on the right", iconPosition = IconPosition.RIGHT)
    @BindIcon(value = VaadinIcon.ABACUS)
    public String getIconRightLabel() {
        return "Icon on the right";
    }

    @UINestedComponent(position = 140, label = "HTML Content")
    public HtmlContentLabelPmo getHtmlContentLabelPmo() {
        return new HtmlContentLabelPmo();
    }

    @UINestedComponent(position = 150, label = "UILabel in table")
    public LabelTablePmo getLabelTable() {
        return new LabelTablePmo();
    }

    @UISection(caption = "UILabel in table")
    public static class LabelTablePmo implements ContainerPmo<LabelRowPmo> {

        @Override
        public List<LabelRowPmo> getItems() {
            return List.of(new LabelRowPmo());
        }

        @Override
        public int getPageLength() {
            return 0;
        }

        @Override
        public Optional<TableFooterPmo> getFooterPmo() {
            return Optional.of(column -> "Footer for " + column);
        }

    }

    public static class LabelRowPmo {

        @UITableColumn(width = 200)
        @UILabel(position = 10, label = "Label with HtmlContent as return type")
        public HtmlContent getHtmlContentLabel() {
            return HtmlContent.builder()
                    .styledTag("i", "color: red;", "HTML")
                    .text(" ")
                    .tag("b", "Content")
                    .build();
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

    @UIHorizontalLayout
    public static class HtmlContentLabelPmo {
        @UINestedComponent(position = 10, label = "")
        public HtmlContentPropertyLabelPmo getHtmlContentPropertyLabelPmo() {
            return new HtmlContentPropertyLabelPmo();
        }

        @UINestedComponent(position = 20, label = "")
        public HtmlContentReturnTypeLabelPmo getHtmlContentReturnTypeLabelPmo() {
            return new HtmlContentReturnTypeLabelPmo();
        }

        @UISection(caption = "Label with HtmlContent using htmlContent=true", layout = SectionLayout.VERTICAL)
        public class HtmlContentPropertyLabelPmo {

            @UILabel(position = 10, label = "Label with HTML Content", htmlContent = true)
            @BindIcon(value = VaadinIcon.ACCORDION_MENU)
            @BindSuffix("%")
            public String getHtmlContentLabel() {
                return "<i style=\"color: red;\">HTML</i> <b>Content</b>";
            }

            @UILabel(position = 15, label = "Label with sanitized HTML content", htmlContent = true)
            public String getSanitizedHtmlContentLabel() {
                return "<b><iframe onload=\"alert('LIN-3319 :: Should not be visible!');\"/>" +
                        "This should be bold text without showing the stripped tag 'iframe'</b>";
            }

            @UILabel(position = 16, label = "Sanitized HTML content containing a Vaadin icon", htmlContent = true)
            public String getSanitizedHtmlContentWithIconLabel() {
                var icon = VaadinIcon.PLUS.create();
                icon.setColor("red");
                var iconHtml = icon.getElement().getOuterHTML();
                return "This text should end with a red plus icon " + iconHtml;
            }

            @UILabel(position = 20, label = "Label without HTML Content")
            public String getNotHtmlContentLabel() {
                return "<b>NOT</b> HTML Content";
            }
        }

        @UISection(caption = "Label with HtmlContent using HtmlContent as returnType", layout = SectionLayout.VERTICAL)
        public static class HtmlContentReturnTypeLabelPmo {

            // tag::labelPmo-labelHtmlContent[]
            @UILabel(position = 10, label = "Label with HtmlContent")
            @BindIcon(value = VaadinIcon.ACCORDION_MENU)
            @BindSuffix("%")
            public HtmlContent getHtmlContentLabel() {
                return HtmlContent.builder()
                        .styledTag("i", "color: red;", "HTML")
                        .text(" ")
                        .tag("b", "Content")
                        .build();
            }
            // end::labelPmo-labelHtmlContent[]

            @UILabel(position = 15, label = "Label with sanitized HTML content")
            public HtmlContent getSanitizedHtmlContentLabel() {
                var iframe = HtmlContent.builder()
                        .tag("iframe", Map.of("onload", "alert('LIN-3319 :: Should not be visible!');"))
                        .text("This should be bold text without showing the stripped tag 'iframe'")
                        .build();
                return HtmlContent.builder().tag("b", Collections.emptyMap(), iframe).build();
            }

            @UILabel(position = 16, label = "Sanitized HTML content containing a Vaadin icon")
            public HtmlContent getSanitizedHtmlContentWithIconLabel() {
                var icon = VaadinIcon.PLUS.create();
                icon.setColor("red");
                return HtmlContent.builder()
                        .text("This text should end with a red plus icon ")
                        .icon(icon).build();
            }

            @UILabel(position = 20, label = "HTMLContent as escaped String")
            public HtmlContent getNotHtmlContentLabel() {
                return HtmlContent.text("<b>NOT</b> HTML Content");
            }
        }
    }
}
