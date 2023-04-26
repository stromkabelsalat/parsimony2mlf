package name.stromkabelsalat.parsimony2mlf.parsimony.components;

import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component
public class NumComponent {
	public Integer num(final Element liElement) {
		final String numString = liElement
				.getElementsByTag("a")
				.attr("href")
				.replace("messages/", "")
				.replace(".htm", "");

		return Integer.valueOf(numString);
	}
}
