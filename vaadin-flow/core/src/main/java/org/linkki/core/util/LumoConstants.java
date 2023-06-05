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

package org.linkki.core.util;

import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * This class provides constant expressions of the properties defined in {@link LumoUtility}. This
 * allows the properties to be used inside annotations.
 * 
 * @apiNote Properties in {@link LumoUtility} are not constant expressions by design. This is to prevent
 *          inlining, so the class scanner that bundles additional frontend resources can detect usage
 *          of the class. linkki directly includes the utility CSS classes via its theme, which does not
 *          require class scanning.
 */
// generated by LumoConstantsTest#generateClass
public final class LumoConstants {

    private LumoConstants() {
        // prevent instantiation
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.MinHeight */
    public static final class MinHeight {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.MinHeight#FULL */
        public static final String FULL = "min-h-full";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.MinHeight#NONE */
        public static final String NONE = "min-h-0";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.MinHeight#SCREEN */
        public static final String SCREEN = "min-h-screen";

        private MinHeight() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.Accessibility */
    public static final class Accessibility {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Accessibility#SCREEN_READER_ONLY */
        public static final String SCREEN_READER_ONLY = "sr-only";

        private Accessibility() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.Grid */
    public static final class Grid {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Grid#FLOW_COLUMN */
        public static final String FLOW_COLUMN = "grid-flow-col";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Grid#FLOW_ROW */
        public static final String FLOW_ROW = "grid-flow-row";

        private Grid() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent */
    public static final class JustifyContent {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent#AROUND */
        public static final String AROUND = "justify-around";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent#BETWEEN */
        public static final String BETWEEN = "justify-between";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent#CENTER */
        public static final String CENTER = "justify-center";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent#END */
        public static final String END = "justify-end";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent#EVENLY */
        public static final String EVENLY = "justify-evenly";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent#START */
        public static final String START = "justify-start";

        private JustifyContent() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor */
    public static final class BorderColor {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#CONTRAST */
        public static final String CONTRAST = "border-contrast";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#CONTRAST_90 */
        public static final String CONTRAST_90 = "border-contrast-90";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#CONTRAST_80 */
        public static final String CONTRAST_80 = "border-contrast-80";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#CONTRAST_70 */
        public static final String CONTRAST_70 = "border-contrast-70";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#CONTRAST_60 */
        public static final String CONTRAST_60 = "border-contrast-60";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#CONTRAST_50 */
        public static final String CONTRAST_50 = "border-contrast-50";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#CONTRAST_40 */
        public static final String CONTRAST_40 = "border-contrast-40";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#CONTRAST_30 */
        public static final String CONTRAST_30 = "border-contrast-30";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#CONTRAST_20 */
        public static final String CONTRAST_20 = "border-contrast-20";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#CONTRAST_10 */
        public static final String CONTRAST_10 = "border-contrast-10";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#CONTRAST_5 */
        public static final String CONTRAST_5 = "border-contrast-5";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#PRIMARY */
        public static final String PRIMARY = "border-primary";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#PRIMARY_50 */
        public static final String PRIMARY_50 = "border-primary-50";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#PRIMARY_10 */
        public static final String PRIMARY_10 = "border-primary-10";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#ERROR */
        public static final String ERROR = "border-error";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#ERROR_50 */
        public static final String ERROR_50 = "border-error-50";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#ERROR_10 */
        public static final String ERROR_10 = "border-error-10";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#WARNING */
        public static final String WARNING = "border-warning";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#WARNING_10 */
        public static final String WARNING_10 = "border-warning-10";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#WARNING_STRONG */
        public static final String WARNING_STRONG = "border-warning-strong";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#SUCCESS */
        public static final String SUCCESS = "border-success";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#SUCCESS_50 */
        public static final String SUCCESS_50 = "border-success-50";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderColor#SUCCESS_10 */
        public static final String SUCCESS_10 = "border-success-10";

        private BorderColor() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius */
    public static final class BorderRadius {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius#NONE */
        public static final String NONE = "rounded-none";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius#SMALL */
        public static final String SMALL = "rounded-s";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius#MEDIUM */
        public static final String MEDIUM = "rounded-m";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius#LARGE */
        public static final String LARGE = "rounded-l";

        private BorderRadius() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.LineHeight */
    public static final class LineHeight {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.LineHeight#NONE */
        public static final String NONE = "leading-none";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.LineHeight#XSMALL */
        public static final String XSMALL = "leading-xs";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.LineHeight#SMALL */
        public static final String SMALL = "leading-s";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.LineHeight#MEDIUM */
        public static final String MEDIUM = "leading-m";

        private LineHeight() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.FontWeight */
    public static final class FontWeight {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FontWeight#THIN */
        public static final String THIN = "font-thin";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FontWeight#EXTRALIGHT */
        public static final String EXTRALIGHT = "font-extralight";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FontWeight#LIGHT */
        public static final String LIGHT = "font-light";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FontWeight#NORMAL */
        public static final String NORMAL = "font-normal";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FontWeight#MEDIUM */
        public static final String MEDIUM = "font-medium";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FontWeight#SEMIBOLD */
        public static final String SEMIBOLD = "font-semibold";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FontWeight#BOLD */
        public static final String BOLD = "font-bold";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FontWeight#EXTRABOLD */
        public static final String EXTRABOLD = "font-extrabold";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FontWeight#BLACK */
        public static final String BLACK = "font-black";

        private FontWeight() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.Overflow */
    public static final class Overflow {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Overflow#AUTO */
        public static final String AUTO = "overflow-auto";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Overflow#HIDDEN */
        public static final String HIDDEN = "overflow-hidden";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Overflow#SCROLL */
        public static final String SCROLL = "overflow-scroll";

        private Overflow() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.Whitespace */
    public static final class Whitespace {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Whitespace#NORMAL */
        public static final String NORMAL = "whitespace-normal";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Whitespace#NOWRAP */
        public static final String NOWRAP = "whitespace-nowrap";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Whitespace#PRE */
        public static final String PRE = "whitespace-pre";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Whitespace#PRE_LINE */
        public static final String PRE_LINE = "whitespace-pre-line";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Whitespace#PRE_WRAP */
        public static final String PRE_WRAP = "whitespace-pre-wrap";

        private Whitespace() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextOverflow */
    public static final class TextOverflow {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextOverflow#CLIP */
        public static final String CLIP = "overflow-clip";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextOverflow#ELLIPSIS */
        public static final String ELLIPSIS = "overflow-ellipsis";

        private TextOverflow() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.FlexWrap */
    public static final class FlexWrap {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FlexWrap#NOWRAP */
        public static final String NOWRAP = "flex-nowrap";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FlexWrap#WRAP */
        public static final String WRAP = "flex-wrap";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FlexWrap#WRAP_REVERSE */
        public static final String WRAP_REVERSE = "flex-wrap-reverse";

        private FlexWrap() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignSelf */
    public static final class AlignSelf {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignSelf#AUTO */
        public static final String AUTO = "self-auto";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignSelf#BASELINE */
        public static final String BASELINE = "self-baseline";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignSelf#CENTER */
        public static final String CENTER = "self-center";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignSelf#END */
        public static final String END = "self-end";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignSelf#START */
        public static final String START = "self-start";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignSelf#STRETCH */
        public static final String STRETCH = "self-stretch";

        private AlignSelf() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth */
    public static final class MaxWidth {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth#FULL */
        public static final String FULL = "max-w-full";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth#SCREEN_SMALL */
        public static final String SCREEN_SMALL = "max-w-screen-sm";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth#SCREEN_MEDIUM */
        public static final String SCREEN_MEDIUM = "max-w-screen-md";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth#SCREEN_LARGE */
        public static final String SCREEN_LARGE = "max-w-screen-lg";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth#SCREEN_XLARGE */
        public static final String SCREEN_XLARGE = "max-w-screen-xl";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth#SCREEN_XXLARGE */
        public static final String SCREEN_XXLARGE = "max-w-screen-2xl";

        private MaxWidth() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.BoxShadow */
    public static final class BoxShadow {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BoxShadow#XSMALL */
        public static final String XSMALL = "shadow-xs";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BoxShadow#SMALL */
        public static final String SMALL = "shadow-s";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BoxShadow#MEDIUM */
        public static final String MEDIUM = "shadow-m";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BoxShadow#LARGE */
        public static final String LARGE = "shadow-l";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BoxShadow#XLARGE */
        public static final String XLARGE = "shadow-xl";

        private BoxShadow() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection */
    public static final class FlexDirection {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection#COLUMN */
        public static final String COLUMN = "flex-col";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection#COLUMN_REVERSE */
        public static final String COLUMN_REVERSE = "flex-col-reverse";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection#ROW */
        public static final String ROW = "flex-row";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection#ROW_REVERSE */
        public static final String ROW_REVERSE = "flex-row-reverse";

        private FlexDirection() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.Padding */
    public static final class Padding {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Padding#NONE */
        public static final String NONE = "p-0";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Padding#XSMALL */
        public static final String XSMALL = "p-xs";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Padding#SMALL */
        public static final String SMALL = "p-s";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Padding#MEDIUM */
        public static final String MEDIUM = "p-m";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Padding#LARGE */
        public static final String LARGE = "p-l";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Padding#XLARGE */
        public static final String XLARGE = "p-xl";

        private Padding() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.Height */
    public static final class Height {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Height#AUTO */
        public static final String AUTO = "h-auto";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Height#FULL */
        public static final String FULL = "h-full";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Height#NONE */
        public static final String NONE = "h-0";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Height#SCREEN */
        public static final String SCREEN = "h-screen";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Height#XSMALL */
        public static final String XSMALL = "h-xs";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Height#SMALL */
        public static final String SMALL = "h-s";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Height#MEDIUM */
        public static final String MEDIUM = "h-m";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Height#LARGE */
        public static final String LARGE = "h-l";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Height#XLARGE */
        public static final String XLARGE = "h-xl";

        private Height() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextColor */
    public static final class TextColor {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextColor#HEADER */
        public static final String HEADER = "text-header";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextColor#BODY */
        public static final String BODY = "text-body";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextColor#SECONDARY */
        public static final String SECONDARY = "text-secondary";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextColor#TERTIARY */
        public static final String TERTIARY = "text-tertiary";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextColor#DISABLED */
        public static final String DISABLED = "text-disabled";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextColor#PRIMARY */
        public static final String PRIMARY = "text-primary";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextColor#PRIMARY_CONTRAST */
        public static final String PRIMARY_CONTRAST = "text-primary-contrast";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextColor#ERROR */
        public static final String ERROR = "text-error";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextColor#ERROR_CONTRAST */
        public static final String ERROR_CONTRAST = "text-error-contrast";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextColor#WARNING */
        public static final String WARNING = "text-warning";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextColor#WARNING_CONTRAST */
        public static final String WARNING_CONTRAST = "text-warning-contrast";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextColor#SUCCESS */
        public static final String SUCCESS = "text-success";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextColor#SUCCESS_CONTRAST */
        public static final String SUCCESS_CONTRAST = "text-success-contrast";

        private TextColor() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.BoxSizing */
    public static final class BoxSizing {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BoxSizing#BORDER */
        public static final String BORDER = "box-border";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.BoxSizing#CONTENT */
        public static final String CONTENT = "box-content";

        private BoxSizing() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.Width */
    public static final class Width {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Width#AUTO */
        public static final String AUTO = "w-auto";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Width#FULL */
        public static final String FULL = "w-full";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Width#XSMALL */
        public static final String XSMALL = "w-xs";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Width#SMALL */
        public static final String SMALL = "w-s";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Width#MEDIUM */
        public static final String MEDIUM = "w-m";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Width#LARGE */
        public static final String LARGE = "w-l";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Width#XLARGE */
        public static final String XLARGE = "w-xl";

        private Width() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextTransform */
    public static final class TextTransform {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextTransform#CAPITALIZE */
        public static final String CAPITALIZE = "capitalize";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextTransform#LOWERCASE */
        public static final String LOWERCASE = "lowercase";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextTransform#UPPERCASE */
        public static final String UPPERCASE = "uppercase";

        private TextTransform() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background */
    public static final class Background {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#BASE */
        public static final String BASE = "bg-base";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#TRANSPARENT */
        public static final String TRANSPARENT = "bg-transparent";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#CONTRAST */
        public static final String CONTRAST = "bg-contrast";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#CONTRAST_90 */
        public static final String CONTRAST_90 = "bg-contrast-90";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#CONTRAST_80 */
        public static final String CONTRAST_80 = "bg-contrast-80";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#CONTRAST_70 */
        public static final String CONTRAST_70 = "bg-contrast-70";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#CONTRAST_60 */
        public static final String CONTRAST_60 = "bg-contrast-60";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#CONTRAST_50 */
        public static final String CONTRAST_50 = "bg-contrast-50";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#CONTRAST_40 */
        public static final String CONTRAST_40 = "bg-contrast-40";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#CONTRAST_30 */
        public static final String CONTRAST_30 = "bg-contrast-30";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#CONTRAST_20 */
        public static final String CONTRAST_20 = "bg-contrast-20";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#CONTRAST_10 */
        public static final String CONTRAST_10 = "bg-contrast-10";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#CONTRAST_5 */
        public static final String CONTRAST_5 = "bg-contrast-5";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#PRIMARY */
        public static final String PRIMARY = "bg-primary";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#PRIMARY_50 */
        public static final String PRIMARY_50 = "bg-primary-50";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#PRIMARY_10 */
        public static final String PRIMARY_10 = "bg-primary-10";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#ERROR */
        public static final String ERROR = "bg-error";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#ERROR_50 */
        public static final String ERROR_50 = "bg-error-50";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#ERROR_10 */
        public static final String ERROR_10 = "bg-error-10";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#WARNING */
        public static final String WARNING = "bg-warning";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#WARNING_10 */
        public static final String WARNING_10 = "bg-warning-10";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#SUCCESS */
        public static final String SUCCESS = "bg-success";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#SUCCESS_50 */
        public static final String SUCCESS_50 = "bg-success-50";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Background#SUCCESS_10 */
        public static final String SUCCESS_10 = "bg-success-10";

        private Background() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.IconSize */
    public static final class IconSize {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.IconSize#SMALL */
        public static final String SMALL = "icon-s";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.IconSize#MEDIUM */
        public static final String MEDIUM = "icon-m";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.IconSize#LARGE */
        public static final String LARGE = "icon-l";

        private IconSize() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.Position */
    public static final class Position {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Position#ABSOLUTE */
        public static final String ABSOLUTE = "absolute";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Position#FIXED */
        public static final String FIXED = "fixed";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Position#RELATIVE */
        public static final String RELATIVE = "relative";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Position#STATIC */
        public static final String STATIC = "static";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Position#STICKY */
        public static final String STICKY = "sticky";

        private Position() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignItems */
    public static final class AlignItems {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignItems#BASELINE */
        public static final String BASELINE = "items-baseline";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignItems#CENTER */
        public static final String CENTER = "items-center";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignItems#END */
        public static final String END = "items-end";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignItems#START */
        public static final String START = "items-start";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignItems#STRETCH */
        public static final String STRETCH = "items-stretch";

        private AlignItems() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.MinWidth */
    public static final class MinWidth {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.MinWidth#FULL */
        public static final String FULL = "min-w-full";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.MinWidth#NONE */
        public static final String NONE = "min-w-0";

        private MinWidth() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.FontSize */
    public static final class FontSize {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FontSize#XXSMALL */
        public static final String XXSMALL = "text-2xs";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FontSize#XSMALL */
        public static final String XSMALL = "text-xs";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FontSize#SMALL */
        public static final String SMALL = "text-s";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FontSize#MEDIUM */
        public static final String MEDIUM = "text-m";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FontSize#LARGE */
        public static final String LARGE = "text-l";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FontSize#XLARGE */
        public static final String XLARGE = "text-xl";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FontSize#XXLARGE */
        public static final String XXLARGE = "text-2xl";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.FontSize#XXXLARGE */
        public static final String XXXLARGE = "text-3xl";

        private FontSize() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.Gap */
    public static final class Gap {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Gap#XSMALL */
        public static final String XSMALL = "gap-xs";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Gap#SMALL */
        public static final String SMALL = "gap-s";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Gap#MEDIUM */
        public static final String MEDIUM = "gap-m";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Gap#LARGE */
        public static final String LARGE = "gap-l";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Gap#XLARGE */
        public static final String XLARGE = "gap-xl";

        private Gap() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextAlignment */
    public static final class TextAlignment {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextAlignment#LEFT */
        public static final String LEFT = "text-left";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextAlignment#CENTER */
        public static final String CENTER = "text-center";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextAlignment#RIGHT */
        public static final String RIGHT = "text-right";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.TextAlignment#JUSTIFY */
        public static final String JUSTIFY = "text-justify";

        private TextAlignment() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.Flex */
    public static final class Flex {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Flex#AUTO */
        public static final String AUTO = "flex-auto";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Flex#NONE */
        public static final String NONE = "flex-none";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Flex#GROW */
        public static final String GROW = "flex-grow";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Flex#GROW_NONE */
        public static final String GROW_NONE = "flex-grow-0";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Flex#SHRINK */
        public static final String SHRINK = "flex-shrink";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Flex#SHRINK_NONE */
        public static final String SHRINK_NONE = "flex-shrink-0";

        private Flex() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignContent */
    public static final class AlignContent {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignContent#AROUND */
        public static final String AROUND = "content-around";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignContent#BETWEEN */
        public static final String BETWEEN = "content-between";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignContent#CENTER */
        public static final String CENTER = "content-center";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignContent#END */
        public static final String END = "content-end";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignContent#EVENLY */
        public static final String EVENLY = "content-evenly";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignContent#START */
        public static final String START = "content-start";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.AlignContent#STRETCH */
        public static final String STRETCH = "content-stretch";

        private AlignContent() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.MaxHeight */
    public static final class MaxHeight {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.MaxHeight#FULL */
        public static final String FULL = "max-h-full";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.MaxHeight#SCREEN */
        public static final String SCREEN = "max-h-screen";

        private MaxHeight() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.Border */
    public static final class Border {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Border#NONE */
        public static final String NONE = "border-0";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Border#ALL */
        public static final String ALL = "border";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Border#BOTTOM */
        public static final String BOTTOM = "border-b";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Border#LEFT */
        public static final String LEFT = "border-l";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Border#RIGHT */
        public static final String RIGHT = "border-r";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Border#TOP */
        public static final String TOP = "border-t";

        private Border() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.ListStyleType */
    public static final class ListStyleType {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.ListStyleType#NONE */
        public static final String NONE = "list-none";

        private ListStyleType() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.Display */
    public static final class Display {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Display#BLOCK */
        public static final String BLOCK = "block";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Display#FLEX */
        public static final String FLEX = "flex";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Display#GRID */
        public static final String GRID = "grid";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Display#HIDDEN */
        public static final String HIDDEN = "hidden";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Display#INLINE */
        public static final String INLINE = "inline";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Display#INLINE_BLOCK */
        public static final String INLINE_BLOCK = "inline-block";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Display#INLINE_FLEX */
        public static final String INLINE_FLEX = "inline-flex";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Display#INLINE_GRID */
        public static final String INLINE_GRID = "inline-grid";

        private Display() {
            // prevent instantiation
        }
    }

    /** @see com.vaadin.flow.theme.lumo.LumoUtility.Margin */
    public static final class Margin {

        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Margin#AUTO */
        public static final String AUTO = "m-auto";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Margin#NONE */
        public static final String NONE = "m-0";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Margin#XSMALL */
        public static final String XSMALL = "m-xs";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Margin#SMALL */
        public static final String SMALL = "m-s";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Margin#MEDIUM */
        public static final String MEDIUM = "m-m";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Margin#LARGE */
        public static final String LARGE = "m-l";
        /** @see com.vaadin.flow.theme.lumo.LumoUtility.Margin#XLARGE */
        public static final String XLARGE = "m-xl";

        private Margin() {
            // prevent instantiation
        }
    }
}