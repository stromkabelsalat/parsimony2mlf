package name.stromkabelsalat.parsimony2mlf.mlf.colors.repositories;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import name.stromkabelsalat.parsimony2mlf.mlf.colors.components.ColorStringDistanceComponent;

@Repository
public class ValidColorHexRepository {
	private final ColorStringDistanceComponent colorStringDistanceComponent;
	private final List<String> validColorHexes;

	public ValidColorHexRepository(final ColorStringDistanceComponent colorStringDistanceComponent, final ObjectMapper objectMapper)
			throws IOException {
		this.colorStringDistanceComponent = colorStringDistanceComponent;

		final ClassLoader classLoader = ValidColorHexRepository.class.getClassLoader();

		final URL url = classLoader.getResource("valid-color-hexes.json");

		final Object object = objectMapper.readerForListOf(String.class).readValue(url);

		final List<?> list = (List<?>) object;

		this.validColorHexes = list
				.stream()
				.map(String.class::cast)
				.toList();
	}

	public String findMostSimilarTo(final String colorString) {
		final String normalizedColorString = colorString.toLowerCase().strip();

        return this.validColorHexes
                .stream()
                .map(validColorHex -> this.colorStringDistanceComponent.colorStringDistance(validColorHex, normalizedColorString))
                .sorted()
                .findFirst()
                .flatMap(minDistance -> this.validColorHexes
                        .stream()
                        .filter(validColorHex -> this.colorStringDistanceComponent.colorStringDistance(validColorHex, normalizedColorString) == minDistance)
                        .findFirst())
                .orElse(null);
	}
}
