package org.linkki.doc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.fail;
import static org.linkki.doc.PathExistsMatcher.exists;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class IncludeTest {

    // include::{source-dir-custom}/org/linkki/samples/customlayout/pmo/AddressSectionPmo.java[tags=declaration]
    private static final Pattern INCLUDE = Pattern.compile("include::(\\{[^\\}]+\\}[/\\w\\.\\-]+)(?:\\[([^\\]]+)\\])?");
    // {source-dir-custom}
    private static final Pattern REF = Pattern.compile("\\{([^\\}]+)\\}");
    // :source-dir-custom: ../../../../../vaadin8/samples/custom-layout/src/main/java
    private static final Pattern PROPERTY = Pattern.compile("^:([^:]+):(.+)");
    // tag::declaration[]
    // end::declaration[]
    private static final Pattern TAG_END = Pattern.compile("((?:tag)|(?:end))::([^\\[]+)\\[\\]");
    // lines=45..56
    private static final Pattern LINES = Pattern.compile("lines=(\\d+)\\.\\.(\\d+)");

    public static Collection<Object[]> data() throws IOException {
        return Files.walk(Paths.get("target/jade-resources/content")).filter(p -> p.getFileName().toString().endsWith(".adoc"))
                .flatMap(p -> {
                    try {
                        Map<String, String> properties = Files.lines(p, StandardCharsets.UTF_8).map(PROPERTY::matcher)
                                .filter(Matcher::find)
                                .collect(Collectors.toMap(m -> m.group(1), m -> m.group(2).trim()));
                        return Files.lines(p, StandardCharsets.UTF_8).map(INCLUDE::matcher).filter(Matcher::find)
                                .map(matcher -> new Object[] { p, matcher.group(1), getSource(properties, matcher),
                                        Optional.ofNullable(matcher.group(2)) });
                    } catch (Exception e) {
                        e.printStackTrace();
                        fail("Could not initialize " + p + ":\n" + e.getMessage());
                        return null;
                    }
                }).collect(Collectors.toList());
    }

    private static String getSource(Map<String, String> properties, Matcher matcher) {
        String sourceDir = matcher.group(1);
        Matcher refMatcher = REF.matcher(sourceDir);
        while (refMatcher.find()) {
            String reference = refMatcher.group(1);
            String resolvedReference = properties.getOrDefault(reference, reference);
            sourceDir = refMatcher.replaceFirst(resolvedReference);
            refMatcher.reset(sourceDir);
        }
        return sourceDir;
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testInclude(Path from, String source, String resolvedSource, Optional<String> constraints) {
        Path referencedPath = from;
        if (StringUtils.isNotBlank(resolvedSource)) {
            Path parent = from.getParent();
            if (parent != null) {
                referencedPath = parent.resolve(resolvedSource);
            }
        }
        assertThat("the include '" + source + "' from '" + from
                + "' should reference an existing file", referencedPath, exists());
        checkConstraints(from, referencedPath, constraints);
    }

    private void checkConstraints(Path fromPath, Path referencedPath, Optional<String> constraints) {
        constraints.ifPresent(c -> checkConstraints(fromPath, referencedPath, c));
    }

    private void checkConstraints(Path fromPath, Path referencedPath, String constraint) {
        String fileName = referencedPath.getFileName().toString();
        String fileType = fileName.substring(fileName.lastIndexOf('.') + 1);
        Charset charset = "properties".equals(fileType) ? StandardCharsets.ISO_8859_1 : StandardCharsets.UTF_8;
        if (constraint.startsWith("tags=")) {
            String tag = constraint.substring("tags=".length());
            try {
                Map<String, Map<String, Long>> tags = Files.lines(referencedPath, charset)
                        .map(TAG_END::matcher).filter(Matcher::find)
                        .collect(Collectors.groupingBy(m -> m.group(2),
                                                       Collectors.groupingBy(m -> m.group(1), Collectors.counting())));
                assertThat("the include '" + referencedPath + "' does not include the tag '" + tag + "' referenced in '"
                        + fromPath + '"', tags.containsKey(tag), is(true));
                assertThat("the include '" + referencedPath
                        + "' does not include the same number of 'tag' and 'end' entries for '" + tag
                        + "' referenced in '"
                        + fromPath + '"', tags.get(tag).getOrDefault("tag", 0l),
                           is(tags.get(tag).getOrDefault("end", 0l)));
            } catch (Exception e) {
                e.printStackTrace();
                fail("Could not read " + referencedPath + " included in '" + fromPath + "':\n" + e.getMessage());
            }
        } else if (constraint.startsWith("lines=")) {
            Matcher lines = LINES.matcher(constraint);
            lines.find();
            long min = Long.parseLong(lines.group(1));
            long max = Long.parseLong(lines.group(2));
            assertThat("Illegal lines constraint '" + constraint + "' for include '" + referencedPath + "' from '"
                    + fromPath + "'", min,
                       is(lessThanOrEqualTo(max)));
            try {
                long lineCount = Files.lines(referencedPath, charset).count();
                assertThat("the include '" + referencedPath + "[" + constraint + "]' referenced in '" + fromPath
                        + "' only has " + lineCount + " lines", max, is(lessThanOrEqualTo(lineCount)));
            } catch (Exception e) {
                e.printStackTrace();
                fail("Could not read " + referencedPath + ":\n" + e.getMessage());
            }
        } else {
            fail("unknown constraints '" + constraint + "' for include '" + referencedPath + "' from '" + fromPath
                    + "'");
        }
    }

}
