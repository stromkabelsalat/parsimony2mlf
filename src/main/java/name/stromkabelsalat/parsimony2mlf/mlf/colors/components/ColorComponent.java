package name.stromkabelsalat.parsimony2mlf.mlf.colors.components;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import name.stromkabelsalat.parsimony2mlf.mlf.colors.model.Color;

@Component
@RequiredArgsConstructor
public class ColorComponent {
	private final ColorNameToHexComponent colorNameToHexComponent;

	public Color color(final String colorString) {
		final String normalizedColorString = colorString.toLowerCase().strip();

		final int r;
		final int g;
		final int b;

		if (normalizedColorString.startsWith("rgb(")) {
			final String[] components = normalizedColorString.replace("rgb(", "").replace(")", "").split(",");

			r = Integer.parseInt(components[0]);
			g = Integer.parseInt(components[1]);
			b = Integer.parseInt(components[2]);
		} else {
			final String hex = this.colorNameToHexComponent
					.colorNameToHex(normalizedColorString)
					.orElse(normalizedColorString);

			final String pureHex = hex.replaceFirst("#", "");
			final int length = pureHex.length();

			final String rHex;
			final String gHex;
			final String bHex;

			if (length == 3) {
				rHex = pureHex.substring(0, 1) + pureHex.substring(0, 1);
				gHex = pureHex.substring(1, 2) + pureHex.substring(1, 2);
				bHex = pureHex.substring(2, 3) + pureHex.substring(2, 3);
			} else {
				rHex = pureHex.substring(0, 2);
				gHex = pureHex.substring(2, 4);
				bHex = pureHex.substring(4, 6);
			}

			r = Integer.parseInt(rHex, 16);
			g = Integer.parseInt(gHex, 16);
			b = Integer.parseInt(bHex, 16);
		}

		return new Color(r, g, b);
	}
}
