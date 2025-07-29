package org.linkki.core.ui.test;

import org.linkki.archunit.LinkkiArchRules;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;

@AnalyzeClasses(packages = "org.linkki", importOptions = ImportOption.Predefined.OnlyIncludeTests.class)
public class ArchTest {

    @com.tngtech.archunit.junit.ArchTest
    public void test_karibu_ui_extension_should_not_be_static(JavaClasses javaClasses) {
        LinkkiArchRules.KARIBU_UI_EXTENSION_SHOULD_NOT_BE_STATIC.check(javaClasses);
    }
}
