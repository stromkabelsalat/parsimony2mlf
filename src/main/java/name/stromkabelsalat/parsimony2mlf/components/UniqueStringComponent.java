package name.stromkabelsalat.parsimony2mlf.components;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class UniqueStringComponent {
	private final List<String> uniqueStrings = new ArrayList<>();

	public String uniqueString() {
		return this.uniqueString(32);
	}

	public String uniqueString(final int length) {
		if (length > 32) {
			throw new IllegalArgumentException();
		}

		String uniqueStringCandidate;

		do {
			uniqueStringCandidate = UUID.randomUUID().toString().replace("-", "").substring(0, length);

			if (length == 32) {
				uniqueStringCandidate = "🟢" + uniqueStringCandidate.substring(2) + "🔴";
			}
		} while (this.uniqueStrings.contains(uniqueStringCandidate));

		this.uniqueStrings.add(uniqueStringCandidate);

		return uniqueStringCandidate;
	}
}
