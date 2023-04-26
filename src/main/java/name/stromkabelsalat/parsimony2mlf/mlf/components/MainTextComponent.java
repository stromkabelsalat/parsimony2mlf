package name.stromkabelsalat.parsimony2mlf.mlf.components;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MainTextComponent {
	private final BBCodeComponent bbCodeComponent;
	private final QuoteGapsFixerComponent quoteGapsFixerComponent;

	public String mainText(final String body) {
		return Optional
				.of(body)
				.map(this.bbCodeComponent::bbCode)
				.map(this.quoteGapsFixerComponent::fixQuoteGaps)
				.orElse(null);
	}
}
