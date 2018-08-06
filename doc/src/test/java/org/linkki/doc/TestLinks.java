package org.linkki.doc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class TestLinks {

	private static final String USER_AGENT = "User-Agent";
	private static final Pattern HREF = Pattern.compile("<a href=\"([^\"]+)\">");
	private static final Pattern URL = Pattern
			.compile("^((http|https)://)?[-a-zA-Z0-9+&@#/%?=~_|,!:\\.;]*[-a-zA-Z0-9+@#/%=&_|]");
	private static final Pattern LOCAL_REF = Pattern.compile("^([-_a-zA-Z0-9/\\.]+\\.html)?(#[-a-zA-Z0-9]+)?");

	@Parameter(0)
	public Path from;
	@Parameter(1)
	public String link;

	@Parameters
	public static Collection<Object[]> data() throws IOException {
		return Files.walk(Paths.get("target")).filter(TestLinks::isHtmlFileInDocumentation).flatMap(p -> {
			try {
				return Files.lines(p, StandardCharsets.UTF_8).map(HREF::matcher).filter(Matcher::find)
						.map(matcher -> matcher.group(1)).map(url -> new Object[] { p, url });
			} catch (IOException e) {
				e.printStackTrace();
				fail(e.getMessage());
				return null;
			}
		}).collect(Collectors.toList());
	}

	private static boolean isHtmlFileInDocumentation(Path path) {
		String string = path.toString();
		return string.contains("linkki-core-documentation") && string.endsWith(".html");
	}

	@Test
	public void testLink() {
		assertThat("should be a valid URL", link, matchesPattern(URL));
		if (link.startsWith("http")) {
			try {
				URL url = new URL(link);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestProperty(USER_AGENT, StringUtils.EMPTY);
				assertThat("external link '" + link + "' in '" + from + "' returns wrong http status",
						connection.getResponseCode(), is(HttpURLConnection.HTTP_OK));
			} catch (IOException e) {
				fail("external link '" + link + "' in '" + from + "' could not be resolved:\n" + e);
			}
		} else {
			assertThat("should be a valid local reference", link, matchesPattern(LOCAL_REF));
			Matcher matcher = LOCAL_REF.matcher(link);
			matcher.find();
			String referencedFileName = matcher.group(1);
			String referencedAnchor = matcher.group(2);
			Path referencedPath = from;
			if (StringUtils.isNotBlank(referencedFileName)) {
				Path parent = from.getParent();
				if (parent != null) {
					referencedPath = parent.resolve(referencedFileName);
				}
			}
			assertThat(
					"the reference '" + referencedFileName + "' from '" + from + "' should reference an existing file",
					referencedPath, exists());
			if (StringUtils.isNotBlank(referencedAnchor)) {
				String anchor = "id=\"" + referencedAnchor.substring(1) + "\"";
				try {
					assertThat(
							"Anchor '" + referencedAnchor + "' referenced from '" + from + "' should exist in '"
									+ referencedPath + "'",
							Files.lines(referencedPath).anyMatch(line -> line.contains(anchor)), is(true));
				} catch (IOException e) {
					e.printStackTrace();
					fail("the reference '" + referencedFileName + "' from '" + from + "' could not be read:\n" + e);
				}
			}
		}
	}

	private org.hamcrest.Matcher<Path> exists() {
		return new TypeSafeMatcher<Path>() {

			@Override
			public void describeTo(@NonNull Description description) {
				description.appendText("an existing file");
			}

			@Override
			protected boolean matchesSafely(Path item) {
				return item.toFile().exists();
			}
		};
	}

}
