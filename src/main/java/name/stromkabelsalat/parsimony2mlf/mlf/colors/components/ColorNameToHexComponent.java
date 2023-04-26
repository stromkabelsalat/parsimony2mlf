package name.stromkabelsalat.parsimony2mlf.mlf.colors.components;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ColorNameToHexComponent {
	private final Map<String, String> colorNamesToHexes = new HashMap<>();

	public ColorNameToHexComponent(final ApplicationContext applicationContext, final ObjectMapper objectMapper)
			throws IOException {
		final URL url = applicationContext
				.getResource("classpath:color-names-to-hexes.json")
				.getURL();

		final Object object = objectMapper.readerForMapOf(String.class).readValue(url);

		final Map<?, ?> map = (Map<?, ?>) object;

		map.forEach((key, value) -> this.colorNamesToHexes.put((String) key, (String) value));
	}

	public Optional<String> colorNameToHex(final String colorName) {
		final String normalizedColorName = colorName.toLowerCase().strip();

		return Optional.ofNullable(this.colorNamesToHexes.get(normalizedColorName));
	}
}
