package org.linkki.doc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class UnusedTagsTest {

    // include::{source-dir-custom}/org/linkki/samples/customlayout/pmo/AddressSectionPmo.java[tags=declaration]
    private static final Pattern INCLUDE = Pattern.compile("include::(\\{[^\\}]+\\}[/\\w\\.\\-]+)(?:\\[([^\\]]+)\\])?");
    // {source-dir-custom}
    private static final Pattern REF = Pattern.compile("\\{([^\\}]+)\\}");
    // :source-dir-custom: ../../../../../../vaadin8/samples/custom-layout/src/main/java
    private static final Pattern PROPERTY = Pattern.compile("^:([^:]+):(.+)");
    // tag::declaration[]
    // end::declaration[]
    private static final Pattern TAG_END = Pattern.compile("((?:tag)|(?:end))::([^\\[]+)\\[\\]");

    private static Map<Path, Set<String>> usedTags = new HashMap<>();

    public static Collection<Object[]> data() throws IOException {
        usedTags = Files.walk(Paths.get("src/main/jbake"))
                .filter(p -> p.getFileName().toString().endsWith(".adoc"))
                .flatMap(p -> {
                    try {
                        Map<String, String> properties = Files.lines(p, StandardCharsets.UTF_8).map(PROPERTY::matcher)
                                .filter(Matcher::find)
                                .collect(Collectors.toMap(m -> m.group(1), m -> m.group(2).trim()));
                        return Files.lines(p, StandardCharsets.UTF_8).map(INCLUDE::matcher).filter(Matcher::find)
                                .filter(matcher -> matcher.group(2) != null && matcher.group(2).startsWith("tags="))
                                .map(matcher -> Pair.of(getSource(p, properties, matcher),
                                                        matcher.group(2).substring("tags=".length())));
                    } catch (Exception e) {
                        e.printStackTrace();
                        fail("Could not initialize " + p + ":\n" + e.getMessage());
                        return null;
                    }
                })
                .collect(Collectors.groupingBy(Pair::getLeft, Collectors.mapping(Pair::getRight, Collectors.toSet())));
        return Files.walk(Paths.get(".."))
                .filter(p -> p.getFileName().toString().endsWith(".java")
                        && p.toString().contains(Paths.get("src/main/java").toString()))
                .flatMap(p -> {
                    try {
                        return Files.lines(p, StandardCharsets.UTF_8).map(TAG_END::matcher)
                                .filter(Matcher::find)
                                .map(m -> m.group(2))
                                .distinct()
                                .map(tag -> new Object[] { p.toAbsolutePath().normalize(), tag });
                    } catch (Exception e) {
                        e.printStackTrace();
                        fail("Could not initialize " + p + ":\n" + e.getMessage());
                        return null;
                    }
                }).collect(Collectors.toList());
    }

    private static Path getSource(Path from, Map<String, String> properties, Matcher matcher) {
        String sourceDir = matcher.group(1);
        Matcher refMatcher = REF.matcher(sourceDir);
        while (refMatcher.find()) {
            String reference = refMatcher.group(1);
            String resolvedReference = properties.getOrDefault(reference, reference);
            sourceDir = refMatcher.replaceFirst(resolvedReference);
            refMatcher.reset(sourceDir);
        }
        Path referencedPath = from;
        if (StringUtils.isNotBlank(sourceDir)) {
            Path parent = from.getParent();
            if (parent != null) {
                referencedPath = parent.resolve(sourceDir);
            }
        }
        return referencedPath.toAbsolutePath().normalize();
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testUnusedTags(Path sourceFile, String tag) {
        Set<String> usedTagsInSourceFile = usedTags.getOrDefault(sourceFile, Collections.emptySet());
        assertThat("the tag '" + tag + "' in '" + sourceFile + "' should be referenced in an .adoc file",
                   usedTagsInSourceFile, containsInRelativeOrder(tag));
    }

}
