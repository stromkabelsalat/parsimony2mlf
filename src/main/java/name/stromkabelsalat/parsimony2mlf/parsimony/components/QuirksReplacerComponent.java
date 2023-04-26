package name.stromkabelsalat.parsimony2mlf.parsimony.components;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import tools.jackson.databind.ObjectMapper;

@Component
public class QuirksReplacerComponent {
	// _________________*<br>
	public static final String FAKE_HR_REGEX = "_________________*<br>";

	public static final String IGNORE_CASE_REGEX = "(?i)";

	private final String separator;
	private final Map<String, String> quirksReplacements = new HashMap<>();

	public QuirksReplacerComponent(
			@Value("${name.stromkabelsalat.parsimony2mlf.paths.quirks-replacements}") final Path quirksReplacementsPath,
			final ObjectMapper objectMapper, final SeparatorComponent separatorComponent) {
		this.separator = separatorComponent.getSeparator();

		final Object object = objectMapper.readerForMapOf(String.class).readValue(quirksReplacementsPath);

		final Map<?, ?> map = (Map<?, ?>) object;

		map.forEach((key, value) -> this.quirksReplacements.put((String) key, (String) value));
	}

	public String replaceQuirks(final String string) {
		String result = string;

		// hr
		result = result
				.replaceAll(IGNORE_CASE_REGEX + Pattern.quote("<hr align=\"center\">"), this.separator)
				.replaceAll(IGNORE_CASE_REGEX + Pattern.quote("<center><p><hr></p></center>"), this.separator)
				.replaceAll(IGNORE_CASE_REGEX + Pattern.quote("<center><p><hr><p></p></center>"), this.separator)
				.replaceAll(
						IGNORE_CASE_REGEX + Pattern.quote(
								"</center>" + System.lineSeparator() + "<hr>" + System.lineSeparator() + "<center>"),
						"</center> " + this.separator + "<center>")
				.replaceAll(IGNORE_CASE_REGEX + Pattern.quote("</center>" + System.lineSeparator() + "<hr>"),
						"</center> " + this.separator)
				.replaceAll(IGNORE_CASE_REGEX + Pattern.quote("<hr></body>"), this.separator)
				.replaceAll(IGNORE_CASE_REGEX + Pattern.quote("<hr>" + System.lineSeparator() + "</body>"),
						this.separator)
				.replaceAll(
						IGNORE_CASE_REGEX
								+ Pattern.quote("<hr>" + System.lineSeparator() + System.lineSeparator() + "</body>"),
						this.separator);

		// 994200797
		result = result.replace("<hr>" + System.lineSeparator()
				+ "  <p align=\"center\">[ <a href=\"Neu_0800.htm\">Hobby? Barfuß! - Forum</a> ] </p>"
				+ System.lineSeparator() + "  <hr>", this.separator + this.separator);

		// Fake hr
		result = result.replaceAll(FAKE_HR_REGEX, "</p><hr><p>");

		for (final Entry<String, String> entry : this.quirksReplacements.entrySet()) {
			final String target = entry.getKey();
			final String replacement = entry.getValue();

			result = result.replace(target, replacement);
		}

		return result;
	}
}
