package name.stromkabelsalat.parsimony2mlf.components;

import org.springframework.stereotype.Component;

import name.stromkabelsalat.parsimony2mlf.model.LeftRight;

@Component
public class LeftRightComponent {
	public LeftRight leftRight(final String string, final String separator) {
		final int separatorLength = separator.length();
		final int leftIndex = string.lastIndexOf(separator);
		final int rightIndex = leftIndex + separatorLength;

		final String left = string.substring(0, leftIndex);
		final String right = string.substring(rightIndex);

		return new LeftRight(left, right);
	}
}
