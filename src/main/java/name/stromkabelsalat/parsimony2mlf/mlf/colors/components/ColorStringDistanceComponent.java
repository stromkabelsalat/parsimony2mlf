package name.stromkabelsalat.parsimony2mlf.mlf.colors.components;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import name.stromkabelsalat.parsimony2mlf.mlf.colors.model.Color;

@Component
@RequiredArgsConstructor
public class ColorStringDistanceComponent {
	private final ColorComponent colorComponent;

	public double colorStringDistance(final String targetColorString, final String controlColorString) {
		final Color targetColor = this.colorComponent.color(targetColorString);
		final Color controlColor = this.colorComponent.color(controlColorString);

		return targetColor.distanceTo(controlColor);
	}
}
