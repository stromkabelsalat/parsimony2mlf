package name.stromkabelsalat.parsimony2mlf.mlf.colors.repositories;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import name.stromkabelsalat.parsimony2mlf.mlf.colors.components.ColorStringDistanceComponent;

@Repository
public class ValidColorStringRepository {
	private final ColorStringDistanceComponent colorStringDistanceComponent;
	private final List<String> validColorStrings;

	public ValidColorStringRepository(final ColorStringDistanceComponent colorStringDistanceComponent,
			final ApplicationContext applicationContext, final ObjectMapper objectMapper)
			throws IOException {
		this.colorStringDistanceComponent = colorStringDistanceComponent;

		final URL url = applicationContext
				.getResource("classpath:valid-color-strings.json")
				.getURL();

		final Object object = objectMapper.readerForListOf(String.class).readValue(url);

		final List<?> list = (List<?>) object;

		this.validColorStrings = list
				.stream()
				.map(String.class::cast)
				.toList();
	}

	public String findMostSimilarTo(final String colorString) {
		final String normalizedColorString = colorString.toLowerCase().strip();

		return this.validColorStrings
				.stream()
				.map(validColorString -> this.colorStringDistanceComponent.colorStringDistance(validColorString,
						normalizedColorString))
				.sorted()
				.findFirst()
				.flatMap(minDistance -> this.validColorStrings
						.stream()
						.filter(validColorString -> this.colorStringDistanceComponent
								.colorStringDistance(validColorString, normalizedColorString) == minDistance)
						.findFirst())
				.orElse(null);
	}
}
