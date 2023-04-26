package name.stromkabelsalat.parsimony2mlf.mlf.components;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

@Component
public class QuoteGapsFixerComponent {
	public String fixQuoteGaps(final String body) {
		final String[] lines = body
				.strip()
				.split(System.lineSeparator());

		final StringBuilder stringBuilder = new StringBuilder();

		IntStream
				.range(0, lines.length)
				.forEach(i -> {
					final String line = lines[i];

					if (line.isBlank() && i > 0) {
						List
								.of(lines[i - 1], lines[i + 1])
								.stream()
								.map(adjacentLine -> adjacentLine
										.replace(" ", "")
										.replaceFirst("[^>].*", "")
										.length())
								.min(Integer::compareTo)
								.ifPresent(quoteDepth -> IntStream
										.range(0, quoteDepth)
										.forEach(_ -> stringBuilder.append("> ")));
					} else {
						stringBuilder.append(line);
					}

					stringBuilder.append(System.lineSeparator());
				});

		return stringBuilder.toString().strip();
	}
}
