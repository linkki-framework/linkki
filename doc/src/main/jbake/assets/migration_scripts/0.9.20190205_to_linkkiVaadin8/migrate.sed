# vaadin

s/BaseTheme/ValoTheme/g
s/com.vaadin.shared.ui.label.ContentMode/com.vaadin.shared.ui.ContentMode/g
s/com.vaadin.server.FontAwesome/com.vaadin.icons.VaadinIcons/g
s/FontAwesome/VaadinIcons/g
s/setNullSelectionAllowed/setEmptySelectionAllowed/g
s/setItemCaptionProvider\((.*)\)/setItemCaptionGenerator(\1::getUnsafeCaption)/g

# vaadin 7 compatibility

s/com.vaadin.ui.Table/com.vaadin.v7.ui.Table/g
s/(import com.vaadin.annotations.Theme;)/\1\nimport com.vaadin.annotations.Widgetset;/g
s/(@Theme\([^)]*\))/\1\n@Widgetset("com.vaadin.v7.Vaadin7WidgetSet")/g

# Icons

s/VaadinIcons.TRASH_O/VaadinIcons.TRASH/g
s/VaadinIcons.PENCIL_SQUARE_O/VaadinIcons.EDIT/g
s/VaadinIcons.TIMES_CIRCLE/VaadinIcons.CLOSE_CIRCLE/g
s/VaadinIcons.BANK/VaadinIcons.INSTITUTION/g
s/VaadinIcons.BARS/VaadinIcons.MENU/g
s/VaadinIcons.FILES_O/VaadinIcons.FILE_O/g
s/VaadinIcons.SEND/VaadinIcons.SHARE/g
s/VaadinIcons.STAR_HALF_FULL/VaadinIcons.STAR_HALF_LEFT_O/g
s/VaadinIcons.EXCLAMATION_TRIANGLE/VaadinIcons.WARNING/g

# linkki

s/(@UITextField.*)columns = ([[:digit:]]+)/\1 width = "\2em"/g
s/(@UITextArea.*)columns = ([[:digit:]]+)/\1 width = "\2em"/g
s/PmoBasedTableFactory<([^>]*)>/PmoBasedTableFactory/g
s/TableSection<[^>]*>/TableSection/g
s/(import org.linkki.core.ui.util.UiUtil;)/\1\nimport org.linkki.core.ui.UiFramework;/g
s/UiUtil.getUiLocale/UiFramework.getLocale/g
s/(import org.linkki.core.ButtonPmo;)/\1\nimport org.linkki.core.ButtonPmoBuilder;/g
s/ButtonPmo\.new/ButtonPmoBuilder.new/g
s/LinkkiConverterFactory/LinkkiConverterRegistry/g
s/com.vaadin.server.ErrorMessage.ErrorLevel/org.linkki.core.message.Severity/g
s/ErrorLevel/Severity/g
s/org.linkki.core.ui.section.annotations.aspect.FieldValueAspectDefinition/org.linkki.core.ui.section.annotations.aspect.ValueAspectDefinition/g
s/FieldValueAspectDefinition/ValueAspectDefinition/g
s/org.linkki.core.ui.components.LinkkiComboBox/com.vaadin.ui.ComboBox/g
s/LinkkiComboBox/ComboBox/g
s/org.linkki.core.ui.converters.LocalDateTimeToStringConverter/org.linkki.core.ui.formatters.LocalDateTimeFormatter/g
s/org.linkki.core.ui.converters.LocalDateToStringConverter/org.linkki.core.ui.formatters.LocalDateFormatter/g

# deprecated API
s/ComponentFactory.newTextfield/ComponentFactory.newTextField/g

# update pom.xml

s/<artifactId>linkki-application-framework<\/artifactId>/<artifactId>linkki-application-framework-vaadin8<\/artifactId>/g
s/<artifactId>linkki-core<\/artifactId>/<artifactId>linkki-core-vaadin8<\/artifactId>/g
